package io.softwaregarage.hris.admin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.admin.dtos.GroupDTO;
import io.softwaregarage.hris.admin.services.GroupService;
import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN"})
@PageTitle("Group Form")
@Route(value = "group-form", layout = MainLayout.class)
public class GroupFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final GroupService groupService;

    private GroupDTO groupDTO;
    private UUID parameterId;

    private final FormLayout groupDTOFormLayout = new FormLayout();
    private TextField codeTextField, nameTextField, descriptionTextField;

    public GroupFormView(GroupService groupService) {
        this.groupService = groupService;

        add(groupDTOFormLayout);
        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, groupDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            groupDTO = groupService.getById(parameterId);
        }

        buildDepartmentFormLayout();
    }

    private void buildDepartmentFormLayout() {
        codeTextField = new TextField("Code");
        codeTextField.setClearButtonVisible(true);
        codeTextField.setRequired(true);
        codeTextField.setRequiredIndicatorVisible(true);
        if (groupDTO != null) codeTextField.setValue(groupDTO.getCode());

        nameTextField = new TextField("Name");
        nameTextField.setClearButtonVisible(true);
        nameTextField.setRequired(true);
        nameTextField.setRequiredIndicatorVisible(true);
        if (groupDTO != null) nameTextField.setValue(groupDTO.getName());

        descriptionTextField = new TextField("Description");
        descriptionTextField.setClearButtonVisible(true);
        descriptionTextField.setRequired(true);
        descriptionTextField.setRequiredIndicatorVisible(true);
        if (groupDTO != null) descriptionTextField.setValue(groupDTO.getDescription());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateGroupDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(GroupListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(GroupListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        groupDTOFormLayout.add(codeTextField,
                nameTextField,
                descriptionTextField,
                buttonLayout);
        groupDTOFormLayout.setColspan(descriptionTextField, 2);
        groupDTOFormLayout.setColspan(buttonLayout, 2);
        groupDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateGroupDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            groupDTO = groupService.getById(parameterId);
        } else {
            groupDTO = new GroupDTO();
            groupDTO.setCreatedBy(loggedInUser);
        }

        groupDTO.setCode(codeTextField.getValue());
        groupDTO.setName(nameTextField.getValue());
        groupDTO.setDescription(descriptionTextField.getValue());
        groupDTO.setUpdatedBy(loggedInUser);

        groupService.saveOrUpdate(groupDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a group reference.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
