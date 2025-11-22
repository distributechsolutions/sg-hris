package io.softwaregarage.hris.admin.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.admin.dtos.GroupDTO;
import io.softwaregarage.hris.admin.services.GroupService;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN"})
@PageTitle("Group Details")
@Route(value = "group-details", layout = MainLayout.class)
public class GroupDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final GroupService groupService;

    private GroupDTO groupDTO;

    private final FormLayout groupDetailsLayout = new FormLayout();

    public GroupDetailsView(GroupService groupService) {
        this.groupService = groupService;

        add(groupDetailsLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, groupDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            groupDTO = groupService.getById(parameterId);
        }

        buildGroupDetailsLayout();
    }

    public void buildGroupDetailsLayout() {
        // To display the local date and time format.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");

        Span groupCodeLabelSpan = new Span("Code");
        groupCodeLabelSpan.getStyle().set("text-align", "right");

        Span groupCodeValueSpan = new Span(groupDTO.getCode());
        groupCodeValueSpan.getStyle().setFontWeight("bold");

        Span groupNameLabelSpan = new Span("Name");
        groupNameLabelSpan.getStyle().set("text-align", "right");

        Span groupNameValueSpan = new Span(groupDTO.getName());
        groupNameValueSpan.getStyle().setFontWeight("bold");

        Span groupDescriptionLabelSpan = new Span("Description");
        groupDescriptionLabelSpan.getStyle().set("text-align", "right");

        Span groupDescriptionValueSpan = new Span(groupDTO.getDescription());
        groupDescriptionValueSpan.getStyle().setFontWeight("bold");

        Span createdByLabelSpan = new Span("Created by");
        createdByLabelSpan.getStyle().set("text-align", "right");

        Span createdByValueSpan = new Span(groupDTO.getCreatedBy());
        createdByValueSpan.getStyle().setFontWeight("bold");

        Span dateCreatedLabelSpan = new Span("Date created");
        dateCreatedLabelSpan.getStyle().set("text-align", "right");

        Span dateCreatedValueSpan = new Span(dateTimeFormatter.format(groupDTO.getDateAndTimeCreated()));
        dateCreatedValueSpan.getStyle().setFontWeight("bold");

        Span updatedByLabelSpan = new Span("Updated by");
        updatedByLabelSpan.getStyle().set("text-align", "right");

        Span updatedByValueSpan = new Span(groupDTO.getUpdatedBy());
        updatedByValueSpan.getStyle().setFontWeight("bold");

        Span dateUpdatedLabelSpan = new Span("Date updated");
        dateUpdatedLabelSpan.getStyle().set("text-align", "right");

        Span dateUpdatedValueSpan = new Span(dateTimeFormatter.format(groupDTO.getDateAndTimeUpdated()));
        dateUpdatedValueSpan.getStyle().setFontWeight("bold");

        groupDetailsLayout.add(groupCodeLabelSpan,
                groupCodeValueSpan,
                groupNameLabelSpan,
                groupNameValueSpan,
                groupDescriptionLabelSpan,
                groupDescriptionValueSpan,
                createdByLabelSpan,
                createdByValueSpan,
                dateCreatedLabelSpan,
                dateCreatedValueSpan,
                updatedByLabelSpan,
                updatedByValueSpan,
                dateUpdatedLabelSpan,
                dateUpdatedValueSpan);
        groupDetailsLayout.setWidth("720px");
    }
}
