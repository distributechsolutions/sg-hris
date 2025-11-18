package io.softwaregarage.hris.commons.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.utils.EmailUtil;
import io.softwaregarage.hris.utils.StringUtil;

import jakarta.annotation.Resource;

@AnonymousAllowed
@Route(value = "forgot-password")
@PageTitle(value = "Forgot Password | Software Garage HRIS")
public class ForgotPasswordView extends VerticalLayout {
    @Resource
    private final UserService userService;

    @Resource
    private final EmailUtil emailUtil;

    private UserDTO userDTO;

    public ForgotPasswordView(UserService userService, EmailUtil emailUtil) {
        this.userService = userService;
        this.emailUtil = emailUtil;


        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.setWrap(true);
        this.add(this.buildForgotPassword());
    }

    public Component buildForgotPassword() {
        VerticalLayout layout = new VerticalLayout();

        TextField username = new TextField("Enter your username");
        username.setRequiredIndicatorVisible(true);
        username.setRequired(true);
        username.setWidth("300px");

        TextField emailField = new TextField("Enter your email");
        emailField.setRequiredIndicatorVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        emailField.setWidth("300px"); // ensures visible size


        Button submitButton = new Button("Send my new password");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickListener(event -> {
            if (username.getValue().isEmpty()) {
                username.setErrorMessage("Username should not be empty or whitespace.");
            } else if (emailField.getValue().isEmpty()) {
                emailField.setErrorMessage("Email should not be empty or whitespace.");
            } else if (!emailField.getValue().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                emailField.setErrorMessage("This is not a valid email address");
            } else {
                userDTO = userService.getByUsername(username.getValue());
                String generatedRawPassword = StringUtil.generateRandomPassword();

                if (userDTO != null) {
                    userDTO.setPassword(StringUtil.encryptPassword(generatedRawPassword));
                    userDTO.setPasswordChanged(false);
                    userDTO.setUpdatedBy(username.getValue());

                    // Save the updated password and send it to the registered email.
                    userService.saveOrUpdate(userDTO);
                    emailUtil.sendForgotPasswordEmail(emailField.getValue(),
                            userDTO.getEmployeeDTO().getEmployeeFullName(),
                            username.getValue(),
                            generatedRawPassword);

                    this.getUI().ifPresent(ui -> ui.navigate("login"));

                    // Show message notification.
                    Notification notification = Notification.show("Your new password is now send to your email.",
                            5000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addClickListener(event -> this.getUI().ifPresent(
                ui -> ui.navigate("login")));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.add(submitButton, cancelButton);

        layout.setWidth("300px");
        layout.add(new H2("Forgot Password"),
                   username,
                   emailField,
                   buttonLayout);

        return layout;
    }
}
