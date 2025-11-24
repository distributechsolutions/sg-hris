package io.softwaregarage.hris.payroll.views;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.payroll.dtos.TaxExemptionsDTO;
import io.softwaregarage.hris.payroll.services.TaxExemptionsService;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_HR_SUPERVISOR"})
@PageTitle("Tax Exemptions Form")
@Route(value = "tax-exemptions-form", layout = MainLayout.class)
public class TaxExemptionsFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final TaxExemptionsService taxExemptionsService;

    @Resource
    private final EmployeeProfileService employeeProfileService;

    private TaxExemptionsDTO taxExemptionsDTO;
    private UUID parameterId;
    private String loggedInUser;

    private final FormLayout taxExemptionsDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeProfileDTO> employeeDTOComboBox;
    private BigDecimalField taxExemptionPercentageField;
    private ToggleButton activeTaxExemptionsButton;

    public TaxExemptionsFormView(TaxExemptionsService taxExemptionsService,
                         EmployeeProfileService employeeProfileService) {
        this.taxExemptionsService = taxExemptionsService;
        this.employeeProfileService = employeeProfileService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        add(taxExemptionsDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, taxExemptionsDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            taxExemptionsDTO = taxExemptionsService.getById(parameterId);
        }

        buildTaxExemptionsFormLayout();
    }

    private void buildTaxExemptionsFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeProfileDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                employeeProfileService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeProfileDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (taxExemptionsDTO != null) employeeDTOComboBox.setValue(taxExemptionsDTO.getEmployeeProfileDTO());

        taxExemptionPercentageField = new BigDecimalField("Tax Exemption Percentage");
        taxExemptionPercentageField.setPlaceholder("0.0");
        taxExemptionPercentageField.setHelperText("Please input the percentage of tax exemptions in decimal value.");
        taxExemptionPercentageField.setSuffixComponent(new Span("%"));
        taxExemptionPercentageField.setRequiredIndicatorVisible(true);
        taxExemptionPercentageField.setClearButtonVisible(true);
        taxExemptionPercentageField.setRequired(true);
        if (taxExemptionsDTO != null) taxExemptionPercentageField.setValue(BigDecimal.valueOf(taxExemptionsDTO.getTaxExemptionPercentage()));

        activeTaxExemptionsButton = new ToggleButton("Is Active Tax Exemption?");
        activeTaxExemptionsButton.getStyle().set("font-size", "0.875rem");
        activeTaxExemptionsButton.getStyle().set("padding-top", "5px");
        if (taxExemptionsDTO != null) activeTaxExemptionsButton.setValue(taxExemptionsDTO.isActiveTaxExemption());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateTaxExemptionsDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(TaxExemptionsListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(TaxExemptionsListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        taxExemptionsDTOFormLayout.add(employeeDTOComboBox,
                taxExemptionPercentageField,
                activeTaxExemptionsButton,
                buttonLayout);
        taxExemptionsDTOFormLayout.setColspan(employeeDTOComboBox, 2);
        taxExemptionsDTOFormLayout.setColspan(buttonLayout, 2);
        taxExemptionsDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateTaxExemptionsDTO() {
        if (parameterId != null) {
            taxExemptionsDTO = taxExemptionsService.getById(parameterId);
        } else {
            taxExemptionsDTO = new TaxExemptionsDTO();
            taxExemptionsDTO.setCreatedBy(loggedInUser);
        }

        taxExemptionsDTO.setEmployeeProfileDTO(employeeDTOComboBox.getValue());
        taxExemptionsDTO.setTaxExemptionPercentage(taxExemptionPercentageField.getValue().doubleValue());
        taxExemptionsDTO.setActiveTaxExemption(activeTaxExemptionsButton.getValue());
        taxExemptionsDTO.setUpdatedBy(loggedInUser);

        taxExemptionsService.saveOrUpdate(taxExemptionsDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a tax exemption record.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
