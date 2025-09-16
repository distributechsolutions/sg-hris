package io.softwaregarage.hris.attendance.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.combobox.ComboBox;
//import com.vaadin.flow.component.dialog.Dialog;
//import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.component.timepicker.TimePicker;
//import com.vaadin.flow.component.upload.Upload;
//import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.attendance.dtos.EmployeeTimesheetDTO;
import io.softwaregarage.hris.attendance.services.EmployeeTimesheetService;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
//import io.distributechsolutions.hris.utils.CSVUtil;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Timesheet List")
@Route(value = "timesheets-view", layout = MainLayout.class)
public class EmployeeTimesheetListView extends VerticalLayout {
    @Resource private final EmployeeTimesheetService employeeTimesheetService;
    @Resource private final EmployeeProfileService employeeProfileService;

    private Grid<EmployeeTimesheetDTO> timesheetDTOGrid;
    private TextField searchFilterTextField;

    private String loggedInUser;

    public EmployeeTimesheetListView(EmployeeTimesheetService employeeTimesheetService,
                                     EmployeeProfileService employeeProfileService) {
        this.employeeTimesheetService = employeeTimesheetService;
        this.employeeProfileService = employeeProfileService;

        // Get the logged-in user of the system.
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
//        uploadButton.addClickListener(buttonClickEvent -> this.buildUploadTimesheetDialog().open());

        headerToolbarLayout.add(searchFilterTextField, uploadButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<EmployeeTimesheetDTO> buildTimesheetDTOGrid() {

        timesheetDTOGrid = new Grid<>(EmployeeTimesheetDTO.class, false);

        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> employeeTimesheetDTO.getEmployeeDTO().getEmployeeNumber())
                        .setHeader("Employee No.")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> employeeTimesheetDTO.getEmployeeDTO().getFirstName()
                                                                            .concat(" ")
                                                                            .concat(employeeTimesheetDTO.getEmployeeDTO().getMiddleName())
                                                                            .concat(" ")
                                                                            .concat(employeeTimesheetDTO.getEmployeeDTO().getLastName())
                                                                            .concat(employeeTimesheetDTO.getEmployeeDTO().getSuffix() != null ? employeeTimesheetDTO.getEmployeeDTO().getSuffix() : ""))
                        .setHeader("Employee Name")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> DateTimeFormatter.ofPattern("MMM dd, yyyy").format(employeeTimesheetDTO.getLogDate()))
                        .setHeader("Log Date")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> employeeTimesheetDTO.getShiftScheduleDTO().getShiftSchedule())
                        .setHeader("Shift Schedule")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> DateTimeFormatter.ofPattern("HH:mm:ss a").format(employeeTimesheetDTO.getLogTime()))
                        .setHeader("Log Time")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(EmployeeTimesheetDTO::getLogDetail)
                        .setHeader("Log Detail")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, employeeTimesheetDTO) -> {
                                        String theme = String.format("badge %s", employeeTimesheetDTO.getStatus().equalsIgnoreCase("PENDING") ? "contrast" : "success");

                                        Span activeSpan = new Span();
                                        activeSpan.getElement().setAttribute("theme", theme);
                                        activeSpan.setText(employeeTimesheetDTO.getStatus());

                                        layout.setJustifyContentMode(JustifyContentMode.CENTER);
                                        layout.add(activeSpan);
                                    }))
                        .setHeader("Status")
                        .setSortable(true);
        timesheetDTOGrid.addComponentColumn(employeeTimesheetDTO -> buildRowToolbar(employeeTimesheetDTO)).setHeader("Action");
        timesheetDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                          GridVariant.LUMO_COLUMN_BORDERS,
                                          GridVariant.LUMO_WRAP_CELL_CONTENT);
        timesheetDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        timesheetDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        timesheetDTOGrid.setEmptyStateText("No pending timesheet records found.");
        timesheetDTOGrid.setItems(query ->
                employeeTimesheetService.getAll(query.getPage(), query.getPageSize())
                        .stream()
                        .filter(employeeTimesheetDTO -> "PENDING".equals(employeeTimesheetDTO.getStatus()))
        );

        return timesheetDTOGrid;
    }

    public HorizontalLayout buildRowToolbar(EmployeeTimesheetDTO employeeTimesheetDTO) {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Timesheet");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
//                EmployeeTimesheetDTO selectedEmployeeTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
//                buildViewTimesheetDialog(selectedEmployeeTimesheetDTO).open();
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Timesheet");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
//                EmployeeTimesheetDTO selectedEmployeeTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
//                buildEditTimesheetDialog(selectedEmployeeTimesheetDTO).open();
            }
        }));

        Button approveButton = new Button();
        approveButton.setTooltipText("Approve Timesheet");
        approveButton.setIcon(LineAwesomeIcon.THUMBS_UP.create());
        approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        approveButton.addClickListener(buttonClickEvent -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeTimesheetDTO selectedEmployeeTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                selectedEmployeeTimesheetDTO.setStatus("APPROVED");
                selectedEmployeeTimesheetDTO.setUpdatedBy(loggedInUser);

                employeeTimesheetService.saveOrUpdate(selectedEmployeeTimesheetDTO);

                this.updateTimesheetDTOGrid();

                Notification notification = Notification.show("Timesheet approved successfully.", 5000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        rowToolbarLayout.add(viewButton, editButton, approveButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateTimesheetDTOGrid() {
        if (!searchFilterTextField.getValue().isEmpty()) {
            timesheetDTOGrid.setItems(employeeTimesheetService.findByParameter(searchFilterTextField.getValue())
                    .stream()
                    .filter(employeeTimesheetDTO -> "PENDING".equals(employeeTimesheetDTO.getStatus()))
                    .toList()
            );
        } else {
            timesheetDTOGrid.setItems(query ->
                    employeeTimesheetService.getAll(query.getPage(), query.getPageSize())
                            .stream()
                            .filter(employeeTimesheetDTO -> "PENDING".equals(employeeTimesheetDTO.getStatus()))
            );
        }
    }

//    private Dialog buildUploadTimesheetDialog() {
//        MultiFileMemoryBuffer multiFileMemoryBuffer = new MultiFileMemoryBuffer();
//        Upload upload = new Upload(multiFileMemoryBuffer);
//        upload.setAcceptedFileTypes(".csv");
//
//        VerticalLayout uploadTimesheetLayout = new VerticalLayout();
//        uploadTimesheetLayout.setSpacing(false);
//        uploadTimesheetLayout.setPadding(false);
//        uploadTimesheetLayout.setAlignItems(Alignment.STRETCH);
//        uploadTimesheetLayout.getStyle().set("width", "720px").set("max-width", "100%");
//        uploadTimesheetLayout.add(upload);
//
//        Dialog dialog = new Dialog();
//        dialog.setModal(true);
//        dialog.setResizable(false);
//        dialog.setHeaderTitle("Upload Timesheet");
//        dialog.setCloseOnOutsideClick(false);
//        dialog.add(uploadTimesheetLayout);
//
//        Button saveButton = new Button("Save");
//        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        saveButton.addClickListener(buttonClickEvent -> {
//            List<String[]> csvDataList = CSVUtil.readCSVData(multiFileMemoryBuffer);
//
//            if (!csvDataList.isEmpty()) {
//                for (String[] csvData : csvDataList) {
//                    EmployeeTimesheetDTO employeeTimesheetDTO = new EmployeeTimesheetDTO();
//                    employeeTimesheetDTO.setLogDate(LocalDate.parse(csvData[0], DateTimeFormatter.ofPattern("yyyy/MM/dd")));
//                    employeeTimesheetDTO.setLogTimeIn(csvData[6].equals("-") ? null : LocalTime.parse(csvData[6], csvData[6].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
//                    employeeTimesheetDTO.setLogTimeOut(csvData[7].equals("-") ? null : LocalTime.parse(csvData[7], csvData[7].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
//                    employeeTimesheetDTO.setShiftSchedule(csvData[4]);
//                    employeeTimesheetDTO.setExceptionRemarks(csvData[8].equals("-") ? null : csvData[8]);
//                    employeeTimesheetDTO.setLeaveRemarks(csvData[5].equals("-") ? null : csvData[5]);
//                    employeeTimesheetDTO.setRegularWorkedHours(!csvData[6].equals("-") && !csvData[7].equals("-") ? this.getWorkedHours(csvData[4], csvData[6], csvData[7]) : LocalTime.parse("00:00:00"));
//                    employeeTimesheetDTO.setOvertimeWorkedHours(!csvData[6].equals("-") && !csvData[7].equals("-") ? this.getOvertimeHours(csvData[4], csvData[6], csvData[7]) : LocalTime.parse("00:00:00"));
//                    employeeTimesheetDTO.setTotalWorkedHours(LocalTime.parse(csvData[11], csvData[11].contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss")));
//                    employeeTimesheetDTO.setStatus("PENDING");
//                    employeeTimesheetDTO.setCreatedBy(loggedInUser);
//                    employeeTimesheetDTO.setUpdatedBy(loggedInUser);
//
//                    employeeTimesheetService.saveOrUpdate(employeeTimesheetDTO);
//                }
//                // Close the dialog.
//                dialog.close();
//
//                // Update the data grid.
//                this.updateTimesheetDTOGrid();
//
//                // Show the notification message.
//                Notification notification = Notification.show("Timesheet successfully uploaded.");
//                notification.setPosition(Notification.Position.TOP_CENTER);
//                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//                notification.setDuration(5000);
//            }
//        });
//
//        Button cancelButton = new Button("Cancel", e -> dialog.close());
//
//        dialog.getFooter().add(cancelButton);
//        dialog.getFooter().add(saveButton);
//
//        return dialog;
//    }

//    private Dialog buildViewTimesheetDialog(EmployeeTimesheetDTO employeeTimesheetDTO) {
//        FormLayout timesheetDetailsLayout = new FormLayout();
//
//        Span employeeNoLabelSpan = new Span("Employee No");
//        employeeNoLabelSpan.getStyle().set("text-align", "right");
//
//        Span employeeNoValueSpan = new Span(employeeTimesheetDTO.getEmployeeDTO().getEmployeeNumber());
//        employeeNoValueSpan.getStyle().setFontWeight("bold");
//
//        Span employeeNameLabelSpan = new Span("Employee Name");
//        employeeNameLabelSpan.getStyle().set("text-align", "right");
//
//        String employeeName = employeeTimesheetDTO.getEmployeeDTO().getFirstName()
//                .concat(" ")
//                .concat(employeeTimesheetDTO.getEmployeeDTO().getMiddleName())
//                .concat(" ")
//                .concat(employeeTimesheetDTO.getEmployeeDTO().getLastName())
//                .concat(employeeTimesheetDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(employeeTimesheetDTO.getEmployeeDTO().getSuffix()) : "");
//
//        Span employeeNameValueSpan = new Span(employeeName);
//        employeeNameValueSpan.getStyle().setFontWeight("bold");
//
//        Span logDateLabelSpan = new Span("Log Date");
//        logDateLabelSpan.getStyle().set("text-align", "right");
//
//        Span logDateValueSpan = new Span(employeeTimesheetDTO.getLogDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
//        logDateValueSpan.getStyle().setFontWeight("bold");
//
//        Span logTimeInLabelSpan = new Span("Time In");
//        logTimeInLabelSpan.getStyle().set("text-align", "right");
//
//        Span logTimeInValueSpan = new Span(employeeTimesheetDTO.getLogTimeIn() != null ? employeeTimesheetDTO.getLogTimeIn().format(DateTimeFormatter.ofPattern("H:mm:ss a")) : "");
//        logTimeInValueSpan.getStyle().setFontWeight("bold");
//
//        Span logTimeOutLabelSpan = new Span("Time Out");
//        logTimeOutLabelSpan.getStyle().set("text-align", "right");
//
//        Span logTimeOutValueSpan = new Span(employeeTimesheetDTO.getLogTimeOut() != null ? employeeTimesheetDTO.getLogTimeOut().format(DateTimeFormatter.ofPattern("H:mm:ss a")) : "");
//        logTimeOutValueSpan.getStyle().setFontWeight("bold");
//
//        Span shiftScheduleLabelSpan = new Span("Shift Schedule");
//        shiftScheduleLabelSpan.getStyle().set("text-align", "right");
//
//        Span shiftScheduleValueSpan = new Span(employeeTimesheetDTO.getShiftSchedule());
//        shiftScheduleValueSpan.getStyle().setFontWeight("bold");
//
//        Span exceptionRemarksLabelSpan = new Span("Exception Remarks");
//        exceptionRemarksLabelSpan.getStyle().set("text-align", "right");
//
//        Span exceptionRemarksValueSpan = new Span(employeeTimesheetDTO.getExceptionRemarks());
//        exceptionRemarksValueSpan.getStyle().setFontWeight("bold");
//
//        Span leaveRemarksLabelSpan = new Span("Leave Remarks");
//        leaveRemarksLabelSpan.getStyle().set("text-align", "right");
//
//        Span leaveRemarksValueSpan = new Span(employeeTimesheetDTO.getLeaveRemarks());
//        leaveRemarksValueSpan.getStyle().setFontWeight("bold");
//
//        Span regularWorkedHoursLabelSpan = new Span("Regular Worked Hours");
//        regularWorkedHoursLabelSpan.getStyle().set("text-align", "right");
//
//        Span regularWorkedHoursValueSpan = new Span(employeeTimesheetDTO.getRegularWorkedHours().format(DateTimeFormatter.ofPattern("H:mm:ss")));
//        regularWorkedHoursValueSpan.getStyle().setFontWeight("bold");
//
//        Span overtimeWorkedHoursLabelSpan = new Span("Overtime Worked Hours");
//        overtimeWorkedHoursLabelSpan.getStyle().set("text-align", "right");
//
//        Span overtimeWorkedHoursValueSpan = new Span(employeeTimesheetDTO.getOvertimeWorkedHours().format(DateTimeFormatter.ofPattern("H:mm:ss")));
//        overtimeWorkedHoursValueSpan.getStyle().setFontWeight("bold");
//
//        Span totalWorkedHoursLabelSpan = new Span("Total Worked Hours");
//        totalWorkedHoursLabelSpan.getStyle().set("text-align", "right");
//
//        Span totalWorkedHoursValueSpan = new Span(employeeTimesheetDTO.getTotalWorkedHours().format(DateTimeFormatter.ofPattern("H:mm:ss")));
//        totalWorkedHoursValueSpan.getStyle().setFontWeight("bold");
//
//        Span statusLabelSpan = new Span("Status");
//        statusLabelSpan.getStyle().set("text-align", "right");
//
//        Span statusValueSpan = new Span(employeeTimesheetDTO.getStatus());
//        statusValueSpan.getStyle().setFontWeight("bold");
//
//        timesheetDetailsLayout.add(employeeNoLabelSpan,
//                employeeNoValueSpan,
//                employeeNameLabelSpan,
//                employeeNameValueSpan,
//                logDateLabelSpan,
//                logDateValueSpan,
//                logTimeInLabelSpan,
//                logTimeInValueSpan,
//                logTimeOutLabelSpan,
//                logTimeOutValueSpan,
//                shiftScheduleLabelSpan,
//                shiftScheduleValueSpan,
//                exceptionRemarksLabelSpan,
//                exceptionRemarksValueSpan,
//                leaveRemarksLabelSpan,
//                leaveRemarksValueSpan,
//                regularWorkedHoursLabelSpan,
//                regularWorkedHoursValueSpan,
//                overtimeWorkedHoursLabelSpan,
//                overtimeWorkedHoursValueSpan,
//                totalWorkedHoursLabelSpan,
//                totalWorkedHoursValueSpan,
//                statusLabelSpan,
//                statusValueSpan);
//        timesheetDetailsLayout.getStyle().set("width", "720px").set("max-width", "100%");
//
//        Dialog dialog = new Dialog();
//        dialog.setModal(true);
//        dialog.setResizable(false);
//        dialog.setHeaderTitle("View Timesheet Details");
//        dialog.setCloseOnOutsideClick(false);
//        dialog.add(timesheetDetailsLayout);
//
//        Button closeButton = new Button("Close", e -> dialog.close());
//
//        dialog.getFooter().add(closeButton);
//
//        return dialog;
//    }

//    private Dialog buildEditTimesheetDialog(EmployeeTimesheetDTO employeeTimesheetDTO) {
//        TimePicker logTimeInTimePicker = new TimePicker("Time In");
//        logTimeInTimePicker.setStep(Duration.ofSeconds(1));
//        logTimeInTimePicker.setRequired(true);
//        logTimeInTimePicker.setRequiredIndicatorVisible(true);
//
//        TimePicker logTimeOutTimePicker = new TimePicker("Time Out");
//        logTimeOutTimePicker.setStep(Duration.ofSeconds(1));
//        logTimeOutTimePicker.setRequired(true);
//        logTimeOutTimePicker.setRequiredIndicatorVisible(true);
//
//        ComboBox<String> exceptionRemarksComboBox = new ComboBox<>("Exception Remarks");
//        exceptionRemarksComboBox.setItems("Early Out",
//                                          "Late In",
//                                          "Late In, Early Out",
//                                          "Insufficient work time, Missing Punch Out",
//                                          "Insufficient work time, Missing Punch Out, Late In");
//
//        FormLayout formLayout = new FormLayout();
//        formLayout.add(logTimeInTimePicker, logTimeOutTimePicker, exceptionRemarksComboBox);
//        formLayout.getStyle().set("width", "720px").set("max-width", "100%");
//        formLayout.setColspan(exceptionRemarksComboBox, 2);
//
//        Dialog dialog = new Dialog();
//        dialog.setModal(true);
//        dialog.setResizable(false);
//        dialog.setHeaderTitle("Edit Timesheet Details");
//        dialog.setCloseOnOutsideClick(false);
//        dialog.add(formLayout);
//
//        Button saveButton = new Button("Save");
//        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        saveButton.addClickListener(buttonClickEvent -> {
//            LocalTime logTimeIn = logTimeInTimePicker.getValue();
//            LocalTime logTimeOut = logTimeOutTimePicker.getValue();
//            LocalTime regularWorkedHours = this.getWorkedHours(employeeTimesheetDTO.getShiftSchedule(),
//                                                               logTimeIn.format(DateTimeFormatter.ofPattern("H:mm:ss", Locale.ENGLISH)),
//                                                               logTimeOut.format(DateTimeFormatter.ofPattern("H:mm:ss", Locale.ENGLISH)));
//            LocalTime overtimeWorkedHours = this.getOvertimeHours(employeeTimesheetDTO.getShiftSchedule(),
//                                                                logTimeIn.format(DateTimeFormatter.ofPattern("H:mm:ss", Locale.ENGLISH)),
//                                                                logTimeOut.format(DateTimeFormatter.ofPattern("H:mm:ss", Locale.ENGLISH)));
//
//            Duration totalWorkDuration = Duration.between(logTimeIn, logTimeOut);
//
//            // This will check if the time logs are in nigh shift schedule.
//            if (totalWorkDuration.isNegative()) {
//                totalWorkDuration = totalWorkDuration.plusDays(1);
//            }
//
//            LocalTime totalWorkedHours = LocalTime.of((int) totalWorkDuration.toHours(), (int) totalWorkDuration.toMinutes() % 60);
//
//            employeeTimesheetDTO.setLogTimeIn(logTimeIn);
//            employeeTimesheetDTO.setLogTimeOut(logTimeOut);
//            employeeTimesheetDTO.setRegularWorkedHours(regularWorkedHours);
//            employeeTimesheetDTO.setOvertimeWorkedHours(overtimeWorkedHours);
//            employeeTimesheetDTO.setTotalWorkedHours(totalWorkedHours);
//            employeeTimesheetDTO.setExceptionRemarks(exceptionRemarksComboBox.getValue());
//            employeeTimesheetDTO.setUpdatedBy(loggedInUser);
//
//            employeeTimesheetService.saveOrUpdate(employeeTimesheetDTO);
//
//            // Close the dialog.
//            dialog.close();
//
//            // Update the data grid.
//            this.updateTimesheetDTOGrid();
//
//            // Show the notification message.
//            Notification notification = Notification.show("Timesheet successfully updated.");
//            notification.setPosition(Notification.Position.TOP_CENTER);
//            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//            notification.setDuration(5000);
//        });
//
//        Button cancelButton = new Button("Cancel", e -> dialog.close());
//
//        dialog.getFooter().add(cancelButton);
//        dialog.getFooter().add(saveButton);
//
//        return dialog;
//    }

//    private LocalTime getWorkedHours(String shiftSchedule, String loginTime, String logoutTime) {
//        // Get the shift start time based from the shift schedule and convert it to local time.
//        String startShift = shiftSchedule.split(" - ")[0];
//
//        // Set the standard work hours.
//        int standardWorkHours = 8;
//
//        DateTimeFormatter shiftFormatter = DateTimeFormatter.ofPattern("ha");
//        LocalTime shiftStartTime = LocalTime.parse(startShift, shiftFormatter);
//        LocalTime shiftEndTime = shiftStartTime.plusHours(standardWorkHours);
//
//        // Get the values of start time and end time.
//        LocalTime startTime = LocalTime.parse(loginTime, loginTime.contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss"));
//        LocalTime endTime = LocalTime.parse(logoutTime, logoutTime.contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss"));
//
//        // ---------- START ----------
//
//        // STEP 1: Check if the startTime is earlier than the startShiftTime.
//        if (startTime.isBefore(shiftStartTime)) {
//            startTime = shiftStartTime;
//        }
//
//        // Step 2: Check if the endTime is later than the endShiftTime.
//        if (endTime.isAfter(shiftEndTime)) {
//            endTime = shiftEndTime;
//        }
//
//        // Step 3: Check if the endTime is on the next day (this is for night shift schedule)
//        // and calculate the regular worked hours spent.
//        Duration regularWorkedDuration = Duration.between(startTime, endTime);
//
//        if (regularWorkedDuration.isNegative()) {
//            regularWorkedDuration = regularWorkedDuration.plusDays(1);
//        }
//
//        // ---------- END ----------
//
//        return LocalTime.of((int) regularWorkedDuration.toHours(), (int) regularWorkedDuration.toMinutes() % 60);
//    }
//
//    private LocalTime getOvertimeHours(String shiftSchedule, String loginTime, String logoutTime) {
//        // Get the number of worked hours.
//        LocalTime workedHours = this.getWorkedHours(shiftSchedule, loginTime, logoutTime);
//
//        // Get the values of start time and end time.
//        LocalTime startTime = LocalTime.parse(loginTime, loginTime.contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss"));
//        LocalTime endTime = LocalTime.parse(logoutTime, logoutTime.contains("(+1)") ? DateTimeFormatter.ofPattern("H:mm:ss (+1)") : DateTimeFormatter.ofPattern("H:mm:ss"));
//
//        // Get the duration between the end time and worked hours.
//        Duration totalWorkedDuration = Duration.between(startTime, endTime);
//        if (totalWorkedDuration.isNegative()) {
//            totalWorkedDuration = totalWorkedDuration.plusDays(1);
//        }
//
//        LocalTime totalWorkedHours = LocalTime.of((int) totalWorkedDuration.toHours(), (int) totalWorkedDuration.toMinutes() % 60);
//
//        // Compute the overtime hours between
//        Duration overtimeDuration = Duration.between(workedHours, totalWorkedHours);
//        LocalTime overtimeHours = LocalTime.of((int) overtimeDuration.toHours(), (int) overtimeDuration.toMinutes() % 60);
//
//        return overtimeHours;
//    }

}
