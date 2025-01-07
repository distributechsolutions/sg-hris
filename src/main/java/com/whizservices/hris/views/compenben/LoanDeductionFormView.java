package com.whizservices.hris.views.compenben;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import com.whizservices.hris.dtos.compenben.LoanDeductionDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.compenben.LoanDeductionService;
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
@PageTitle("Loan Deduction Form")
@Route(value = "loan-deduction-form", layout = MainLayout.class)
public class LoanDeductionFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final LoanDeductionService loanDeductionService;
    @Resource private final EmployeeService employeeService;

    private LoanDeductionDTO loanDeductionDTO;
    private UUID parameterId;

    private final FormLayout loanDeductionsDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeDTO> employeeDTOComboBox;
    private ComboBox<String> loanTypeComboBox;
    private TextField loanDescriptionTextField;
    private DatePicker loanStartDatePicker, loanEndDatePicker;
    private BigDecimalField loanAmountField, monthlyDeductionField;

    public LoanDeductionFormView(LoanDeductionService loanDeductionService,
                                 EmployeeService employeeService) {
        this.loanDeductionService = loanDeductionService;
        this.employeeService = employeeService;

        setSizeFull();
        setMargin(true);
        add(loanDeductionsDTOFormLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        if (s != null) {
            parameterId = UUID.fromString(s);
            loanDeductionDTO = loanDeductionService.getById(parameterId);
        }

        buildLoanDeductionsFormLayout();
    }

    private void buildLoanDeductionsFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                                                                                       employeeService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (loanDeductionDTO != null) employeeDTOComboBox.setValue(loanDeductionDTO.getEmployeeDTO());

        loanTypeComboBox = new ComboBox<>("Type");
        loanTypeComboBox.setItems("SSS Loan",
                                  "PagIbig (HDMF) Loan",
                                  "Bank Loan",
                                  "Company Loan");
        loanTypeComboBox.setClearButtonVisible(true);
        loanTypeComboBox.setRequired(true);
        loanTypeComboBox.setRequiredIndicatorVisible(true);
        if (loanDeductionDTO != null) loanTypeComboBox.setValue(loanDeductionDTO.getLoanType());

        loanDescriptionTextField = new TextField("Description");
        loanDescriptionTextField.setClearButtonVisible(true);
        loanDescriptionTextField.setRequired(true);
        loanDescriptionTextField.setRequiredIndicatorVisible(true);
        if (loanDeductionDTO != null) loanDescriptionTextField.setValue(loanDeductionDTO.getLoanDescription());

        loanStartDatePicker = new DatePicker("Start Date");
        loanStartDatePicker.setClearButtonVisible(true);
        loanStartDatePicker.setRequired(true);
        loanStartDatePicker.setRequiredIndicatorVisible(true);
        if (loanDeductionDTO != null) loanStartDatePicker.setValue(loanDeductionDTO.getLoanStartDate());

        loanEndDatePicker = new DatePicker("End Date");
        loanEndDatePicker.setClearButtonVisible(true);
        loanEndDatePicker.setRequired(true);
        loanEndDatePicker.setRequiredIndicatorVisible(true);
        if (loanDeductionDTO != null) loanEndDatePicker.setValue(loanDeductionDTO.getLoanEndDate());

        loanAmountField = new BigDecimalField("Loan Amount");
        loanAmountField.setPlaceholder("0.00");
        loanAmountField.setRequired(true);
        loanAmountField.setRequiredIndicatorVisible(true);
        loanAmountField.setPrefixComponent(new Span("PHP "));
        if (loanDeductionDTO != null) loanAmountField.setValue(loanDeductionDTO.getLoanAmount());

        monthlyDeductionField = new BigDecimalField("Monthly Deduction");
        monthlyDeductionField.setPlaceholder("0.00");
        monthlyDeductionField.setRequired(true);
        monthlyDeductionField.setRequiredIndicatorVisible(true);
        monthlyDeductionField.setPrefixComponent(new Span("PHP "));
        if (loanDeductionDTO != null) monthlyDeductionField.setValue(loanDeductionDTO.getMonthlyDeduction());


        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateLoanDeductionsDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(LoanDeductionListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(LoanDeductionListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        loanDeductionsDTOFormLayout.add(employeeDTOComboBox,
                                        loanTypeComboBox,
                                        loanDescriptionTextField,
                                        loanStartDatePicker,
                                        loanEndDatePicker,
                                        loanAmountField,
                                        monthlyDeductionField,
                                        buttonLayout);
        loanDeductionsDTOFormLayout.setColspan(employeeDTOComboBox, 2);
        loanDeductionsDTOFormLayout.setColspan(buttonLayout, 2);
        loanDeductionsDTOFormLayout.setMaxWidth("768px");
    }

    private void saveOrUpdateLoanDeductionsDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            loanDeductionDTO = loanDeductionService.getById(parameterId);
        } else {
            loanDeductionDTO = new LoanDeductionDTO();
            loanDeductionDTO.setCreatedBy(loggedInUser);
        }

        loanDeductionDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        loanDeductionDTO.setLoanType(loanTypeComboBox.getValue());
        loanDeductionDTO.setLoanDescription(loanDescriptionTextField.getValue());
        loanDeductionDTO.setLoanStartDate(loanStartDatePicker.getValue());
        loanDeductionDTO.setLoanEndDate(loanEndDatePicker.getValue());
        loanDeductionDTO.setLoanAmount(loanAmountField.getValue());
        loanDeductionDTO.setMonthlyDeduction(monthlyDeductionField.getValue());
        loanDeductionDTO.setUpdatedBy(loggedInUser);

        loanDeductionService.saveOrUpdate(loanDeductionDTO);
    }
}
