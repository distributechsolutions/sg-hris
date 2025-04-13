package io.distributechsolutions.hris.views.reference;

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

import io.distributechsolutions.hris.dtos.reference.PositionDTO;
import io.distributechsolutions.hris.services.reference.PositionService;
import io.distributechsolutions.hris.utils.SecurityUtil;
import io.distributechsolutions.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_HR_SUPERVISOR"})
@PageTitle("Position Form")
@Route(value = "position-form", layout = MainLayout.class)
public class PositionFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final PositionService positionService;
    private PositionDTO positionDTO;
    private UUID parameterId;

    private final FormLayout positionDTOFormLayout = new FormLayout();
    private TextField codeTextField, nameTextField;

    public PositionFormView(PositionService positionService) {
        this.positionService = positionService;

        add(positionDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, positionDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            positionDTO = positionService.getById(parameterId);
        }

        buildPositionFormLayout();
    }

    private void buildPositionFormLayout() {
        codeTextField = new TextField("Code");
        codeTextField.setClearButtonVisible(true);
        codeTextField.setRequired(true);
        codeTextField.setRequiredIndicatorVisible(true);
        if (positionDTO != null) codeTextField.setValue(positionDTO.getCode());

        nameTextField = new TextField("Name");
        nameTextField.setClearButtonVisible(true);
        nameTextField.setRequired(true);
        nameTextField.setRequiredIndicatorVisible(true);
        if (positionDTO != null) nameTextField.setValue(positionDTO.getName());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdatePositionDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(PositionListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(PositionListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        positionDTOFormLayout.add(codeTextField,
                                  nameTextField,
                                  buttonLayout);
        positionDTOFormLayout.setColspan(buttonLayout, 2);
        positionDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdatePositionDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            positionDTO = positionService.getById(parameterId);
        } else {
            positionDTO = new PositionDTO();
            positionDTO.setCreatedBy(loggedInUser);
        }

        positionDTO.setCode(codeTextField.getValue());
        positionDTO.setName(nameTextField.getValue());
        positionDTO.setUpdatedBy(loggedInUser);

        positionService.saveOrUpdate(positionDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a position reference.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
