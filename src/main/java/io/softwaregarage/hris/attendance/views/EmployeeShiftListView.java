package io.softwaregarage.hris.attendance.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.attendance.dtos.EmployeeShiftScheduleDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.attendance.dtos.EmployeeTimesheetDTO;
import io.softwaregarage.hris.attendance.services.EmployeeShiftScheduleService;
import io.softwaregarage.hris.attendance.services.EmployeeTimesheetService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Employee Shift")
@Route(value = "employee-shift-list", layout = MainLayout.class)
public class EmployeeShiftListView extends VerticalLayout {
    @Resource
    private final EmployeeShiftScheduleService employeeShiftScheduleService;

    @Resource
    private final EmployeeTimesheetService  employeeTimesheetService;

    @Resource
    private final UserService userService;

    private EmployeeShiftScheduleDTO employeeShiftScheduleDTO;

    private Grid<EmployeeShiftScheduleDTO> employeeShiftDTOGrid;
    private TextField searchFilterTextField;

    private final String loggedInUser;
    private UserDTO userDTO;

    public EmployeeShiftListView(EmployeeShiftScheduleService employeeShiftScheduleService,
                                 EmployeeTimesheetService employeeTimesheetService,
                                 UserService userService) {
        this.employeeShiftScheduleService = employeeShiftScheduleService;
        this.employeeTimesheetService = employeeTimesheetService;
        this.userService = userService;

        // Get the logged-in user of the system.
        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        this.add(buildHeaderToolbar(), buildEmployeeShiftDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent ->
                this.updateEmployeeShiftDTOGrid());

        Button addEmployeeShiftButton = new Button("Add Employee Shift");
        addEmployeeShiftButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addEmployeeShiftButton.addClickListener(event ->
                addEmployeeShiftButton.getUI().ifPresent(ui -> ui.navigate(EmployeeShiftFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addEmployeeShiftButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<EmployeeShiftScheduleDTO> buildEmployeeShiftDTOGrid() {
        employeeShiftDTOGrid = new Grid<>(EmployeeShiftScheduleDTO.class, false);

        employeeShiftDTOGrid.addColumn(employeeShiftDTO ->
                        employeeShiftDTO.getEmployeeDTO().getEmployeeNumber())
                            .setHeader("Employee No.")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(employeeShiftDTO ->
                        employeeShiftDTO.getEmployeeDTO().getFirstName()
                                .concat(" ")
                                .concat(employeeShiftDTO.getEmployeeDTO().getLastName())
                                .concat(employeeShiftDTO.getEmployeeDTO().getSuffix() != null
                                        ? employeeShiftDTO.getEmployeeDTO().getSuffix()
                                        : ""))
                            .setHeader("Employee Name")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(EmployeeShiftScheduleDTO::getShiftSchedule)
                            .setHeader("Shift Schedule")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(EmployeeShiftScheduleDTO::getShiftHours)
                            .setHeader("Shift Hours")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(EmployeeShiftScheduleDTO::getShiftScheduledDays)
                            .setHeader("Scheduled Days")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(employeeShiftDTO ->
                        employeeShiftDTO.getShiftStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
                            .setHeader("Start Shift")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(employeeShiftDTO ->
                        employeeShiftDTO.getShiftEndTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
                            .setHeader("End Shift")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(
                new ComponentRenderer<>(HorizontalLayout::new, (layout,
                                                                employeeShiftDTO) -> {
                                            String theme = String.format("badge %s",
                                                    employeeShiftDTO.isActiveShift() ? "success" : "contrast");

                                            Span activeSpan = new Span();
                                            activeSpan.getElement().setAttribute("theme", theme);
                                            activeSpan.setText(employeeShiftDTO.isActiveShift() ? "YES" : "NO");

                                            layout.setJustifyContentMode(JustifyContentMode.CENTER);
                                            layout.add(activeSpan);
                                        }))
                            .setHeader("Is Active Shift?")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(employeeShiftDTO ->
                        employeeShiftDTO.getAssignedApproverEmployeeProfileDTO().getFirstName()
                                .concat(" ")
                                .concat(employeeShiftDTO.getAssignedApproverEmployeeProfileDTO().getLastName())
                                .concat(employeeShiftDTO.getAssignedApproverEmployeeProfileDTO().getSuffix() != null
                                        ? employeeShiftDTO.getAssignedApproverEmployeeProfileDTO().getSuffix()
                                        : ""))
                .setHeader("Approver")
                .setSortable(true);
        employeeShiftDTOGrid.addComponentColumn(leaveFilingDTO ->
                buildRowToolbar()).setHeader("Action");
        employeeShiftDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                        GridVariant.LUMO_COLUMN_BORDERS,
                                        GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeShiftDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeeShiftDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeeShiftDTOGrid.setEmptyStateText("No employee shifts found.");
        employeeShiftDTOGrid.setItems(query ->
                employeeShiftScheduleService.getAll(query.getPage(), query.getPageSize()).stream());

        return employeeShiftDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewEmployeeShiftButton = new Button();
        viewEmployeeShiftButton.setTooltipText("View Employee Shift");
        viewEmployeeShiftButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewEmployeeShiftButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewEmployeeShiftButton.addClickListener(buttonClickEvent ->
                viewEmployeeShiftButton.getUI().ifPresent(ui -> {
            if (employeeShiftDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeShiftScheduleDTO selectedEmployeeShiftScheduleDTO = employeeShiftDTOGrid.getSelectionModel()
                        .getFirstSelectedItem().get();
                ui.navigate(EmployeeShiftDetailsView.class, selectedEmployeeShiftScheduleDTO.getId().toString());
            }
        }));

        Button editEmployeeShiftButton = new Button();
        editEmployeeShiftButton.setTooltipText("Edit Employee Shift");
        editEmployeeShiftButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editEmployeeShiftButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editEmployeeShiftButton.addClickListener(buttonClickEvent ->
                editEmployeeShiftButton.getUI().ifPresent(ui -> {
            if (employeeShiftDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeShiftScheduleDTO selectedEmployeeShiftScheduleDTO = employeeShiftDTOGrid.getSelectionModel()
                        .getFirstSelectedItem().get();
                ui.navigate(EmployeeShiftFormView.class, selectedEmployeeShiftScheduleDTO.getId().toString());
            }
        }));

        Button deleteEmployeeShiftButton = new Button();
        deleteEmployeeShiftButton.setTooltipText("Delete Employee Shift");
        deleteEmployeeShiftButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteEmployeeShiftButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteEmployeeShiftButton.addClickListener(buttonClickEvent -> {
            if (employeeShiftDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeShiftScheduleDTO selectedEmployeeShiftScheduleDTO = employeeShiftDTOGrid.getSelectionModel()
                        .getFirstSelectedItem().get();

                // Check first if that record is not an active employee anymore. If it is not active, you may proceed
                // for the deletion.
                if (selectedEmployeeShiftScheduleDTO.getEmployeeDTO().getStatus().equals("RESIGNED")
                        || selectedEmployeeShiftScheduleDTO.getEmployeeDTO().getStatus().equals("RETIRED")
                        || selectedEmployeeShiftScheduleDTO.getEmployeeDTO().getStatus().equals("TERMINATED")
                        || selectedEmployeeShiftScheduleDTO.getEmployeeDTO().getStatus().equals("DECEASED")) {
                    // Show the confirmation dialog.
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Delete Employee Shift");
                    confirmDialog.setText(new Html("""
                                               <p>
                                               WARNING! Are you sure you want to delete the employee's shift schedule?
                                               This will also delete all the employee's timesheet related to this shift
                                               schedule.
                                               </p>
                                               """));
                    confirmDialog.setConfirmText("Yes, Delete it.");
                    confirmDialog.setConfirmButtonTheme("error primary");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Delete first the related timesheet of the employee.
                        List<EmployeeTimesheetDTO> employeeTimesheetDTOList = employeeTimesheetService
                                .findByEmployeeDTO(selectedEmployeeShiftScheduleDTO.getEmployeeDTO());
                        for (EmployeeTimesheetDTO employeeTimesheetDTO : employeeTimesheetDTOList) {
                            employeeTimesheetService.delete(employeeTimesheetDTO);
                        }

                        // Get the selected department and delete it.
                        employeeShiftScheduleService.delete(selectedEmployeeShiftScheduleDTO);

                        // Refresh the data grid from the backend after the delete operation.
                        employeeShiftDTOGrid.getDataProvider().refreshAll();

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully deleted the selected employee shift and all of its timesheet.",
                                5000,
                                Notification.Position.TOP_CENTER);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                        // Close the confirmation dialog.
                        confirmDialog.close();
                    });
                    confirmDialog.setCancelable(true);
                    confirmDialog.setCancelText("No");
                    confirmDialog.open();
                } else {
                    // Show notification message.
                    Notification notification = Notification.show("You cannot delete the selected employee shift. Employee is still in active status.",
                            5000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        if (userDTO.getRole().equals("ROLE_ADMIN")) {
            rowToolbarLayout.add(viewEmployeeShiftButton, editEmployeeShiftButton, deleteEmployeeShiftButton);
        } else {
            rowToolbarLayout.add(viewEmployeeShiftButton, editEmployeeShiftButton);
        }
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateEmployeeShiftDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            employeeShiftDTOGrid.setItems(employeeShiftScheduleService
                    .findByParameter(searchFilterTextField.getValue()));
        } else {
            employeeShiftDTOGrid.setItems(query ->
                    employeeShiftScheduleService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
