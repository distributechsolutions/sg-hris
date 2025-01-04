package com.whizservices.hris.views.admin;

import com.vaadin.flow.component.Component;
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

import com.whizservices.hris.dtos.admin.UserDTO;
import com.whizservices.hris.services.admin.UserService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;

import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER"})
@PageTitle("User List")
@Route(value = "user-list", layout = MainLayout.class)
public class UserListView extends VerticalLayout {
    @Resource
    private final UserService userService;

    private TextField searchFilterTextField;
    private Grid<UserDTO> userDTOGrid;

    public UserListView(UserService userService) {
        this.userService = userService;

        this.add(buildHeaderToolbar(), buildUserDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateUserDTOGrid());

        Button addUserButton = new Button("Add User");
        addUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addUserButton.addClickListener(buttonClickEvent -> addUserButton.getUI().ifPresent(ui -> ui.navigate(UserFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addUserButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<UserDTO> buildUserDTOGrid() {
        userDTOGrid = new Grid<>(UserDTO.class, false);

        userDTOGrid.addColumn(UserDTO::getUsername).setHeader("Username").setSortable(true);
        userDTOGrid.addColumn(userDTO -> userDTO.getEmployeeDTO().getLastName()
                                    .concat(userDTO.getEmployeeDTO().getSuffix() != null ? userDTO.getEmployeeDTO().getSuffix() : "")
                                    .concat(", ")
                                    .concat(userDTO.getEmployeeDTO().getFirstName()))
                    .setHeader("Employee Name").setSortable(true);
        userDTOGrid.addColumn(UserDTO::getEmailAddress).setHeader("Email Address").setSortable(true);
        userDTOGrid.addColumn(UserDTO::getRole).setHeader("Role").setSortable(true);
        userDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, userDTO) -> {
            String theme = String.format("badge %s", userDTO.isAccountActive() ? "success" : "error");

            Span activeSpan = new Span();
            activeSpan.getElement().setAttribute("theme", theme);
            activeSpan.setText(userDTO.isAccountActive() ? "Yes" : "No");

            layout.setJustifyContentMode(JustifyContentMode.CENTER);
            layout.add(activeSpan);
        })).setHeader("Is Active?").setSortable(true);
        userDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, userDTO) -> {
            String theme = String.format("badge %s", userDTO.isPasswordChanged() ? "success" : "error");

            Span activeSpan = new Span();
            activeSpan.getElement().setAttribute("theme", theme);
            activeSpan.setText(userDTO.isPasswordChanged() ? "Yes" : "No");

            layout.setJustifyContentMode(JustifyContentMode.CENTER);
            layout.add(activeSpan);
        })).setHeader("Is Password Changed?").setSortable(true);
        userDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        userDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                     GridVariant.LUMO_COLUMN_BORDERS,
                                     GridVariant.LUMO_WRAP_CELL_CONTENT);
        userDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        userDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        userDTOGrid.setAllRowsVisible(true);
        userDTOGrid.setEmptyStateText("No user records found.");
        userDTOGrid.setItems((query -> userService.getAll(query.getPage(), query.getPageSize()).stream()));

        return userDTOGrid;
    }

    public Component buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewUserButton = new Button();
        viewUserButton.setTooltipText("View User");
        viewUserButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewUserButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewUserButton.addClickListener(buttonClickEvent -> viewUserButton.getUI().ifPresent(ui -> {
            if (userDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                UserDTO selectedUserDTO = userDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(UserDetailsView.class, selectedUserDTO.getId().toString());
            }
        }));

        Button editUserButton = new Button();
        editUserButton.setTooltipText("Edit User");
        editUserButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editUserButton.addClickListener(buttonClickEvent -> editUserButton.getUI().ifPresent(ui -> {
            if (userDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                UserDTO selectedUserDTO = userDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(UserFormView.class, selectedUserDTO.getId().toString());
            }
        }));

        Button deleteUserButton = new Button();
        deleteUserButton.setTooltipText("Delete User");
        deleteUserButton.setIcon(LineAwesomeIcon.TRASH_SOLID.create());
        deleteUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteUserButton.addClickListener(buttonClickEvent -> deleteUserButton.getUI().ifPresent(ui -> {
            if (userDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                // Show the confirmation dialog.
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Delete User Account");
                confirmDialog.setText(new Html("<p>WARNING! This will permanently remove the record in the database. Are you sure you want to delete the selected user account?</p>"));
                confirmDialog.setConfirmText("Yes, Delete it.");
                confirmDialog.setConfirmButtonTheme("error primary");
                confirmDialog.addConfirmListener(confirmEvent -> {
                    // Delete the selected user account.
                    UserDTO selectedUserDTO = userDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                    userService.delete(selectedUserDTO);

                    // Show success notification.
                    Notification notification = Notification.show("You have successfully deleted the selected user account.",  5000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                    // Update the user data grid.
                    this.updateUserDTOGrid();
                });
                confirmDialog.setCancelable(true);
                confirmDialog.setCancelText("No");
                confirmDialog.open();
            }
        }));

        rowToolbarLayout.add(viewUserButton, editUserButton, deleteUserButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateUserDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            userDTOGrid.setItems(userService.findByParameter(searchFilterTextField.getValue()));
        } else {
            userDTOGrid.setItems(query -> userService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
