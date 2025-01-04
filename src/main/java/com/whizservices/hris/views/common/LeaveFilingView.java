package com.whizservices.hris.views.common;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.admin.UserDTO;
import com.whizservices.hris.dtos.compenben.LeaveBenefitsDTO;
import com.whizservices.hris.dtos.attendance.LeaveFilingDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.admin.UserService;
import com.whizservices.hris.services.compenben.LeaveBenefitsService;
import com.whizservices.hris.services.attendance.LeaveFilingService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.vaadin.lineawesome.LineAwesomeIcon;

@PermitAll
@PageTitle("Leave Filing")
@Route(value = "leave-filing-view", layout = MainLayout.class)
public class LeaveFilingView extends VerticalLayout {
    @Resource private final LeaveFilingService leaveFilingService;
    @Resource private final LeaveBenefitsService leaveBenefitsService;
    @Resource private final EmployeeService employeeService;
    @Resource private final UserService userService;

    private UserDTO userDTO;
    private EmployeeDTO employeeDTO;
    private LeaveFilingDTO leaveFilingDTO;

    private List<LeaveFilingDTO> leaveFilingDTOList;
    private List<LeaveBenefitsDTO> leaveBenefitsDTOList;
    private List<EmployeeDTO> approverEmployeeDTOList;

    private String loggedInUser;

    private Grid<LeaveFilingDTO> employeeLeaveFilingDTOGrid;
    private FormLayout leaveFilingLayout;
    private ComboBox<LeaveBenefitsDTO> leaveBenefitsDTOComboBox;
    private ComboBox<EmployeeDTO> employeeApproverDTOComboBox;
    private DateTimePicker leaveFromDateTimePicker, leaveToDateTimePicker;
    private IntegerField leaveCountField;
    private TextField leaveRemarks;
    private Button saveButton, cancelButton;

    public LeaveFilingView(LeaveFilingService leaveFilingService,
                           LeaveBenefitsService leaveBenefitsService,
                           EmployeeService employeeService,
                           UserService userService) {
        this.leaveFilingService = leaveFilingService;
        this.leaveBenefitsService = leaveBenefitsService;
        this.employeeService = employeeService;
        this.userService = userService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (loggedInUser != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        if (userDTO != null) {
            employeeDTO = userDTO.getEmployeeDTO();
        }

        if (employeeDTO != null) {
            leaveFilingDTOList = leaveFilingService.getByEmployeeDTO(employeeDTO);
        }

        employeeLeaveFilingDTOGrid = new Grid<>(LeaveFilingDTO.class, false);
        leaveFilingLayout = new FormLayout();

        this.buildLeaveFilingLayout();
        this.buildEmployeeLeaveFilingDTOGrid();

        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
        this.setPadding(true);
        this.add(leaveFilingLayout, employeeLeaveFilingDTOGrid);
    }

    private void buildLeaveFilingLayout() {
        leaveBenefitsDTOComboBox = new ComboBox<>("Leave Benefit Type");
        leaveBenefitsDTOList = leaveBenefitsService.getByEmployeeDTO(employeeDTO);
        leaveBenefitsDTOComboBox.setItems(leaveBenefitsDTOList);
        leaveBenefitsDTOComboBox.setItemLabelGenerator(leaveBenefitsDTO -> "(".concat(leaveBenefitsDTO.getLeaveCode())
                                                                              .concat(") - ")
                                                                              .concat(leaveBenefitsDTO.getLeaveType()));
        leaveBenefitsDTOComboBox.setRequired(true);
        leaveBenefitsDTOComboBox.setRequiredIndicatorVisible(true);

        employeeApproverDTOComboBox = new ComboBox<>("Approver");
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeDTO, Void> query = new Query<>();
        approverEmployeeDTOList = employeeService.getEmployeesWhoAreApprovers();
        employeeApproverDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName()
                                                                                       .toLowerCase()
                                                                                       .contains(filterString.toLowerCase()),
                                                                            approverEmployeeDTOList);
        employeeApproverDTOComboBox.setItemLabelGenerator(employeeDTO -> employeeDTO.getFirstName().concat(" ").concat(employeeDTO.getLastName()));
        employeeApproverDTOComboBox.setRequired(true);
        employeeApproverDTOComboBox.setRequiredIndicatorVisible(true);

        leaveFromDateTimePicker = new DateTimePicker("Leave from");
        leaveFromDateTimePicker.setMin(LocalDateTime.now());
        leaveFromDateTimePicker.setRequiredIndicatorVisible(true);

        leaveToDateTimePicker = new DateTimePicker("Leave to");
        leaveToDateTimePicker.setMin(LocalDateTime.now());
        leaveToDateTimePicker.setRequiredIndicatorVisible(true);

        leaveCountField = new IntegerField("No. of days");
        leaveCountField.setMin(1);
        leaveCountField.setRequired(true);
        leaveCountField.setRequiredIndicatorVisible(true);

        leaveRemarks = new TextField("Remarks");

        saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            // Save the leave filing.
            if (leaveFilingDTO == null) {
                leaveFilingDTO = new LeaveFilingDTO();
                leaveFilingDTO.setCreatedBy(loggedInUser);
            }

            leaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsDTOComboBox.getValue());
            leaveFilingDTO.setAssignedApproverEmployeeDTO(employeeApproverDTOComboBox.getValue());
            leaveFilingDTO.setLeaveDateAndTimeFrom(leaveFromDateTimePicker.getValue());
            leaveFilingDTO.setLeaveDateAndTimeTo(leaveToDateTimePicker.getValue());
            leaveFilingDTO.setLeaveCount(leaveCountField.getValue());
            leaveFilingDTO.setRemarks(leaveRemarks.getValue());
            leaveFilingDTO.setLeaveStatus("PENDING");
            leaveFilingDTO.setUpdatedBy(loggedInUser);

