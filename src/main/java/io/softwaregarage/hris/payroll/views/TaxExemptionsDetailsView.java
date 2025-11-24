package io.softwaregarage.hris.payroll.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.payroll.dtos.TaxExemptionsDTO;
import io.softwaregarage.hris.payroll.services.TaxExemptionsService;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_HR_SUPERVISOR"})
@PageTitle("Tax Exemptions Details")
@Route(value = "tax-exemptions-details", layout = MainLayout.class)
public class TaxExemptionsDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final TaxExemptionsService taxExemptionsService;

    private TaxExemptionsDTO taxExemptionsDTO;

    private final FormLayout taxExemptionsDetailsLayout = new FormLayout();

    public TaxExemptionsDetailsView(TaxExemptionsService taxExemptionsService) {
        this.taxExemptionsService = taxExemptionsService;

        add(taxExemptionsDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, taxExemptionsDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            taxExemptionsDTO = taxExemptionsService.getById(parameterId);
        }

        buildTaxExemptionsDetailsLayout();
    }

    public void buildTaxExemptionsDetailsLayout() {
        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(taxExemptionsDTO.getEmployeeProfileDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        String employeeName = taxExemptionsDTO.getEmployeeProfileDTO().getFirstName()
                .concat(" ")
                .concat(taxExemptionsDTO.getEmployeeProfileDTO().getMiddleName())
                .concat(" ")
                .concat(taxExemptionsDTO.getEmployeeProfileDTO().getLastName())
                .concat(taxExemptionsDTO.getEmployeeProfileDTO().getSuffix() != null ? " ".concat(taxExemptionsDTO.getEmployeeProfileDTO().getSuffix()) : "");

        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span exemptionPercentageLabelSpan = new Span("Tax Exemption Percentage");
        exemptionPercentageLabelSpan.getStyle().set("text-align", "right");

        Span exemptionPercentageValueSpan = new Span(String.valueOf(taxExemptionsDTO.getTaxExemptionPercentage()));
        exemptionPercentageValueSpan.getStyle().setFontWeight("bold");

        Span activeExemptionLabelSpan = new Span("Is Active Tax Exemption?");
        activeExemptionLabelSpan.getStyle().set("text-align", "right");

        Span activeExemptionValueSpan = new Span(taxExemptionsDTO.isActiveTaxExemption() ? "Yes" : "No");
        activeExemptionValueSpan.getStyle().setFontWeight("bold");

        taxExemptionsDetailsLayout.add(employeeNoLabelSpan,
                employeeNoValueSpan,
                employeeNameLabelSpan,
                employeeNameValueSpan,
                exemptionPercentageLabelSpan,
                exemptionPercentageValueSpan,
                activeExemptionLabelSpan,
                activeExemptionLabelSpan);
        taxExemptionsDetailsLayout.setWidth("720px");
    }
}
