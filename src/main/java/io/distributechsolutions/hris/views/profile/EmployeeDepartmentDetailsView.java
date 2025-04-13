package io.distributechsolutions.hris.views.profile;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.distributechsolutions.hris.dtos.profile.EmployeeDepartmentDTO;
import io.distributechsolutions.hris.services.profile.EmployeeDepartmentService;
import io.distributechsolutions.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Department Details")
@Route(value = "employee-department-details", layout = MainLayout.class)
public class EmployeeDepartmentDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final EmployeeDepartmentService employeeDepartmentService;
    private EmployeeDepartmentDTO employeeDepartmentDTO;

    private final FormLayout employeeDepartmentDetailsLayout = new FormLayout();

    public EmployeeDepartmentDetailsView(EmployeeDepartmentService employeeDepartmentService) {
        this.employeeDepartmentService = employeeDepartmentService;

        add(employeeDepartmentDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, employeeDepartmentDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            employeeDepartmentDTO = employeeDepartmentService.getById(parameterId);
        }

        buildEmployeeDepartmentDetailsLayout();
    }

    public void buildEmployeeDepartmentDetailsLayout() {
        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(employeeDepartmentDTO.getEmployeeDTO().getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span employeeNameLabelSpan = new Span("Employee Name");
        employeeNameLabelSpan.getStyle().set("text-align", "right");

        String employeeName = employeeDepartmentDTO.getEmployeeDTO().getFirstName().concat(" ")
                .concat(employeeDepartmentDTO.getEmployeeDTO().getMiddleName())
                .concat(" ")
                .concat(employeeDepartmentDTO.getEmployeeDTO().getLastName())
                .concat(employeeDepartmentDTO.getEmployeeDTO().getSuffix() != null ? " ".concat(employeeDepartmentDTO.getEmployeeDTO().getSuffix()) : "");

        Span employeeNameValueSpan = new Span(employeeName);
        employeeNameValueSpan.getStyle().setFontWeight("bold");

        Span departmentLabelSpan = new Span("Department");
        departmentLabelSpan.getStyle().set("text-align", "right");

        Span departmentValueSpan = new Span(employeeDepartmentDTO.getDepartmentDTO().getName());
        departmentValueSpan.getStyle().setFontWeight("bold");

        Span isCurrentDepartmentLabelSpan = new Span("Is Current Department?");
        isCurrentDepartmentLabelSpan.getStyle().set("text-align", "right");

        Span isCurrentDepartmentValueSpan = new Span(employeeDepartmentDTO.isCurrentDepartment() ? "Yes" : "No");
        isCurrentDepartmentValueSpan.getStyle().setFontWeight("bold");

        employeeDepartmentDetailsLayout.add(employeeNoLabelSpan,
                employeeNoValueSpan,
                employeeNameLabelSpan,
                employeeNameValueSpan,
                departmentLabelSpan,
                departmentValueSpan,
                isCurrentDepartmentLabelSpan,
                isCurrentDepartmentValueSpan);
        employeeDepartmentDetailsLayout.setWidth("720px");
    }
}
