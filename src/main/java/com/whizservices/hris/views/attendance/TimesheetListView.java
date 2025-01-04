package com.whizservices.hris.views.attendance;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.attendance.TimesheetDTO;
import com.whizservices.hris.services.attendance.TimesheetService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.utils.CSVUtil;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Timesheet List")
@Route(value = "timesheets-view", layout = MainLayout.class)
public class TimesheetListView extends VerticalLayout {
    @Resource private final TimesheetService timesheetService;
    @Resource private final EmployeeService employeeService;

    private Grid<TimesheetDTO> timesheetDTOGrid;
    private TextField searchFilterTextField;

    private String loggedInUser;

    public TimesheetListView(TimesheetService timesheetService,
                             EmployeeService employeeService) {
        this.timesheetService = timesheetService;
        this.employeeService = employeeService;

        // Get the logged in user of the system.
        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        this.add(this.buildHeaderToolbar(),
                 this.buildTimesheetDTOGrid());
        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
    }

    public HorizontalLayout buildHeaderToolbar() {
        HorizontalLayout headerToolbarLayout = new HorizontalLayout();

        searchFilterTextField = new TextField();
        searchFilterTextField.setWidth("350px");
        searchFilterTextField.setPlaceholder("Search");
        searchFilterTextField.setPrefixComponent(LineAwesomeIcon.SEARCH_SOLID.create());
        searchFilterTextField.getStyle().set("margin", "0 auto 0 0");
        searchFilterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateTimesheetDTOGrid());

        Button uploadButton = new Button("Upload Timesheet");
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uploadButton.addClickListener(buttonClickEvent -> this.buildUploadTimesheetDialog().open());

