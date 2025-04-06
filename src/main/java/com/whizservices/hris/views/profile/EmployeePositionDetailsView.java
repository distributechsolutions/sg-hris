package com.whizservices.hris.views.profile;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.profile.EmployeePositionDTO;
import com.whizservices.hris.services.profile.EmployeePositionService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Position Details")
@Route(value = "employee-position-details", layout = MainLayout.class)
public class EmployeePositionDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final EmployeePositionService employeePositionService;
    private EmployeePositionDTO employeePositionDTO;

    private final FormLayout employeePositionDetailsLayout = new FormLayout();

    public EmployeePositionDetailsView(EmployeePositionService employeePositionService) {
        this.employeePositionService = employeePositionService;

        add(employeePositionDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, employeePositionDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            employeePositionDTO = employeePositionService.getById(parameterId);
        }

        buildEmployeePositionDetailsLayout();
    }

    public void buildEmployeePositionDetailsLayout() {
        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(employeePositionDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        String employeeName = employeePositionDTO.getEmployeeDTO().getFirstName().concat(" ")
                .concat(employeePositionDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(employeePositionDTO.getEmployeeDTO().getLastName())
                .concat(employeePositionDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(employeePositionDTO.getEmployeeDTO().getSuffix()) : "");

        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span positionLabelSpan = new Span("Position");
        positionLabelSpan.getStyle().set("text-align", "right");

        Span positionValueSpan = new Span(employeePositionDTO.getPositionDTO().getName());
        positionValueSpan.getStyle().setFontWeight("bold");

        Span isActivePositionLabelSpan = new Span("Is Active Position?");
        isActivePositionLabelSpan.getStyle().set("text-align", "right");

        Span isActivePositionValueSpan = new Span(employeePositionDTO.isCurrentPosition() ? "Yes" : "No");
        isActivePositionValueSpan.getStyle().setFontWeight("bold");

        employeePositionDetailsLayout.add(employeeNoLabelSpan,
                                          employeeNoValueSpan,
                                          employeeNameLabelSpan,
                                          employeeNameValueSpan,
                                          positionLabelSpan,
                                          positionValueSpan,
                                          isActivePositionLabelSpan,
                                          isActivePositionValueSpan);
        employeePositionDetailsLayout.setWidth("720px");
    }
}
