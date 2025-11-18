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
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employees")
@Route(value = "employee-list", layout = MainLayout.class)
public class EmployeeProfileListView extends VerticalLayout {
    @Resource
    private final EmployeeProfileService employeeProfileService;

    private Grid<EmployeeProfileDTO> employeeDTOGrid;
    private TextField searchFilterTextField;

    public EmployeeProfileListView(EmployeeProfileService employeeProfileService) {
        this.employeeProfileService = employeeProfileService;

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
        addEmployeeButton.addClickListener(buttonClickEvent -> addEmployeeButton.getUI().ifPresent(ui -> ui.navigate(EmployeeProfileFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addEmployeeButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<EmployeeProfileDTO> buildEmployeeDTOGrid() {
        employeeDTOGrid = new Grid<>(EmployeeProfileDTO.class, false);

        employeeDTOGrid.addColumn(EmployeeProfileDTO::getEmployeeNumber)
                       .setHeader("Employee No.")
                       .setSortable(true);
        employeeDTOGrid.addColumn(employeeDTO -> employeeDTO.getEmployeeFullName())
                       .setHeader("Employee Name")
                       .setSortable(true);
        employeeDTOGrid.addColumn(new LocalDateRenderer<>(EmployeeProfileDTO::getDateHired, "MMM dd, yyyy"))
                       .setHeader("Date Hired")
                       .setSortable(true);
        employeeDTOGrid.addColumn(new LocalDateRenderer<>(EmployeeProfileDTO::getStartDate, "MMM dd, yyyy"))
                       .setHeader("Start Date")
                       .setSortable(true);
        employeeDTOGrid.addColumn(new LocalDateRenderer<>(EmployeeProfileDTO::getDateResigned, "MMM dd, yyyy"))
                        .setHeader("Date Resigned")
                        .setSortable(true);
        employeeDTOGrid.addColumn(new LocalDateRenderer<>(EmployeeProfileDTO::getEndDate, "MMM dd, yyyy"))
                       .setHeader("End Date")
                       .setSortable(true);
        employeeDTOGrid.addColumn(EmployeeProfileDTO::getEmploymentType)
                       .setHeader("Employment Type")
                       .setSortable(true);
        employeeDTOGrid.addColumn(EmployeeProfileDTO::getContractDuration)
                       .setHeader("Contract Duration")
                       .setSortable(true);
        employeeDTOGrid.addColumn(new ComponentRenderer<>(HorizontalLayout::new, (layout, employeeProfileDTO) -> {
                                      String theme;

                                      switch (employeeProfileDTO.getStatus()) {
                                          case "ONBOARDING":
                                              theme = String.format("badge %s", "primary");
                                              break;
                                          case "ACTIVE":
                                              theme = String.format("badge %s", "success primary");
                                              break;
                                          case "ON LEAVE":
                                              theme = String.format("badge %s", "success");
                                              break;
                                          case "SUSPENDED":
                                              theme = String.format("badge %s", "badge warning primary");
                                              break;
                                          case "TERMINATED":
                                              theme = String.format("badge %s", "badge error primary");
                                              break;
                                          case "RETIRED":
                                              theme = String.format("badge %s", "badge contrast");
                                              break;
                                          case "DECEASED":
                                              theme = String.format("badge %s", "badge contrast primary");
                                              break;
                                          default:
                                              theme = "badge";
                                      }

                                      Span activeSpan = new Span();
                                      activeSpan.getElement().setAttribute("theme", theme);
                                      activeSpan.setText(employeeProfileDTO.getStatus());

                                      layout.setJustifyContentMode(JustifyContentMode.CENTER);
                                      layout.add(activeSpan);
                                  }))
                       .setHeader("Status")
                       .setSortable(true);
        employeeDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        employeeDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                         GridVariant.LUMO_COLUMN_BORDERS,
                                         GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeeDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        employeeDTOGrid.setAllRowsVisible(true);
        employeeDTOGrid.setEmptyStateText("No employee records found.");
        employeeDTOGrid.setItems((query -> employeeProfileService.getAll(query.getPage(), query.getPageSize()).stream()));

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
                EmployeeProfileDTO selectedEmployeeProfileDTO = employeeDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeeProfileDetailsView.class, selectedEmployeeProfileDTO.getId().toString());
            }
        }));

        Button editEmployeeButton = new Button();
        editEmployeeButton.setTooltipText("Edit Employee");
        editEmployeeButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editEmployeeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editEmployeeButton.addClickListener(buttonClickEvent -> editEmployeeButton.getUI().ifPresent(ui -> {
            if (employeeDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeProfileDTO selectedEmployeeProfileDTO = employeeDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(EmployeeProfileFormView.class, selectedEmployeeProfileDTO.getId().toString());
            }
        }));

        Button documentEmployeeButton = new Button();
        documentEmployeeButton.setTooltipText("Upload Documents");
        documentEmployeeButton.setIcon(LineAwesomeIcon.FOLDER_OPEN_SOLID.create());
        documentEmployeeButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
        documentEmployeeButton.addClickListener(buttonClickEvent -> documentEmployeeButton.getUI().ifPresent(ui -> {
            if (employeeDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeProfileDTO selectedEmployeeProfileDTO = employeeDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(DocumentProfileFormView.class, selectedEmployeeProfileDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewEmployeeButton, editEmployeeButton, documentEmployeeButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateEmployeeDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            employeeDTOGrid.setItems(employeeProfileService.findByParameter(searchFilterTextField.getValue()));
        } else {
            employeeDTOGrid.setItems(query -> employeeProfileService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
