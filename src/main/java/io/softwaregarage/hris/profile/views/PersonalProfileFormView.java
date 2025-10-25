package io.softwaregarage.hris.profile.views;

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

import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.profile.dtos.PersonalProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.profile.services.PersonalProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.DashboardView;

import jakarta.annotation.Resource;

import java.time.LocalDate;
import java.util.Objects;

import org.vaadin.lineawesome.LineAwesomeIcon;

public class PersonalProfileFormView extends FormLayout {
    @Resource private final PersonalProfileService personalProfileService;
    @Resource private final UserService userService;
    @Resource private final EmployeeProfileService employeeProfileService;

    private EmployeeProfileDTO employeeProfileDTO;
    private UserDTO userDTO;
    private PersonalProfileDTO personalProfileDTO;

    private final String loggedInUser;

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

    public PersonalProfileFormView(PersonalProfileService personalProfileService,
                                   UserService userService,
                                   EmployeeProfileService employeeProfileService) {
        this.personalProfileService = personalProfileService;
        this.userService = userService;
        this.employeeProfileService = employeeProfileService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (loggedInUser != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        if (userDTO != null) {
            employeeProfileDTO = userDTO.getEmployeeDTO();
        }

        if (employeeProfileDTO != null) {
            personalProfileDTO = personalProfileService.getByEmployeeDTO(employeeProfileDTO);
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
        if (personalProfileDTO != null) {
            dateOfBirthDatePicker.setValue(personalProfileDTO.getDateOfBirth());
            dateOfBirthDatePicker.setReadOnly(true);
        }

        placeOfBirthTextField = new TextField("Place of Birth");
        placeOfBirthTextField.setClearButtonVisible(true);
        placeOfBirthTextField.setRequired(true);
        if (personalProfileDTO != null) {
            placeOfBirthTextField.setValue(personalProfileDTO.getPlaceOfBirth());
            placeOfBirthTextField.setReadOnly(true);
        }

        maritalStatusComboBox = new ComboBox<>("Marital Status");
        maritalStatusComboBox.setItems("Single", "Married", "Widowed", "Annulled");
        maritalStatusComboBox.setClearButtonVisible(true);
        maritalStatusComboBox.setRequired(true);
        if (personalProfileDTO != null) {
            maritalStatusComboBox.setValue(personalProfileDTO.getMaritalStatus());
            maritalStatusComboBox.setReadOnly(true);
        }

        maidenNameTextField = new TextField("Maiden Name");
        maidenNameTextField.setClearButtonVisible(true);
        if (employeeProfileDTO != null && employeeProfileDTO.getGender().equalsIgnoreCase("Female")) {
            maidenNameTextField.setRequired(true);
        }
        if (personalProfileDTO != null) {
            maidenNameTextField.setValue(personalProfileDTO.getMaidenName());
            maidenNameTextField.setReadOnly(true);
        }

        spouseNameTextField = new TextField("Spouse Name");
        spouseNameTextField.setClearButtonVisible(true);
        if (maritalStatusComboBox.getValue() != null && (maritalStatusComboBox.getValue().equals("Married") || maritalStatusComboBox.getValue().equals("Widowed"))) {
            spouseNameTextField.setRequired(true);
        }
        if (personalProfileDTO != null) {
            spouseNameTextField.setValue(personalProfileDTO.getSpouseName());
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
        if (personalProfileDTO != null) {
            contactNumberTextField.setValue(String.valueOf(personalProfileDTO.getContactNumber()));
            contactNumberTextField.setReadOnly(true);
        }

        emailAddressEmailField = new EmailField("Email Address");
        emailAddressEmailField.setSuffixComponent(new Span(LineAwesomeIcon.ENVELOPE.create()));
        emailAddressEmailField.setClearButtonVisible(true);
        emailAddressEmailField.setRequired(true);
        if (personalProfileDTO != null) {
            emailAddressEmailField.setValue(personalProfileDTO.getEmailAddress());
            emailAddressEmailField.setReadOnly(true);
        }

        taxIdentificationNumberTextField = new TextField("Tax Identification Number (TIN)");
        taxIdentificationNumberTextField.setClearButtonVisible(true);
        taxIdentificationNumberTextField.setRequired(true);
        taxIdentificationNumberTextField.setMinLength(9);
        taxIdentificationNumberTextField.setMaxLength(9);
        taxIdentificationNumberTextField.setAllowedCharPattern("[0-9]");
        taxIdentificationNumberTextField.setHelperText("Input the 9-digit TIN. Last digits 000 is not included.");
        if (personalProfileDTO != null) {
            taxIdentificationNumberTextField.setValue(personalProfileDTO.getTaxIdentificationNumber());
            taxIdentificationNumberTextField.setReadOnly(true);
        }

        sssNumberTextField = new TextField("SSS Number");
        sssNumberTextField.setClearButtonVisible(true);
        sssNumberTextField.setRequired(true);
        sssNumberTextField.setMinLength(10);
        sssNumberTextField.setMaxLength(10);
        sssNumberTextField.setAllowedCharPattern("[0-9]");
        sssNumberTextField.setHelperText("Input the 10-digit SSS number");
        if (personalProfileDTO != null) {
            sssNumberTextField.setValue(personalProfileDTO.getSssNumber());
            sssNumberTextField.setReadOnly(true);
        }

        hdmfNumberTextField = new TextField("HDMF Number");
        hdmfNumberTextField.setClearButtonVisible(true);
        hdmfNumberTextField.setRequired(true);
        hdmfNumberTextField.setMinLength(12);
        hdmfNumberTextField.setMaxLength(12);
        hdmfNumberTextField.setAllowedCharPattern("[0-9]");
        hdmfNumberTextField.setHelperText("Input the 12-digit PagIbig number.");
        if (personalProfileDTO != null) {
            hdmfNumberTextField.setValue(personalProfileDTO.getHdmfNumber());
            hdmfNumberTextField.setReadOnly(true);
        }

        philhealthNumberTextField = new TextField("Philhealth Number");
        philhealthNumberTextField.setClearButtonVisible(true);
        philhealthNumberTextField.setRequired(true);
        philhealthNumberTextField.setMinLength(12);
        philhealthNumberTextField.setMaxLength(12);
        philhealthNumberTextField.setAllowedCharPattern("[0-9]");
        philhealthNumberTextField.setHelperText("Enter the 12-digit Philhealth number.");
        if (personalProfileDTO != null) {
            philhealthNumberTextField.setValue(personalProfileDTO.getPhilhealthNumber());
            philhealthNumberTextField.setReadOnly(true);
        }

        saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            this.saveOrUpdatePersonalInfoDTO(personalProfileDTO);
            this.setReadOnlyFields(true);
            editButton.setEnabled(true);

            Notification notification = Notification.show("You have successfully saved your personal information.",  5000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        if (personalProfileDTO != null) saveButton.setEnabled(false);

        editButton = new Button("Edit");
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        editButton.addClickListener(buttonClickEvent -> {
            editButton.setEnabled(false);
            saveButton.setEnabled(true);
            this.setReadOnlyFields(false);
        });
        if (personalProfileDTO == null) editButton.setEnabled(false);

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

    private void saveOrUpdatePersonalInfoDTO(PersonalProfileDTO personalProfileDTO) {
        if (personalProfileDTO == null) {
            personalProfileDTO = new PersonalProfileDTO();
            personalProfileDTO.setCreatedBy(loggedInUser);
        }

        personalProfileDTO.setEmployeeDTO(employeeProfileDTO);
        personalProfileDTO.setDateOfBirth(dateOfBirthDatePicker.getValue());
        personalProfileDTO.setPlaceOfBirth(placeOfBirthTextField.getValue());
        personalProfileDTO.setMaritalStatus(maritalStatusComboBox.getValue());
        personalProfileDTO.setMaidenName(maidenNameTextField.getValue());
        personalProfileDTO.setSpouseName(spouseNameTextField.getValue());
        personalProfileDTO.setContactNumber(Long.valueOf(contactNumberTextField.getValue()));
        personalProfileDTO.setEmailAddress(emailAddressEmailField.getValue());
        personalProfileDTO.setTaxIdentificationNumber(taxIdentificationNumberTextField.getValue());
        personalProfileDTO.setSssNumber(sssNumberTextField.getValue());
        personalProfileDTO.setHdmfNumber(hdmfNumberTextField.getValue());
        personalProfileDTO.setPhilhealthNumber(philhealthNumberTextField.getValue());
        personalProfileDTO.setUpdatedBy(loggedInUser);

        personalProfileService.saveOrUpdate(personalProfileDTO);
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
