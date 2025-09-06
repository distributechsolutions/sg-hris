package io.softwaregarage.hris.admin.views;

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

import io.softwaregarage.hris.admin.dtos.DepartmentDTO;
import io.softwaregarage.hris.admin.services.DepartmentService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_HR_SUPERVISOR"})
@PageTitle("Department List")
@Route(value = "department-list", layout = MainLayout.class)
public class DepartmentListView extends VerticalLayout {
    @Resource private final DepartmentService departmentService;

    private TextField searchFilterTextField;
    private Grid<DepartmentDTO> departmentDTOGrid;

    public DepartmentListView(DepartmentService departmentService) {
        this.departmentService = departmentService;

        this.add(buildHeaderToolbar(), buildDepartmentDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateDepartmentDTOGrid());

        Button addDepartmentButton = new Button("Add Department");
        addDepartmentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addDepartmentButton.addClickListener(buttonClickEvent -> addDepartmentButton.getUI().ifPresent(ui -> ui.navigate(DepartmentFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addDepartmentButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<DepartmentDTO> buildDepartmentDTOGrid() {
        departmentDTOGrid = new Grid<>(DepartmentDTO.class, false);

        departmentDTOGrid.addColumn(DepartmentDTO::getCode).setHeader("Code").setSortable(true);
        departmentDTOGrid.addColumn(DepartmentDTO::getName).setHeader("Name").setSortable(true);
        departmentDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        departmentDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                           GridVariant.LUMO_COLUMN_BORDERS,
                                           GridVariant.LUMO_WRAP_CELL_CONTENT);
        departmentDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        departmentDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        departmentDTOGrid.setAllRowsVisible(true);
        departmentDTOGrid.setEmptyStateText("No department records found.");
        departmentDTOGrid.setItems((query -> departmentService.getAll(query.getPage(), query.getPageSize()).stream()));

        return departmentDTOGrid;
    }

    public Component buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Department");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (departmentDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                DepartmentDTO selectedDepartmentDTO = departmentDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(DepartmentDetailsView.class, selectedDepartmentDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Department");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (departmentDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                DepartmentDTO selectedDepartmentDTO = departmentDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(DepartmentFormView.class, selectedDepartmentDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateDepartmentDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            departmentDTOGrid.setItems(departmentService.findByParameter(searchFilterTextField.getValue()));
        } else {
            departmentDTOGrid.setItems(query -> departmentService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
