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

import com.whizservices.hris.dtos.compenben.LoanDeductionDTO;
import com.whizservices.hris.services.compenben.LoanDeductionService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Loan Deduction List")
@Route(value = "loan-deduction-list", layout = MainLayout.class)
public class LoanDeductionListView extends VerticalLayout {
    @Resource
    private final LoanDeductionService loanDeductionService;

    private Grid<LoanDeductionDTO> loanDeductionDTOGrid;
    private TextField searchFilterTextField;

    public LoanDeductionListView(LoanDeductionService loanDeductionService) {
        this.loanDeductionService = loanDeductionService;

        this.add(buildHeaderToolbar(), buildLoanDeductionDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateLoanDeductionDTOGrid());

        Button addButton = new Button("Add Loan Deduction");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(buttonClickEvent -> addButton.getUI().ifPresent(ui -> ui.navigate(LoanDeductionFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<LoanDeductionDTO> buildLoanDeductionDTOGrid() {
        loanDeductionDTOGrid = new Grid<>(LoanDeductionDTO.class, false);

        loanDeductionDTOGrid.addColumn(loanDeductionDTO -> loanDeductionDTO.getEmployeeDTO().getEmployeeNumber())
                            .setHeader("Employee No.")
                            .setSortable(true);
        loanDeductionDTOGrid.addColumn(loanDeductionDTO -> loanDeductionDTO.getEmployeeDTO().getFirstName()
                                                                                                             .concat(" ")
                                                                                                             .concat(loanDeductionDTO.getEmployeeDTO().getLastName())
                                                                                                             .concat(loanDeductionDTO.getEmployeeDTO().getSuffix() != null ?
                                                                                                                     loanDeductionDTO.getEmployeeDTO().getSuffix() :
                                                                                                                     ""))
                            .setHeader("Employee Name")
                            .setSortable(true);
        loanDeductionDTOGrid.addColumn(LoanDeductionDTO::getLoanType)
                            .setHeader("Type")
                            .setSortable(true);
        loanDeductionDTOGrid.addColumn(LoanDeductionDTO::getLoanDescription)
                            .setHeader("Description")
                            .setSortable(true);
        loanDeductionDTOGrid.addColumn(loanDeductionDTO -> loanDeductionDTO.getLoanStartDate() + " to " + loanDeductionDTO.getLoanEndDate())
                            .setHeader("Date Duration")
                            .setSortable(true);
        loanDeductionDTOGrid.addColumn(loanDeductionDTO -> "PHP " + loanDeductionDTO.getLoanAmount())
                            .setHeader("Amount")
                            .setSortable(true);
        loanDeductionDTOGrid.addColumn(loanDeductionDTO -> "PHP " + loanDeductionDTO.getMonthlyDeduction())
                            .setHeader("Monthly Deduction")
                            .setSortable(true);
        loanDeductionDTOGrid.addComponentColumn(loanDeductionDTO -> buildRowToolbar()).setHeader("Action");
        loanDeductionDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                              GridVariant.LUMO_COLUMN_BORDERS,
                                              GridVariant.LUMO_WRAP_CELL_CONTENT);
        loanDeductionDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        loanDeductionDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        loanDeductionDTOGrid.setEmptyStateText("No benefit records found.");
        loanDeductionDTOGrid.setItems((query -> loanDeductionService.getAll(query.getPage(), query.getPageSize()).stream()));

        return loanDeductionDTOGrid;
    }

    public HorizontalLayout buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Loan Deduction");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (loanDeductionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                LoanDeductionDTO selectedLoanDeductionDTO = loanDeductionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(LoanDeductionDetailsView.class, selectedLoanDeductionDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Loan Deduction");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (loanDeductionDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                LoanDeductionDTO selectedLoanDeductionDTO = loanDeductionDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(LoanDeductionFormView.class, selectedLoanDeductionDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateLoanDeductionDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            loanDeductionDTOGrid.setItems(loanDeductionService.findByParameter(searchFilterTextField.getValue()));
        } else {
            loanDeductionDTOGrid.setItems(query -> loanDeductionService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
