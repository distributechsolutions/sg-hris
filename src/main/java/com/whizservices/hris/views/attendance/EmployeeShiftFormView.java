package com.whizservices.hris.views.attendance;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import com.whizservices.hris.dtos.attendance.EmployeeShiftScheduleDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.attendance.EmployeeShiftScheduleService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Employee Shift Form")
@Route(value = "employee-shift-form", layout = MainLayout.class)
public class EmployeeShiftFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final EmployeeShiftScheduleService employeeShiftScheduleService;
    @Resource private final EmployeeService employeeService;

    private EmployeeShiftScheduleDTO employeeShiftScheduleDTO;
    private UUID parameterId;
    private String loggedInUser;

    private final FormLayout employeeShiftDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeDTO> employeeDTOComboBox;
    private ComboBox<String> shiftScheduleComboBox;
    private TimePicker startShiftTimePicker, endShiftTimePicker;
    private IntegerField noOfHoursField;
    private CheckboxGroup<String> shiftScheduledDaysCheckboxGroup;
    private ToggleButton activeShiftToggleButton;

    public EmployeeShiftFormView(EmployeeShiftScheduleService employeeShiftScheduleService,
                                 EmployeeService employeeService) {
        this.employeeShiftScheduleService = employeeShiftScheduleService;
        this.employeeService = employeeService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        setSizeFull();
        setMargin(true);
        add(employeeShiftDTOFormLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            employeeShiftScheduleDTO = employeeShiftScheduleService.getById(parameterId);
        }

        buildEmployeeShiftFormLayout();
    }

    private void buildEmployeeShiftFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                                                                                       employeeService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (employeeShiftScheduleDTO != null) employeeDTOComboBox.setValue(employeeShiftScheduleDTO.getEmployeeDTO());

        shiftScheduleComboBox = new ComboBox<>("Shift Schedule");
        shiftScheduleComboBox.setItems("Morning Shift", "Mid Shift", "Night Shift");
        shiftScheduleComboBox.setRequiredIndicatorVisible(true);
        shiftScheduleComboBox.setRequired(true);
        if (employeeShiftScheduleDTO != null) shiftScheduleComboBox.setValue(employeeShiftScheduleDTO.getShiftSchedule());

        noOfHoursField = new IntegerField("No. of Hours");
        noOfHoursField.setRequired(true);
        noOfHoursField.setRequiredIndicatorVisible(true);
        noOfHoursField.setMin(0);
        if (employeeShiftScheduleDTO != null) noOfHoursField.setValue(employeeShiftScheduleDTO.getShiftHours());

        startShiftTimePicker = new TimePicker("Start Time");
        startShiftTimePicker.setRequired(true);
        startShiftTimePicker.setRequiredIndicatorVisible(true);
        if (employeeShiftScheduleDTO != null) startShiftTimePicker.setValue(employeeShiftScheduleDTO.getShiftStartTime());

        endShiftTimePicker = new TimePicker("End Time");
        endShiftTimePicker.setRequired(true);
        endShiftTimePicker.setRequiredIndicatorVisible(true);
        if (employeeShiftScheduleDTO != null) endShiftTimePicker.setValue(employeeShiftScheduleDTO.getShiftStartTime());

        shiftScheduledDaysCheckboxGroup = new CheckboxGroup<>("Scheduled Days");
        shiftScheduledDaysCheckboxGroup.setItems("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        shiftScheduledDaysCheckboxGroup.setRequiredIndicatorVisible(true);
        shiftScheduledDaysCheckboxGroup.setRequired(true);
        if (employeeShiftScheduleDTO != null) shiftScheduledDaysCheckboxGroup.setValue(Arrays.stream(employeeShiftScheduleDTO.getShiftScheduledDays().split(","))
                                                                                                                                     .map(String::trim)
                                                                                                                                     .collect(Collectors.toSet()));

        activeShiftToggleButton = new ToggleButton( "Active Shift");
        activeShiftToggleButton.getStyle().set("font-size", "0.875rem");
        activeShiftToggleButton.getStyle().set("padding-top", "5px");
        if (employeeShiftScheduleDTO != null) activeShiftToggleButton.setValue(employeeShiftScheduleDTO.isActiveShift());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateEmployeeShiftDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(EmployeeShiftListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(EmployeeShiftListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        employeeShiftDTOFormLayout.add(employeeDTOComboBox,
                                       shiftScheduleComboBox,
                                       noOfHoursField,
                                       startShiftTimePicker,
                                       endShiftTimePicker,
                                       shiftScheduledDaysCheckboxGroup,
                                       activeShiftToggleButton,
                                       buttonLayout);
        employeeShiftDTOFormLayout.setColspan(employeeDTOComboBox, 2);
        employeeShiftDTOFormLayout.setColspan(shiftScheduledDaysCheckboxGroup, 2);
        employeeShiftDTOFormLayout.setColspan(activeShiftToggleButton, 2);
        employeeShiftDTOFormLayout.setColspan(buttonLayout, 2);
        employeeShiftDTOFormLayout.setMaxWidth("768px");
    }

    private void saveOrUpdateEmployeeShiftDTO() {
        if (parameterId != null) {
            employeeShiftScheduleDTO = employeeShiftScheduleService.getById(parameterId);
        } else {
            employeeShiftScheduleDTO = new EmployeeShiftScheduleDTO();
            employeeShiftScheduleDTO.setCreatedBy(loggedInUser);
        }

        employeeShiftScheduleDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        employeeShiftScheduleDTO.setShiftSchedule(shiftScheduleComboBox.getValue());
        employeeShiftScheduleDTO.setShiftHours(noOfHoursField.getValue());
        employeeShiftScheduleDTO.setShiftStartTime(startShiftTimePicker.getValue());
        employeeShiftScheduleDTO.setShiftEndTime(endShiftTimePicker.getValue());
        employeeShiftScheduleDTO.setShiftScheduledDays(String.join(", ", shiftScheduledDaysCheckboxGroup.getValue()));
        employeeShiftScheduleDTO.setActiveShift(activeShiftToggleButton.getValue());
        employeeShiftScheduleDTO.setUpdatedBy(loggedInUser);

        employeeShiftScheduleService.saveOrUpdate(employeeShiftScheduleDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved an employee shift.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
