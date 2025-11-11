package io.softwaregarage.hris.commons.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.utils.EmailUtil;
import io.softwaregarage.hris.utils.StringUtil;

import jakarta.annotation.Resource;

@AnonymousAllowed
@Route(value = "login")
@PageTitle(value = "Login | Software Garage HRIS")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    @Resource private final UserService userService;
    @Resource private final EmailUtil emailUtil;
    private final LoginForm loginForm = new LoginForm();
    private UserDTO userDTO;

    public LoginView(UserService userService, EmailUtil emailUtil) {
        this.userService = userService;
        this.emailUtil = emailUtil;

        loginForm.setAction("login");
        loginForm.addForgotPasswordListener(event -> {
            Dialog dialog = buildForgotPasswordDialog();
            this.getUI().ifPresent(ui -> ui.add(dialog));
            dialog.open();
        });

        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.add(new H1("Software Garage HRIS"), loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }

    public Dialog buildForgotPasswordDialog() {
        Dialog forgotPasswordDialog = new Dialog();
        forgotPasswordDialog.setModal(true);
        forgotPasswordDialog.setResizable(false);
        forgotPasswordDialog.setCloseOnOutsideClick(false);

        VerticalLayout layout = new VerticalLayout();

        TextField username = new TextField("Enter your username");
        username.setRequiredIndicatorVisible(true);
        username.setRequired(true);
        username.setWidthFull();

        EmailField userEmail = new EmailField("Enter your registered email");
        userEmail.setRequiredIndicatorVisible(true);
        userEmail.setRequired(true);
        userEmail.setWidthFull();

        Button submitButton = new Button("Send my new password to my registered email");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickListener(event -> {
            if (username.getValue().isEmpty()) {
                username.setErrorMessage("Username should not be empty or whitespace.");
            } else if (userEmail.getValue().isEmpty()) {
                userEmail.setErrorMessage("Email should not be empty or whitespace.");
            } else if (!userEmail.getValue().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                userEmail.setErrorMessage("This is not a valid email address");
            } else {
                userDTO = userService.getByUsername(username.getValue());
                String generatedRawPassword = StringUtil.generateRandomPassword();

                if (userDTO != null) {
                    userDTO.setPassword(StringUtil.encryptPassword(generatedRawPassword));
                    userDTO.setPasswordChanged(false);
                    userDTO.setUpdatedBy(username.getValue());

                    // Save the updated password and send it to the registered email.
                    userService.saveOrUpdate(userDTO);
                    emailUtil.sendForgotPasswordEmail(userEmail.getValue(),
                                                            userDTO.getEmployeeDTO().getEmployeeFullName(),
                                                            username.getValue(),
                                                            generatedRawPassword);

                    forgotPasswordDialog.close();

                    // Show message notification.
                    Notification notification = Notification.show("Your new password is now send to your email.",
                                                          5000,
                                                                  Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        cancelButton.addClickListener(event -> forgotPasswordDialog.close());

        layout.add(username, userEmail);

        forgotPasswordDialog.setHeaderTitle("Forgot password?");
        forgotPasswordDialog.add(layout);
        forgotPasswordDialog.getFooter().add(submitButton, cancelButton);

        return forgotPasswordDialog;
    }
}
