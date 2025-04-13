package io.distributechsolutions.hris.views.compenben;

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
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.distributechsolutions.hris.dtos.compenben.RatesDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.compenben.AllowanceService;
import io.distributechsolutions.hris.services.compenben.RatesService;
import io.distributechsolutions.hris.services.profile.EmployeeService;
import io.distributechsolutions.hris.utils.SecurityUtil;
import io.distributechsolutions.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Rates Form")
@Route(value = "rates-form", layout = MainLayout.class)
public class RatesFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final RatesService ratesService;
    @Resource private final EmployeeService employeeService;
    @Resource private final AllowanceService allowanceService;

    private RatesDTO ratesDTO;
    private UUID parameterId;
    private String loggedInUser;

    private final FormLayout ratesDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeDTO> employeeDTOComboBox;
    private RadioButtonGroup<String> rateTypeRadioButtonGroup;
    private BigDecimalField totalMonthlyAllowanceDecimalField,
                            basicRateDecimalField,
                            dailyRateDecimalField,
                            hourlyRateDecimalField,
                            overtimeHourlyRateDecimalField,
                            lateHourlyRateDecimalField,
                            absentDailyRateDecimalField;

    public RatesFormView(RatesService ratesService,
                         EmployeeService employeeService,
                         AllowanceService allowanceService) {
        this.ratesService = ratesService;
        this.employeeService = employeeService;
        this.allowanceService = allowanceService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        add(ratesDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, ratesDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
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

        rateTypeRadioButtonGroup = new RadioButtonGroup<>("Rate Type");
        rateTypeRadioButtonGroup.setItems("Monthly", "Daily");
        rateTypeRadioButtonGroup.setRequiredIndicatorVisible(true);
        rateTypeRadioButtonGroup.setRequired(true);
        if (ratesDTO != null) rateTypeRadioButtonGroup.setValue(ratesDTO.getRateType());

        totalMonthlyAllowanceDecimalField = new BigDecimalField("Total Monthly Allowance");
        totalMonthlyAllowanceDecimalField.setPlaceholder("0.00");
        totalMonthlyAllowanceDecimalField.setHelperText("Value will automatically get from the selected employee.");
        totalMonthlyAllowanceDecimalField.setPrefixComponent(new Span("PHP"));
        totalMonthlyAllowanceDecimalField.setReadOnly(true);
        if (ratesDTO != null) totalMonthlyAllowanceDecimalField.setValue(allowanceService.getSumOfAllowanceByEmployeeDTO(employeeDTOComboBox.getValue()));

        basicRateDecimalField = new BigDecimalField("Basic Rate");
        basicRateDecimalField.setPlaceholder("0.00");
        basicRateDecimalField.setRequired(true);
        basicRateDecimalField.setRequiredIndicatorVisible(true);
        basicRateDecimalField.setHelperText("Provide the monthly salary rate.");
        basicRateDecimalField.setPrefixComponent(new Span("PHP"));
        if (ratesDTO != null) basicRateDecimalField.setValue(ratesDTO.getBasicCompensationRate());

        dailyRateDecimalField = new BigDecimalField("Daily Rate");
        dailyRateDecimalField.setPlaceholder("0.00");
        dailyRateDecimalField.setHelperText("This is an auto-compute field.");
        dailyRateDecimalField.setPrefixComponent(new Span("PHP"));
        dailyRateDecimalField.setReadOnly(true);
        if (ratesDTO != null) dailyRateDecimalField.setValue(ratesDTO.getDailyCompensationRate());

        hourlyRateDecimalField = new BigDecimalField("Hourly Rate");
        hourlyRateDecimalField.setPlaceholder("0.00");
        hourlyRateDecimalField.setHelperText("This is an auto-compute field.");
        hourlyRateDecimalField.setPrefixComponent(new Span("PHP"));
        hourlyRateDecimalField.setReadOnly(true);
        if (ratesDTO != null) hourlyRateDecimalField.setValue(ratesDTO.getHourlyCompensationRate());

        overtimeHourlyRateDecimalField = new BigDecimalField("Overtime Hourly Rate");
        overtimeHourlyRateDecimalField.setPlaceholder("0.00");
        overtimeHourlyRateDecimalField.setHelperText("This is an auto-compute field.");
        overtimeHourlyRateDecimalField.setPrefixComponent(new Span("PHP"));
        overtimeHourlyRateDecimalField.setReadOnly(true);
        if (ratesDTO != null) overtimeHourlyRateDecimalField.setValue(ratesDTO.getOvertimeHourlyCompensationRate());

        lateHourlyRateDecimalField = new BigDecimalField("Late Hourly Rate");
        lateHourlyRateDecimalField.setPlaceholder("0.00");
        lateHourlyRateDecimalField.setHelperText("This is an auto-compute field.");
        lateHourlyRateDecimalField.setPrefixComponent(new Span("PHP"));
        lateHourlyRateDecimalField.setReadOnly(true);
        if (ratesDTO != null) lateHourlyRateDecimalField.setValue(ratesDTO.getLateHourlyDeductionRate());

        absentDailyRateDecimalField = new BigDecimalField("Absent Daily Rate");
        absentDailyRateDecimalField.setPlaceholder("0.00");
        absentDailyRateDecimalField.setHelperText("This is an auto-compute field.");
        absentDailyRateDecimalField.setPrefixComponent(new Span("PHP"));
        absentDailyRateDecimalField.setReadOnly(true);
        if (ratesDTO != null) absentDailyRateDecimalField.setValue(ratesDTO.getDailyAbsentDeductionRate());

        // Do the auto-compute of fields from the selected value of employee's combobox.
        employeeDTOComboBox.addValueChangeListener(event -> {
            EmployeeDTO employeeDTO = event.getValue();

            if (employeeDTO != null) {
                totalMonthlyAllowanceDecimalField.setValue(allowanceService.getSumOfAllowanceByEmployeeDTO(employeeDTO));
            } else {
                totalMonthlyAllowanceDecimalField.clear();
            }
        });

        basicRateDecimalField.addValueChangeListener(event -> {
            BigDecimal monthlySalaryRate = event.getValue();

            // Populate the computed values in each field.
            if (monthlySalaryRate != null) {
                BigDecimal totalMonthlyAllowance = totalMonthlyAllowanceDecimalField.getValue() != null ? totalMonthlyAllowanceDecimalField.getValue() : new BigDecimal("0.00");
                BigDecimal totalMonthlySalaryRate = monthlySalaryRate.add(totalMonthlyAllowance);
                BigDecimal dailySalaryRate = totalMonthlySalaryRate.divide(new BigDecimal("26"), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal hourlySalaryRate = dailySalaryRate.divide(new BigDecimal("8"), 2, BigDecimal.ROUND_HALF_UP);

                dailyRateDecimalField.setValue(dailySalaryRate);
                hourlyRateDecimalField.setValue(hourlySalaryRate);
                overtimeHourlyRateDecimalField.setValue(hourlySalaryRate);
                lateHourlyRateDecimalField.setValue(hourlySalaryRate);
                absentDailyRateDecimalField.setValue(dailySalaryRate);
            } else {
                dailyRateDecimalField.clear();
                hourlyRateDecimalField.clear();
                overtimeHourlyRateDecimalField.clear();
                lateHourlyRateDecimalField.clear();
                absentDailyRateDecimalField.clear();
            }

        });

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
                               rateTypeRadioButtonGroup,
                               totalMonthlyAllowanceDecimalField,
                               basicRateDecimalField,
                               dailyRateDecimalField,
                               hourlyRateDecimalField,
                               overtimeHourlyRateDecimalField,
                               lateHourlyRateDecimalField,
                               absentDailyRateDecimalField,
                               buttonLayout);
        ratesDTOFormLayout.setColspan(employeeDTOComboBox, 2);
        ratesDTOFormLayout.setColspan(buttonLayout, 2);
        ratesDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateRatesDTO() {
        if (parameterId != null) {
            ratesDTO = ratesService.getById(parameterId);
        } else {
            ratesDTO = new RatesDTO();
            ratesDTO.setCreatedBy(loggedInUser);
        }

        ratesDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        ratesDTO.setRateType(rateTypeRadioButtonGroup.getValue());
        ratesDTO.setBasicCompensationRate(basicRateDecimalField.getValue());
        ratesDTO.setDailyCompensationRate(dailyRateDecimalField.getValue());
        ratesDTO.setHourlyCompensationRate(hourlyRateDecimalField.getValue());
        ratesDTO.setOvertimeHourlyCompensationRate(overtimeHourlyRateDecimalField.getValue());
        ratesDTO.setLateHourlyDeductionRate(lateHourlyRateDecimalField.getValue());
        ratesDTO.setDailyAbsentDeductionRate(absentDailyRateDecimalField.getValue());
        ratesDTO.setUpdatedBy(loggedInUser);

        ratesService.saveOrUpdate(ratesDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a salary rate record.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
