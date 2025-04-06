package com.whizservices.hris.views.reference;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.whizservices.hris.dtos.reference.CalendarHolidaysDTO;
import com.whizservices.hris.services.reference.CalendarHolidaysService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Calendar Holiday Details")
@Route(value = "calendar-holiday-details", layout = MainLayout.class)
public class CalendarHolidaysDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final CalendarHolidaysService calendarHolidaysService;
    private CalendarHolidaysDTO calendarHolidaysDTO;

    private final FormLayout calendarHolidaysDetailsLayout = new FormLayout();

    public CalendarHolidaysDetailsView(CalendarHolidaysService calendarHolidaysService) {
        this.calendarHolidaysService = calendarHolidaysService;

        add(calendarHolidaysDetailsLayout);

        setSizeFull();
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, calendarHolidaysDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        if (s != null) {
            UUID parameterId = UUID.fromString(s);
            calendarHolidaysDTO = calendarHolidaysService.getById(parameterId);
        }

        buildCalendarHolidayDetailsLayout();
    }

    public void buildCalendarHolidayDetailsLayout() {
        // To display the local date and time format.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

        Span holidayTypeLabelSpan = new Span("Holiday Type");
        holidayTypeLabelSpan.getStyle().set("text-align", "right");

        Span holidayTypeValueSpan = new Span(calendarHolidaysDTO.getHolidayType());
        holidayTypeValueSpan.getStyle().setFontWeight("bold");

        Span holidayDescriptionLabelSpan = new Span("Holiday Description");
        holidayDescriptionLabelSpan.getStyle().set("text-align", "right");

        Span holidayDescriptionValueSpan = new Span(calendarHolidaysDTO.getHolidayDescription());
        holidayDescriptionValueSpan.getStyle().setFontWeight("bold");

        Span holidayYearLabelSpan = new Span("Holiday Year");
        holidayYearLabelSpan.getStyle().set("text-align", "right");

        Span holidayYearValueSpan = new Span(String.valueOf(calendarHolidaysDTO.getHolidayYear()));
        holidayYearValueSpan.getStyle().setFontWeight("bold");

        Span holidayDateLabelSpan = new Span("Holiday Date");
        holidayDateLabelSpan.getStyle().set("text-align", "right");

        Span holidayDateValueSpan = new Span(dateFormatter.format(calendarHolidaysDTO.getHolidayDate()));
        holidayDateValueSpan.getStyle().setFontWeight("bold");

        Span createdByLabelSpan = new Span("Created by");
        createdByLabelSpan.getStyle().set("text-align", "right");

        Span createdByValueSpan = new Span(calendarHolidaysDTO.getCreatedBy());
        createdByValueSpan.getStyle().setFontWeight("bold");

        Span dateCreatedLabelSpan = new Span("Date created");
        dateCreatedLabelSpan.getStyle().set("text-align", "right");

        Span dateCreatedValueSpan = new Span(dateTimeFormatter.format(calendarHolidaysDTO.getDateAndTimeCreated()));
        dateCreatedValueSpan.getStyle().setFontWeight("bold");

        Span updatedByLabelSpan = new Span("Updated by");
        updatedByLabelSpan.getStyle().set("text-align", "right");

        Span updatedByValueSpan = new Span(calendarHolidaysDTO.getUpdatedBy());
        updatedByValueSpan.getStyle().setFontWeight("bold");

        Span dateUpdatedLabelSpan = new Span("Date updated");
        dateUpdatedLabelSpan.getStyle().set("text-align", "right");

        Span dateUpdatedValueSpan = new Span(dateTimeFormatter.format(calendarHolidaysDTO.getDateAndTimeUpdated()));
        dateUpdatedValueSpan.getStyle().setFontWeight("bold");

        calendarHolidaysDetailsLayout.add(holidayTypeLabelSpan,
                                          holidayTypeValueSpan,
                                          holidayDescriptionLabelSpan,
                                          holidayDescriptionValueSpan,
                                          holidayYearLabelSpan,
                                          holidayYearValueSpan,
                                          holidayDateLabelSpan,
                                          holidayDateValueSpan,
                                          createdByLabelSpan,
                                          createdByValueSpan,
                                          dateCreatedLabelSpan,
                                          dateCreatedValueSpan,
                                          updatedByLabelSpan,
                                          updatedByValueSpan,
                                          dateUpdatedLabelSpan,
                                          dateUpdatedValueSpan);
        calendarHolidaysDetailsLayout.setWidth("720px");
    }
}
