package com.whizservices.hris.views.payroll;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;

import com.whizservices.hris.dtos.attendance.EmployeeTimesheetDTO;
import com.whizservices.hris.dtos.compenben.RatesDTO;
import com.whizservices.hris.dtos.payroll.EmployeePayrollDTO;
import com.whizservices.hris.services.attendance.EmployeeTimesheetService;
import com.whizservices.hris.services.compenben.AllowanceService;
import com.whizservices.hris.services.compenben.RatesService;
import com.whizservices.hris.services.payroll.EmployeePayrollService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.utils.PayrollComputationUtil;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.security.RolesAllowed;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Payroll Generator")
@Route(value = "payroll-generator", layout = MainLayout.class)
public class PayrollGeneratorView extends VerticalLayout {
    private final EmployeeService employeeService;
    private final EmployeePayrollService employeePayrollService;
    private final EmployeeTimesheetService employeeTimesheetService;
    private final RatesService ratesService;
    private final AllowanceService allowanceService;

    private DatePicker cutOffFromDatePicker, cutOffToDatePicker;
    private Button searchCutOffButton, generatePayrollButton;
    private Grid<EmployeeTimesheetDTO> timesheetDTOGrid;

    private String loggedInUser;

    public PayrollGeneratorView(EmployeeService employeeService,
                                EmployeePayrollService employeePayrollService,
                                EmployeeTimesheetService employeeTimesheetService,
                                RatesService ratesService,
                                AllowanceService allowanceService) {
        this.employeeService = employeeService;
        this.employeePayrollService = employeePayrollService;
        this.employeeTimesheetService = employeeTimesheetService;
        this.ratesService = ratesService;
        this.allowanceService = allowanceService;

        // Get the logged-in user of the system.
        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        this.add(buildFilterToolbar(), buildTimesheetDTOGrid());
        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
    }

    public HorizontalLayout buildFilterToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        cutOffFromDatePicker = new DatePicker();
        cutOffFromDatePicker.getStyle().set("margin-right", "5px");
        cutOffFromDatePicker.setPlaceholder("Cut-off from");

        cutOffToDatePicker = new DatePicker();
        cutOffToDatePicker.getStyle().set("margin-right", "5px");
        cutOffToDatePicker.setPlaceholder("Cut-off to");

        searchCutOffButton = new Button("Search");
        searchCutOffButton.setIconAfterText(true);
        searchCutOffButton.setIcon(LumoIcon.SEARCH.create());
        searchCutOffButton.addClickListener(e -> {
            if (cutOffFromDatePicker.getValue() != null && cutOffToDatePicker.getValue() != null) {
                timesheetDTOGrid.setItems(employeeTimesheetService.findByLogDateRange(cutOffFromDatePicker.getValue(), cutOffToDatePicker.getValue()));
            }
        });

