package com.whizservices.hris.views.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee List")
@Route(value = "employee-list", layout = MainLayout.class)
public class EmployeeListView extends VerticalLayout {
    @Resource
    private final EmployeeService employeeService;

    private Grid<EmployeeDTO> employeeDTOGrid;
    private TextField searchFilterTextField;

    public EmployeeListView(EmployeeService employeeService) {
        this.employeeService = employeeService;

        this.add(buildHeaderToolbar(), buildEmployeeDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateEmployeeDTOGrid());

        Button addEmployeeButton = new Button("Add Employee");
        addEmployeeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addEmployeeButton.addClickListener(buttonClickEvent -> addEmployeeButton.getUI().ifPresent(ui -> ui.navigate(EmployeeFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addEmployeeButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<EmployeeDTO> buildEmployeeDTOGrid() {
        employeeDTOGrid = new Grid<>(EmployeeDTO.class, false);

        employeeDTOGrid.addColumn(EmployeeDTO::getEmployeeNumber)
                       .setHeader("Employee No.")
                       .setSortable(true);
        employeeDTOGrid.addColumn(EmployeeDTO::getBiometricsNumber)
                       .setHeader("Biometric No.")
                       .setSortable(true);
        employeeDTOGrid.addColumn(employeeDTO -> employeeDTO.getFirstName().concat(" ")
                                                                           .concat(employeeDTO.getMiddleName())
                                                                           .concat(" ")
                                                                           .concat(employeeDTO.getLastName())
                                                                           .concat(employeeDTO.getSuffix() != null ? employeeDTO.getSuffix() : ""))
                       .setHeader("Employee Name")
                       .setSortable(true);
        employeeDTOGrid.addColumn(new LocalDateRenderer<>(EmployeeDTO::getDateHired, "MMM dd, yyyy"))
                       .setHeader("Date Hired")
                       .setSortable(true);
        employeeDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        employeeDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                         GridVariant.LUMO_COLUMN_BORDERS,
                                         GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeeDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeeDTOGrid.setAllRowsVisible(true);
        employeeDTOGrid.setEmptyStateText("No employee records found.");
        employeeDTOGrid.setItems((query -> employeeService.getAll(query.getPage(), query.getPageSize()).stream()));

        return employeeDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewEmployeeButton = new Button();
        viewEmployeeButton.setTooltipText("View Employee");
        viewEmployeeButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewEmployeeButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewEmployeeButton.addClickListener(buttonClickEvent -> viewEmployeeButton.getUI().ifPresent(ui -> {
            if (employeeDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeDTO selectedEmployeeDTO = employeeDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeeDetailsView.class, selectedEmployeeDTO.getId().toString());
            }
        }));

        Button editEmployeeButton = new Button();
        editEmployeeButton.setTooltipText("Edit Employee");
        editEmployeeButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editEmployeeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editEmployeeButton.addClickListener(buttonClickEvent -> editEmployeeButton.getUI().ifPresent(ui -> {
            if (employeeDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeDTO selectedEmployeeDTO = employeeDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeeFormView.class, selectedEmployeeDTO.getId().toString());
            }
        }));

        Button documentEmployeeButton = new Button();
        documentEmployeeButton.setTooltipText("Upload Documents");
        documentEmployeeButton.setIcon(LineAwesomeIcon.FOLDER_OPEN_SOLID.create());
        documentEmployeeButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
        documentEmployeeButton.addClickListener(buttonClickEvent -> documentEmployeeButton.getUI().ifPresent(ui -> {
            if (employeeDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeDTO selectedEmployeeDTO = employeeDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeeDocumentFormView.class, selectedEmployeeDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewEmployeeButton, editEmployeeButton, documentEmployeeButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateEmployeeDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            employeeDTOGrid.setItems(employeeService.findByParameter(searchFilterTextField.getValue()));
        } else {
            employeeDTOGrid.setItems(query -> employeeService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
