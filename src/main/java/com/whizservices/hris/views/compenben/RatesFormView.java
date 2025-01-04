package com.whizservices.hris.views.compenben;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.compenben.RatesDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.compenben.RatesService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Rates Form")
@Route(value = "rates-form", layout = MainLayout.class)
public class RatesFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final RatesService ratesService;
    @Resource
    private final EmployeeService employeeService;

    private RatesDTO ratesDTO;
    private UUID parameterId;

    private final FormLayout ratesDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeDTO> employeeDTOComboBox;
    private BigDecimalField monthlyRateDecimalField,
                            dailyRateDecimalField,
                            hourlyRateDecimalField,
                            overtimeHourlyRateDecimalField,
                            lateHourlyRateDecimalField,
                            absentDailyRateDecimalField;
    private Checkbox currentRatesCheckbox;

    public RatesFormView(RatesService ratesService,
                         EmployeeService employeeService) {
        this.ratesService = ratesService;
        this.employeeService = employeeService;

        setSizeFull();
        setMargin(true);
        add(ratesDTOFormLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            ratesDTO = ratesService.getById(parameterId);
        }

        buildRatesFormLayout();
    }

    private void buildRatesFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                                                                    employeeService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (ratesDTO != null) employeeDTOComboBox.setValue(ratesDTO.getEmployeeDTO());

        // Add a prefix div label for each of the decimal fields.
        Div phpPrefix = new Div();
        phpPrefix.setText("PHP");

        monthlyRateDecimalField = new BigDecimalField("Monthly Rate");
        monthlyRateDecimalField.setPlaceholder("0.00");
        monthlyRateDecimalField.setRequired(true);
        monthlyRateDecimalField.setRequiredIndicatorVisible(true);
        monthlyRateDecimalField.setHelperText("If it is not required, just set its value to 0.00");
        monthlyRateDecimalField.setPrefixComponent(phpPrefix);
        if (ratesDTO != null) monthlyRateDecimalField.setValue(ratesDTO.getMonthlyRate());

        dailyRateDecimalField = new BigDecimalField("Daily Rate");
        dailyRateDecimalField.setPlaceholder("0.00");
        dailyRateDecimalField.setRequired(true);
        dailyRateDecimalField.setRequiredIndicatorVisible(true);
        dailyRateDecimalField.setHelperText("If it is not required, just set its value to 0.00");
        dailyRateDecimalField.setPrefixComponent(phpPrefix);
        if (ratesDTO != null) dailyRateDecimalField.setValue(ratesDTO.getDailyRate());

        hourlyRateDecimalField = new BigDecimalField("Hourly Rate");
        hourlyRateDecimalField.setPlaceholder("0.00");
        hourlyRateDecimalField.setRequired(true);
        hourlyRateDecimalField.setRequiredIndicatorVisible(true);
        hourlyRateDecimalField.setHelperText("If it is not required, just set its value to 0.00");
        hourlyRateDecimalField.setPrefixComponent(phpPrefix);
        if (ratesDTO != null) hourlyRateDecimalField.setValue(ratesDTO.getHourlyRate());

        overtimeHourlyRateDecimalField = new BigDecimalField("Overtime Hourly Rate");
        overtimeHourlyRateDecimalField.setPlaceholder("0.00");
        overtimeHourlyRateDecimalField.setRequired(true);
        overtimeHourlyRateDecimalField.setRequiredIndicatorVisible(true);
        overtimeHourlyRateDecimalField.setHelperText("If it is not required, just set its value to 0.00");
        overtimeHourlyRateDecimalField.setPrefixComponent(phpPrefix);
        if (ratesDTO != null) overtimeHourlyRateDecimalField.setValue(ratesDTO.getOvertimeHourlyRate());

        lateHourlyRateDecimalField = new BigDecimalField("Late Hourly Rate");
        lateHourlyRateDecimalField.setPlaceholder("0.00");
        lateHourlyRateDecimalField.setRequired(true);
        lateHourlyRateDecimalField.setRequiredIndicatorVisible(true);
        lateHourlyRateDecimalField.setHelperText("If it is not required, just set its value to 0.00");
        lateHourlyRateDecimalField.setPrefixComponent(phpPrefix);
        if (ratesDTO != null) lateHourlyRateDecimalField.setValue(ratesDTO.getLateHourlyRate());

        absentDailyRateDecimalField = new BigDecimalField("Absent Daily Rate");
        absentDailyRateDecimalField.setPlaceholder("0.00");
        absentDailyRateDecimalField.setRequired(true);
        absentDailyRateDecimalField.setRequiredIndicatorVisible(true);
        absentDailyRateDecimalField.setHelperText("If it is not required, just set its value to 0.00");
        absentDailyRateDecimalField.setPrefixComponent(phpPrefix);
        if (ratesDTO != null) absentDailyRateDecimalField.setValue(ratesDTO.getAbsentDailyRate());

        currentRatesCheckbox = new Checkbox("Is Current Rate?");
        if (ratesDTO != null) currentRatesCheckbox.setValue(ratesDTO.isCurrentRates());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateRatesDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(RatesListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(RatesListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        ratesDTOFormLayout.add(employeeDTOComboBox,
                               monthlyRateDecimalField,
                               dailyRateDecimalField,
                               hourlyRateDecimalField,
                               overtimeHourlyRateDecimalField,
                               lateHourlyRateDecimalField,
                               absentDailyRateDecimalField,
                               currentRatesCheckbox,
                               buttonLayout);
        ratesDTOFormLayout.setColspan(employeeDTOComboBox, 2);
        ratesDTOFormLayout.setColspan(buttonLayout, 2);
        ratesDTOFormLayout.setMaxWidth("768px");
    }

    private void saveOrUpdateRatesDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            ratesDTO = ratesService.getById(parameterId);
        } else {
            ratesDTO = new RatesDTO();
            ratesDTO.setCreatedBy(loggedInUser);
        }

        ratesDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        ratesDTO.setMonthlyRate(monthlyRateDecimalField.getValue());
        ratesDTO.setDailyRate(dailyRateDecimalField.getValue());
        ratesDTO.setHourlyRate(hourlyRateDecimalField.getValue());
        ratesDTO.setOvertimeHourlyRate(overtimeHourlyRateDecimalField.getValue());
        ratesDTO.setLateHourlyRate(lateHourlyRateDecimalField.getValue());
        ratesDTO.setAbsentDailyRate(absentDailyRateDecimalField.getValue());
        ratesDTO.setCurrentRates(currentRatesCheckbox.getValue());
        ratesDTO.setUpdatedBy(loggedInUser);

        ratesService.saveOrUpdate(ratesDTO);
    }
}
