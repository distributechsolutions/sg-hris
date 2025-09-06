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
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.attendance.dtos.EmployeeLeaveFilingDTO;
import io.softwaregarage.hris.compenben.dtos.LeaveBenefitsDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.attendance.services.EmployeeLeaveFilingService;
import io.softwaregarage.hris.compenben.services.LeaveBenefitsService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Objects;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_MANAGER",
               "ROLE_SUPERVISOR"})
@Route(value = "leave-approvals-list-view", layout = MainLayout.class)
public class EmployeeLeaveApprovalsListView extends VerticalLayout {
    @Resource private final EmployeeLeaveFilingService employeeLeaveFilingService;
    @Resource private final LeaveBenefitsService leaveBenefitsService;
    @Resource private final UserService userService;

    private Grid<EmployeeLeaveFilingDTO> leaveFilingDTOGrid;
    private TextField searchFilterTextField;

    private final String loggedInUser;
    private UserDTO userDTO;

    public EmployeeLeaveApprovalsListView(EmployeeLeaveFilingService employeeLeaveFilingService,
                                          LeaveBenefitsService leaveBenefitsService,
                                          UserService userService) {
        this.employeeLeaveFilingService = employeeLeaveFilingService;
        this.leaveBenefitsService = leaveBenefitsService;
        this.userService = userService;

        // Get the logged-in user of the system.
        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        this.add(buildHeaderToolbar(), buildLeaveFilingDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateLeaveFilingDTOGrid());

        headerToolbarLayout.add(searchFilterTextField);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<EmployeeLeaveFilingDTO> buildLeaveFilingDTOGrid() {
        leaveFilingDTOGrid = new Grid<>(EmployeeLeaveFilingDTO.class, false);

        leaveFilingDTOGrid.addColumn(employeeLeaveFilingDTO -> employeeLeaveFilingDTO.getLeaveBenefitsDTO().getEmployeeDTO().getEmployeeNumber())
                          .setHeader("Employee No.")
                          .setSortable(true);
        leaveFilingDTOGrid.addColumn(employeeLeaveFilingDTO -> employeeLeaveFilingDTO.getLeaveBenefitsDTO().getEmployeeDTO().getFirstName()
                                                                                                                        .concat(" ")
                                                                                                                        .concat(employeeLeaveFilingDTO.getLeaveBenefitsDTO().getEmployeeDTO().getMiddleName())
                                                                                                                        .concat(" ")
                                                                                                                        .concat(employeeLeaveFilingDTO.getLeaveBenefitsDTO().getEmployeeDTO().getLastName())
                                                                                                                        .concat(employeeLeaveFilingDTO.getLeaveBenefitsDTO().getEmployeeDTO().getSuffix() != null ? employeeLeaveFilingDTO.getLeaveBenefitsDTO().getEmployeeDTO().getSuffix() : ""))
                          .setHeader("Employee Name")
                          .setSortable(true);
        leaveFilingDTOGrid.addColumn(employeeLeaveFilingDTO -> employeeLeaveFilingDTO.getLeaveBenefitsDTO().getLeaveType())
                           .setHeader("Leave Type")
                           .setSortable(true);
        leaveFilingDTOGrid.addColumn(EmployeeLeaveFilingDTO::getLeaveCount)
                          .setHeader("No. of Leaves Filed")
                          .setSortable(true);
        leaveFilingDTOGrid.addColumn(new LocalDateTimeRenderer<>(EmployeeLeaveFilingDTO::getLeaveDateAndTimeFrom, "MMM dd, yyyy HH:mm"))
                          .setHeader("Leave From")
                          .setSortable(true);
        leaveFilingDTOGrid.addColumn(new LocalDateTimeRenderer<>(EmployeeLeaveFilingDTO::getLeaveDateAndTimeTo, "MMM dd, yyyy HH:mm"))
                          .setHeader("Leave To")
                          .setSortable(true);
        leaveFilingDTOGrid.addColumn(EmployeeLeaveFilingDTO::getRemarks)
                          .setHeader("Remarks")
                          .setSortable(true);
        leaveFilingDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, employeeLeaveFilingDTO) -> {
                                String theme = String.format("badge %s", employeeLeaveFilingDTO.getLeaveStatus().equalsIgnoreCase("PENDING") ? "contrast" : "success");

                                Span activeSpan = new Span();
                                activeSpan.getElement().setAttribute("theme", theme);
                                activeSpan.setText(employeeLeaveFilingDTO.getLeaveStatus());

                                layout.setJustifyContentMode(JustifyContentMode.CENTER);
                                layout.add(activeSpan);
                            }))
                          .setHeader("Status")
                          .setSortable(true);
        leaveFilingDTOGrid.addComponentColumn(employeeLeaveFilingDTO -> buildRowToolbar()).setHeader("Action");
        leaveFilingDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                            GridVariant.LUMO_COLUMN_BORDERS,
                                            GridVariant.LUMO_WRAP_CELL_CONTENT);
        leaveFilingDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        leaveFilingDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        leaveFilingDTOGrid.setEmptyStateText("No pending leave approvals found.");
        leaveFilingDTOGrid.setItems(employeeLeaveFilingService.getByLeaveStatusAndAssignedApproverEmployeeDTO("PENDING", userDTO.getEmployeeDTO()));

        return leaveFilingDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button approveButton = new Button();
        approveButton.setTooltipText("Approve Leave");
        approveButton.setIcon(LineAwesomeIcon.THUMBS_UP.create());
        approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        approveButton.addClickListener(buttonClickEvent -> approveButton.getUI().ifPresent(ui -> {
            if (leaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                if (leaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get().getLeaveStatus().equals("PENDING")) {
                    EmployeeLeaveFilingDTO employeeLeaveFilingDTO = leaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                    LeaveBenefitsDTO leaveBenefitsDTO = employeeLeaveFilingDTO.getLeaveBenefitsDTO();

                    if (leaveBenefitsDTO.getLeaveType().contains("Unpaid")) {
                        // Set the status of leave filing to "APPROVED" and save.
                        employeeLeaveFilingDTO.setLeaveStatus("APPROVED");
                        employeeLeaveFilingDTO.setUpdatedBy(loggedInUser);

                        employeeLeaveFilingService.saveOrUpdate(employeeLeaveFilingDTO);

                        // Update the data grid.
                        leaveFilingDTOGrid.setItems(employeeLeaveFilingService.getByLeaveStatusAndAssignedApproverEmployeeDTO("PENDING", userDTO.getEmployeeDTO()));

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully approved the leave request.",  5000, Notification.Position.TOP_CENTER);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        // Check first if the remaining leave count balance is greater than the applied leave.
                        if (leaveBenefitsDTO.getLeaveCount() > employeeLeaveFilingDTO.getLeaveCount()) {
                            // Set the status of leave filing to "APPROVED" and save.
                            employeeLeaveFilingDTO.setLeaveStatus("APPROVED");
                            employeeLeaveFilingDTO.setUpdatedBy(loggedInUser);

                            employeeLeaveFilingService.saveOrUpdate(employeeLeaveFilingDTO);

                            // Deduct the leave count of the selected leave benefit.
                            leaveBenefitsDTO.setLeaveCount(leaveBenefitsDTO.getLeaveCount() - employeeLeaveFilingDTO.getLeaveCount());
                            leaveBenefitsDTO.setUpdatedBy(loggedInUser);

                            leaveBenefitsService.saveOrUpdate(leaveBenefitsDTO);

                            // Update the data grid.
                            leaveFilingDTOGrid.setItems(employeeLeaveFilingService.getByLeaveStatusAndAssignedApproverEmployeeDTO("PENDING", userDTO.getEmployeeDTO()));

                            // Show notification message.
                            Notification notification = Notification.show("You have successfully approved the leave request.",  5000, Notification.Position.TOP_CENTER);
                            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        } else {
                            Notification notification = Notification.show("You cannot approve the leave request.\n The requested leave days is greater than the remaining leave days.",  5000, Notification.Position.TOP_CENTER);
                            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    }

                    // Reload the main layout.
                    reloadMainLayout();
                }
            }
        }));

        Button rejectButton = new Button();
        rejectButton.setTooltipText("Reject Leave");
        rejectButton.setIcon(LineAwesomeIcon.THUMBS_DOWN.create());
        rejectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        rejectButton.addClickListener(buttonClickEvent -> rejectButton.getUI().ifPresent(ui -> {
            if (leaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                if (leaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get().getLeaveStatus().equals("PENDING")) {
                    // Show the confirmation dialog.
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Reject Leave Filing");
                    confirmDialog.setText(new Html("<p>Are you sure you want to reject the filed leave?</p>"));
                    confirmDialog.setConfirmText("Yes");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Set the status of leave filing to "REJECTED" and save.
                        EmployeeLeaveFilingDTO employeeLeaveFilingDTO = leaveFilingDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                        employeeLeaveFilingDTO.setLeaveStatus("REJECTED");
                        employeeLeaveFilingDTO.setUpdatedBy(loggedInUser);

                        employeeLeaveFilingService.saveOrUpdate(employeeLeaveFilingDTO);

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully rejected the leave request.",  5000, Notification.Position.TOP_CENTER);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                        // Close the confirmation dialog.
                        confirmDialog.close();

                        // Update the data grid.
                        updateLeaveFilingDTOGrid();

                        // Reload the main layout.
                        reloadMainLayout();
                    });
                    confirmDialog.setCancelable(true);
                    confirmDialog.setCancelText("No");
                    confirmDialog.open();
                }
            }
        }));

        rowToolbarLayout.add(approveButton, rejectButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateLeaveFilingDTOGrid() {
        if (!searchFilterTextField.getValue().isEmpty()) {
            leaveFilingDTOGrid.setItems(employeeLeaveFilingService.findByParameter(searchFilterTextField.getValue())
                                                                                                .stream()
                                                                                                .filter(employeeLeaveFilingDTO -> employeeLeaveFilingDTO.getLeaveStatus().equalsIgnoreCase("PENDING"))
                                                                                                .filter(employeeLeaveFilingDTO -> employeeLeaveFilingDTO.getAssignedApproverEmployeeDTO().equals(userDTO.getEmployeeDTO()))
                                                                                                .toList());
        } else {
            leaveFilingDTOGrid.setItems(employeeLeaveFilingService.getByLeaveStatusAndAssignedApproverEmployeeDTO("PENDING", userDTO.getEmployeeDTO()));
        }
    }

    private void reloadMainLayout() {
        getUI().get().getPage().reload();
    }
}
