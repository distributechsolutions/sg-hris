package io.softwaregarage.hris.profile.views;

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
import io.softwaregarage.hris.profile.dtos.PositionProfileDTO;
import io.softwaregarage.hris.profile.services.PositionProfileService;
import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Assign Position")
@Route(value = "employee-position-list", layout = MainLayout.class)
public class PositionProfileListView extends VerticalLayout {
    @Resource
    private final PositionProfileService positionProfileService;

    @Resource
    private final UserService userService;

    private UserDTO userDTO;

    private Grid<PositionProfileDTO> employeePositionDTOGrid;
    private TextField searchFilterTextField;

    public PositionProfileListView(PositionProfileService positionProfileService,  UserService userService) {
        this.positionProfileService = positionProfileService;
        this.userService = userService;

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(SecurityUtil.getAuthenticatedUser().getUsername());
        }

        this.add(buildHeaderToolbar(), buildEmployeePositionDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateEmployeePositionDTOGrid());

        Button addButton = new Button("Add Employee Position");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(PositionProfileFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<PositionProfileDTO> buildEmployeePositionDTOGrid() {
        employeePositionDTOGrid = new Grid<>(PositionProfileDTO.class, false);

        employeePositionDTOGrid.addColumn(employeePositionDTO -> employeePositionDTO.getEmployeeDTO().getEmployeeNumber())
                .setHeader("Employee No.")
                .setSortable(true);
        employeePositionDTOGrid.addColumn(employeePositionDTO -> employeePositionDTO.getEmployeeDTO().getFirstName().concat(" ")
                        .concat(employeePositionDTO.getEmployeeDTO().getMiddleName())
                        .concat(" ")
                        .concat(employeePositionDTO.getEmployeeDTO().getLastName())
                        .concat(employeePositionDTO.getEmployeeDTO().getSuffix() != null ? employeePositionDTO.getEmployeeDTO().getSuffix() : ""))
                .setHeader("Employee Name")
                .setSortable(true);
        employeePositionDTOGrid.addColumn(employeePositionDTO -> employeePositionDTO.getPositionDTO().getName())
                .setHeader("Position")
                .setSortable(true);
        employeePositionDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, employeePositionDTO) -> {
            String theme = String.format("badge %s", employeePositionDTO.isCurrentPosition() ? "success" : "error");

            Span activeSpan = new Span();
            activeSpan.getElement().setAttribute("theme", theme);
            activeSpan.setText(employeePositionDTO.isCurrentPosition() ? "Yes" : "No");

            layout.setJustifyContentMode(JustifyContentMode.CENTER);
            layout.add(activeSpan);
        })).setHeader("Is Current Position?").setSortable(true);
        employeePositionDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        employeePositionDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                 GridVariant.LUMO_COLUMN_BORDERS,
                                                 GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeePositionDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeePositionDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeePositionDTOGrid.setAllRowsVisible(true);
        employeePositionDTOGrid.setEmptyStateText("No employee positions found.");
        employeePositionDTOGrid.setItems((query -> positionProfileService.getAll(query.getPage(), query.getPageSize()).stream()));

        return employeePositionDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Employee Position");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                PositionProfileDTO selectedPositionProfileDTO = employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(PositionProfileDetailsView.class, selectedPositionProfileDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Employee Position");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                PositionProfileDTO selectedPositionProfileDTO = employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(PositionProfileFormView.class, selectedPositionProfileDTO.getId().toString());
            }
        }));

        Button deleteButton = new Button();
        deleteButton.setTooltipText("Delete Assigned Position");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(buttonClickEvent -> {
            if (employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                PositionProfileDTO selectedPositionProfileDTO = employeePositionDTOGrid.getSelectionModel()
                        .getFirstSelectedItem().get();

                // Check first if that record is not an active employee anymore. If it is not active, you may proceed
                // for the deletion.
                if (selectedPositionProfileDTO.getEmployeeDTO().getStatus().equals("RESIGNED")
                        || selectedPositionProfileDTO.getEmployeeDTO().getStatus().equals("RETIRED")
                        || selectedPositionProfileDTO.getEmployeeDTO().getStatus().equals("TERMINATED")
                        || selectedPositionProfileDTO.getEmployeeDTO().getStatus().equals("DECEASED")) {
                    // Show the confirmation dialog.
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Delete Assigned Position");
                    confirmDialog.setText(new Html("""
                                               <p>
                                               WARNING! Are you sure you want to delete the selected employee in the
                                               assigned position?
                                               </p>
                                               """));
                    confirmDialog.setConfirmText("Yes, Delete it.");
                    confirmDialog.setConfirmButtonTheme("error primary");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Get the selected department and delete it.
                        positionProfileService.delete(selectedPositionProfileDTO);

                        // Refresh the data grid from the backend after the delete operation.
                        employeePositionDTOGrid.getDataProvider().refreshAll();

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully deleted the selected employee in the assigned position.",
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
                    Notification notification = Notification.show("You cannot delete the selected employee in the assigned position. Employee is still in active status.",
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

    private void updateEmployeePositionDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            employeePositionDTOGrid.setItems(positionProfileService.findByParameter(searchFilterTextField.getValue()));
        } else {
            employeePositionDTOGrid.setItems(query -> positionProfileService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
