package io.softwaregarage.hris.compenben.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.compenben.dtos.GovernmentContributionsDTO;
import io.softwaregarage.hris.compenben.services.GovernmentContributionsService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Contributions")
@Route(value = "government-contributions-list", layout = MainLayout.class)
public class GovernmentContributionsListView extends VerticalLayout {
    @Resource
    private final GovernmentContributionsService governmentContributionsService;

    private Grid<GovernmentContributionsDTO> governmentContributionsDTOGrid;
    private TextField searchFilterTextField;

    public GovernmentContributionsListView(GovernmentContributionsService governmentContributionsService) {
        this.governmentContributionsService = governmentContributionsService;

        this.add(buildHeaderToolbar(), buildGovernmentContributionsDTOGrid());
        this.setSizeFull();
        this.setAlignItems(FlexComponent.Alignment.STRETCH);
    }

    public HorizontalLayout buildHeaderToolbar() {
        HorizontalLayout headerToolbarLayout = new HorizontalLayout();

        searchFilterTextField = new TextField();
        searchFilterTextField.setWidth("350px");
        searchFilterTextField.setPlaceholder("Search");
        searchFilterTextField.setPrefixComponent(LineAwesomeIcon.SEARCH_SOLID.create());
        searchFilterTextField.getStyle().set("margin", "0 auto 0 0");
        searchFilterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateGovernmentContributionsDTOGrid());

        Button addButton = new Button("Add Contributions");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(GovernmentContributionsFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<GovernmentContributionsDTO> buildGovernmentContributionsDTOGrid() {
        governmentContributionsDTOGrid = new Grid<>(GovernmentContributionsDTO.class, false);

        governmentContributionsDTOGrid.addColumn(governmentContributionsDTO -> governmentContributionsDTO.getEmployeeDTO().getEmployeeNumber())
                                      .setHeader("Employee No.")
                                      .setSortable(true);
        governmentContributionsDTOGrid.addColumn(governmentContributionsDTO -> governmentContributionsDTO.getEmployeeDTO().getFirstName()
                                                                                                                                                    .concat(" ")
                                                                                                                                                    .concat(governmentContributionsDTO.getEmployeeDTO().getLastName())
                                                                                                                                                    .concat(governmentContributionsDTO.getEmployeeDTO().getSuffix() != null ?
                                                                                                                                                            governmentContributionsDTO.getEmployeeDTO().getSuffix() :
                                                                                                                                                            ""))
                                      .setHeader("Employee Name")
                                      .setSortable(true);
        governmentContributionsDTOGrid.addColumn(governmentContributionsDTO -> String.format("PHP %.2f", governmentContributionsDTO.getSssContributionAmount()))
                                      .setHeader("SSS Contribution Amount")
                                      .setSortable(true);
        governmentContributionsDTOGrid.addColumn(governmentContributionsDTO -> String.format("PHP %.2f", governmentContributionsDTO.getHdmfContributionAmount()))
                                      .setHeader("HDMF Contribution Amount")
                                      .setSortable(true);
        governmentContributionsDTOGrid.addColumn(governmentContributionsDTO -> String.format("PHP %.2f", governmentContributionsDTO.getPhilhealthContributionAmount()))
                                      .setHeader("Philhealth Contribution Amount")
                                      .setSortable(true);
        governmentContributionsDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        governmentContributionsDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                        GridVariant.LUMO_COLUMN_BORDERS,
                                                        GridVariant.LUMO_WRAP_CELL_CONTENT);
        governmentContributionsDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        governmentContributionsDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        governmentContributionsDTOGrid.setEmptyStateText("No benefit records found.");
        governmentContributionsDTOGrid.setItems((query -> governmentContributionsService.getAll(query.getPage(), query.getPageSize()).stream()));

        return governmentContributionsDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Leave Benefit");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (governmentContributionsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                GovernmentContributionsDTO selectedGovernmentContributionsDTO = governmentContributionsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(GovernmentContributionsDetailsView.class, selectedGovernmentContributionsDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Leave Benefit");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (governmentContributionsDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                GovernmentContributionsDTO selectedGovernmentContributionsDTO = governmentContributionsDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(GovernmentContributionsFormView.class, selectedGovernmentContributionsDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateGovernmentContributionsDTOGrid() {
        if (searchFilterTextField.getValue() != null || !searchFilterTextField.getValue().isBlank()) {
            governmentContributionsDTOGrid.setItems(governmentContributionsService.findByParameter(searchFilterTextField.getValue()));
        } else {
            governmentContributionsDTOGrid.setItems(query -> governmentContributionsService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
