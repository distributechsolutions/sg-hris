package io.distributechsolutions.hris.views.info;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

import io.distributechsolutions.hris.dtos.admin.UserDTO;
import io.distributechsolutions.hris.dtos.info.PersonalInfoDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.admin.UserService;
import io.distributechsolutions.hris.services.info.PersonalInfoService;
import io.distributechsolutions.hris.services.profile.EmployeeService;
import io.distributechsolutions.hris.utils.SecurityUtil;
import io.distributechsolutions.hris.views.common.DashboardView;

import jakarta.annotation.Resource;

import java.time.LocalDate;
import java.util.Objects;

import org.vaadin.lineawesome.LineAwesomeIcon;

public class PersonalInfoForm extends FormLayout {
    @Resource private final PersonalInfoService personalInfoService;
    @Resource private final UserService userService;
    @Resource private final EmployeeService employeeService;

    private EmployeeDTO employeeDTO;
    private UserDTO userDTO;
    private PersonalInfoDTO personalInfoDTO;

    private String loggedInUser;

    private DatePicker dateOfBirthDatePicker;
    private TextField placeOfBirthTextField;
    private ComboBox<String> maritalStatusComboBox;
    private TextField maidenNameTextField;
    private TextField spouseNameTextField;
    private TextField contactNumberTextField;
    private EmailField emailAddressEmailField;
    private TextField taxIdentificationNumberTextField;
    private TextField sssNumberTextField;
    private TextField hdmfNumberTextField;
    private TextField philhealthNumberTextField;
    private Button saveButton;
    private Button editButton;
    private Button cancelButton;

