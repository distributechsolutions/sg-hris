package com.whizservices.hris.views.compenben;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.compenben.RatesDTO;
import com.whizservices.hris.services.compenben.RatesService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Rates List")
@Route(value = "rates-list", layout = MainLayout.class)
public class RatesListView extends VerticalLayout {
    @Resource
    private final RatesService ratesService;

    private Grid<RatesDTO> ratesDTOGrid;
    private TextField searchFilterTextField;

    public RatesListView(RatesService ratesService) {
        this.ratesService = ratesService;

        this.add(buildHeaderToolbar(), buildRatesDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateRatesDTOGrid());

        Button addButton = new Button("Add Employee Rates");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(RatesFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<RatesDTO> buildRatesDTOGrid() {
        ratesDTOGrid = new Grid<>(RatesDTO.class, false);

        ratesDTOGrid.addColumn(ratesDTO -> ratesDTO.getEmployeeDTO().getEmployeeNumber())
                    .setHeader("Employee No.")
                    .setSortable(true);
        ratesDTOGrid.addColumn(ratesDTO -> ratesDTO.getEmployeeDTO().getFirstName()
                                                                    .concat(" ")
                                                                    .concat(ratesDTO.getEmployeeDTO().getMiddleName())
                                                                    .concat(" ")
                                                            .concat(ratesDTO.getEmployeeDTO().getLastName())
                                                            .concat(ratesDTO.getEmployeeDTO().getSuffix() != null ? ratesDTO.getEmployeeDTO().getSuffix() : ""))
                    .setHeader("Employee Name")
                    .setSortable(true);
        ratesDTOGrid.addColumn(RatesDTO::getRateType)
                    .setHeader("Rate Type")
                    .setSortable(true);
        ratesDTOGrid.addColumn(ratesDTO -> "PHP ".concat(String.valueOf(ratesDTO.getBasicCompensationRate())))
                    .setHeader("Basic Rate")
                    .setSortable(true);
        ratesDTOGrid.addColumn(ratesDTO -> "PHP ".concat(String.valueOf(ratesDTO.getDailyCompensationRate())))
                    .setHeader("Daily Rate")
                    .setSortable(true);
        ratesDTOGrid.addColumn(ratesDTO -> "PHP ".concat(String.valueOf(ratesDTO.getHourlyCompensationRate())))
                    .setHeader("Hourly Rate")
                    .setSortable(true);
        ratesDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        ratesDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                      GridVariant.LUMO_COLUMN_BORDERS,
                                      GridVariant.LUMO_WRAP_CELL_CONTENT);
        ratesDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        ratesDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        ratesDTOGrid.setAllRowsVisible(true);
        ratesDTOGrid.setEmptyStateText("No rate records found.");
        ratesDTOGrid.setItems((query -> ratesService.getAll(query.getPage(), query.getPageSize()).stream()));

        return ratesDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Employee Rates");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (ratesDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                RatesDTO selectedRatesDTO = ratesDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(RatesDetailsView.class, selectedRatesDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Employee Rates");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (ratesDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                RatesDTO selectedRatesDTO = ratesDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(RatesFormView.class, selectedRatesDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateRatesDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            ratesDTOGrid.setItems(ratesService.findByParameter(searchFilterTextField.getValue()));
        } else {
            ratesDTOGrid.setItems(query -> ratesService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
