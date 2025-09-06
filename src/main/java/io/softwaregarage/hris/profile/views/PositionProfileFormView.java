package io.softwaregarage.hris.profile.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.admin.dtos.PositionDTO;
import io.softwaregarage.hris.admin.services.PositionService;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.dtos.PositionProfileDTO;
import io.softwaregarage.hris.profile.services.PositionProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_HR_SUPERVISOR",
        "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Position Form")
@Route(value = "employee-position-form", layout = MainLayout.class)
public class PositionProfileFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final PositionProfileService positionProfileService;
    @Resource
    private final EmployeeProfileService employeeProfileService;
    @Resource
    private final PositionService positionService;

    private PositionProfileDTO positionProfileDTO;
    private UUID parameterId;

    private final FormLayout employeePositionDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeProfileDTO> employeeDTOComboBox;
    private ComboBox<PositionDTO> positionDTOComboBox;
    private Checkbox currentPositionCheckbox;

    public PositionProfileFormView(PositionProfileService employeePositionProfileService,
                                   EmployeeProfileService employeeProfileService,
                                   PositionService positionService) {
        this.positionProfileService = employeePositionProfileService;
        this.employeeProfileService = employeeProfileService;
        this.positionService = positionService;

        add(employeePositionDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, employeePositionDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            positionProfileDTO = positionProfileService.getById(parameterId);
        }

        buildEmployeePositionFormLayout();
    }

    private void buildEmployeePositionFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeProfileDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                employeeProfileService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeProfileDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (positionProfileDTO != null) employeeDTOComboBox.setValue(positionProfileDTO.getEmployeeDTO());

        // Create the query object that will do the pagination of position records in the combo box component.
        Query<PositionDTO, Void> positionQuery = new Query<>();

        positionDTOComboBox = new ComboBox<>("Position");
        positionDTOComboBox.setItems((positionDTO, filterString) -> positionDTO.getName().toLowerCase().contains(filterString.toLowerCase()),
                positionService.getAll(positionQuery.getPage(), positionQuery.getPageSize()));
        positionDTOComboBox.setItemLabelGenerator(io.softwaregarage.hris.admin.dtos.PositionDTO::getName);
        positionDTOComboBox.setClearButtonVisible(true);
        positionDTOComboBox.setRequired(true);
        positionDTOComboBox.setRequiredIndicatorVisible(true);
        if (positionProfileDTO != null) positionDTOComboBox.setValue(positionProfileDTO.getPositionDTO());

        currentPositionCheckbox = new Checkbox("Is Position Active?");
        if (positionProfileDTO != null) currentPositionCheckbox.setValue(positionProfileDTO.isCurrentPosition());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateEmployeePositionDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(PositionProfileListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(PositionProfileListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setWidth("768px");
        buttonLayout.setPadding(true);

        employeePositionDTOFormLayout.add(employeeDTOComboBox,
                positionDTOComboBox,
                currentPositionCheckbox,
                buttonLayout);
        employeePositionDTOFormLayout.setColspan(currentPositionCheckbox, 2);
        employeePositionDTOFormLayout.setColspan(buttonLayout, 2);
        employeePositionDTOFormLayout.setWidth("768px");
    }

    private void saveOrUpdateEmployeePositionDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            positionProfileDTO = positionProfileService.getById(parameterId);
        } else {
            positionProfileDTO = new PositionProfileDTO();
            positionProfileDTO.setCreatedBy(loggedInUser);
        }

        positionProfileDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        positionProfileDTO.setPositionDTO(positionDTOComboBox.getValue());
        positionProfileDTO.setCurrentPosition(currentPositionCheckbox.getValue());
        positionProfileDTO.setUpdatedBy(loggedInUser);

        positionProfileService.saveOrUpdate(positionProfileDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved an employee position.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