    public PersonalInfoForm(PersonalInfoService personalInfoService,
                            UserService userService,
                            EmployeeService employeeService) {
        this.personalInfoService = personalInfoService;
        this.userService = userService;
        this.employeeService = employeeService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (loggedInUser != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        if (userDTO != null) {
            employeeDTO = userDTO.getEmployeeDTO();
        }

        if (employeeDTO != null) {
            personalInfoDTO = personalInfoService.getByEmployeeDTO(employeeDTO);
        }

        this.buildPersonalInfoForm();
        this.getStyle().setPadding("10px");
    }

    public void buildPersonalInfoForm() {
        dateOfBirthDatePicker = new DatePicker("Date of Birth");
        dateOfBirthDatePicker.setClearButtonVisible(true);
        dateOfBirthDatePicker.setMax(LocalDate.of(LocalDate.now().getYear() - 18,
                                                  LocalDate.now().getMonth(),
                                                  LocalDate.now().getDayOfMonth()));
        dateOfBirthDatePicker.setRequired(true);
        if (personalInfoDTO != null) {
            dateOfBirthDatePicker.setValue(personalInfoDTO.getDateOfBirth());
            dateOfBirthDatePicker.setReadOnly(true);
        }

        placeOfBirthTextField = new TextField("Place of Birth");
        placeOfBirthTextField.setClearButtonVisible(true);
        placeOfBirthTextField.setRequired(true);
        if (personalInfoDTO != null) {
            placeOfBirthTextField.setValue(personalInfoDTO.getPlaceOfBirth());
            placeOfBirthTextField.setReadOnly(true);
        }

        maritalStatusComboBox = new ComboBox<>("Marital Status");
        maritalStatusComboBox.setItems("Single", "Married", "Widowed", "Annulled");
        maritalStatusComboBox.setClearButtonVisible(true);
        maritalStatusComboBox.setRequired(true);
        if (personalInfoDTO != null) {
            maritalStatusComboBox.setValue(personalInfoDTO.getMaritalStatus());
            maritalStatusComboBox.setReadOnly(true);
        }

        maidenNameTextField = new TextField("Maiden Name");
        maidenNameTextField.setClearButtonVisible(true);
        if (employeeDTO != null && employeeDTO.getGender().equalsIgnoreCase("Female")) {
            maidenNameTextField.setRequired(true);
        }
        if (personalInfoDTO != null) {
            maidenNameTextField.setValue(personalInfoDTO.getMaidenName());
            maidenNameTextField.setReadOnly(true);
        }

        spouseNameTextField = new TextField("Spouse Name");
        spouseNameTextField.setClearButtonVisible(true);
        if (maritalStatusComboBox.getValue() != null && (maritalStatusComboBox.getValue().equals("Married") || maritalStatusComboBox.getValue().equals("Widowed"))) {
            spouseNameTextField.setRequired(true);
        }
        if (personalInfoDTO != null) {
            spouseNameTextField.setValue(personalInfoDTO.getSpouseName());
            spouseNameTextField.setReadOnly(true);
        }

        contactNumberTextField = new TextField("Contact Number");
        contactNumberTextField.setPrefixComponent(new Span("+63"));
        contactNumberTextField.setSuffixComponent(new Span(LineAwesomeIcon.PHONE_SQUARE_SOLID.create()));
        contactNumberTextField.setClearButtonVisible(true);
        contactNumberTextField.setRequired(true);
        contactNumberTextField.setMinLength(10);
        contactNumberTextField.setMaxLength(10);
        contactNumberTextField.setAllowedCharPattern("[0-9]");
        if (personalInfoDTO != null) {
            contactNumberTextField.setValue(String.valueOf(personalInfoDTO.getContactNumber()));
            contactNumberTextField.setReadOnly(true);
        }

        emailAddressEmailField = new EmailField("Email Address");
        emailAddressEmailField.setSuffixComponent(new Span(LineAwesomeIcon.ENVELOPE.create()));
        emailAddressEmailField.setClearButtonVisible(true);
        emailAddressEmailField.setRequired(true);
        if (personalInfoDTO != null) {
            emailAddressEmailField.setValue(personalInfoDTO.getEmailAddress());
            emailAddressEmailField.setReadOnly(true);
        }

        taxIdentificationNumberTextField = new TextField("Tax Identification Number (TIN)");
        taxIdentificationNumberTextField.setClearButtonVisible(true);
        taxIdentificationNumberTextField.setRequired(true);
        taxIdentificationNumberTextField.setMinLength(9);
        taxIdentificationNumberTextField.setMaxLength(9);
        taxIdentificationNumberTextField.setAllowedCharPattern("[0-9]");
        taxIdentificationNumberTextField.setHelperText("Input the 9-digit TIN. Last digits 000 is not included.");
        if (personalInfoDTO != null) {
            taxIdentificationNumberTextField.setValue(personalInfoDTO.getTaxIdentificationNumber());
            taxIdentificationNumberTextField.setReadOnly(true);
        }

        sssNumberTextField = new TextField("SSS Number");
        sssNumberTextField.setClearButtonVisible(true);
        sssNumberTextField.setRequired(true);
        sssNumberTextField.setMinLength(10);
        sssNumberTextField.setMaxLength(10);
        sssNumberTextField.setAllowedCharPattern("[0-9]");
        sssNumberTextField.setHelperText("Input the 10-digit SSS number");
        if (personalInfoDTO != null) {
            sssNumberTextField.setValue(personalInfoDTO.getSssNumber());
            sssNumberTextField.setReadOnly(true);
        }

        hdmfNumberTextField = new TextField("HDMF Number");
        hdmfNumberTextField.setClearButtonVisible(true);
        hdmfNumberTextField.setRequired(true);
        hdmfNumberTextField.setMinLength(12);
        hdmfNumberTextField.setMaxLength(12);
        hdmfNumberTextField.setAllowedCharPattern("[0-9]");
        hdmfNumberTextField.setHelperText("Input the 12-digit PagIbig number.");
        if (personalInfoDTO != null) {
            hdmfNumberTextField.setValue(personalInfoDTO.getHdmfNumber());
            hdmfNumberTextField.setReadOnly(true);
        }

        philhealthNumberTextField = new TextField("Philhealth Number");
        philhealthNumberTextField.setClearButtonVisible(true);
        philhealthNumberTextField.setRequired(true);
        philhealthNumberTextField.setMinLength(12);
        philhealthNumberTextField.setMaxLength(12);
        philhealthNumberTextField.setAllowedCharPattern("[0-9]");
        philhealthNumberTextField.setHelperText("Enter the 12-digit Philhealth number.");
        if (personalInfoDTO != null) {
            philhealthNumberTextField.setValue(personalInfoDTO.getPhilhealthNumber());
            philhealthNumberTextField.setReadOnly(true);
        }

        saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            this.saveOrUpdatePersonalInfoDTO(personalInfoDTO);
            this.setReadOnlyFields(true);
            editButton.setEnabled(true);

            Notification notification = Notification.show("You have successfully saved your personal information.",  5000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        if (personalInfoDTO != null) saveButton.setEnabled(false);

        editButton = new Button("Edit");
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        editButton.addClickListener(buttonClickEvent -> {
            editButton.setEnabled(false);
            saveButton.setEnabled(true);
            this.setReadOnlyFields(false);
        });
        if (personalInfoDTO == null) editButton.setEnabled(false);

        cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate(DashboardView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, editButton, saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setPadding(true);

        this.add(maritalStatusComboBox,
                 dateOfBirthDatePicker,
                 placeOfBirthTextField,
                 maidenNameTextField,
                 spouseNameTextField,
                 contactNumberTextField,
                 emailAddressEmailField,
                 taxIdentificationNumberTextField,
                 sssNumberTextField,
                 hdmfNumberTextField,
                 philhealthNumberTextField,
                 buttonLayout);
        this.setColspan(placeOfBirthTextField, 2);
        this.setColspan(buttonLayout, 2);
        this.setMaxWidth("768px");
    }

    private void saveOrUpdatePersonalInfoDTO(PersonalInfoDTO personalInfoDTO) {
        if (personalInfoDTO == null) {
            personalInfoDTO = new PersonalInfoDTO();
            personalInfoDTO.setCreatedBy(loggedInUser);
        }

        personalInfoDTO.setEmployeeDTO(employeeDTO);
        personalInfoDTO.setDateOfBirth(dateOfBirthDatePicker.getValue());
        personalInfoDTO.setPlaceOfBirth(placeOfBirthTextField.getValue());
        personalInfoDTO.setMaritalStatus(maritalStatusComboBox.getValue());
        personalInfoDTO.setMaidenName(maidenNameTextField.getValue());
        personalInfoDTO.setSpouseName(spouseNameTextField.getValue());
        personalInfoDTO.setContactNumber(Long.valueOf(contactNumberTextField.getValue()));
        personalInfoDTO.setEmailAddress(emailAddressEmailField.getValue());
        personalInfoDTO.setTaxIdentificationNumber(taxIdentificationNumberTextField.getValue());
        personalInfoDTO.setSssNumber(sssNumberTextField.getValue());
        personalInfoDTO.setHdmfNumber(hdmfNumberTextField.getValue());
        personalInfoDTO.setPhilhealthNumber(philhealthNumberTextField.getValue());
        personalInfoDTO.setUpdatedBy(loggedInUser);

        personalInfoService.saveOrUpdate(personalInfoDTO);
    }

    private void setReadOnlyFields(boolean isReadOnly) {
        dateOfBirthDatePicker.setReadOnly(isReadOnly);
        placeOfBirthTextField.setReadOnly(isReadOnly);
        maritalStatusComboBox.setReadOnly(isReadOnly);
        maidenNameTextField.setReadOnly(isReadOnly);
        spouseNameTextField.setReadOnly(isReadOnly);
        contactNumberTextField.setReadOnly(isReadOnly);
        emailAddressEmailField.setReadOnly(isReadOnly);
        taxIdentificationNumberTextField.setReadOnly(isReadOnly);
        sssNumberTextField.setReadOnly(isReadOnly);
        hdmfNumberTextField.setReadOnly(isReadOnly);
        philhealthNumberTextField.setReadOnly(isReadOnly);
    }
}
