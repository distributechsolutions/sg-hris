package io.softwaregarage.hris.profile.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.data.renderer.LocalDateRenderer;
import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.profile.dtos.DependentProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.profile.services.DependentProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.DashboardView;

import jakarta.annotation.Resource;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.vaadin.lineawesome.LineAwesomeIcon;

public class DependentProfileFormView extends VerticalLayout {
    @Resource private final DependentProfileService dependentProfileService;
    @Resource private final UserService userService;
    @Resource private final EmployeeProfileService employeeProfileService;

    private List<DependentProfileDTO> dependentProfileDTOList;
    private DependentProfileDTO dependentProfileDTO;
    private UserDTO userDTO;
    private EmployeeProfileDTO employeeProfileDTO;

    private String loggedInUser;

    private Grid<DependentProfileDTO> dependentInfoDTOGrid;
    private FormLayout dependentInfoFormLayout;
    private TextField fullNameTextField;
    private DatePicker dateOfBirthDatePicker;
    private ComboBox<String> relationshipComboBox;
    private Button saveButton, cancelButton, viewButton, editButton, deleteButton;

    public DependentProfileFormView(DependentProfileService dependentProfileService,
                                    UserService userService,
                                    EmployeeProfileService employeeProfileService) {
        this.dependentProfileService = dependentProfileService;
        this.userService = userService;
        this.employeeProfileService = employeeProfileService;

        loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (loggedInUser != null) {
            userDTO = userService.getByUsername(loggedInUser);
        }

        if (userDTO != null) {
            employeeProfileDTO = userDTO.getEmployeeDTO();
        }

        if (employeeProfileDTO != null) {
            dependentProfileDTOList = dependentProfileService.getByEmployeeDTO(employeeProfileDTO);
        }

        dependentInfoDTOGrid = new Grid<>(DependentProfileDTO.class, false);
        dependentInfoFormLayout = new FormLayout();

        this.buildDependentInfoFormLayout();
        this.buildDependentInfoDTOGrid();

        this.add(dependentInfoFormLayout, dependentInfoDTOGrid);
    }

