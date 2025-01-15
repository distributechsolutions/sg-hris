package com.whizservices.hris.views.reference;

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

import com.whizservices.hris.dtos.reference.CalendarHolidaysDTO;
import com.whizservices.hris.services.reference.CalendarHolidaysService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIcon;

import java.time.format.DateTimeFormatter;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Calendar Holiday List")
@Route(value = "calendar-holiday-list", layout = MainLayout.class)
public class CalendarHolidaysListView extends VerticalLayout {
    @Resource
    private final CalendarHolidaysService calendarHolidaysService;

    private TextField searchFilterTextField;
    private Grid<CalendarHolidaysDTO> calendarHolidaysDTOGrid;

    public CalendarHolidaysListView(CalendarHolidaysService calendarHolidaysService) {
        this.calendarHolidaysService = calendarHolidaysService;

        this.add(buildHeaderToolbar(), buildCalendarHolidaysDTOGrid());
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
        searchFilterTextField.addValueChangeListener(valueChangeEvent -> this.updateCalendarHolidaysDTOGrid());

        Button addHolidayButton = new Button("Add Holiday");
        addHolidayButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addHolidayButton.addClickListener(buttonClickEvent -> addHolidayButton.getUI().ifPresent(ui -> ui.navigate(CalendarHolidaysFormView.class)));

        headerToolbarLayout.add(searchFilterTextField, addHolidayButton);
        headerToolbarLayout.setAlignItems(Alignment.CENTER);
        headerToolbarLayout.getThemeList().clear();

        return headerToolbarLayout;
    }

    private Grid<CalendarHolidaysDTO> buildCalendarHolidaysDTOGrid() {
        calendarHolidaysDTOGrid = new Grid<>(CalendarHolidaysDTO.class, false);

        calendarHolidaysDTOGrid.addColumn(CalendarHolidaysDTO::getHolidayType).setHeader("Type").setSortable(true);
        calendarHolidaysDTOGrid.addColumn(CalendarHolidaysDTO::getHolidayDescription).setHeader("Description").setSortable(true);
        calendarHolidaysDTOGrid.addColumn(CalendarHolidaysDTO::getHolidayYear).setHeader("Year").setSortable(true);
        calendarHolidaysDTOGrid.addColumn(calendarHolidaysDTO -> DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(calendarHolidaysDTO.getHolidayDate())).setHeader("Date").setSortable(true);
        calendarHolidaysDTOGrid.addComponentColumn(userDTO -> buildRowToolbar()).setHeader("Action");
        calendarHolidaysDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                 GridVariant.LUMO_COLUMN_BORDERS,
                                                 GridVariant.LUMO_WRAP_CELL_CONTENT);
        calendarHolidaysDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        calendarHolidaysDTOGrid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        calendarHolidaysDTOGrid.setAllRowsVisible(true);
        calendarHolidaysDTOGrid.setEmptyStateText("No position records found.");
        calendarHolidaysDTOGrid.setItems((query -> calendarHolidaysService.getAll(query.getPage(), query.getPageSize()).stream()));

        return calendarHolidaysDTOGrid;
    }

    public Component buildRowToolbar() {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Holiday");
        viewButton.setIcon(LineAwesomeIcon.SEARCH_SOLID.create());
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> viewButton.getUI().ifPresent(ui -> {
            if (calendarHolidaysDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                CalendarHolidaysDTO selectedCalendarHolidaysDTO = calendarHolidaysDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(CalendarHolidaysDetailsView.class, selectedCalendarHolidaysDTO.getId().toString());
            }
        }));

        Button editButton = new Button();
        editButton.setTooltipText("Edit Holiday");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        editButton.addClickListener(buttonClickEvent -> editButton.getUI().ifPresent(ui -> {
            if (calendarHolidaysDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                CalendarHolidaysDTO selectedCalendarHolidaysDTO = calendarHolidaysDTOGrid.getSelectionModel().getFirstSelectedItem().get();
                ui.navigate(CalendarHolidaysFormView.class, selectedCalendarHolidaysDTO.getId().toString());
            }
        }));

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void updateCalendarHolidaysDTOGrid() {
        if (searchFilterTextField.getValue() != null || searchFilterTextField.getValue().isBlank()) {
            calendarHolidaysDTOGrid.setItems(calendarHolidaysService.findByParameter(searchFilterTextField.getValue()));
        } else {
            calendarHolidaysDTOGrid.setItems(query -> calendarHolidaysService.getAll(query.getPage(), query.getPageSize()).stream());
        }
    }
}
