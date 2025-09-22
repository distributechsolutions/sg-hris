package io.softwaregarage.hris.attendance.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.server.StreamResource;
import io.softwaregarage.hris.attendance.dtos.EmployeeTimesheetDTO;
import io.softwaregarage.hris.attendance.services.EmployeeTimesheetService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

        // Include the ID of the record but do not display it.
        timesheetDTOGrid.addColumn(EmployeeTimesheetDTO::getId).setHeader("ID").setVisible(false);
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> employeeTimesheetDTO.getEmployeeDTO().getEmployeeNumber())
                        .setHeader("Employee No.")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> employeeTimesheetDTO.getEmployeeDTO().getFirstName()
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
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> DateTimeFormatter.ofPattern("hh:mm:ss a").format(employeeTimesheetDTO.getLogTime()))
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
                EmployeeTimesheetDTO selectedEmployeeTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                buildViewTimesheetDialog(selectedEmployeeTimesheetDTO).open();
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Timesheet");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeTimesheetDTO selectedEmployeeTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                buildEditTimesheetDialog(selectedEmployeeTimesheetDTO).open();
            }
        }));

        Button approveButton = new Button();
        approveButton.setTooltipText("Approve Timesheet");
        approveButton.setIcon(LineAwesomeIcon.THUMBS_UP.create());
        approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        approveButton.addClickListener(buttonClickEvent -> approveButton.getUI().ifPresent(ui -> {
            if (timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeTimesheetDTO selectedEmployeeTimesheetDTO = timesheetDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                selectedEmployeeTimesheetDTO.setStatus("APPROVED");
                selectedEmployeeTimesheetDTO.setUpdatedBy(loggedInUser);

                employeeTimesheetService.saveOrUpdate(selectedEmployeeTimesheetDTO);

                this.updateTimesheetDTOGrid();

                Notification notification = Notification.show("Timesheet approved successfully.", 5000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        }));

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

    private Dialog buildViewTimesheetDialog(EmployeeTimesheetDTO employeeTimesheetDTO) {
        FormLayout timesheetDetailsLayout = new FormLayout();

        Span employeeNoLabelSpan = new Span("Employee No");
        Span employeeNoValueSpan = new Span(employeeTimesheetDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        String employeeName = employeeTimesheetDTO.getEmployeeDTO().getFirstName()
                .concat(" ")
                .concat(employeeTimesheetDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(employeeTimesheetDTO.getEmployeeDTO().getLastName())
                .concat(employeeTimesheetDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(employeeTimesheetDTO.getEmployeeDTO().getSuffix()) : "");
        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span logDateLabelSpan = new Span("Log Date");
        Span logDateValueSpan = new Span(employeeTimesheetDTO.getLogDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        logDateValueSpan.getStyle().setFontWeight("bold");

        Span logTimeInLabelSpan = new Span("Log Time");
        Span logTimeInValueSpan = new Span(employeeTimesheetDTO.getLogTime().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        logTimeInValueSpan.getStyle().setFontWeight("bold");

        Span logTimeOutLabelSpan = new Span("Log Detail");
        Span logTimeOutValueSpan = new Span(employeeTimesheetDTO.getLogDetail());
        logTimeOutValueSpan.getStyle().setFontWeight("bold");

        Span shiftScheduleLabelSpan = new Span("Shift Schedule");
        Span shiftScheduleValueSpan = new Span(employeeTimesheetDTO.getShiftScheduleDTO().getShiftSchedule());
        shiftScheduleValueSpan.getStyle().setFontWeight("bold");

        Span shiftScheduleTimeLabelSpan = new Span("Shift Time");
        Span shiftScheduleTimeValueSpan = new Span(employeeTimesheetDTO.getShiftScheduleDTO()
                                                                  .getShiftStartTime()
                                                                  .format(DateTimeFormatter.ofPattern("hh:mm a"))
                                                                  .concat(" to ")
                                                                  .concat(employeeTimesheetDTO.getShiftScheduleDTO()
                                                                                               .getShiftEndTime()
                                                                                               .format(DateTimeFormatter.ofPattern("hh:mm a"))));
        shiftScheduleTimeValueSpan.getStyle().setFontWeight("bold");

        Span statusLabelSpan = new Span("Status");
        Span statusValueSpan = new Span(employeeTimesheetDTO.getStatus());
        statusValueSpan.getStyle().setFontWeight("bold");

        String fileName = "log_image_".concat(employeeTimesheetDTO.getEmployeeDTO().getEmployeeNumber()).concat(".jpg");
        Image logImage = this.convertLogImage(fileName, employeeTimesheetDTO.getLogImage());

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
                                   shiftScheduleTimeLabelSpan,
                                   shiftScheduleTimeValueSpan,
                                   statusLabelSpan,
                                   statusValueSpan,
                                   logImage);
        timesheetDetailsLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                                                  new FormLayout.ResponsiveStep("500px", 2));
        timesheetDetailsLayout.setColspan(logImage, 2);
        timesheetDetailsLayout.getStyle().set("width", "500px").set("max-width", "100%");

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

    private Dialog buildEditTimesheetDialog(EmployeeTimesheetDTO employeeTimesheetDTO) {
        Span employeeNoLabelSpan = new Span("Employee No");
        Span employeeNoValueSpan = new Span(employeeTimesheetDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        String employeeName = employeeTimesheetDTO.getEmployeeDTO().getFirstName()
                .concat(" ")
                .concat(employeeTimesheetDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(employeeTimesheetDTO.getEmployeeDTO().getLastName())
                .concat(employeeTimesheetDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(employeeTimesheetDTO.getEmployeeDTO().getSuffix()) : "");
        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        DatePicker logDatePicker = new DatePicker("Log Date");
        logDatePicker.setValue(employeeTimesheetDTO.getLogDate());

        TimePicker logTimeTimePicker = new TimePicker("Log Time");
        logTimeTimePicker.setStep(Duration.ofSeconds(1));
        logTimeTimePicker.setRequired(true);
        logTimeTimePicker.setRequiredIndicatorVisible(true);
        logTimeTimePicker.setValue(employeeTimesheetDTO.getLogTime());

        RadioButtonGroup<String> logDetailRadioGroup = new RadioButtonGroup<>("Log Detail");
        logDetailRadioGroup.setItems("Log In", "Log Out");
        logDetailRadioGroup.setValue(employeeTimesheetDTO.getLogDetail());

        FormLayout formLayout = new FormLayout();
        formLayout.add(employeeNoLabelSpan,
                       employeeNoValueSpan,
                       employeeNameLabelSpan,
                       employeeNameValueSpan,
                       logDatePicker,
                       logTimeTimePicker,
                       logDetailRadioGroup);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                                      new FormLayout.ResponsiveStep("500px", 2));
        formLayout.getStyle().set("width", "500px").set("max-width", "100%");

        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setHeaderTitle("Edit Timesheet Details");
        dialog.setCloseOnOutsideClick(false);
        dialog.add(formLayout);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            employeeTimesheetDTO.setLogDate(logDatePicker.getValue());
            employeeTimesheetDTO.setLogTime(logTimeTimePicker.getValue());
            employeeTimesheetDTO.setLogDetail(logDetailRadioGroup.getValue());
            employeeTimesheetDTO.setUpdatedBy(loggedInUser);

            employeeTimesheetService.saveOrUpdate(employeeTimesheetDTO);

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

    private Image convertLogImage(String fileName, byte[] logImage) {
        StreamResource streamResource = new StreamResource(fileName, () -> new ByteArrayInputStream(logImage));
        Image image = new Image(streamResource, "Log Image Preview");
        image.setWidth("320px");
        image.setHeight("auto");
        return image;
    }

}