            leaveFilingService.saveOrUpdate(leaveFilingDTO);

            // Clear the fields.
            this.clearFields();

            // Show notification message.
            Notification notification = Notification.show("You have successfully filed your leave request.",  5000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            // Update the data grid.
            this.updateLeaveFilingDataGrid();
        });

        cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> this.clearFields());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setPadding(true);

        leaveFilingLayout.setColspan(buttonLayout, 2);
        leaveFilingLayout.add(leaveBenefitsDTOComboBox,
                employeeApproverDTOComboBox,
                leaveFromDateTimePicker,
                leaveToDateTimePicker,
                leaveCountField,
                leaveRemarks,
                buttonLayout);
        leaveFilingLayout.setMaxWidth("768px");
    }

    private void buildEmployeeLeaveFilingDTOGrid() {
        employeeLeaveFilingDTOGrid.addColumn(leaveFilingDTO -> leaveFilingDTO.getLeaveBenefitsDTO().getLeaveType())
                                  .setHeader("Leave Type");
        employeeLeaveFilingDTOGrid.addColumn(leaveFilingDTO -> leaveFilingDTO.getAssignedApproverEmployeeDTO().getFirstName()
                                                               + " "
                                                               + leaveFilingDTO.getAssignedApproverEmployeeDTO().getLastName())
                                  .setHeader("Approver");
        employeeLeaveFilingDTOGrid.addColumn(new LocalDateTimeRenderer<>(LeaveFilingDTO::getLeaveDateAndTimeFrom, "MMM dd, yyyy HH:mm"))
                                  .setHeader("Leave From");
        employeeLeaveFilingDTOGrid.addColumn(new LocalDateTimeRenderer<>(LeaveFilingDTO::getLeaveDateAndTimeTo, "MMM dd, yyyy HH:mm"))
                                  .setHeader("Leave To");
        employeeLeaveFilingDTOGrid.addColumn(LeaveFilingDTO::getRemarks).setHeader("Remarks");
        employeeLeaveFilingDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, leaveFilingDTO) -> {
            Icon statusIcon = null;
            String theme = "";
            String leaveStatus = leaveFilingDTO.getLeaveStatus();

            if (leaveStatus.equals("APPROVED")) {
                statusIcon = VaadinIcon.CHECK.create();
                theme = "badge success";
            } else if (leaveStatus.equals("REJECTED")) {
                statusIcon = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
                theme = "badge error";
            } else if (leaveStatus.equals("CANCELLED")) {
                statusIcon = VaadinIcon.BAN.create();
                theme = "badge contrast";
            } else {
                statusIcon = VaadinIcon.INFO_CIRCLE_O.create();
                theme = "badge";
            }

            statusIcon.getStyle().set("padding", "var(--lumo-space-xs)");

            Span statusSpan = new Span(statusIcon, new Span(leaveStatus));
            statusSpan.getElement().setAttribute("theme", theme);

            layout.setJustifyContentMode(JustifyContentMode.CENTER);
            layout.add(statusSpan);
        })).setHeader("Status");
        employeeLeaveFilingDTOGrid.addComponentColumn(leaveFilingDTO -> leaveFilingDTO.getLeaveStatus().equalsIgnoreCase("PENDING") ? this.buildRowToolbar() : new Span()).setHeader("Action");
        employeeLeaveFilingDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                    GridVariant.LUMO_COLUMN_BORDERS,
                                                    GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeLeaveFilingDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeeLeaveFilingDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeeLeaveFilingDTOGrid.setAllRowsVisible(true);
        employeeLeaveFilingDTOGrid.setEmptyStateText("No leave filing found.");
        employeeLeaveFilingDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                    GridVariant.LUMO_COLUMN_BORDERS,
                                                    GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeLeaveFilingDTOGrid.setItems(leaveFilingDTOList);
    }

    public Component buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button cancelLeaveFilingButton = new Button();
        cancelLeaveFilingButton.setTooltipText("Cancel Leave");
        cancelLeaveFilingButton.setIcon(LineAwesomeIcon.BAN_SOLID.create());
        cancelLeaveFilingButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        cancelLeaveFilingButton.addClickListener(buttonClickEvent -> cancelLeaveFilingButton.getUI().ifPresent(ui -> {
            if (employeeLeaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                if (employeeLeaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get().getLeaveStatus().equals("PENDING")) {
                    // Show the confirmation dialog.
                    ConfirmDialog  confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Cancel Leave Filing");
                    confirmDialog.setText(new Html("<p>Are you sure you want to cancel your filed leave?</p>"));
                    confirmDialog.setConfirmText("Yes");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Set the status of leave filing to "CANCELLED" and save.
                        LeaveFilingDTO employeeLeaveFilingDTO = employeeLeaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                        employeeLeaveFilingDTO.setLeaveStatus("CANCELLED");
                        employeeLeaveFilingDTO.setUpdatedBy(loggedInUser);

                        leaveFilingService.saveOrUpdate(employeeLeaveFilingDTO);

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully cancelled your leave request.",  5000, Notification.Position.TOP_CENTER);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                        // Close the confirmation dialog.
                        confirmDialog.close();

                        // Update the data grid.
                        this.updateLeaveFilingDataGrid();
                    });
                    confirmDialog.setCancelable(true);
                    confirmDialog.setCancelText("No");
                    confirmDialog.open();
                } else {
                    Notification notification = Notification.show("You cannot cancel a leave request that is already approved, rejected or cancelled.",  5000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR, NotificationVariant.LUMO_PRIMARY);
                }
            }
        }));

        Button editLeaveFilingButton = new Button();
        editLeaveFilingButton.setTooltipText("Edit Leave");
        editLeaveFilingButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editLeaveFilingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editLeaveFilingButton.addClickListener(buttonClickEvent -> editLeaveFilingButton.getUI().ifPresent(ui -> {
            if (employeeLeaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                if (employeeLeaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get().getLeaveStatus().equals("PENDING")) {
                    leaveFilingDTO = employeeLeaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                    leaveBenefitsDTOComboBox.setValue(leaveFilingDTO.getLeaveBenefitsDTO());
                    employeeApproverDTOComboBox.setValue(leaveFilingDTO.getAssignedApproverEmployeeDTO());
                    leaveFromDateTimePicker.setValue(leaveFilingDTO.getLeaveDateAndTimeFrom());
                    leaveToDateTimePicker.setValue(leaveFilingDTO.getLeaveDateAndTimeTo());
                    leaveCountField.setValue(leaveFilingDTO.getLeaveCount());
                    leaveRemarks.setValue(leaveFilingDTO.getRemarks());
                } else {
                    Notification notification = Notification.show("You cannot edit a leave request that is already approved, rejected or cancelled.",  5000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR, NotificationVariant.LUMO_PRIMARY);
                }
            }
        }));

        rowToolbarLayout.add(cancelLeaveFilingButton, editLeaveFilingButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void clearFields() {
        leaveBenefitsDTOComboBox.clear();
        employeeApproverDTOComboBox.clear();
        leaveFromDateTimePicker.clear();
        leaveToDateTimePicker.clear();
        leaveCountField.clear();
        leaveRemarks.clear();
    }

    private void updateLeaveFilingDataGrid() {
        leaveFilingDTOList = leaveFilingService.getByEmployeeDTO(employeeDTO);
        employeeLeaveFilingDTOGrid.setItems(leaveFilingDTOList);
    }
}