    private void buildDependentInfoFormLayout() {
        fullNameTextField = new TextField("Full Name");
        fullNameTextField.setRequired(true);
        fullNameTextField.setRequiredIndicatorVisible(true);

        dateOfBirthDatePicker = new DatePicker("Date of birth");
        dateOfBirthDatePicker.setRequired(true);
        dateOfBirthDatePicker.setRequiredIndicatorVisible(true);

        relationshipComboBox = new ComboBox<>("Relationship");
        relationshipComboBox.setItems("Father", "Mother", "Sibling", "Grandfather", "Grandmother", "Spouse", "Son", "Daughter");
        relationshipComboBox.setRequired(true);
        relationshipComboBox.setRequiredIndicatorVisible(true);

        saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            // Save the dependent and clear the fields.
            this.saveDependentInfoDTO();
            this.clearFields();

            // Update the dependent grid table.
            dependentProfileDTOList = dependentProfileService.getByEmployeeDTO(employeeProfileDTO);
            dependentInfoDTOGrid.setItems(dependentProfileDTOList);
        });

        cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> buttonClickEvent.getSource().getUI().ifPresent(ui -> ui.navigate(DashboardView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setPadding(true);

        dependentInfoFormLayout.setColspan(fullNameTextField, 2);
        dependentInfoFormLayout.setColspan(buttonLayout, 2);
        dependentInfoFormLayout.add(fullNameTextField,
                                    dateOfBirthDatePicker,
                                    relationshipComboBox,
                                    buttonLayout);
        dependentInfoFormLayout.setWidth("768px");
    }

    private void buildDependentInfoDTOGrid() {
        dependentInfoDTOGrid.addColumn(DependentProfileDTO::getFullName).setHeader("Name");
        dependentInfoDTOGrid.addColumn(new LocalDateRenderer<>(DependentProfileDTO::getDateOfBirth, "MMM dd, yyyy")).setHeader("Date of Birth");
        dependentInfoDTOGrid.addColumn(DependentProfileDTO::getAge).setHeader("Age");
        dependentInfoDTOGrid.addColumn(DependentProfileDTO::getRelationship).setHeader("Relationship");
        dependentInfoDTOGrid.addComponentColumn(addressDTO -> this.buildDependentInfoRowToolbar()).setHeader("Action");
        dependentInfoDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                              GridVariant.LUMO_COLUMN_BORDERS,
                                              GridVariant.LUMO_WRAP_CELL_CONTENT);
        dependentInfoDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        dependentInfoDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        dependentInfoDTOGrid.setEmptyStateText("No dependent information found.");
        dependentInfoDTOGrid.setAllRowsVisible(true);
        dependentInfoDTOGrid.setItems(dependentProfileDTOList);
    }

    private Component buildDependentInfoRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        viewButton = new Button();
        viewButton.setTooltipText("View Dependent");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> this.loadDependentInfoDTO(true));

        editButton = new Button();
        editButton.setTooltipText("Edit Dependent");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(buttonClickEvent -> this.loadDependentInfoDTO(false));

        deleteButton = new Button();
        deleteButton.setTooltipText("Delete Dependent");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(buttonClickEvent -> {
            if (dependentInfoDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                // Show the confirmation dialog.
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Delete Depdendent Information");
                confirmDialog.setText(new Html("<p>WARNING! This will permanently remove the record in the database. Are you sure you want to delete the selected dependent information?</p>"));
                confirmDialog.setConfirmText("Yes, Delete it.");
                confirmDialog.setConfirmButtonTheme("error primary");
                confirmDialog.addConfirmListener(confirmEvent -> {
                    // Get the selected dependent information and delete it.
                    DependentProfileDTO selectedDependentProfileDTO = dependentInfoDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                    dependentProfileService.delete(selectedDependentProfileDTO);

                    // Show notification message.
                    Notification notification = Notification.show("You have successfully deleted the selected dependent information.",  5000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                    // Close the confirmation dialog.
                    confirmDialog.close();

                    // Update the dependent grid table.
                    dependentProfileDTOList = dependentProfileService.getByEmployeeDTO(employeeProfileDTO);
                    dependentInfoDTOGrid.setItems(dependentProfileDTOList);
                });
                confirmDialog.setCancelable(true);
                confirmDialog.setCancelText("No");
                confirmDialog.open();
            }
        });

        rowToolbarLayout.add(viewButton, editButton, deleteButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void saveDependentInfoDTO() {
        if (dependentProfileDTO == null) {
            dependentProfileDTO = new DependentProfileDTO();
            dependentProfileDTO.setEmployeeDTO(employeeProfileDTO);
            dependentProfileDTO.setCreatedBy(loggedInUser);
        }

        dependentProfileDTO.setFullName(fullNameTextField.getValue());
        dependentProfileDTO.setDateOfBirth(dateOfBirthDatePicker.getValue());
        dependentProfileDTO.setAge(LocalDate.now().getYear() - dateOfBirthDatePicker.getValue().getYear());
        dependentProfileDTO.setRelationship(relationshipComboBox.getValue());
        dependentProfileDTO.setUpdatedBy(loggedInUser);

        dependentProfileService.saveOrUpdate(dependentProfileDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved your dependent.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void clearFields() {
        fullNameTextField.clear();
        dateOfBirthDatePicker.clear();
        relationshipComboBox.clear();
    }

    private void loadDependentInfoDTO(boolean readOnly) {
        dependentProfileDTO = dependentInfoDTOGrid.getSelectionModel().getFirstSelectedItem().get();

        fullNameTextField.setValue(dependentProfileDTO.getFullName());
        fullNameTextField.setReadOnly(readOnly);

        dateOfBirthDatePicker.setValue(dependentProfileDTO.getDateOfBirth());
        dateOfBirthDatePicker.setReadOnly(readOnly);

        relationshipComboBox.setValue(dependentProfileDTO.getRelationship());
        relationshipComboBox.setReadOnly(readOnly);
    }
}
