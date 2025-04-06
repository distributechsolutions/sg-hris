package com.whizservices.hris.views.compenben;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.compenben.LeaveBenefitsDTO;
import com.whizservices.hris.services.compenben.LeaveBenefitsService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Leave Benefits Details")
@Route(value = "leave-benefits-details", layout = MainLayout.class)
public class LeaveBenefitsDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final LeaveBenefitsService leaveBenefitsService;
    private LeaveBenefitsDTO leaveBenefitsDTO;

    private final FormLayout leaveBenefitsDetailsLayout = new FormLayout();

    public LeaveBenefitsDetailsView(LeaveBenefitsService leaveBenefitsService) {
        this.leaveBenefitsService = leaveBenefitsService;

        add(leaveBenefitsDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, leaveBenefitsDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        if (s != null) {
            UUID parameterId = UUID.fromString(s);
            leaveBenefitsDTO = leaveBenefitsService.getById(parameterId);
        }

        buildLeaveBenefitsDetailsLayout();
    }

    public void buildLeaveBenefitsDetailsLayout() {
        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(leaveBenefitsDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        String employeeName = leaveBenefitsDTO.getEmployeeDTO().getFirstName()
                .concat(" ")
                .concat(leaveBenefitsDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(leaveBenefitsDTO.getEmployeeDTO().getLastName())
                .concat(leaveBenefitsDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(leaveBenefitsDTO.getEmployeeDTO().getSuffix()) : "");

        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span leaveCodeLabelSpan = new Span("Leave Code");
        leaveCodeLabelSpan.getStyle().set("text-align", "right");

        Span leaveCodeValueSpan = new Span(leaveBenefitsDTO.getLeaveCode());
        leaveCodeValueSpan.getStyle().setFontWeight("bold");

        Span leaveTypeLabelSpan = new Span("Leave Type");
        leaveTypeLabelSpan.getStyle().set("text-align", "right");

        Span leaveTypeValueSpan = new Span(leaveBenefitsDTO.getLeaveType());
        leaveTypeValueSpan.getStyle().setFontWeight("bold");

        Span leaveForYearLabelSpan = new Span("Leave for Year");
        leaveForYearLabelSpan.getStyle().set("text-align", "right");

        Span leaveForYearValueSpan = new Span(String.valueOf(leaveBenefitsDTO.getLeaveForYear()));
        leaveForYearValueSpan.getStyle().setFontWeight("bold");

        Span leaveCountLabelSpan = new Span("Leave Count");
        leaveCountLabelSpan.getStyle().set("text-align", "right");

        Span leaveCountValueSpan = new Span(String.valueOf(leaveBenefitsDTO.getLeaveCount()));
        leaveCountValueSpan.getStyle().setFontWeight("bold");

        Span isLeaveActiveLabelSpan = new Span("Is Leave Active?");
        isLeaveActiveLabelSpan.getStyle().set("text-align", "right");

        Span isLeaveActiveValueSpan = new Span(leaveBenefitsDTO.isLeaveActive() ? "Yes" : "No");
        isLeaveActiveValueSpan.getStyle().setFontWeight("bold");

        leaveBenefitsDetailsLayout.add(employeeNoLabelSpan,
                                       employeeNoValueSpan,
                                       employeeNameLabelSpan,
                                       employeeNameValueSpan,
                                       leaveCodeLabelSpan,
                                       leaveCodeValueSpan,
                                       leaveTypeLabelSpan,
                                       leaveTypeValueSpan,
                                       leaveForYearLabelSpan,
                                       leaveForYearValueSpan,
                                       leaveCountLabelSpan,
                                       leaveCountValueSpan,
                                       isLeaveActiveLabelSpan,
                                       isLeaveActiveValueSpan);
        leaveBenefitsDetailsLayout.setWidth("720px");
    }
}
