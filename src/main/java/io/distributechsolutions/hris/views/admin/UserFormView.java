package io.distributechsolutions.hris.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import io.distributechsolutions.hris.dtos.admin.UserDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.EmailService;
import io.distributechsolutions.hris.services.admin.UserService;
import io.distributechsolutions.hris.services.profile.EmployeeService;
import io.distributechsolutions.hris.utils.SecurityUtil;
import io.distributechsolutions.hris.utils.StringUtil;
import io.distributechsolutions.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER"})
@PageTitle("User Form")
@Route(value = "user-form", layout = MainLayout.class)
public class UserFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final UserService userService;
    @Resource private final EmployeeService employeeService;
    @Resource private final EmailService emailService;

    private UserDTO userDTO;
    private UUID parameterId;

    private final FormLayout userDTOFormLayout = new FormLayout();
    private TextField usernameTextField;
    private EmailField emailField;
    private ComboBox<String> roleComboBox;
    private ComboBox<EmployeeDTO> employeeDTOComboBox;
    private Checkbox accountActiveCheckbox, passwordChangedCheckbox;

    public UserFormView(UserService userService,
                        EmployeeService employeeService,
                        EmailService emailService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.emailService = emailService;

        add(userDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, userDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        if (s != null) {
            parameterId = UUID.fromString(s);
            userDTO = userService.getById(parameterId);
        }

        buildUserFormLayout();
    }

    private void buildUserFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeDTO, Void> query = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                employeeService.getAll(query.getPage(), query.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (userDTO != null) employeeDTOComboBox.setValue(userDTO.getEmployeeDTO());

        usernameTextField = new TextField("Username");
        usernameTextField.setClearButtonVisible(true);
        usernameTextField.setRequired(true);
        usernameTextField.setRequiredIndicatorVisible(true);
        if (userDTO != null) usernameTextField.setValue(userDTO.getUsername());

        roleComboBox = new ComboBox<>("Role");
        roleComboBox.setItems("ROLE_ADMIN",
                              "ROLE_HR_MANAGER",
                              "ROLE_HR_SUPERVISOR",
                              "ROLE_HR_EMPLOYEE",
                              "ROLE_MANAGER",
                              "ROLE_SUPERVISOR",
                              "ROLE_EMPLOYEE");
        roleComboBox.setClearButtonVisible(true);
        roleComboBox.setRequired(true);
        roleComboBox.setRequiredIndicatorVisible(true);
        if (userDTO != null) roleComboBox.setValue(userDTO.getRole());

        emailField = new EmailField("Email Address");
        emailField.setHelperText("Use personal email");
        emailField.setClearButtonVisible(true);
        emailField.setSuffixComponent(LineAwesomeIcon.ENVELOPE.create());
        emailField.setRequired(true);
        emailField.setRequiredIndicatorVisible(true);
        if (userDTO != null) emailField.setValue(userDTO.getEmailAddress());

        accountActiveCheckbox = new Checkbox("Is Account Active?");
        if (userDTO != null) accountActiveCheckbox.setValue(userDTO.isAccountActive());

        passwordChangedCheckbox = new Checkbox("Is Password Changed?");
        if (userDTO != null) passwordChangedCheckbox.setValue(userDTO.isPasswordChanged());

        HorizontalLayout checkBoxLayout = new HorizontalLayout();
        checkBoxLayout.add(accountActiveCheckbox, passwordChangedCheckbox);
        checkBoxLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        checkBoxLayout.setMaxWidth("720px");
        checkBoxLayout.setPadding(true);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateUserDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(UserListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(UserListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        userDTOFormLayout.add(employeeDTOComboBox,
                              usernameTextField,
                              roleComboBox,
                              emailField,
                              checkBoxLayout,
                              buttonLayout);
        userDTOFormLayout.setColspan(checkBoxLayout, 2);
        userDTOFormLayout.setColspan(buttonLayout, 2);
        userDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateUserDTO() {
        String generatedPassword = StringUtil.generateRandomPassword();
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            userDTO = userService.getById(parameterId);
        } else {
            userDTO = new UserDTO();
            userDTO.setPassword(StringUtil.encryptPassword(generatedPassword));
            userDTO.setCreatedBy(loggedInUser);
        }

        userDTO.setEmployeeDTO(employeeService.getById(employeeDTOComboBox.getValue().getId()));
        userDTO.setUsername(usernameTextField.getValue());
        userDTO.setRole(roleComboBox.getValue());
        userDTO.setEmailAddress(emailField.getValue());
        userDTO.setAccountActive(accountActiveCheckbox.getValue());
        userDTO.setPasswordChanged(passwordChangedCheckbox.getValue());
        userDTO.setUpdatedBy(loggedInUser);

        userService.saveOrUpdate(userDTO);

        // Send the generated password if the user is new record or password has not changed.
        if (!userDTO.isPasswordChanged()) {
            emailService.sendWelcomeEmailForNewUser(userDTO.getEmailAddress(),
                                                    userDTO.getEmployeeDTO().getFirstName(),
                                                    userDTO.getUsername(),
                                                    generatedPassword);
        }

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a user account.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}