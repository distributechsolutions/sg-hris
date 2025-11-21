package io.softwaregarage.hris.admin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.admin.dtos.PositionDTO;
import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.admin.services.PositionService;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.profile.dtos.PositionProfileDTO;
import io.softwaregarage.hris.profile.services.PositionProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_HR_SUPERVISOR"})
@PageTitle("Positions")
@Route(value = "position-list", layout = MainLayout.class)
public class PositionListView extends VerticalLayout {
    @Resource
    private final PositionService positionService;

    @Resource
    private final PositionProfileService positionProfileService;

    @Resource
    private final UserService userService;

    private UserDTO userDTO;

    private TextField searchFilterTextField;
    private Grid<PositionDTO> positionDTOGrid;

    public PositionListView(PositionService positionService,
                            PositionProfileService positionProfileService,
                            UserService userService) {
        this.positionService = positionService;
        this.positionProfileService = positionProfileService;
        this.userService = userService;

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(SecurityUtil.getAuthenticatedUser().getUsername());
        }

        this.add(buildHeaderToolbar(), buildPositionDTOGrid());
        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
    }

    public Component buildHeaderToolbar() {
        HorizontalLayout headerToolbarLayout = new HorizontalLayout();

        searchFilterTextField = new TextField();
        searchFilterTextField.setWidth("350px");
        searchFilterTextField.setPlaceholder("Search");
        searchFilterTextField.setPrefixComponent(LineAwesomeIcon.SEARCH_SOLID.create());
        searchFilterTextField.getStyle().set("margin", "0 auto 0 0");
        searchFilterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updatePositionDTOGrid());

        Button addPositionButton = new Button("Add Position");
        addPositionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addPositionButton.addClickListener(buttonClickEvent -> addPositionButton.getUI().ifPresent(ui -> ui.navigate(PositionFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addPositionButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<PositionDTO> buildPositionDTOGrid() {
        positionDTOGrid = new Grid<>(PositionDTO.class, false);

        positionDTOGrid.addColumn(PositionDTO::getCode).setHeader("Code").setSortable(true);
        positionDTOGrid.addColumn(PositionDTO::getName).setHeader("Name").setSortable(true);
        positionDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        positionDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                         GridVariant.LUMO_COLUMN_BORDERS,
                                         GridVariant.LUMO_WRAP_CELL_CONTENT);
        positionDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        positionDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        positionDTOGrid.setAllRowsVisible(true);
        positionDTOGrid.setEmptyStateText("No position records found.");
        positionDTOGrid.setItems((query -> positionService.getAll(query.getPage(), query.getPageSize()).stream()));

        return positionDTOGrid;
    }

    public Component buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Position");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (positionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                PositionDTO selectedPositionDTO = positionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(PositionDetailsView.class, selectedPositionDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Position");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (positionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                PositionDTO selectedPositionDTO = positionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(PositionFormView.class, selectedPositionDTO.getId().toString());
            }
        }));

        Button deleteButton = new Button();
        deleteButton.setTooltipText("Delete Position");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(buttonClickEvent -> {
            if (positionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                PositionDTO selectedPositionDTO = positionDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                List<PositionProfileDTO> positionProfileDTOList = positionProfileService.findByParameter(selectedPositionDTO.getCode());
                if (positionProfileDTOList.isEmpty()) {
                    // Show the confirmation dialog.
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Delete Position");
                    confirmDialog.setText(new Html("""
                                               <p>
                                               WARNING! Before deleting this position, be sure that there are no
                                               employees assigned to it. Are you sure you want to delete the selected
                                               position?
                                               </p>
                                               """));
                    confirmDialog.setConfirmText("Yes, Delete it.");
                    confirmDialog.setConfirmButtonTheme("error primary");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Get the selected position and delete it.
                        positionService.delete(selectedPositionDTO);

                        // Refresh the data grid from the backend after the delete operation.
                        positionDTOGrid.getDataProvider().refreshAll();

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully deleted the selected position.",
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
                    Notification notification = Notification.show("You cannot delete the selected position. There are employees assigned to it.",
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

    private void updatePositionDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            positionDTOGrid.setItems(positionService.findByParameter(searchFilterTextField.getValue()));
        } else {
            positionDTOGrid.setItems(query -> positionService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
