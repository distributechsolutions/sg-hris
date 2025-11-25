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
import io.softwaregarage.hris.payroll.dtos.TaxRatesDTO;
import io.softwaregarage.hris.payroll.services.TaxRatesService;
import io.softwaregarage.hris.utils.SecurityUtil;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.format.DateTimeFormatter;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_PAYROLL_MANAGER",
        "ROLE_PAYROLL_EMPLOYEE"})
@PageTitle("Tax Rates")
@Route(value = "tax-rates-list", layout = MainLayout.class)
public class TaxRatesListView extends VerticalLayout {
    @Resource
    private final TaxRatesService taxRatesService;

    @Resource
    private final UserService userService;

    private UserDTO userDTO;

    private Grid<TaxRatesDTO> taxRatesDTOGrid;
    private TextField searchFilterTextField;

    public TaxRatesListView(TaxRatesService taxRatesService, UserService userService) {
        this.taxRatesService = taxRatesService;
        this.userService = userService;

        if (SecurityUtil.getAuthenticatedUser() != null) {
            userDTO = userService.getByUsername(SecurityUtil.getAuthenticatedUser().getUsername());
        }

        this.add(buildHeaderToolbar(), buildTaxRatesDTOGrid());
        this.setSizeFull();
        this.setAlignItems(Alignment.STRETCH);
    }

    public HorizontalLayout buildHeaderToolbar() {
        HorizontalLayout taxRatesHeaderToolbarLayout = new HorizontalLayout();

        searchFilterTextField = new TextField();
        searchFilterTextField.setWidth("350px");
        searchFilterTextField.setPlaceholder("Search");
        searchFilterTextField.setPrefixComponent(LineAwesomeIcon.SEARCH_SOLID.create());
        searchFilterTextField.getStyle().set("margin", "0 auto 0 0");
        searchFilterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateTaxRatesDTOGrid());

        Button addButton = new Button("Add Tax Rate");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(TaxRatesFormView.class)));

        taxRatesHeaderToolbarLayout.add(searchFilterTextField, addButton);
        taxRatesHeaderToolbarLayout.setAlignItems(Alignment.CENTER);
        taxRatesHeaderToolbarLayout.getThemeList().clear();

        return taxRatesHeaderToolbarLayout;
    }

    private Grid<TaxRatesDTO> buildTaxRatesDTOGrid() {
        taxRatesDTOGrid = new Grid<>(TaxRatesDTO.class, false);

        taxRatesDTOGrid.addColumn(TaxRatesDTO::getTaxYear).setHeader("Tax Year").setSortable(true);
        taxRatesDTOGrid.addColumn(taxRatesDTO -> DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        .format(taxRatesDTO.getEffectiveDate()))
                .setHeader("Effectve Date")
                .setSortable(true);
        taxRatesDTOGrid.addColumn(TaxRatesDTO::getLowerBoundAmount)
                .setHeader("Lower Bound Amount (PHP)")
                .setSortable(true);
        taxRatesDTOGrid.addColumn(TaxRatesDTO::getUpperBoundAmount)
                .setHeader("Upper Bound Amount (PHP)")
                .setSortable(true);
        taxRatesDTOGrid.addColumn(TaxRatesDTO::getBaseTax).setHeader("Base Tax Amount").setSortable(true);
        taxRatesDTOGrid.addColumn(TaxRatesDTO::getRate).setHeader("Rate (%)").setSortable(true);
        taxRatesDTOGrid.addColumn(taxRatesDTO -> taxRatesDTO.isActiveTaxRate() ? "Yes" : "No")
                .setHeader("Is Active Tax Rate?")
                .setSortable(true);
        taxRatesDTOGrid.addComponentColumn(taxExemptionsDTO -> buildRowToolbar()).setHeader("Action");
        taxRatesDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_WRAP_CELL_CONTENT);
        taxRatesDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taxRatesDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        taxRatesDTOGrid.setAllRowsVisible(true);
        taxRatesDTOGrid.setEmptyStateText("No tax exemption records found.");
        taxRatesDTOGrid.setItems((query -> taxRatesService.getAll(query.getPage(), query.getPageSize()).stream()));

        return taxRatesDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Tax Rate");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (taxRatesDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TaxRatesDTO selectedTaxRatesDTO = taxRatesDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(TaxRatesDetailsView.class, selectedTaxRatesDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Tax Rate");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (taxRatesDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TaxRatesDTO selectedTaxRatesDTO = taxRatesDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(TaxRatesFormView.class, selectedTaxRatesDTO.getId().toString());
            }
        }));

        // Show the delete button if the role of the logged-in user is ROLE_ADMIN.
        Button deleteButton = new Button();
        deleteButton.setTooltipText("Delete Tax Rate");
        deleteButton.setIcon(LineAwesomeIcon.TRASH_ALT_SOLID.create());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(buttonClickEvent -> {
            if (taxRatesDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                TaxRatesDTO selectedTaxRatesDTO = taxRatesDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                // Show the confirmation dialog.
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Delete Tax Exemptions");
                confirmDialog.setText(new Html("""
                                               <p>
                                               WARNING! Are you sure you want to delete the selected tax rate?
                                               </p>
                                               """));
                confirmDialog.setConfirmText("Yes, Delete it.");
                confirmDialog.setConfirmButtonTheme("error primary");
                confirmDialog.addConfirmListener(confirmEvent -> {
                    // Get the selected rate and delete it.
                    taxRatesService.delete(selectedTaxRatesDTO);

                    // Refresh the data grid from the backend after the delete operation.
                    taxRatesDTOGrid.getDataProvider().refreshAll();

                    // Show notification message.
                    Notification notification = Notification.show("You have successfully deleted the selected tax rate.",
                            5000,
                            Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                    // Close the confirmation dialog.
                    confirmDialog.close();
                });
                confirmDialog.setCancelable(true);
                confirmDialog.setCancelText("No");
                confirmDialog.open();
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

    private void updateTaxRatesDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            taxRatesDTOGrid.setItems(taxRatesService.findByParameter(searchFilterTextField.getValue()));
        } else {
            taxRatesDTOGrid.setItems(query -> taxRatesService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
