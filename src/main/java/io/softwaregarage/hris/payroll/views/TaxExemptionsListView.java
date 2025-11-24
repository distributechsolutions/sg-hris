package io.softwaregarage.hris.payroll.views;

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

import io.softwaregarage.hris.admin.dtos.UserDTO;
import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.commons.views.MainLayout;
import io.softwaregarage.hris.payroll.dtos.TaxExemptionsDTO;
import io.softwaregarage.hris.payroll.services.TaxExemptionsService;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_HR_SUPERVISOR"})
@PageTitle("Tax Exemptions")
@Route(value = "tax-exemptions-list", layout = MainLayout.class)
public class TaxExemptionsListView extends VerticalLayout {
    @Resource
    private final TaxExemptionsService taxExemptionsService;

    @Resource
    private final UserService userService;

    private UserDTO userDTO;

    private Grid<TaxExemptionsDTO> taxExemptionsDTOGrid;
    private TextField searchFilterTextField;

    public TaxExemptionsListView(TaxExemptionsService taxExemptionsService, UserService userService) {
        this.taxExemptionsService = taxExemptionsService;
        this.userService = userService;

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(SecurityUtil.getAuthenticatedUser().getUsername());
        }

        this.add(buildHeaderToolbar(), buildTaxExemptionsDTOGrid());
        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
    }

    public HorizontalLayout buildHeaderToolbar() {
        HorizontalLayout taxExemptionsHeaderToolbarLayout = new HorizontalLayout();

        searchFilterTextField = new TextField();
        searchFilterTextField.setWidth("350px");
        searchFilterTextField.setPlaceholder("Search");
        searchFilterTextField.setPrefixComponent(LineAwesomeIcon.SEARCH_SOLID.create());
        searchFilterTextField.getStyle().set("margin", "0 auto 0 0");
        searchFilterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateTaxExemptionsDTOGrid());

        Button addButton = new Button("Add Tax Exemptions");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(TaxExemptionsFormView.class)));

        taxExemptionsHeaderToolbarLayout.add(searchFilterTextField, addButton);
        taxExemptionsHeaderToolbarLayout.setAlignItems(Alignment.CENTER);
        taxExemptionsHeaderToolbarLayout.getThemeList().clear();

        return taxExemptionsHeaderToolbarLayout;
    }

    private Grid<TaxExemptionsDTO> buildTaxExemptionsDTOGrid() {
        taxExemptionsDTOGrid = new Grid<>(TaxExemptionsDTO.class, false);

        taxExemptionsDTOGrid.addColumn(taxExemptionsDTO -> taxExemptionsDTO.getEmployeeProfileDTO().getEmployeeNumber())
                .setHeader("Employee No.")
                .setSortable(true);
        taxExemptionsDTOGrid.addColumn(taxExemptionsDTO -> taxExemptionsDTO.getEmployeeProfileDTO().getFirstName()
                        .concat(" ")
                        .concat(taxExemptionsDTO.getEmployeeProfileDTO().getMiddleName())
                        .concat(" ")
                        .concat(taxExemptionsDTO.getEmployeeProfileDTO().getLastName())
                        .concat(taxExemptionsDTO.getEmployeeProfileDTO().getSuffix() != null ? taxExemptionsDTO.getEmployeeProfileDTO().getSuffix() : ""))
                .setHeader("Employee Name")
                .setSortable(true);
        taxExemptionsDTOGrid.addColumn(TaxExemptionsDTO::getTaxExemptionPercentage)
                .setHeader("Exemption Tax Percentage")
                .setSortable(true);
        taxExemptionsDTOGrid.addColumn(taxExemptionsDTO -> taxExemptionsDTO.isActiveTaxExemption() ? "Yes" : "No")
                .setHeader("Is Active Tax Exemption?")
                .setSortable(true);
        taxExemptionsDTOGrid.addComponentColumn(taxExemptionsDTO -> buildRowToolbar()).setHeader("Action");
        taxExemptionsDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_WRAP_CELL_CONTENT);
        taxExemptionsDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taxExemptionsDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        taxExemptionsDTOGrid.setAllRowsVisible(true);
        taxExemptionsDTOGrid.setEmptyStateText("No tax exemption records found.");
        taxExemptionsDTOGrid.setItems((query -> taxExemptionsService.getAll(query.getPage(), query.getPageSize()).stream()));

        return taxExemptionsDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Tax Exemptions");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (taxExemptionsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TaxExemptionsDTO selectedTaxExemptionsDTO = taxExemptionsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(TaxExemptionsDetailsView.class, selectedTaxExemptionsDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Tax Exemptions");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (taxExemptionsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TaxExemptionsDTO selectedTaxExemptionsDTO = taxExemptionsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(TaxExemptionsFormView.class, selectedTaxExemptionsDTO.getId().toString());
            }
        }));

        // Show the delete button if the role of the logged-in user is ROLE_ADMIN.
        Button deleteButton = new Button();
        deleteButton.setTooltipText("Delete Tax Exemptions");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(buttonClickEvent -> {
            if (taxExemptionsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TaxExemptionsDTO selectedTaxExemptionsDTO = taxExemptionsDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                // Check first if that record is not an active employee anymore. If it is not active, you may proceed
                // for the deletion.
                if (selectedTaxExemptionsDTO.getEmployeeProfileDTO().getStatus().equals("RESIGNED")
                        || selectedTaxExemptionsDTO.getEmployeeProfileDTO().getStatus().equals("RETIRED")
                        || selectedTaxExemptionsDTO.getEmployeeProfileDTO().getStatus().equals("TERMINATED")
                        || selectedTaxExemptionsDTO.getEmployeeProfileDTO().getStatus().equals("DECEASED")) {
                    // Show the confirmation dialog.
                    ConfirmDialog confirmDialog = new ConfirmDialog();
                    confirmDialog.setHeader("Delete Tax Exemptions");
                    confirmDialog.setText(new Html("""
                                               <p>
                                               WARNING! Are you sure you want to delete the selected employee's tax exemption?
                                               </p>
                                               """));
                    confirmDialog.setConfirmText("Yes, Delete it.");
                    confirmDialog.setConfirmButtonTheme("error primary");
                    confirmDialog.addConfirmListener(confirmEvent -> {
                        // Get the selected rate and delete it.
                        taxExemptionsService.delete(selectedTaxExemptionsDTO);

                        // Refresh the data grid from the backend after the delete operation.
                        taxExemptionsDTOGrid.getDataProvider().refreshAll();

                        // Show notification message.
                        Notification notification = Notification.show("You have successfully deleted the selected employee's tax exemption.",
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
                    Notification notification = Notification.show("You cannot delete the selected employee's tax exemption. Employee is still in active status.",
                            5000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        rowToolbarLayout.add(viewButton, editButton, deleteButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateTaxExemptionsDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            taxExemptionsDTOGrid.setItems(taxExemptionsService.findByParameter(searchFilterTextField.getValue()));
        } else {
            taxExemptionsDTOGrid.setItems(query -> taxExemptionsService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
