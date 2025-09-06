package io.softwaregarage.hris.attendance.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.attendance.dtos.EmployeeShiftScheduleDTO;
import io.softwaregarage.hris.attendance.services.EmployeeShiftScheduleService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Employee Shift Details")
@Route(value = "employee-shift-details", layout = MainLayout.class)
public class EmployeeShiftDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final EmployeeShiftScheduleService employeeShiftScheduleService;

    private EmployeeShiftScheduleDTO employeeShiftScheduleDTO;

    private FormLayout employeeShiftDetailsLayout = new FormLayout();

    public EmployeeShiftDetailsView(EmployeeShiftScheduleService employeeShiftScheduleService) {
        this.employeeShiftScheduleService = employeeShiftScheduleService;

        add(employeeShiftDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, employeeShiftDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            employeeShiftScheduleDTO = employeeShiftScheduleService.getById(parameterId);
        }

        buildEmployeeShiftDetailsLayout();
    }

    public void buildEmployeeShiftDetailsLayout() {
        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(employeeShiftScheduleDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        Span employeeNameValueSpan = new Span(employeeShiftScheduleDTO.getEmployeeDTO().getEmployeeFullName());
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span shiftScheduleLabelSpan = new Span("Shift Schedule");
        shiftScheduleLabelSpan.getStyle().set("text-align", "right");

        Span shiftScheduleValueSpan = new Span(employeeShiftScheduleDTO.getShiftSchedule());
        shiftScheduleValueSpan.getStyle().setFontWeight("bold");

        Span noOfHoursLabelSpan = new Span("No. of Hours");
        noOfHoursLabelSpan.getStyle().set("text-align", "right");

        Span noOfHoursValueSpan = new Span(String.valueOf(employeeShiftScheduleDTO.getShiftHours()).concat(" hrs"));
        noOfHoursValueSpan.getStyle().setFontWeight("bold");

        Span startTimeLabelSpan = new Span("Start Time");
        startTimeLabelSpan.getStyle().set("text-align", "right");

        Span startTimeValueSpan = new Span(employeeShiftScheduleDTO.getShiftStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        startTimeValueSpan.getStyle().setFontWeight("bold");

        Span endTimeLabelSpan = new Span("End Time");
        endTimeLabelSpan.getStyle().set("text-align", "right");

        Span endTimeValueSpan = new Span(employeeShiftScheduleDTO.getShiftEndTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        endTimeValueSpan.getStyle().setFontWeight("bold");

        Span shiftScheduledDaysLabelSpan = new Span("Scheduled Days");
        shiftScheduledDaysLabelSpan.getStyle().set("text-align", "right");

        Span shiftScheduledDaysValueSpan = new Span(employeeShiftScheduleDTO.getShiftScheduledDays());
        shiftScheduledDaysValueSpan.getStyle().setFontWeight("bold");

        Span activeShiftLabelSpan = new Span("Is Active Shift?");
        activeShiftLabelSpan.getStyle().set("text-align", "right");

        Span activeShiftValueSpan = new Span(employeeShiftScheduleDTO.isActiveShift() ? "Yes" : "No");
        activeShiftValueSpan.getStyle().setFontWeight("bold");

        employeeShiftDetailsLayout.add(employeeNoLabelSpan,
                                       employeeNoValueSpan,
                                       employeeNameLabelSpan,
                                       employeeNameValueSpan,
                                       shiftScheduleLabelSpan,
                                       shiftScheduleValueSpan,
                                       noOfHoursLabelSpan,
                                       noOfHoursValueSpan,
                                       startTimeLabelSpan,
                                       startTimeValueSpan,
                                       endTimeLabelSpan,
                                       endTimeValueSpan,
                                       shiftScheduledDaysLabelSpan,
                                       shiftScheduledDaysValueSpan,
                                       activeShiftLabelSpan,
                                       activeShiftValueSpan);
        employeeShiftDetailsLayout.setWidth("720px");
    }
}
