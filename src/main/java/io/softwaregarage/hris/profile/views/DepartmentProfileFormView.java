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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.admin.services.DepartmentService;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.dtos.DepartmentProfileDTO;
import io.softwaregarage.hris.profile.services.DepartmentProfileService;
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
@PageTitle("Employee Department Form")
@Route(value = "employee-department-form", layout = MainLayout.class)
public class DepartmentProfileFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final DepartmentProfileService departmentProfileService;
    @Resource
    private final EmployeeProfileService employeeProfileService;
    @Resource
    private final DepartmentService departmentService;

    private DepartmentProfileDTO departmentProfileDTO;
    private UUID parameterId;

    private final FormLayout employeeDepartmentDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeProfileDTO> employeeDTOComboBox;
    private ComboBox<io.softwaregarage.hris.admin.dtos.DepartmentDTO> departmentDTOComboBox;
    private Checkbox currentDepartmentCheckbox;

    public DepartmentProfileFormView(DepartmentProfileService employeeDepartmentProfileService,
                                     EmployeeProfileService employeeProfileService,
                                     DepartmentService departmentService) {
        this.departmentProfileService = employeeDepartmentProfileService;
        this.employeeProfileService = employeeProfileService;
        this.departmentService = departmentService;

        add(employeeDepartmentDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, employeeDepartmentDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            departmentProfileDTO = departmentProfileService.getById(parameterId);
        }

        buildEmployeeDepartmentFormLayout();
    }

    private void buildEmployeeDepartmentFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeProfileDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                employeeProfileService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeProfileDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (departmentProfileDTO != null) employeeDTOComboBox.setValue(departmentProfileDTO.getEmployeeDTO());

        // Create the query object that will do the pagination of position records in the combo box component.
        Query<io.softwaregarage.hris.admin.dtos.DepartmentDTO, Void> departmentQuery = new Query<>();

        departmentDTOComboBox = new ComboBox<>("Department");
        departmentDTOComboBox.setItems((departmentDTO, filterString) -> departmentDTO.getName().toLowerCase().contains(filterString.toLowerCase()),
                departmentService.getAll(departmentQuery.getPage(), departmentQuery.getPageSize()));
        departmentDTOComboBox.setItemLabelGenerator(io.softwaregarage.hris.admin.dtos.DepartmentDTO::getName);
        departmentDTOComboBox.setClearButtonVisible(true);
        departmentDTOComboBox.setRequired(true);
        departmentDTOComboBox.setRequiredIndicatorVisible(true);
        if (departmentProfileDTO != null) departmentDTOComboBox.setValue(departmentProfileDTO.getDepartmentDTO());

        currentDepartmentCheckbox = new Checkbox("Is Current Department?");
        if (departmentProfileDTO != null) currentDepartmentCheckbox.setValue(departmentProfileDTO.isCurrentDepartment());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateEmployeeDepartmentDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(DepartmentProfileListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(DepartmentProfileListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        employeeDepartmentDTOFormLayout.add(employeeDTOComboBox,
                                            departmentDTOComboBox,
                                            currentDepartmentCheckbox,
                                            buttonLayout);
        employeeDepartmentDTOFormLayout.setColspan(currentDepartmentCheckbox, 2);
        employeeDepartmentDTOFormLayout.setColspan(buttonLayout, 2);
        employeeDepartmentDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateEmployeeDepartmentDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            departmentProfileDTO = departmentProfileService.getById(parameterId);
        } else {
            departmentProfileDTO = new DepartmentProfileDTO();
            departmentProfileDTO.setCreatedBy(loggedInUser);
        }

        departmentProfileDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        departmentProfileDTO.setDepartmentDTO(departmentDTOComboBox.getValue());
        departmentProfileDTO.setCurrentDepartment(currentDepartmentCheckbox.getValue());
        departmentProfileDTO.setUpdatedBy(loggedInUser);

        departmentProfileService.saveOrUpdate(departmentProfileDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved an employee department.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
