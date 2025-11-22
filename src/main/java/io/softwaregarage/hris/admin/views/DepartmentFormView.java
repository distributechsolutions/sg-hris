package io.softwaregarage.hris.admin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.admin.dtos.DepartmentDTO;
import io.softwaregarage.hris.admin.dtos.GroupDTO;
import io.softwaregarage.hris.admin.services.DepartmentService;
import io.softwaregarage.hris.admin.services.GroupService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_HR_SUPERVISOR"})
@PageTitle("Department Form")
@Route(value = "department-form", layout = MainLayout.class)
public class DepartmentFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final DepartmentService departmentService;

    @Resource
    private final GroupService groupService;

    private DepartmentDTO departmentDTO;
    private UUID parameterId;

    private final FormLayout departmentDTOFormLayout = new FormLayout();
    private TextField codeTextField, nameTextField;
    private ComboBox<GroupDTO> groupComboBox;

    public DepartmentFormView(DepartmentService departmentService,
                              GroupService groupService) {
        this.departmentService = departmentService;
        this.groupService = groupService;

        add(departmentDTOFormLayout);
        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, departmentDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            departmentDTO = departmentService.getById(parameterId);
        }

        buildDepartmentFormLayout();
    }

    private void buildDepartmentFormLayout() {
        codeTextField = new TextField("Code");
        codeTextField.setClearButtonVisible(true);
        codeTextField.setRequired(true);
        codeTextField.setRequiredIndicatorVisible(true);
        if (departmentDTO != null) codeTextField.setValue(departmentDTO.getCode());

        nameTextField = new TextField("Name");
        nameTextField.setClearButtonVisible(true);
        nameTextField.setRequired(true);
        nameTextField.setRequiredIndicatorVisible(true);
        if (departmentDTO != null) nameTextField.setValue(departmentDTO.getName());

        Query<GroupDTO, Void> groupQuery = new Query<>();
        groupComboBox = new ComboBox<>("Group");
        groupComboBox.setItems((groupDTO, filterString) -> groupDTO.getName()
                .toLowerCase().contains(filterString.toLowerCase()),
                groupService.getAll(groupQuery.getPage(), groupQuery.getPageSize()));
        groupComboBox.setItemLabelGenerator(GroupDTO::getName);
        groupComboBox.setRequiredIndicatorVisible(true);
        groupComboBox.setRequired(true);
        groupComboBox.setClearButtonVisible(true);
        if (departmentDTO != null) groupComboBox.setValue(departmentDTO.getGroupDTO());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateDepartmentDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(DepartmentListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(DepartmentListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        departmentDTOFormLayout.add(codeTextField,
                                  nameTextField,
                                  groupComboBox,
                                  buttonLayout);
        departmentDTOFormLayout.setColspan(buttonLayout, 2);
        departmentDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateDepartmentDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            departmentDTO = departmentService.getById(parameterId);
        } else {
            departmentDTO = new DepartmentDTO();
            departmentDTO.setCreatedBy(loggedInUser);
        }

        departmentDTO.setCode(codeTextField.getValue());
        departmentDTO.setName(nameTextField.getValue());
        departmentDTO.setGroupDTO(groupComboBox.getValue());
        departmentDTO.setUpdatedBy(loggedInUser);

        departmentService.saveOrUpdate(departmentDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a department reference.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
