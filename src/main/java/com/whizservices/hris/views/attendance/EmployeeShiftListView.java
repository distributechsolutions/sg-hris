package com.whizservices.hris.views.attendance;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.admin.UserDTO;
import com.whizservices.hris.dtos.attendance.EmployeeShiftScheduleDTO;
import com.whizservices.hris.services.admin.UserService;
import com.whizservices.hris.services.attendance.EmployeeShiftScheduleService;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.security.RolesAllowed;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Employee Shift")
@Route(value = "employee-shift-list", layout = MainLayout.class)
public class EmployeeShiftListView extends VerticalLayout {
    private final EmployeeShiftScheduleService employeeShiftScheduleService;
    private final UserService userService;

    private Grid<EmployeeShiftScheduleDTO> employeeShiftDTOGrid;
    private TextField searchFilterTextField;

    private final String loggedInUser;
    private UserDTO userDTO;

    public EmployeeShiftListView(EmployeeShiftScheduleService employeeShiftScheduleService,
                                 UserService userService) {
        this.employeeShiftScheduleService = employeeShiftScheduleService;
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateEmployeeShiftDTOGrid());

        Button addEmployeeShiftButton = new Button("Add Employee Shift");
        addEmployeeShiftButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addEmployeeShiftButton.addClickListener(event -> addEmployeeShiftButton.getUI().ifPresent(ui -> ui.navigate(EmployeeShiftFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addEmployeeShiftButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<EmployeeShiftScheduleDTO> buildEmployeeShiftDTOGrid() {
        employeeShiftDTOGrid = new Grid<>(EmployeeShiftScheduleDTO.class, false);

        employeeShiftDTOGrid.addColumn(employeeShiftDTO -> employeeShiftDTO.getEmployeeDTO().getEmployeeNumber())
                            .setHeader("Employee No.")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(employeeShiftDTO -> employeeShiftDTO.getEmployeeDTO().getFirstName()
                                    .concat(" ")
                                    .concat(employeeShiftDTO.getEmployeeDTO().getMiddleName())
                                    .concat(" ")
                                    .concat(employeeShiftDTO.getEmployeeDTO().getLastName())
                                    .concat(employeeShiftDTO.getEmployeeDTO().getSuffix() != null ? employeeShiftDTO.getEmployeeDTO().getSuffix() : ""))
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
        employeeShiftDTOGrid.addColumn(employeeShiftDTO -> employeeShiftDTO.getShiftStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
                            .setHeader("Start Shift")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(employeeShiftDTO -> employeeShiftDTO.getShiftEndTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
                            .setHeader("End Shift")
                            .setSortable(true);
        employeeShiftDTOGrid.addColumn(EmployeeShiftScheduleDTO::getShiftHours)
                            .setHeader("Remarks")
                            .setSortable(true);
        employeeShiftDTOGrid.addComponentColumn(leaveFilingDTO -> buildRowToolbar()).setHeader("Action");
        employeeShiftDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                            GridVariant.LUMO_COLUMN_BORDERS,
                                            GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeShiftDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeeShiftDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeeShiftDTOGrid.setEmptyStateText("No employee shifts found.");
        employeeShiftDTOGrid.setItems(query -> employeeShiftScheduleService.getAll(query.getPage(), query.getPageSize()).stream());

        return employeeShiftDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewEmployeeShiftButton = new Button();
        viewEmployeeShiftButton.setTooltipText("View Employee Shift");
        viewEmployeeShiftButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewEmployeeShiftButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewEmployeeShiftButton.addClickListener(buttonClickEvent -> viewEmployeeShiftButton.getUI().ifPresent(ui -> {
            if (employeeShiftDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeShiftScheduleDTO selectedEmployeeShiftScheduleDTO = employeeShiftDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeeShiftDetailsView.class, selectedEmployeeShiftScheduleDTO.getId().toString());
            }
        }));

        Button editEmployeeShiftButton = new Button();
        editEmployeeShiftButton.setTooltipText("Edit Employee Shift");
        editEmployeeShiftButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editEmployeeShiftButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editEmployeeShiftButton.addClickListener(buttonClickEvent -> editEmployeeShiftButton.getUI().ifPresent(ui -> {
            if (employeeShiftDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeShiftScheduleDTO selectedEmployeeShiftScheduleDTO = employeeShiftDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeeShiftFormView.class, selectedEmployeeShiftScheduleDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewEmployeeShiftButton, editEmployeeShiftButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateEmployeeShiftDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            employeeShiftDTOGrid.setItems(employeeShiftScheduleService.findByParameter(searchFilterTextField.getValue()));
        } else {
            employeeShiftDTOGrid.setItems(query -> employeeShiftScheduleService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
