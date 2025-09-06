package io.softwaregarage.hris.profile.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.profile.dtos.DepartmentProfileDTO;
import io.softwaregarage.hris.profile.services.DepartmentProfileService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_HR_SUPERVISOR",
        "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Department List")
@Route(value = "employee-department-list", layout = MainLayout.class)
public class DepartmentProfileListView extends VerticalLayout {
    @Resource
    private final DepartmentProfileService departmentProfileService;

    private Grid<DepartmentProfileDTO> employeeDepartmentDTOGrid;
    private TextField searchFilterTextField;

    public DepartmentProfileListView(DepartmentProfileService departmentProfileService) {
        this.departmentProfileService = departmentProfileService;

        this.add(buildHeaderToolbar(), buildEmployeeDepartmentDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateEmployeeDepartmentDTOGrid());

        Button addButton = new Button("Add Employee Department");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(DepartmentProfileFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<DepartmentProfileDTO> buildEmployeeDepartmentDTOGrid() {
        employeeDepartmentDTOGrid = new Grid<>(DepartmentProfileDTO.class, false);

        employeeDepartmentDTOGrid.addColumn(employeeDepartmentDTO -> employeeDepartmentDTO.getEmployeeDTO().getEmployeeNumber())
                .setHeader("Employee No.")
                .setSortable(true);
        employeeDepartmentDTOGrid.addColumn(employeeDepartmentDTO -> employeeDepartmentDTO.getEmployeeDTO().getFirstName().concat(" ")
                        .concat(employeeDepartmentDTO.getEmployeeDTO().getMiddleName())
                        .concat(" ")
                        .concat(employeeDepartmentDTO.getEmployeeDTO().getLastName())
                        .concat(employeeDepartmentDTO.getEmployeeDTO().getSuffix() != null ? employeeDepartmentDTO.getEmployeeDTO().getSuffix() : ""))
                .setHeader("Employee Name")
                .setSortable(true);
        employeeDepartmentDTOGrid.addColumn(employeeDepartmentDTO -> employeeDepartmentDTO.getDepartmentDTO().getName())
                .setHeader("Department")
                .setSortable(true);
        employeeDepartmentDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, employeeDepartmentDTO) -> {
            String theme = String.format("badge %s", employeeDepartmentDTO.isCurrentDepartment() ? "success" : "error");

            Span activeSpan = new Span();
            activeSpan.getElement().setAttribute("theme", theme);
            activeSpan.setText(employeeDepartmentDTO.isCurrentDepartment() ? "Yes" : "No");

            layout.setJustifyContentMode(JustifyContentMode.CENTER);
            layout.add(activeSpan);
        })).setHeader("Is Current Department?").setSortable(true);
        employeeDepartmentDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        employeeDepartmentDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeDepartmentDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeeDepartmentDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeeDepartmentDTOGrid.setAllRowsVisible(true);
        employeeDepartmentDTOGrid.setEmptyStateText("No employee departments found.");
        employeeDepartmentDTOGrid.setItems((query -> departmentProfileService.getAll(query.getPage(), query.getPageSize()).stream()));

        return employeeDepartmentDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Employee Department");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (employeeDepartmentDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                DepartmentProfileDTO selectedDepartmentProfileDTO = employeeDepartmentDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(DepartmentProfileDetailsView.class, selectedDepartmentProfileDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Employee Department");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (employeeDepartmentDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                DepartmentProfileDTO selectedDepartmentProfileDTO = employeeDepartmentDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(DepartmentProfileFormView.class, selectedDepartmentProfileDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateEmployeeDepartmentDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            employeeDepartmentDTOGrid.setItems(departmentProfileService.findByParameter(searchFilterTextField.getValue()));
        } else {
            employeeDepartmentDTOGrid.setItems(query -> departmentProfileService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
