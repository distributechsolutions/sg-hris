package io.softwaregarage.hris.payroll.views;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.payroll.dtos.TaxRatesDTO;
import io.softwaregarage.hris.payroll.services.TaxRatesService;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_PAYROLL_MANAGER",
        "ROLE_PAYROLL_EMPLOYEE"})
@PageTitle("Tax Rates Form")
@Route(value = "tax-rates-form", layout = MainLayout.class)
public class TaxRatesFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final TaxRatesService taxRatesService;

    private TaxRatesDTO taxRatesDTO;
    private UUID parameterId;
    private String loggedInUser;

    private final FormLayout taxRatesDTOFormLayout = new FormLayout();
    private IntegerField taxYearField;
    private DatePicker effectiveDatePicker;
    private BigDecimalField lowerBoundAmountField, upperBoundAmountField, baseTaxAmountField, taxRateField;
    private ToggleButton activeTaxRateButton;

    public TaxRatesFormView(TaxRatesService taxRatesService) {
        this.taxRatesService = taxRatesService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        add(taxRatesDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, taxRatesDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            taxRatesDTO = taxRatesService.getById(parameterId);
        }

        buildTaxRatesFormLayout();
    }

    private void buildTaxRatesFormLayout() {
        taxYearField = new IntegerField("Tax Year");
        taxYearField.setRequired(true);
        taxYearField.setRequiredIndicatorVisible(true);
        taxYearField.setMin(LocalDate.now().getYear());
        taxYearField.setErrorMessage("This is not a valid year.");
        if (taxRatesDTO != null) taxYearField.setValue(Math.toIntExact(taxRatesDTO.getTaxYear()));

        effectiveDatePicker = new DatePicker("Effective Date");
        effectiveDatePicker.setRequired(true);
        effectiveDatePicker.setRequiredIndicatorVisible(true);
        effectiveDatePicker.setMin(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        if (taxRatesDTO != null) effectiveDatePicker.setValue(taxRatesDTO.getEffectiveDate());

        lowerBoundAmountField = new BigDecimalField("Lower Bound Amount");
        lowerBoundAmountField.setPlaceholder("0.0");
        lowerBoundAmountField.setPrefixComponent(new Span("PHP"));
        lowerBoundAmountField.setRequiredIndicatorVisible(true);
        lowerBoundAmountField.setClearButtonVisible(true);
        lowerBoundAmountField.setRequired(true);
        if (taxRatesDTO != null) lowerBoundAmountField.setValue(taxRatesDTO.getLowerBoundAmount());

        upperBoundAmountField = new BigDecimalField("Upper Bound Amount");
        upperBoundAmountField.setPlaceholder("0.0");
        upperBoundAmountField.setPrefixComponent(new Span("PHP"));
        upperBoundAmountField.setClearButtonVisible(true);
        if (taxRatesDTO != null) upperBoundAmountField.setValue(taxRatesDTO.getUpperBoundAmount());

        baseTaxAmountField = new BigDecimalField("Base Tax Amount");
        baseTaxAmountField.setPlaceholder("0.0");
        baseTaxAmountField.setPrefixComponent(new Span("PHP"));
        baseTaxAmountField.setRequiredIndicatorVisible(true);
        baseTaxAmountField.setClearButtonVisible(true);
        baseTaxAmountField.setRequired(true);
        if (taxRatesDTO != null) baseTaxAmountField.setValue(taxRatesDTO.getBaseTax());

        taxRateField = new BigDecimalField("Tax Rate");
        taxRateField.setPlaceholder("0.0");
        taxRateField.setSuffixComponent(new Span("%"));
        taxRateField.setRequiredIndicatorVisible(true);
        taxRateField.setClearButtonVisible(true);
        taxRateField.setRequired(true);
        if (taxRatesDTO != null) baseTaxAmountField.setValue(taxRatesDTO.getRate());

        activeTaxRateButton = new ToggleButton("Is Active Tax Rate?");
        activeTaxRateButton.getStyle().set("font-size", "0.875rem");
        activeTaxRateButton.getStyle().set("padding-top", "5px");
        if (taxRatesDTO != null) activeTaxRateButton.setValue(taxRatesDTO.isActiveTaxRate());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateTaxRatesDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(TaxRatesListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(TaxRatesListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        taxRatesDTOFormLayout.add(taxYearField,
                effectiveDatePicker,
                lowerBoundAmountField,
                upperBoundAmountField,
                baseTaxAmountField,
                taxRateField,
                activeTaxRateButton,
                buttonLayout);
        taxRatesDTOFormLayout.setColspan(buttonLayout, 2);
        taxRatesDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateTaxRatesDTO() {
        if (parameterId != null) {
            taxRatesDTO = taxRatesService.getById(parameterId);
        } else {
            taxRatesDTO = new TaxRatesDTO();
            taxRatesDTO.setCreatedBy(loggedInUser);
        }

        taxRatesDTO.setTaxYear(taxYearField.getValue().longValue());
        taxRatesDTO.setEffectiveDate(effectiveDatePicker.getValue());
        taxRatesDTO.setLowerBoundAmount(lowerBoundAmountField.getValue());
        taxRatesDTO.setUpperBoundAmount(upperBoundAmountField.getValue());
        taxRatesDTO.setBaseTax(baseTaxAmountField.getValue());
        taxRatesDTO.setRate(taxRateField.getValue());
        taxRatesDTO.setActiveTaxRate(activeTaxRateButton.getValue());
        taxRatesDTO.setUpdatedBy(loggedInUser);

        taxRatesService.saveOrUpdate(taxRatesDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a tax rate record.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
