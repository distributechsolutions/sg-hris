package io.softwaregarage.hris.commons.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.utils.StringUtil;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;

import java.util.Objects;

@PermitAll
@PageTitle("Change Password")
@Route(value = "change-password", layout = MainLayout.class)
public class ChangePasswordView extends VerticalLayout {
    @Resource
    private final UserService userService;
    private final String loggedInUser;
    private UserDTO userDTO;
    private final VerticalLayout layout = new VerticalLayout();

    public ChangePasswordView(UserService userService) {
        this.userService = userService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (loggedInUser != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        this.buildChangePasswordView();
        this.add(layout);
        this.setMargin(true);
    }

    public void buildChangePasswordView() {
        PasswordField newPassword = new PasswordField();
        newPassword.setLabel("Enter your new password");
        newPassword.setMaxWidth("550px");
        newPassword.setMinWidth("350px");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setLabel("Confirm your new password");
        confirmPassword.setMaxWidth("550px");
        confirmPassword.setMinWidth("350px");

        Button changePasswordButton = new Button("Save");
        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        changePasswordButton.addClickListener(e -> {
            boolean isError = true;
            String message = "";

            if (newPassword.getValue().isEmpty()) {
                message = "Your new password should not be empty.";
            } else if (newPassword.getValue().length() < 8) {
                message = "Your new password should be at least 8 characters.";
            } else if (confirmPassword.getValue().isEmpty()) {
                message = "Your confirm password should not be empty.";
            } else if (confirmPassword.getValue().length() < 8) {
                message = "Your confirm password should be at least 8 characters.";
            } else if (!newPassword.getValue().equals(confirmPassword.getValue())) {
                message = "Passwords do not match.";
            } else {
                message = "You have successfully changed your password.";
                isError = false;
            }

            // Display the notification message.
            Notification notification = new Notification();
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.addThemeVariants(isError ? NotificationVariant.LUMO_ERROR : NotificationVariant.LUMO_SUCCESS);
            notification.setText(message);
            notification.setDuration(5000);

            if (!isError) {
                userDTO.setPassword(StringUtil.encryptPassword(newPassword.getValue()));
                userDTO.setUpdatedBy(loggedInUser);
                userService.saveOrUpdate(userDTO);

                // Display the notification message before logging out.
                notification.open();

                // Logout to this system.
                SecurityUtil.logout();
            } else {
                notification.open();
            }
        });

        Button resetPasswordButton = new Button("Reset");
        resetPasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,  ButtonVariant.LUMO_CONTRAST);
        resetPasswordButton.addClickListener(e -> {
            newPassword.clear();
            confirmPassword.clear();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.add(changePasswordButton, resetPasswordButton);

        layout.add(new Paragraph("""
                                 Enter your new password at least 8 characters. 
                                 After saving it, you will be logged out to this application. 
                                 Login again to use the new password.
                                 """),
                   newPassword,
                   confirmPassword,
                   buttonLayout);
        layout.setSizeFull();
    }

}
