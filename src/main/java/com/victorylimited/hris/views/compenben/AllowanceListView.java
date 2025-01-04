package com.victorylimited.hris.views.compenben;

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

import com.victorylimited.hris.dtos.compenben.AllowanceDTO;
import com.victorylimited.hris.services.compenben.AllowanceService;
import com.victorylimited.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Allowance List")
@Route(value = "allowance-list", layout = MainLayout.class)
public class AllowanceListView extends VerticalLayout {
    @Resource
    private final AllowanceService allowanceService;

    private Grid<AllowanceDTO> allowanceDTOGrid;
    private TextField searchFilterTextField;

    public AllowanceListView(AllowanceService allowanceService) {
        this.allowanceService = allowanceService;

        this.add(buildHeaderToolbar(), buildAllowanceDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateAllowanceDTOGrid());

        Button addButton = new Button("Add Allowance");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(AllowanceFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<AllowanceDTO> buildAllowanceDTOGrid() {
        allowanceDTOGrid = new Grid<>(AllowanceDTO.class, false);

        allowanceDTOGrid.addColumn(allowanceDTO -> allowanceDTO.getEmployeeDTO().getEmployeeNumber())
                .setHeader("Employee No.")
                .setSortable(true);
        allowanceDTOGrid.addColumn(allowanceDTO -> allowanceDTO.getEmployeeDTO().getFirstName()
                        .concat(" ")
                        .concat(allowanceDTO.getEmployeeDTO().getLastName())
                        .concat(allowanceDTO.getEmployeeDTO().getSuffix() != null ? allowanceDTO.getEmployeeDTO().getSuffix() : ""))
                .setHeader("Employee Name")
                .setSortable(true);
        allowanceDTOGrid.addColumn(AllowanceDTO::getAllowanceCode)
                .setHeader("Allowance Code")
                .setSortable(true);
        allowanceDTOGrid.addColumn(AllowanceDTO::getAllowanceType)
                .setHeader("Allowance Type")
                .setSortable(true);
        allowanceDTOGrid.addColumn(AllowanceDTO::getAllowanceAmount)
                .setHeader("Allowance Amount")
                .setSortable(true);
        allowanceDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        allowanceDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_WRAP_CELL_CONTENT);
        allowanceDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        allowanceDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        allowanceDTOGrid.setEmptyStateText("No benefit records found.");
        allowanceDTOGrid.setItems((query -> allowanceService.getAll(query.getPage(), query.getPageSize()).stream()));

        return allowanceDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Leave Benefit");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (allowanceDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                AllowanceDTO selectedAllowanceDTO = allowanceDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(AllowanceDetailsView.class, selectedAllowanceDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Leave Benefit");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (allowanceDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                AllowanceDTO selectedAllowanceDTO = allowanceDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(AllowanceFormView.class, selectedAllowanceDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateAllowanceDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            allowanceDTOGrid.setItems(allowanceService.findByParameter(searchFilterTextField.getValue()));
        } else {
            allowanceDTOGrid.setItems(query -> allowanceService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
