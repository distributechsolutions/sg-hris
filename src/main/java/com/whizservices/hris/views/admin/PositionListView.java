package com.whizservices.hris.views.admin;

import com.vaadin.flow.component.Component;
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

import com.whizservices.hris.dtos.admin.PositionDTO;
import com.whizservices.hris.services.admin.PositionService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;

import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_HR_SUPERVISOR"})
@PageTitle("Position List")
@Route(value = "position-list", layout = MainLayout.class)
public class PositionListView extends VerticalLayout {
    @Resource private final PositionService positionService;

    private TextField searchFilterTextField;
    private Grid<PositionDTO> positionDTOGrid;

    public PositionListView(PositionService positionService) {
        this.positionService = positionService;

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

        rowToolbarLayout.add(viewButton, editButton);
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
