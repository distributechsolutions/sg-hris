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
import io.softwaregarage.hris.payroll.dtos.TaxRatesDTO;
import io.softwaregarage.hris.payroll.services.TaxRatesService;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_PAYROLL_MANAGER",
        "ROLE_PAYROLL_EMPLOYEE"})
@PageTitle("Tax Rates Details")
@Route(value = "tax-rates-details", layout = MainLayout.class)
public class TaxRatesDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final TaxRatesService taxRatesService;

    private TaxRatesDTO taxRatesDTO;

    private final FormLayout taxRatesDetailsLayout = new FormLayout();

    public TaxRatesDetailsView(TaxRatesService taxRatesService) {
        this.taxRatesService = taxRatesService;

        add(taxRatesDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, taxRatesDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            taxRatesDTO = taxRatesService.getById(parameterId);
        }

        buildTaxRatesDetailsLayout();
    }

    public void buildTaxRatesDetailsLayout() {
        Span taxYearLabelSpan = new Span("Tax Year");
        taxYearLabelSpan.getStyle().set("text-align", "right");

        Span taxYearValueSpan = new Span(String.valueOf(taxRatesDTO.getTaxYear()));
        taxYearValueSpan.getStyle().setFontWeight("bold");

        Span effectiveDateLabelSpan = new Span("Effective Date");
        effectiveDateLabelSpan.getStyle().set("text-align", "right");

        Span effectiveDateValueSpan = new Span(DateTimeFormatter.ofPattern("MMM dd, yyyy").format(taxRatesDTO.getEffectiveDate()));
        effectiveDateValueSpan.getStyle().setFontWeight("bold");

        Span lowerBoundAmountLabelSpan = new Span("Lower Bound Amount");
        lowerBoundAmountLabelSpan.getStyle().set("text-align", "right");

        Span lowerBoundAmountValueSpan = new Span("PHP " + String.valueOf(taxRatesDTO.getLowerBoundAmount()));
        lowerBoundAmountValueSpan.getStyle().setFontWeight("bold");

        Span upperBoundAmountLabelSpan = new Span("Lower Bound Amount");
        upperBoundAmountLabelSpan.getStyle().set("text-align", "right");

        Span upperBoundAmountValueSpan = new Span("PHP " + String.valueOf(taxRatesDTO.getUpperBoundAmount()));
        upperBoundAmountValueSpan.getStyle().setFontWeight("bold");

        Span baseTaxAmountLabelSpan = new Span("Base Tax Amount");
        baseTaxAmountLabelSpan.getStyle().set("text-align", "right");

        Span baseTaxAmountValueSpan = new Span("PHP " + String.valueOf(taxRatesDTO.getBaseTax()));
        baseTaxAmountValueSpan.getStyle().setFontWeight("bold");

        Span rateLabelSpan = new Span("Rate %");
        rateLabelSpan.getStyle().set("text-align", "right");

        Span rateValueSpan = new Span(String.valueOf(taxRatesDTO.getRate()));
        rateValueSpan.getStyle().setFontWeight("bold");

        Span activeRateLabelSpan = new Span("Is Active Tax Rate?");
        activeRateLabelSpan.getStyle().set("text-align", "right");

        Span activeRateValueSpan = new Span(taxRatesDTO.isActiveTaxRate() ? "Yes" : "No");
        activeRateValueSpan.getStyle().setFontWeight("bold");

        taxRatesDetailsLayout.add(taxYearLabelSpan,
                taxYearValueSpan,
                effectiveDateLabelSpan,
                effectiveDateValueSpan,
                lowerBoundAmountLabelSpan,
                lowerBoundAmountValueSpan,
                upperBoundAmountLabelSpan,
                upperBoundAmountValueSpan,
                baseTaxAmountLabelSpan,
                baseTaxAmountValueSpan,
                rateLabelSpan,
                rateValueSpan,
                activeRateLabelSpan,
                activeRateValueSpan);
        taxRatesDetailsLayout.setWidth("720px");
    }
}
