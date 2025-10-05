package io.softwaregarage.hris.profile.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Form")
@Route(value = "employee-form", layout = MainLayout.class)
public class EmployeeProfileFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final EmployeeProfileService employeeProfileService;
    private EmployeeProfileDTO employeeProfileDTO;
    private UUID parameterId;

    private final FormLayout employeeDTOFormLayout = new FormLayout();
    private TextField employeeNoTextField,
                      lastNameTextField,
                      firstNameTextField,
                      middleNameTextField,
                      suffixTextField;
    private ComboBox<String> genderComboBox,
                             employmentTypeComboBox,
                             contractDurationComboBox,
                             statusComboBox;
    private DatePicker dateHiredDatePicker,
                       dateResignedDatePicker,
                       startDatePicker,
                       endDatePicker;

    public EmployeeProfileFormView(EmployeeProfileService employeeProfileService) {
        this.employeeProfileService = employeeProfileService;

        add(employeeDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, employeeDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            employeeProfileDTO = employeeProfileService.getById(parameterId);
        }

        buildEmployeeFormLayout();
    }

    private void buildEmployeeFormLayout() {
        employeeNoTextField = new TextField("Employee Number");
        employeeNoTextField.setRequired(true);
        employeeNoTextField.setAllowedCharPattern("\\d*");
        employeeNoTextField.setMaxLength(10);
        employeeNoTextField.setClearButtonVisible(true);
        if (employeeProfileDTO != null) employeeNoTextField.setValue(employeeProfileDTO.getEmployeeNumber());

        lastNameTextField = new TextField("Last Name");
        lastNameTextField.setRequired(true);
        lastNameTextField.setMinLength(2);
        lastNameTextField.setMaxLength(50);
        lastNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        lastNameTextField.setClearButtonVisible(true);
        if (employeeProfileDTO != null) lastNameTextField.setValue(employeeProfileDTO.getLastName());

        firstNameTextField = new TextField("First Name");
        firstNameTextField.setRequired(true);
        firstNameTextField.setMinLength(2);
        firstNameTextField.setMaxLength(50);
        firstNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        firstNameTextField.setClearButtonVisible(true);
        if (employeeProfileDTO != null) firstNameTextField.setValue(employeeProfileDTO.getFirstName());

        middleNameTextField = new TextField("Middle Name");
        middleNameTextField.setMinLength(2);
        middleNameTextField.setMaxLength(50);
        middleNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        middleNameTextField.setClearButtonVisible(true);
        if (employeeProfileDTO != null) middleNameTextField.setValue(employeeProfileDTO.getMiddleName());

        suffixTextField = new TextField("Suffix");
        suffixTextField.setPlaceholder("Sr, Jr, III, IV...");
        suffixTextField.setMinLength(2);
        suffixTextField.setMaxLength(5);
        suffixTextField.setAllowedCharPattern("[a-zA-Z]*");
        suffixTextField.setWidth("25%");
        suffixTextField.setClearButtonVisible(true);
        if (employeeProfileDTO != null && employeeProfileDTO.getSuffix() != null) suffixTextField.setValue(employeeProfileDTO.getSuffix());

        genderComboBox = new ComboBox<>("Gender");
        genderComboBox.setRequired(true);
        genderComboBox.setItems("Male", "Female");
        genderComboBox.setClearButtonVisible(true);
        if (employeeProfileDTO != null) genderComboBox.setValue(employeeProfileDTO.getGender());

        dateHiredDatePicker = new DatePicker("Date Hired");
        dateHiredDatePicker.setRequired(true);
        dateHiredDatePicker.setClearButtonVisible(true);
        if (employeeProfileDTO != null) dateHiredDatePicker.setValue(employeeProfileDTO.getDateHired());

        dateResignedDatePicker = new DatePicker("Date Resigned");
        dateResignedDatePicker.setClearButtonVisible(true);
        if (employeeProfileDTO != null && employeeProfileDTO.getDateResigned() != null) dateResignedDatePicker.setValue(employeeProfileDTO.getDateResigned());

        employmentTypeComboBox = new ComboBox<>("Employment Type");
        employmentTypeComboBox.setRequired(true);
        employmentTypeComboBox.setItems("REGULAR", "PROBATIONARY", "CONTRACTUAL");
        employmentTypeComboBox.setClearButtonVisible(true);
        if (employeeProfileDTO != null) employmentTypeComboBox.setValue(employeeProfileDTO.getEmploymentType());

        // Contract duration list value.
        String[] contractDurations = {"12 months",
                                      "11 months",
                                      "10 months",
                                      "9 months",
                                      "8 months",
                                      "7 months",
                                      "6 months",
                                      "5 months",
                                      "4 months",
                                      "3 months",
                                      "2 months",
                                      "1 months",
                                      "NONE"};

        contractDurationComboBox = new ComboBox<>("Contract Duration");
        contractDurationComboBox.setRequired(true);
        contractDurationComboBox.setClearButtonVisible(true);
        contractDurationComboBox.setItems(contractDurations);
        if (employeeProfileDTO != null) contractDurationComboBox.setValue(employeeProfileDTO.getContractDuration());

        employmentTypeComboBox.addValueChangeListener(event -> {
            String selectedType = event.getValue();

            Stream<String> filteredStream = Arrays.stream(contractDurations);

            if ("PROBATIONARY".equalsIgnoreCase(selectedType)) {
                filteredStream = filteredStream.filter(duration -> "6 months".equalsIgnoreCase(duration));
            } else if ("REGULAR".equalsIgnoreCase(selectedType)) {
                filteredStream = filteredStream.filter(duration -> "NONE".equalsIgnoreCase(duration));
            } else {
                filteredStream = filteredStream.filter(duration -> !"NONE".equalsIgnoreCase(duration));
            }

            contractDurationComboBox.setItems(filteredStream.toList());
        });

        startDatePicker = new DatePicker("Start Date");
        startDatePicker.setRequired(true);
        startDatePicker.setClearButtonVisible(true);
        if (employeeProfileDTO != null) startDatePicker.setValue(employeeProfileDTO.getStartDate());

        endDatePicker = new DatePicker("End Date");
        endDatePicker.setClearButtonVisible(true);
        if (employeeProfileDTO != null && employeeProfileDTO.getEndDate() != null) endDatePicker.setValue(employeeProfileDTO.getEndDate());

        statusComboBox =  new ComboBox<>("Status");
        statusComboBox.setRequired(true);
        statusComboBox.setItems("ONBOARDING", "ACTIVE", "ON LEAVE", "SUSPENDED", "TERMINATED", "RESIGNED", "RETIRED", "DECEASED");
        statusComboBox.setClearButtonVisible(true);
        if (employeeProfileDTO != null) statusComboBox.setValue(employeeProfileDTO.getStatus());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateEmployeeDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(EmployeeProfileListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(EmployeeProfileListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        employeeDTOFormLayout.add(employeeNoTextField,
                                  lastNameTextField,
                                  suffixTextField,
                                  firstNameTextField,
                                  middleNameTextField,
                                  genderComboBox,
                                  employmentTypeComboBox,
                                  contractDurationComboBox,
                                  dateHiredDatePicker,
                                  startDatePicker,
                                  dateResignedDatePicker,
                                  endDatePicker,
                                  statusComboBox,
                                  buttonLayout);
        employeeDTOFormLayout.setColspan(employeeNoTextField, 2);
        employeeDTOFormLayout.setColspan(buttonLayout, 2);
        employeeDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateEmployeeDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            employeeProfileDTO = employeeProfileService.getById(parameterId);
        } else {
            employeeProfileDTO = new EmployeeProfileDTO();
            employeeProfileDTO.setCreatedBy(loggedInUser);
        }

        employeeProfileDTO.setEmployeeNumber(employeeNoTextField.getValue());
        employeeProfileDTO.setLastName(lastNameTextField.getValue());
        employeeProfileDTO.setSuffix(suffixTextField.getValue());
        employeeProfileDTO.setFirstName(firstNameTextField.getValue());
        employeeProfileDTO.setMiddleName(middleNameTextField.getValue());
        employeeProfileDTO.setGender(genderComboBox.getValue());
        employeeProfileDTO.setDateHired(dateHiredDatePicker.getValue());
        employeeProfileDTO.setDateResigned(dateResignedDatePicker.getValue());
        employeeProfileDTO.setEmploymentType(employmentTypeComboBox.getValue());
        employeeProfileDTO.setContractDuration(contractDurationComboBox.getValue());
        employeeProfileDTO.setStartDate(startDatePicker.getValue());
        employeeProfileDTO.setEndDate(endDatePicker.getValue());
        employeeProfileDTO.setStatus(statusComboBox.getValue());
        employeeProfileDTO.setUpdatedBy(loggedInUser);

        employeeProfileService.saveOrUpdate(employeeProfileDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved the employee record.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
