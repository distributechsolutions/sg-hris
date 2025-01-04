package com.whizservices.hris.views.admin;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.whizservices.hris.dtos.admin.DepartmentDTO;
import com.whizservices.hris.services.admin.DepartmentService;
import com.whizservices.hris.views.MainLayout;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_HR_SUPERVISOR"})
@PageTitle("Department Details")
@Route(value = "department-details", layout = MainLayout.class)
public class DepartmentDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final DepartmentService departmentService;
    private DepartmentDTO departmentDTO;

    private final FormLayout departmentDetailsLayout = new FormLayout();

    public DepartmentDetailsView(DepartmentService departmentService) {
        this.departmentService = departmentService;

        setSizeFull();
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        add(departmentDetailsLayout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            departmentDTO = departmentService.getById(parameterId);
        }

        buildDepartmentDetailsLayout();
    }

    public void buildDepartmentDetailsLayout() {
        // To display the local date and time format.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");

        Span departmentCodeLabelSpan = new Span("Code");
        departmentCodeLabelSpan.getStyle().set("text-align", "right");

        Span departmentCodeValueSpan = new Span(departmentDTO.getCode());
        departmentCodeValueSpan.getStyle().setFontWeight("bold");

        Span departmentNameLabelSpan = new Span("Name");
        departmentNameLabelSpan.getStyle().set("text-align", "right");

        Span departmentNameValueSpan = new Span(departmentDTO.getName());
        departmentNameValueSpan.getStyle().setFontWeight("bold");

        Span createdByLabelSpan = new Span("Created by");
        createdByLabelSpan.getStyle().set("text-align", "right");

        Span createdByValueSpan = new Span(departmentDTO.getCreatedBy());
        createdByValueSpan.getStyle().setFontWeight("bold");

        Span dateCreatedLabelSpan = new Span("Date created");
        dateCreatedLabelSpan.getStyle().set("text-align", "right");

        Span dateCreatedValueSpan = new Span(dateTimeFormatter.format(departmentDTO.getDateAndTimeCreated()));
        dateCreatedValueSpan.getStyle().setFontWeight("bold");

        Span updatedByLabelSpan = new Span("Updated by");
        updatedByLabelSpan.getStyle().set("text-align", "right");

        Span updatedByValueSpan = new Span(departmentDTO.getUpdatedBy());
        updatedByValueSpan.getStyle().setFontWeight("bold");

        Span dateUpdatedLabelSpan = new Span("Date updated");
        dateUpdatedLabelSpan.getStyle().set("text-align", "right");

        Span dateUpdatedValueSpan = new Span(dateTimeFormatter.format(departmentDTO.getDateAndTimeUpdated()));
        dateUpdatedValueSpan.getStyle().setFontWeight("bold");

        departmentDetailsLayout.add(departmentCodeLabelSpan,
                departmentCodeValueSpan,
                departmentNameLabelSpan,
                departmentNameValueSpan,
                createdByLabelSpan,
                createdByValueSpan,
                dateCreatedLabelSpan,
                dateCreatedValueSpan,
                updatedByLabelSpan,
                updatedByValueSpan,
                dateUpdatedLabelSpan,
                dateUpdatedValueSpan);
        departmentDetailsLayout.setWidth("720px");
    }
}
