package io.softwaregarage.hris.profile.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.profile.dtos.PositionProfileDTO;
import io.softwaregarage.hris.profile.services.PositionProfileService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Position Details")
@Route(value = "employee-position-details", layout = MainLayout.class)
public class PositionProfileDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final PositionProfileService positionProfileService;
    private PositionProfileDTO positionProfileDTO;

    private final FormLayout employeePositionDetailsLayout = new FormLayout();

    public PositionProfileDetailsView(PositionProfileService positionProfileService) {
        this.positionProfileService = positionProfileService;

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
            positionProfileDTO = positionProfileService.getById(parameterId);
        }

        buildEmployeePositionDetailsLayout();
    }

    public void buildEmployeePositionDetailsLayout() {
        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(positionProfileDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        String employeeName = positionProfileDTO.getEmployeeDTO().getFirstName().concat(" ")
                .concat(positionProfileDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(positionProfileDTO.getEmployeeDTO().getLastName())
                .concat(positionProfileDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(positionProfileDTO.getEmployeeDTO().getSuffix()) : "");

        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span positionLabelSpan = new Span("Position");
        positionLabelSpan.getStyle().set("text-align", "right");

        Span positionValueSpan = new Span(positionProfileDTO.getPositionDTO().getName());
        positionValueSpan.getStyle().setFontWeight("bold");

        Span isActivePositionLabelSpan = new Span("Is Active Position?");
        isActivePositionLabelSpan.getStyle().set("text-align", "right");

        Span isActivePositionValueSpan = new Span(positionProfileDTO.isCurrentPosition() ? "Yes" : "No");
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
