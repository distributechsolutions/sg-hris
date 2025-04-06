package com.whizservices.hris.views.compenben;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.compenben.GovernmentContributionsDTO;
import com.whizservices.hris.services.compenben.GovernmentContributionsService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Government Contributions Details")
@Route(value = "government-contributions-details", layout = MainLayout.class)
public class GovernmentContributionsDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final GovernmentContributionsService governmentContributionsService;
    private GovernmentContributionsDTO governmentContributionsDTO;

    private final FormLayout governmentContributionsDetailsLayout = new FormLayout();

    public GovernmentContributionsDetailsView(GovernmentContributionsService governmentContributionsService) {
        this.governmentContributionsService = governmentContributionsService;

        add(governmentContributionsDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, governmentContributionsDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        if (s != null) {
            UUID parameterId = UUID.fromString(s);
            governmentContributionsDTO = governmentContributionsService.getById(parameterId);
        }

        buildGovernmentContributionsDetailsLayout();
    }

    public void buildGovernmentContributionsDetailsLayout() {
        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(governmentContributionsDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        String employeeName = governmentContributionsDTO.getEmployeeDTO().getFirstName()
                                                       .concat(" ")
                                                       .concat(governmentContributionsDTO.getEmployeeDTO().getMiddleName())
                                                       .concat(" ")
                                                       .concat(governmentContributionsDTO.getEmployeeDTO().getLastName())
                                                       .concat(governmentContributionsDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(governmentContributionsDTO.getEmployeeDTO().getSuffix()) : "");

        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span sssAmountLabelSpan = new Span("SSS Contribution Amount");
        sssAmountLabelSpan.getStyle().set("text-align", "right");

        Span sssAmountValueSpan = new Span("PHP" + governmentContributionsDTO.getSssContributionAmount());
        sssAmountValueSpan.getStyle().setFontWeight("bold");

        Span hdmfAmountLabelSpan = new Span("HDMF Contribution Amount");
        hdmfAmountLabelSpan.getStyle().set("text-align", "right");

        Span hdmfAmountValueSpan = new Span("PHP" + governmentContributionsDTO.getHdmfContributionAmount());
        hdmfAmountValueSpan.getStyle().setFontWeight("bold");

        Span philhealthAmountLabelSpan = new Span("Philhealth Contribution Amount");
        philhealthAmountLabelSpan.getStyle().set("text-align", "right");

        Span philhealthAmountValueSpan = new Span("PHP" + governmentContributionsDTO.getPhilhealthContributionAmount());
        philhealthAmountValueSpan.getStyle().setFontWeight("bold");

        governmentContributionsDetailsLayout.add(employeeNoLabelSpan,
                employeeNoValueSpan,
                employeeNameLabelSpan,
                employeeNameValueSpan,
                sssAmountLabelSpan,
                sssAmountValueSpan,
                hdmfAmountLabelSpan,
                hdmfAmountValueSpan,
                philhealthAmountLabelSpan,
                philhealthAmountValueSpan);
        governmentContributionsDetailsLayout.setWidth("720px");
    }
}