        headerToolbarLayout.add(searchFilterTextField, uploadButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<TimesheetDTO> buildTimesheetDTOGrid() {
        timesheetDTOGrid = new Grid<>(TimesheetDTO.class, false);

        timesheetDTOGrid.addColumn(timesheetDTO -> timesheetDTO.getEmployeeDTO().getEmployeeNumber())
                        .setHeader("Employee No.")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(timesheetDTO -> timesheetDTO.getEmployeeDTO().getFirstName()
                                                                            .concat(" ")
                                                                            .concat(timesheetDTO.getEmployeeDTO().getMiddleName())
                                                                            .concat(" ")
                                                                            .concat(timesheetDTO.getEmployeeDTO().getLastName())
                                                                            .concat(timesheetDTO.getEmployeeDTO().getSuffix() != null ? timesheetDTO.getEmployeeDTO().getSuffix() : ""))
                        .setHeader("Employee Name")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(timesheetDTO -> timesheetDTO.getLogDate())
                        .setHeader("Log Date")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(timesheetDTO -> timesheetDTO.getShiftSchedule())
                        .setHeader("Shift Schedule")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(timesheetDTO -> timesheetDTO.getLogTimeIn())
                        .setHeader("Time In")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(timesheetDTO -> timesheetDTO.getLogTimeOut())
                        .setHeader("Time Out")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(timesheetDTO -> timesheetDTO.getExceptionRemarks())
                        .setHeader("Exception Remarks")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, timesheetDTO) -> {
                                        String theme = String.format("badge %s", timesheetDTO.getStatus().equalsIgnoreCase("PENDING") ? "contrast" : "success");

                                        Span activeSpan = new Span();
                                        activeSpan.getElement().setAttribute("theme", theme);
                                        activeSpan.setText(timesheetDTO.getStatus());

                                        layout.setJustifyContentMode(JustifyContentMode.CENTER);
                                        layout.add(activeSpan);
                                    }))
                        .setHeader("Status")
                        .setSortable(true);
        timesheetDTOGrid.addComponentColumn(timesheetDTO -> buildRowToolbar(timesheetDTO)).setHeader("Action");
        timesheetDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                          GridVariant.LUMO_COLUMN_BORDERS,
                                          GridVariant.LUMO_WRAP_CELL_CONTENT);
        timesheetDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        timesheetDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        timesheetDTOGrid.setEmptyStateText("No rate records found.");
        timesheetDTOGrid.setItems((query -> timesheetService.getAll(query.getPage(), query.getPageSize()).stream().filter(timesheetDTO -> timesheetDTO.getStatus().equalsIgnoreCase("PENDING"))));

        return timesheetDTOGrid;
    }

    public HorizontalLayout buildRowToolbar(TimesheetDTO timesheetDTO) {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Timesheet");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TimesheetDTO selectedTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                buildViewTimesheetDialog(selectedTimesheetDTO).open();
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Timesheet");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TimesheetDTO selectedTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                buildEditTimesheetDialog(selectedTimesheetDTO).open();
            }
        }));

        Button approveButton = new Button();
        approveButton.setTooltipText("Approve Timesheet");
        approveButton.setIcon(LineAwesomeIcon.THUMBS_UP.create());
        approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        approveButton.addClickListener(buttonClickEvent -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TimesheetDTO selectedTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                selectedTimesheetDTO.setStatus("APPROVED");
                selectedTimesheetDTO.setUpdatedBy(loggedInUser);

                timesheetService.saveOrUpdate(selectedTimesheetDTO);

                this.updateTimesheetDTOGrid();

                Notification notification = Notification.show("Timesheet approved successfully.", 5000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        // If the row of record does not have time in or time out, add the edit button. Else, only the view button.
        if (timesheetDTO.getLogTimeIn() == null || timesheetDTO.getLogTimeOut() == null) {
            rowToolbarLayout.add(viewButton, editButton, approveButton);
        } else {
            rowToolbarLayout.add(viewButton, approveButton);
        }

        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateTimesheetDTOGrid() {
        if (!searchFilterTextField.getValue().isEmpty()) {
            timesheetDTOGrid.setItems(timesheetService.findByParameter(searchFilterTextField.getValue()).stream().filter(timesheetDTO -> timesheetDTO.getStatus().equalsIgnoreCase("PENDING")).toList());
        } else {
            timesheetDTOGrid.setItems(query -> timesheetService.getAll(query.getPage(), query.getPageSize()).stream().filter(timesheetDTO -> timesheetDTO.getStatus().equalsIgnoreCase("PENDING")));
        }
    }

    private Dialog buildUploadTimesheetDialog() {
        MultiFileMemoryBuffer multiFileMemoryBuffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(multiFileMemoryBuffer);
        upload.setAcceptedFileTypes(".csv");

        VerticalLayout uploadTimesheetLayout = new VerticalLayout();
        uploadTimesheetLayout.setSpacing(false);
        uploadTimesheetLayout.setPadding(false);
        uploadTimesheetLayout.setAlignItems(Alignment.STRETCH);
        uploadTimesheetLayout.getStyle().set("width", "720px").set("max-width", "100%");
        uploadTimesheetLayout.add(upload);

        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setHeaderTitle("Upload Timesheet");
        dialog.setCloseOnOutsideClick(false);
        dialog.add(uploadTimesheetLayout);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            List<String[]> csvDataList = CSVUtil.readCSVData(multiFileMemoryBuffer);

            if (!csvDataList.isEmpty()) {
                for (String[] csvData : csvDataList) {
                    TimesheetDTO timesheetDTO = new TimesheetDTO();
                    timesheetDTO.setEmployeeDTO(employeeService.getEmployeeByBiometricId(csvData[2]));
                    timesheetDTO.setLogDate(LocalDate.parse(csvData[0], DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                    timesheetDTO.setLogTimeIn(csvData[6].equals("-") ? null : LocalTime.parse(csvData[6], csvData[6].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
                    timesheetDTO.setLogTimeOut(csvData[7].equals("-") ? null : LocalTime.parse(csvData[7], csvData[7].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
                    timesheetDTO.setShiftSchedule(csvData[4]);
                    timesheetDTO.setExceptionRemarks(csvData[8].equals("-") ? null : csvData[8]);
                    timesheetDTO.setLeaveRemarks(csvData[5].equals("-") ? null : csvData[5]);
                    timesheetDTO.setRegularWorkedHours(LocalTime.parse(csvData[9], csvData[9].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
                    timesheetDTO.setOvertimeWorkedHours(LocalTime.parse(csvData[10], csvData[10].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
                    timesheetDTO.setTotalWorkedHours(LocalTime.parse(csvData[11], csvData[11].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
                    timesheetDTO.setStatus("PENDING");
                    timesheetDTO.setCreatedBy(loggedInUser);
                    timesheetDTO.setUpdatedBy(loggedInUser);

                    timesheetService.saveOrUpdate(timesheetDTO);
                }
                // Close the dialog.
                dialog.close();

                // Update the data grid.
                this.updateTimesheetDTOGrid();

                // Show the notification message.
                Notification notification = Notification.show("Timesheet successfully uploaded.");
                notification.setPosition(Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setDuration(5000);
            }
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        return dialog;
    }

    private Dialog buildViewTimesheetDialog(TimesheetDTO timesheetDTO) {
        FormLayout timesheetDetailsLayout = new FormLayout();

        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(timesheetDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        String employeeName = timesheetDTO.getEmployeeDTO().getFirstName()
                .concat(" ")
                .concat(timesheetDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(timesheetDTO.getEmployeeDTO().getLastName())
                .concat(timesheetDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(timesheetDTO.getEmployeeDTO().getSuffix()) : "");

        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span logDateLabelSpan = new Span("Log Date");
        logDateLabelSpan.getStyle().set("text-align", "right");

        Span logDateValueSpan = new Span(timesheetDTO.getLogDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        logDateValueSpan.getStyle().setFontWeight("bold");

        Span logTimeInLabelSpan = new Span("Time In");
        logTimeInLabelSpan.getStyle().set("text-align", "right");

        Span logTimeInValueSpan = new Span(timesheetDTO.getLogTimeIn() != null ? timesheetDTO.getLogTimeIn().format(DateTimeFormatter.ofPattern("H:mm:ss a")) : "");
        logTimeInValueSpan.getStyle().setFontWeight("bold");

        Span logTimeOutLabelSpan = new Span("Time Out");
        logTimeOutLabelSpan.getStyle().set("text-align", "right");

        Span logTimeOutValueSpan = new Span(timesheetDTO.getLogTimeOut() != null ? timesheetDTO.getLogTimeOut().format(DateTimeFormatter.ofPattern("H:mm:ss a")) : "");
        logTimeOutValueSpan.getStyle().setFontWeight("bold");

        Span shiftScheduleLabelSpan = new Span("Shift Schedule");
        shiftScheduleLabelSpan.getStyle().set("text-align", "right");

        Span shiftScheduleValueSpan = new Span(timesheetDTO.getShiftSchedule());
        shiftScheduleValueSpan.getStyle().setFontWeight("bold");

        Span exceptionRemarksLabelSpan = new Span("Exception Remarks");
        exceptionRemarksLabelSpan.getStyle().set("text-align", "right");

        Span exceptionRemarksValueSpan = new Span(timesheetDTO.getExceptionRemarks());
        exceptionRemarksValueSpan.getStyle().setFontWeight("bold");

        Span leaveRemarksLabelSpan = new Span("Leave Remarks");
        leaveRemarksLabelSpan.getStyle().set("text-align", "right");

        Span leaveRemarksValueSpan = new Span(timesheetDTO.getLeaveRemarks());
        leaveRemarksValueSpan.getStyle().setFontWeight("bold");

        Span regularWorkedHoursLabelSpan = new Span("Regular Worked Hours");
        regularWorkedHoursLabelSpan.getStyle().set("text-align", "right");

        Span regularWorkedHoursValueSpan = new Span(timesheetDTO.getRegularWorkedHours().format(DateTimeFormatter.ofPattern("H:mm:ss a")));
        regularWorkedHoursValueSpan.getStyle().setFontWeight("bold");

        Span overtimeWorkedHoursLabelSpan = new Span("Overtime Worked Hours");
        overtimeWorkedHoursLabelSpan.getStyle().set("text-align", "right");

        Span overtimeWorkedHoursValueSpan = new Span(timesheetDTO.getOvertimeWorkedHours().format(DateTimeFormatter.ofPattern("H:mm:ss a")));
        overtimeWorkedHoursValueSpan.getStyle().setFontWeight("bold");

        Span totalWorkedHoursLabelSpan = new Span("Total Worked Hours");
        totalWorkedHoursLabelSpan.getStyle().set("text-align", "right");

        Span totalWorkedHoursValueSpan = new Span(timesheetDTO.getTotalWorkedHours().format(DateTimeFormatter.ofPattern("H:mm:ss a")));
        totalWorkedHoursValueSpan.getStyle().setFontWeight("bold");

        Span statusLabelSpan = new Span("Status");
        statusLabelSpan.getStyle().set("text-align", "right");

        Span statusValueSpan = new Span(timesheetDTO.getStatus());
        statusValueSpan.getStyle().setFontWeight("bold");

        timesheetDetailsLayout.add(employeeNoLabelSpan,
                employeeNoValueSpan,
                employeeNameLabelSpan,
                employeeNameValueSpan,
                logDateLabelSpan,
                logDateValueSpan,
                logTimeInLabelSpan,
                logTimeInValueSpan,
                logTimeOutLabelSpan,
                logTimeOutValueSpan,
                shiftScheduleLabelSpan,
                shiftScheduleValueSpan,
                exceptionRemarksLabelSpan,
                exceptionRemarksValueSpan,
                leaveRemarksLabelSpan,
                leaveRemarksValueSpan,
                regularWorkedHoursLabelSpan,
                regularWorkedHoursValueSpan,
                overtimeWorkedHoursLabelSpan,
                overtimeWorkedHoursValueSpan,
                totalWorkedHoursLabelSpan,
                totalWorkedHoursValueSpan,
                statusLabelSpan,
                statusValueSpan);
        timesheetDetailsLayout.getStyle().set("width", "720px").set("max-width", "100%");

        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setHeaderTitle("View Timesheet Details");
        dialog.setCloseOnOutsideClick(false);
        dialog.add(timesheetDetailsLayout);

        Button closeButton = new Button("Close", e -> dialog.close());

        dialog.getFooter().add(closeButton);

        return dialog;
    }

    private Dialog buildEditTimesheetDialog(TimesheetDTO timesheetDTO) {
        TimePicker logTimeInTimePicker = new TimePicker("Time In");
        logTimeInTimePicker.setStep(Duration.ofSeconds(1));
        logTimeInTimePicker.setRequired(true);
        logTimeInTimePicker.setRequiredIndicatorVisible(true);

        TimePicker logTimeOutTimePicker = new TimePicker("Time Out");
        logTimeOutTimePicker.setStep(Duration.ofSeconds(1));
        logTimeOutTimePicker.setRequired(true);
        logTimeOutTimePicker.setRequiredIndicatorVisible(true);

        ComboBox<String> exceptionRemarksComboBox = new ComboBox<>("Exception Remarks");
        exceptionRemarksComboBox.setItems("Early Out",
                                          "Late In",
                                          "Late In, Early Out",
                                          "Insufficient work time, Missing Punch Out",
                                          "Insufficient work time, Missing Punch Out, Late In");

        FormLayout formLayout = new FormLayout();
        formLayout.add(logTimeInTimePicker, logTimeOutTimePicker, exceptionRemarksComboBox);
        formLayout.getStyle().set("width", "720px").set("max-width", "100%");
        formLayout.setColspan(exceptionRemarksComboBox, 2);

        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setHeaderTitle("Edit Timesheet Details");
        dialog.setCloseOnOutsideClick(false);
        dialog.add(formLayout);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            // Get the regular duration of time log between the start time and time out.
            LocalTime startLocaTime = null;

            // Morning Shift
            if (timesheetDTO.getShiftSchedule().equals("7AM - 7PM")) {
                startLocaTime = LocalTime.of(7, 0, 0);
            }

            // Mid-Day Shift
            if (timesheetDTO.getShiftSchedule().equals("11AM - 11PM")) {
                startLocaTime = LocalTime.of(11, 0, 0);
            }

            // Night Shift
            if (timesheetDTO.getShiftSchedule().equals("7PM - 7AM")) {
                startLocaTime = LocalTime.of(19, 0, 0);
            }

            // Get the regular time duration.
            Duration regularTimeDuration;

            // If end time is before start time, it means the duration spans over midnight
            if (logTimeOutTimePicker.getValue().isBefore(startLocaTime)) {
                regularTimeDuration = Duration.between(startLocaTime, LocalTime.MAX).plus(Duration.between(LocalTime.MIN, logTimeOutTimePicker.getValue()));
            } else {
                regularTimeDuration = Duration.between(startLocaTime, logTimeOutTimePicker.getValue());
            }

            long regularWorkedSeconds = regularTimeDuration.getSeconds();
            LocalTime regularWorkedHours = LocalTime.ofSecondOfDay(regularWorkedSeconds);

            // Get the total duration of time log between time in and time out.
            Duration totalTimeDuration;

            // If end time is before start time, it means the duration spans over midnight
            if (logTimeOutTimePicker.getValue().isBefore(logTimeInTimePicker.getValue())) {
                totalTimeDuration = Duration.between(logTimeInTimePicker.getValue(), LocalTime.MAX).plus(Duration.between(LocalTime.MIN, logTimeOutTimePicker.getValue()));
            } else {
                totalTimeDuration = Duration.between(logTimeInTimePicker.getValue(), logTimeOutTimePicker.getValue());
            }

            long totalWorkedSeconds = totalTimeDuration.getSeconds();
            LocalTime totalWorkedHours = LocalTime.ofSecondOfDay(totalWorkedSeconds);

            timesheetDTO.setLogTimeIn(logTimeInTimePicker.getValue());
            timesheetDTO.setLogTimeOut(logTimeOutTimePicker.getValue());
            timesheetDTO.setRegularWorkedHours(regularWorkedHours);
            timesheetDTO.setTotalWorkedHours(totalWorkedHours);
            timesheetDTO.setExceptionRemarks(exceptionRemarksComboBox.getValue());
            timesheetDTO.setUpdatedBy(loggedInUser);

            timesheetService.saveOrUpdate(timesheetDTO);

            // Close the dialog.
            dialog.close();

            // Update the data grid.
            this.updateTimesheetDTOGrid();

            // Show the notification message.
            Notification notification = Notification.show("Timesheet successfully updated.");
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(5000);
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        return dialog;
    }
}
