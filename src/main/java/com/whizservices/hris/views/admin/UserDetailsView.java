package com.whizservices.hris.views.admin;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.whizservices.hris.dtos.admin.UserDTO;
import com.whizservices.hris.services.admin.UserService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER"})
@PageTitle("User Details")
@Route(value = "user-details", layout = MainLayout.class)
public class UserDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final UserService userService;
    private UserDTO userDTO;

    private final FormLayout userDetailsLayout = new FormLayout();

    public UserDetailsView(UserService userService) {
        this.userService = userService;

        setSizeFull();
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        add(userDetailsLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            userDTO = userService.getById(parameterId);
        }

        buildUserDetailsLayout();
    }

    public void buildUserDetailsLayout() {
        Span employeeFullNameLabelSpan = new Span("Employee Name");
        employeeFullNameLabelSpan.getStyle().set("text-align", "right");

        String fullName = userDTO.getEmployeeDTO().getFirstName().concat(" ")
                .concat(userDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(userDTO.getEmployeeDTO().getLastName())
                .concat(userDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(userDTO.getEmployeeDTO().getSuffix()) : "");

        Span employeeFullNameValueSpan = new Span(fullName);
        employeeFullNameValueSpan.getStyle().setFontWeight("bold");

        Span usernameLabelSpan = new Span("Username");
        usernameLabelSpan.getStyle().set("text-align", "right");

        Span usernameValueSpan = new Span(userDTO.getUsername());
        usernameValueSpan.getStyle().setFontWeight("bold");

        Span roleLabelSpan = new Span("Role");
        roleLabelSpan.getStyle().set("text-align", "right");

        Span roleValueSpan = new Span(userDTO.getRole());
        roleValueSpan.getStyle().setFontWeight("bold");

        Span emailLabelSpan = new Span("Email");
        emailLabelSpan.getStyle().set("text-align", "right");

        Span emailValueSpan = new Span(userDTO.getEmailAddress());
        emailValueSpan.getStyle().setFontWeight("bold");

        Span accountActiveLabelSpan = new Span("Is Active?");
        accountActiveLabelSpan.getStyle().set("text-align", "right");

        Span accountActiveValueSpan = new Span(userDTO.isAccountActive() ? "Yes" : "No");
        accountActiveValueSpan.getStyle().setFontWeight("bold");

        Span passwordChangeLabelSpan = new Span("Is Password Changed?");
        passwordChangeLabelSpan.getStyle().set("text-align", "right");

        Span passwordChangedValueSpan = new Span(userDTO.isPasswordChanged() ? "Yes" : "No");
        passwordChangedValueSpan.getStyle().setFontWeight("bold");

        userDetailsLayout.add(employeeFullNameLabelSpan,
                              employeeFullNameValueSpan,
                              usernameLabelSpan,
                              usernameValueSpan,
                              roleLabelSpan,
                              roleValueSpan,
                              emailLabelSpan,
                              emailValueSpan,
                              accountActiveLabelSpan,
                              accountActiveValueSpan,
                              passwordChangeLabelSpan,
                              passwordChangedValueSpan);
        userDetailsLayout.setWidth("720px");
    }
}