        generatePayrollButton = new Button("Generate Payroll");
        generatePayrollButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generatePayrollButton.addClickListener(buttonClickEvent -> {
            ListDataProvider<EmployeeTimesheetDTO> timesheetDTOListDataProvider = (ListDataProvider<EmployeeTimesheetDTO>) timesheetDTOGrid.getDataProvider();

            if (!timesheetDTOListDataProvider.getItems().isEmpty()) {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Generate Payroll");
                confirmDialog.setText("""
                                      WARNING!
                                      Any employeeTimesheet that was not approved in the given cut-off dates will not be included in generating the payroll.
                                      Are you sure you want to generate the payroll?
                                      """);
                confirmDialog.addConfirmListener(confirmEvent -> {
                    Map<UUID, List<EmployeeTimesheetDTO>> groupedByEmployee = timesheetDTOListDataProvider.getItems()
                                                                                                  .stream()
                                                                                                  .collect(Collectors.groupingBy(employeeTimesheetDTO -> employeeTimesheetDTO.getEmployeeDTO().getId()));
                    groupedByEmployee.forEach((uuid, timesheetDTOs) -> {
                        RatesDTO ratesDTO = ratesService.findByEmployeeDTO(employeeService.getById(uuid));
                        BigDecimal totalAllowanceAmount = allowanceService.getSumOfAllowanceByEmployeeDTO(employeeService.getById(uuid));
                        AtomicInteger absentCount = new AtomicInteger(0);

//                        timesheetDTOs.forEach(employeeTimesheetDTO -> {
//                            if (employeeTimesheetDTO.getLeaveRemarks().equalsIgnoreCase("Absent")) {
//                                absentCount.set(absentCount.get() + 1);
//                            }
//                        });

                        EmployeePayrollDTO employeePayrollDTO = new EmployeePayrollDTO();
                        employeePayrollDTO.setEmployeeDTO(employeeService.getById(uuid));
                        employeePayrollDTO.setCutOffFromDate(cutOffFromDatePicker.getValue());
                        employeePayrollDTO.setCutOffToDate(cutOffToDatePicker.getValue());
                        employeePayrollDTO.setPayrollFrequency(PayrollComputationUtil.getPayrollFrequency(cutOffFromDatePicker.getValue(), cutOffToDatePicker.getValue()));
                        employeePayrollDTO.setBasicPayAmount(ratesDTO.getBasicCompensationRate());
                        employeePayrollDTO.setAllowancePayAmount(totalAllowanceAmount);
                        employeePayrollDTO.setAbsentDeductionAmount(ratesDTO.getDailyAbsentDeductionRate().multiply(BigDecimal.valueOf(absentCount.get())));

                        employeePayrollDTO.setCreatedBy(loggedInUser);
                        employeePayrollDTO.setUpdatedBy(loggedInUser);

                        employeePayrollService.saveOrUpdate(employeePayrollDTO);
                    });
                });
                confirmDialog.setRejectable(true);
                confirmDialog.addRejectListener(rejectEvent -> confirmDialog.close());
                confirmDialog.open();
            } else {
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                notification.setText("Please search the employeeTimesheet that you want to generate the payroll.");
                notification.setDuration(5000);
                notification.setPosition(Notification.Position.TOP_CENTER);
                notification.open();
            }
        });

        Span searchCutOffSpan = new Span();
        searchCutOffSpan.getStyle().set("margin", "0 auto 0 0");
        searchCutOffSpan.add(cutOffFromDatePicker, cutOffToDatePicker, searchCutOffButton);

        toolbar.add(searchCutOffSpan, generatePayrollButton);

        return toolbar; 
    }

    public Grid<EmployeeTimesheetDTO> buildTimesheetDTOGrid() {
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
        timesheetDTOGrid.addColumn(EmployeeTimesheetDTO::getLogDate)
                        .setHeader("Log Date")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(employeeTimesheetDTO -> employeeTimesheetDTO.getShiftScheduleDTO().getShiftSchedule())
                        .setHeader("Shift Schedule")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(EmployeeTimesheetDTO::getLogTime)
                        .setHeader("Log Time")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(EmployeeTimesheetDTO::getLogDetail)
                        .setHeader("Log Detail")
                        .setSortable(true);
        timesheetDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, employeeTimesheetDTO) -> {
                            String theme = String.format("badge %s", employeeTimesheetDTO.getStatus().equalsIgnoreCase("APPROVED") ? "success" : "tertiary");

                            Span activeSpan = new Span();
                            activeSpan.getElement().setAttribute("theme", theme);
                            activeSpan.setText(employeeTimesheetDTO.getStatus());

                            layout.setJustifyContentMode(JustifyContentMode.CENTER);
                            layout.add(activeSpan);
                        }))
                        .setHeader("Status")
                        .setSortable(true);
        timesheetDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                          GridVariant.LUMO_COLUMN_BORDERS,
                                          GridVariant.LUMO_WRAP_CELL_CONTENT);
        timesheetDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        timesheetDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        timesheetDTOGrid.setEmptyStateText("No approved employeeTimesheet records found.");

        return timesheetDTOGrid;
    }
}
