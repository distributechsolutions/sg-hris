package io.softwaregarage.hris.commons.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import io.softwaregarage.hris.configs.SecurityConfig;
import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.compenben.dtos.LeaveBenefitsDTO;
import io.softwaregarage.hris.profile.dtos.AddressProfileDTO;
import io.softwaregarage.hris.profile.dtos.DependentProfileDTO;
import io.softwaregarage.hris.profile.dtos.PersonalProfileDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.compenben.services.LeaveBenefitsService;
import io.softwaregarage.hris.profile.services.AddressProfileService;
import io.softwaregarage.hris.profile.services.DependentProfileService;
import io.softwaregarage.hris.profile.services.PersonalProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.utils.StringUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import org.vaadin.lineawesome.LineAwesomeIcon;

@PermitAll
@PageTitle("My Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
    @Resource private final UserService userService;
    @Resource private final PersonalProfileService personalProfileService;
    @Resource private final AddressProfileService addressProfileService;
    @Resource private final DependentProfileService dependentProfileService;
    @Resource private final LeaveBenefitsService leaveBenefitsService;

    private UserDTO userDTO;

    enum MessageLevel {
        INFO,
        SUCCESS,
        WARNING,
        DANGER
    }

    public DashboardView(UserService userService,
                         PersonalProfileService personalService,
                         AddressProfileService addressProfileService,
                         DependentProfileService dependentProfileService,
                         LeaveBenefitsService leaveBenefitsService) {
        this.userService = userService;
        this.personalProfileService = personalService;
        this.addressProfileService = addressProfileService;
        this.dependentProfileService = dependentProfileService;
        this.leaveBenefitsService = leaveBenefitsService;

        // Gets the user data transfer object based from the logged-in user.
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        // Shows the change password dialog if the user has not changed its password for the first time.
        if (userDTO != null && !userDTO.isPasswordChanged()) {
            buildChangePasswordDialog().open();
        }

        // This will show the notification messages for the incomplete employee profiles.
        Div profileMessageNotification;
        Div addressMessageNotification;
        Div dependentMessageNotification;
        Div notificationSectionDiv;

        H3 headerNotification = new H3("Notifications");
        headerNotification.getStyle().set("padding-bottom", "10px");

        if (userDTO != null) {
            notificationSectionDiv = new Div();
            notificationSectionDiv.add(headerNotification);

            PersonalProfileDTO personalProfileDTO = personalProfileService.getByEmployeeDTO(userDTO.getEmployeeDTO());
            List<AddressProfileDTO> addressProfileDTOList = addressProfileService.getByEmployeeDTO(userDTO.getEmployeeDTO());
            List<DependentProfileDTO> dependentProfileDTOList = dependentProfileService.getByEmployeeDTO(userDTO.getEmployeeDTO());

            // Shows the notification message if the user hasn't yet add its personal information.
            if (personalProfileDTO == null) {
                profileMessageNotification = buildNotification("Please add your personal information in 'My Profile' menu.",
                                                               MessageLevel.INFO,
                                                               LineAwesomeIcon.INFO_CIRCLE_SOLID.create());
                notificationSectionDiv.add(profileMessageNotification);
            }

            // Shows the notification message if the user hasn't yet add its address information.
            if (addressProfileDTOList.isEmpty()) {
                addressMessageNotification = buildNotification("Please add your address information in 'My Profile' menu.",
                                                               MessageLevel.INFO,
                                                               LineAwesomeIcon.INFO_CIRCLE_SOLID.create());
                notificationSectionDiv.add(addressMessageNotification);
            }

            // Shows the notification message if the user hasn't yet add its dependent information.
            if (dependentProfileDTOList.isEmpty()) {
                dependentMessageNotification = buildNotification("Please add your dependent information in 'My Profile' menu.",
                                                                 MessageLevel.INFO,
                                                                 LineAwesomeIcon.INFO_CIRCLE_SOLID.create());
                notificationSectionDiv.add(dependentMessageNotification);
            }

            if (personalProfileDTO != null && !addressProfileDTOList.isEmpty() && !dependentProfileDTOList.isEmpty()) {
                Span messageSpan = new Span("No notifications found. You are all caught up.");
                notificationSectionDiv.add(messageSpan);
            }

            this.add(notificationSectionDiv, this.buildLeaveBenefitsSection());
        }

        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
        this.setPadding(true);
    }

    private Dialog buildChangePasswordDialog() {
        Dialog changePasswordDialog = new Dialog("Change Password");

        PasswordField currentPasswordField = new PasswordField("Current Password");
        currentPasswordField.setRequired(true);
        currentPasswordField.setRequiredIndicatorVisible(true);

        PasswordField newPasswordField = new PasswordField("New Password");
        newPasswordField.setRequired(true);
        newPasswordField.setRequiredIndicatorVisible(true);

        PasswordField confirmNewPasswordField = new PasswordField("Confirm New Password");
        confirmNewPasswordField.setRequired(true);
        confirmNewPasswordField.setRequiredIndicatorVisible(true);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(clickEvent -> {
            Notification successNotification, errorNotification;
            boolean isCurrentPasswordMatch = new SecurityConfig().passwordEncoder().matches(currentPasswordField.getValue(), userDTO.getPassword());

            if (isCurrentPasswordMatch) {
                if (newPasswordField.getValue().equals(confirmNewPasswordField.getValue())) {
                    userDTO.setPassword(StringUtil.encryptPassword(newPasswordField.getValue()));
                    userDTO.setPasswordChanged(true);
                    userDTO.setUpdatedBy(Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername());
                    userDTO.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

                    userService.saveOrUpdate(userDTO);

                    successNotification = new Notification("You have successfully updated your password.", 5000, Notification.Position.TOP_CENTER);
                    successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    successNotification.open();

                    changePasswordDialog.close();
                } else {
                    errorNotification = new Notification("Your new password is not matched!", 5000, Notification.Position.TOP_CENTER);
                    errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    errorNotification.open();
                }
            } else {
                errorNotification = new Notification("Your current password is wrong!", 5000, Notification.Position.TOP_CENTER);
                errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                errorNotification.open();
            }
        });

        FormLayout changePasswordLayout = new FormLayout();
        changePasswordLayout.add(currentPasswordField,
                                 newPasswordField,
                                 confirmNewPasswordField);
        changePasswordLayout.setWidth("500px");

        changePasswordDialog.add(changePasswordLayout);
        changePasswordDialog.getFooter().add(saveButton);
        changePasswordDialog.setCloseOnOutsideClick(false);

        return changePasswordDialog;
    }

    private Div buildNotification(String message, MessageLevel messageLevel, SvgIcon svgIcon) {
        Div text = new Div(new Text(message));

        HorizontalLayout layout = new HorizontalLayout(svgIcon, text);
        layout.setAlignItems(Alignment.CENTER);

        Div notificationDiv = new Div();
        notificationDiv.getStyle().set("padding", "20px")
                                  .set("border-radius", "5px")
                                  .set("color", "#fdfefe")
                                  .set("margin-bottom", "5px");

        // Change the background color based on the message level.
        switch (messageLevel) {
            case INFO -> notificationDiv.getStyle().set("background-color", "#2196F3");
            case SUCCESS -> notificationDiv.getStyle().set("background-color", "#04AA6D");
            case WARNING -> notificationDiv.getStyle().set("background-color", "#ff9800");
            case DANGER -> notificationDiv.getStyle().set("background-color", "#f44336");
        }

        notificationDiv.add(layout);

        return notificationDiv;
    }

    private Div buildLeaveBenefitsSection() {
        Div leaveBenefitsSection = new Div();
        HorizontalLayout leaveBenefitsLayout = new HorizontalLayout();
        List<LeaveBenefitsDTO> leaveBenefitsDTOList = leaveBenefitsService.getByEmployeeDTO(userDTO.getEmployeeDTO());
        List<LeaveBenefitsDTO> filteredLeaveBenefitsDTOList = leaveBenefitsDTOList.stream().filter(leaveBenefitsDTO -> leaveBenefitsDTO.getLeaveForYear() == LocalDateTime.now(ZoneId.of("Asia/Manila")).getYear() && !leaveBenefitsDTO.getLeaveType().contains("Unpaid")).toList();

        leaveBenefitsSection.add(new H3("Leave Benefits"));

        if (!leaveBenefitsDTOList.isEmpty()) {
            for (LeaveBenefitsDTO leaveBenefitsDTO : filteredLeaveBenefitsDTOList) {
                H1 leaveCountHeader = new H1(String.valueOf(leaveBenefitsDTO.getLeaveCount()));

                Button applyLeaveButton = new Button("Apply");
                applyLeaveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                applyLeaveButton.addClickListener(event -> applyLeaveButton.getUI().ifPresent(ui -> ui.navigate(LeaveFilingView.class)));

                Card leaveCard = new Card();
                leaveCard.setHeader(leaveCountHeader);
                leaveCard.addToFooter(applyLeaveButton);
                leaveCard.add(new Paragraph(leaveBenefitsDTO.getLeaveType()));

                // Change the colors of leave count headers.
                if (leaveBenefitsDTO.getLeaveCount() == 3 && leaveBenefitsDTO.getLeaveCount() == 2) {
                    leaveCountHeader.getStyle().set("color", "#ffc107");
                } else if (leaveBenefitsDTO.getLeaveCount() == 1) {
                    leaveCountHeader.getStyle().set("color", "#dc3545");
                } else if (leaveBenefitsDTO.getLeaveCount() > 3) {
                    leaveCountHeader.getStyle().set("color", "#198754");
                } else {
                    leaveCountHeader.getStyle().set("color", "#adb5bd");
                }

                leaveBenefitsLayout.add(leaveCard);
            }

            leaveBenefitsSection.add(leaveBenefitsLayout);
        } else {
            Span messageSpan = new Span("No leave benefits currently assigned  to you.");
            leaveBenefitsSection.add(messageSpan);
        }

        return leaveBenefitsSection;
    }
}
