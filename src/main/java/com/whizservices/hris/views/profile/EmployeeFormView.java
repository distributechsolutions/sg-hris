package com.whizservices.hris.views.profile;

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

import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Form")
@Route(value = "employee-form", layout = MainLayout.class)
public class EmployeeFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final EmployeeService employeeService;
    private EmployeeDTO employeeDTO;
    private UUID parameterId;

    private final FormLayout employeeDTOFormLayout = new FormLayout();
    private TextField employeeNoTextField,
                      lastNameTextField,
                      firstNameTextField,
                      middleNameTextField,
                      suffixTextField;
    private ComboBox<String> genderComboBox;
    private DatePicker dateHiredDatePicker;

    public EmployeeFormView(EmployeeService employeeService) {
        this.employeeService = employeeService;

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
            employeeDTO = employeeService.getById(parameterId);
        }

        buildEmployeeFormLayout();
    }

    private void buildEmployeeFormLayout() {
        employeeNoTextField = new TextField("Employee Number");
        employeeNoTextField.setRequired(true);
        employeeNoTextField.setAllowedCharPattern("\\d*");
        employeeNoTextField.setMaxLength(10);
        employeeNoTextField.setClearButtonVisible(true);
        if (employeeDTO != null) employeeNoTextField.setValue(employeeDTO.getEmployeeNumber());

        lastNameTextField = new TextField("Last Name");
        lastNameTextField.setRequired(true);
        lastNameTextField.setMinLength(2);
        lastNameTextField.setMaxLength(50);
        lastNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        lastNameTextField.setClearButtonVisible(true);
        if (employeeDTO != null) lastNameTextField.setValue(employeeDTO.getLastName());

        firstNameTextField = new TextField("First Name");
        firstNameTextField.setRequired(true);
        firstNameTextField.setMinLength(2);
        firstNameTextField.setMaxLength(50);
        firstNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        firstNameTextField.setClearButtonVisible(true);
        if (employeeDTO != null) firstNameTextField.setValue(employeeDTO.getFirstName());

        middleNameTextField = new TextField("Middle Name");
        middleNameTextField.setMinLength(2);
        middleNameTextField.setMaxLength(50);
        middleNameTextField.setAllowedCharPattern("[a-zA-Z ]*");
        middleNameTextField.setClearButtonVisible(true);
        if (employeeDTO != null) middleNameTextField.setValue(employeeDTO.getMiddleName());

        suffixTextField = new TextField("Suffix");
        suffixTextField.setPlaceholder("Sr, Jr, III, IV...");
        suffixTextField.setMinLength(2);
        suffixTextField.setMaxLength(5);
        suffixTextField.setAllowedCharPattern("[a-zA-Z]*");
        suffixTextField.setWidth("25%");
        suffixTextField.setClearButtonVisible(true);
        if (employeeDTO != null) suffixTextField.setValue(employeeDTO.getSuffix());

        genderComboBox = new ComboBox<>("Gender");
        genderComboBox.setRequired(true);
        genderComboBox.setItems("Male", "Female");
        genderComboBox.setClearButtonVisible(true);
        if (employeeDTO != null) genderComboBox.setValue(employeeDTO.getGender());

        dateHiredDatePicker = new DatePicker("Date Hired");
        dateHiredDatePicker.setRequired(true);
        dateHiredDatePicker.setClearButtonVisible(true);
        if (employeeDTO != null) dateHiredDatePicker.setValue(employeeDTO.getDateHired());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateEmployeeDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(EmployeeListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(EmployeeListView.class)));

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
                                  dateHiredDatePicker,
                                  buttonLayout);
        employeeDTOFormLayout.setColspan(employeeNoTextField, 2);
        employeeDTOFormLayout.setColspan(buttonLayout, 2);
        employeeDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateEmployeeDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            employeeDTO = employeeService.getById(parameterId);
        } else {
            employeeDTO = new EmployeeDTO();
            employeeDTO.setCreatedBy(loggedInUser);
        }

        employeeDTO.setEmployeeNumber(employeeNoTextField.getValue());
        employeeDTO.setLastName(lastNameTextField.getValue());
        employeeDTO.setSuffix(suffixTextField.getValue());
        employeeDTO.setFirstName(firstNameTextField.getValue());
        employeeDTO.setMiddleName(middleNameTextField.getValue());
        employeeDTO.setGender(genderComboBox.getValue());
        employeeDTO.setDateHired(dateHiredDatePicker.getValue());
        employeeDTO.setUpdatedBy(loggedInUser);

        employeeService.saveOrUpdate(employeeDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved the employee record.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
