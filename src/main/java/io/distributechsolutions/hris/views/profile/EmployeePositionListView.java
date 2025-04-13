package io.distributechsolutions.hris.views.profile;

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

import io.distributechsolutions.hris.dtos.profile.EmployeePositionDTO;
import io.distributechsolutions.hris.services.profile.EmployeePositionService;
import io.distributechsolutions.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Position List")
@Route(value = "employee-position-list", layout = MainLayout.class)
public class EmployeePositionListView extends VerticalLayout {
    @Resource private final EmployeePositionService employeePositionService;

    private Grid<EmployeePositionDTO> employeePositionDTOGrid;
    private TextField searchFilterTextField;

    public EmployeePositionListView(EmployeePositionService employeePositionService) {
        this.employeePositionService = employeePositionService;

        this.add(buildHeaderToolbar(), buildEmployeePositionDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateEmployeePositionDTOGrid());

        Button addButton = new Button("Add Employee Position");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(EmployeePositionFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<EmployeePositionDTO> buildEmployeePositionDTOGrid() {
        employeePositionDTOGrid = new Grid<>(EmployeePositionDTO.class, false);

        employeePositionDTOGrid.addColumn(employeePositionDTO -> employeePositionDTO.getEmployeeDTO().getEmployeeNumber())
                .setHeader("Employee No.")
                .setSortable(true);
        employeePositionDTOGrid.addColumn(employeePositionDTO -> employeePositionDTO.getEmployeeDTO().getFirstName().concat(" ")
                        .concat(employeePositionDTO.getEmployeeDTO().getMiddleName())
                        .concat(" ")
                        .concat(employeePositionDTO.getEmployeeDTO().getLastName())
                        .concat(employeePositionDTO.getEmployeeDTO().getSuffix() != null ? employeePositionDTO.getEmployeeDTO().getSuffix() : ""))
                .setHeader("Employee Name")
                .setSortable(true);
        employeePositionDTOGrid.addColumn(employeePositionDTO -> employeePositionDTO.getPositionDTO().getName())
                .setHeader("Position")
                .setSortable(true);
        employeePositionDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, employeePositionDTO) -> {
            String theme = String.format("badge %s", employeePositionDTO.isCurrentPosition() ? "success" : "error");

            Span activeSpan = new Span();
            activeSpan.getElement().setAttribute("theme", theme);
            activeSpan.setText(employeePositionDTO.isCurrentPosition() ? "Yes" : "No");

            layout.setJustifyContentMode(JustifyContentMode.CENTER);
            layout.add(activeSpan);
        })).setHeader("Is Current Position?").setSortable(true);
        employeePositionDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        employeePositionDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                 GridVariant.LUMO_COLUMN_BORDERS,
                                                 GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeePositionDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeePositionDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeePositionDTOGrid.setAllRowsVisible(true);
        employeePositionDTOGrid.setEmptyStateText("No employee positions found.");
        employeePositionDTOGrid.setItems((query -> employeePositionService.getAll(query.getPage(), query.getPageSize()).stream()));

        return employeePositionDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Employee Position");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeePositionDTO selectedEmployeePositionDTO = employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeePositionDetailsView.class, selectedEmployeePositionDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Employee Position");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeePositionDTO selectedEmployeePositionDTO = employeePositionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeePositionFormView.class, selectedEmployeePositionDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateEmployeePositionDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            employeePositionDTOGrid.setItems(employeePositionService.findByParameter(searchFilterTextField.getValue()));
        } else {
            employeePositionDTOGrid.setItems(query -> employeePositionService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
