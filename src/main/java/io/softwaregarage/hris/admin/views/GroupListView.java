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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.admin.dtos.DepartmentDTO;
import io.softwaregarage.hris.admin.dtos.GroupDTO;
import io.softwaregarage.hris.admin.services.DepartmentService;
import io.softwaregarage.hris.admin.services.GroupService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;

@RolesAllowed({"ROLE_ADMIN"})
@PageTitle("Group")
@Route(value = "group-list", layout = MainLayout.class)
public class GroupListView extends VerticalLayout {
    @Resource
    private final GroupService groupService;

    @Resource
    private final DepartmentService departmentService;

    private TextField searchGroupFilterTextField;
    private Grid<GroupDTO> groupDTOGrid;

    public GroupListView(GroupService groupService, DepartmentService departmentService) {
        this.groupService = groupService;
        this.departmentService = departmentService;

        this.add(buildGroupHeaderToolbar(), buildGroupDTOGrid());
        this.setSizeFull();
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
    }

    public Component buildGroupHeaderToolbar() {
        HorizontalLayout groupHeaderToolbarLayout = new HorizontalLayout();

        searchGroupFilterTextField = new TextField();
        searchGroupFilterTextField.setWidth("350px");
        searchGroupFilterTextField.setPlaceholder("Search");
        searchGroupFilterTextField.setPrefixComponent(LineAwesomeIcon.SEARCH_SOLID.create());
        searchGroupFilterTextField.getStyle().set("margin", "0 auto 0 0");
        searchGroupFilterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchGroupFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateGroupDTOGrid());

        Button addGroupButton = new Button("Add Group");
        addGroupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addGroupButton.addClickListener(buttonClickEvent -> addGroupButton.getUI().ifPresent(ui -> ui.navigate(GroupFormView.class)));

        groupHeaderToolbarLayout.add(searchGroupFilterTextField, addGroupButton);
        groupHeaderToolbarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        groupHeaderToolbarLayout.getThemeList().clear();

        return groupHeaderToolbarLayout;
    }

    private Grid<GroupDTO> buildGroupDTOGrid() {
        groupDTOGrid = new Grid<>(GroupDTO.class, false);

        groupDTOGrid.addColumn(GroupDTO::getCode).setHeader("Code").setSortable(true);
        groupDTOGrid.addColumn(GroupDTO::getName).setHeader("Name").setSortable(true);
        groupDTOGrid.addColumn(GroupDTO::getDescription).setHeader("Description").setSortable(true);
        groupDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        groupDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_WRAP_CELL_CONTENT);
        groupDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        groupDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        groupDTOGrid.setAllRowsVisible(true);
        groupDTOGrid.setEmptyStateText("No group records found.");
        groupDTOGrid.setItems((query -> groupService.getAll(query.getPage(), query.getPageSize()).stream()));

        return groupDTOGrid;
    }

    public Component buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Group");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (groupDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                GroupDTO selectedGroupDTO = groupDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(GroupDetailsView.class, selectedGroupDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Group");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (groupDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                GroupDTO selectedGroupDTO = groupDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(GroupFormView.class, selectedGroupDTO.getId().toString());
            }
        }));

        Button deleteButton = new Button();
        deleteButton.setTooltipText("Delete Group");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(buttonClickEvent -> {
            if (groupDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                GroupDTO selectedGroupDTO = groupDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                List<DepartmentDTO> departmentDTOList = departmentService.getDepartmentsByGroup(selectedGroupDTO);
                if (departmentDTOList.isEmpty()) {
                    // Show the confirmation dialog.
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Delete Group");
                    confirmDialog.setText(new Html("""
                                               <p>
                                               WARNING! Before deleting this group, be sure that there are no
                                               departments assigned to it. Are you sure you want to delete the selected
                                               group?
                                               </p>
                                               """));
                    confirmDialog.setConfirmText("Yes, Delete it.");
                    confirmDialog.setConfirmButtonTheme("error primary");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Get the selected group and delete it.
                        groupService.delete(selectedGroupDTO);

                        // Refresh the data grid from the backend after the delete operation.
                        groupDTOGrid.getDataProvider().refreshAll();

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully deleted the selected group.",
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
                    Notification notification = Notification.show("You cannot delete the selected group. There are departments assigned to it.",
                            5000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        rowToolbarLayout.add(viewButton, editButton, deleteButton);
        rowToolbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateGroupDTOGrid() {
        if (searchGroupFilterTextField.getValue() != null || searchGroupFilterTextField.getValue().isBlank()) {
            groupDTOGrid.setItems(groupService.findByParameter(searchGroupFilterTextField.getValue()));
        } else {
            groupDTOGrid.setItems(query -> groupService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
