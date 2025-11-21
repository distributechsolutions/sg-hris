package io.softwaregarage.hris.compenben.views;

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
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.attendance.dtos.EmployeeLeaveFilingDTO;
import io.softwaregarage.hris.attendance.services.EmployeeLeaveFilingService;
import io.softwaregarage.hris.compenben.dtos.LeaveBenefitsDTO;
import io.softwaregarage.hris.compenben.services.LeaveBenefitsService;
import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Leave Benefits")
@Route(value = "leave-benefits-list", layout = MainLayout.class)
public class LeaveBenefitsListView extends VerticalLayout {
    @Resource
    private final LeaveBenefitsService leaveBenefitsService;

    @Resource
    private final EmployeeLeaveFilingService employeeLeaveFilingService;

    @Resource
    private final UserService userService;

    private UserDTO userDTO;

    private Grid<LeaveBenefitsDTO> leaveBenefitsDTOGrid;
    private TextField searchFilterTextField;

    public LeaveBenefitsListView(LeaveBenefitsService leaveBenefitsService,
                                 EmployeeLeaveFilingService employeeLeaveFilingService,
                                 UserService userService) {
        this.leaveBenefitsService = leaveBenefitsService;
        this.employeeLeaveFilingService = employeeLeaveFilingService;
        this.userService = userService;

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(SecurityUtil.getAuthenticatedUser().getUsername());
        }

        this.add(buildHeaderToolbar(), buildLeaveBenefitsDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateLeaveBenefitsDTOGrid());

        Button addButton = new Button("Add Leave Benefit");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(LeaveBenefitsFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<LeaveBenefitsDTO> buildLeaveBenefitsDTOGrid() {
        leaveBenefitsDTOGrid = new Grid<>(LeaveBenefitsDTO.class, false);

        leaveBenefitsDTOGrid.addColumn(leaveBenefitsDTO -> leaveBenefitsDTO.getEmployeeDTO().getEmployeeNumber())
                            .setHeader("Employee No.")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(leaveBenefitsDTO -> leaveBenefitsDTO.getEmployeeDTO().getFirstName()
                                                                                            .concat(" ")
                                                                                            .concat(leaveBenefitsDTO.getEmployeeDTO().getLastName())
                                                                                            .concat(leaveBenefitsDTO.getEmployeeDTO().getSuffix() != null ? leaveBenefitsDTO.getEmployeeDTO().getSuffix() : ""))
                            .setHeader("Employee Name")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveCode)
                            .setHeader("Leave Code")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveType)
                            .setHeader("Leave Type")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveCount)
                            .setHeader("Leave Count")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(LeaveBenefitsDTO::getLeaveForYear)
                            .setHeader("For Year")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, leaveBenefitsDTO) -> {
                                            String theme = String.format("badge %s", leaveBenefitsDTO.isLeaveActive() ? "success" : "error");

                                            Span activeSpan = new Span();
                                            activeSpan.getElement().setAttribute("theme", theme);
                                            activeSpan.setText(leaveBenefitsDTO.isLeaveActive() ? "Yes" : "No");

                                            layout.setJustifyContentMode(JustifyContentMode.CENTER);
                                            layout.add(activeSpan);
                                        }))
                            .setHeader("Is Leave Active?")
                            .setSortable(true);
        leaveBenefitsDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        leaveBenefitsDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                              GridVariant.LUMO_COLUMN_BORDERS,
                                              GridVariant.LUMO_WRAP_CELL_CONTENT);
        leaveBenefitsDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        leaveBenefitsDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        leaveBenefitsDTOGrid.setAllRowsVisible(true);
        leaveBenefitsDTOGrid.setEmptyStateText("No benefit records found.");
        leaveBenefitsDTOGrid.setItems((query -> leaveBenefitsService.getAll(query.getPage(), query.getPageSize()).stream()));

        return leaveBenefitsDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Leave Benefit");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                LeaveBenefitsDTO selectedLeaveBenefitsDTO = leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(LeaveBenefitsDetailsView.class, selectedLeaveBenefitsDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Leave Benefit");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                LeaveBenefitsDTO selectedLeaveBenefitsDTO = leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(LeaveBenefitsFormView.class, selectedLeaveBenefitsDTO.getId().toString());
            }
        }));

        // Show the delete button if the role of the logged-in user is ROLE_ADMIN.
        Button deleteButton = new Button();
        deleteButton.setTooltipText("Delete Leave Benefit");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(buttonClickEvent -> {
            if (leaveBenefitsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                LeaveBenefitsDTO selectedLeaveBenefitsDTO = leaveBenefitsDTOGrid.getSelectionModel()
                        .getFirstSelectedItem().get();

                // Check first if that record is not an active employee anymore. If it is not active, you may proceed
                // for the deletion.
                if (selectedLeaveBenefitsDTO.getEmployeeDTO().getStatus().equals("RESIGNED")
                        || selectedLeaveBenefitsDTO.getEmployeeDTO().getStatus().equals("RETIRED")
                        || selectedLeaveBenefitsDTO.getEmployeeDTO().getStatus().equals("TERMINATED")
                        || selectedLeaveBenefitsDTO.getEmployeeDTO().getStatus().equals("DECEASED")) {
                    // Show the confirmation dialog.
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Delete Leave Benefit");
                    confirmDialog.setText(new Html("""
                                               <p>
                                               WARNING! Are you sure you want to delete the selected leave benefit of
                                               the employee? This will also delete the filed leaves made by the employee.
                                               </p>
                                               """));
                    confirmDialog.setConfirmText("Yes, Delete it.");
                    confirmDialog.setConfirmButtonTheme("error primary");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Delete first the related filed leaves of employee
                        List<EmployeeLeaveFilingDTO> employeeLeaveFilingDTOList = employeeLeaveFilingService
                                .getByEmployeeDTO(selectedLeaveBenefitsDTO.getEmployeeDTO());
                        if (!employeeLeaveFilingDTOList.isEmpty()) {
                            for (EmployeeLeaveFilingDTO employeeLeaveFilingDTO : employeeLeaveFilingDTOList) {
                                employeeLeaveFilingService.delete(employeeLeaveFilingDTO);
                            }
                        }

                        // Get the selected department and delete it.
                        leaveBenefitsService.delete(selectedLeaveBenefitsDTO);

                        // Refresh the data grid from the backend after the delete operation.
                        leaveBenefitsDTOGrid.getDataProvider().refreshAll();

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully deleted the selected employee leave benefit and all its filed leaves.",
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
                    Notification notification = Notification.show("You cannot delete the selected employee benefit. Employee is still in active status.",
                            5000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        if (userDTO.getRole().equals("ROLE_ADMIN")) {
            rowToolbarLayout.add(viewButton, editButton, deleteButton);
        } else {
            rowToolbarLayout.add(viewButton, editButton);
        }

        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateLeaveBenefitsDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            leaveBenefitsDTOGrid.setItems(leaveBenefitsService.findByParameter(searchFilterTextField.getValue()));
        } else {
            leaveBenefitsDTOGrid.setItems(query -> leaveBenefitsService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
