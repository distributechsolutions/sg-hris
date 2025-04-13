package io.distributechsolutions.hris.views.reference;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import io.distributechsolutions.hris.dtos.reference.PositionDTO;
import io.distributechsolutions.hris.services.reference.PositionService;
import io.distributechsolutions.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN", "ROLE_HR_MANAGER", "ROLE_HR_SUPERVISOR"})
@PageTitle("Position Details")
@Route(value = "position-details", layout = MainLayout.class)
public class PositionDetailsView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final PositionService positionService;
    private PositionDTO positionDTO;

    private final FormLayout positionDetailsLayout = new FormLayout();

    public PositionDetailsView(PositionService positionService) {
        this.positionService = positionService;

        add(positionDetailsLayout);

        setSizeFull();
        setMargin(true);
        setAlignItems(Alignment.CENTER);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, positionDetailsLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            positionDTO = positionService.getById(parameterId);
        }

        buildPositionDetailsLayout();
    }

    public void buildPositionDetailsLayout() {
        // To display the local date and time format.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");

        Span positionCodeLabelSpan = new Span("Code");
        positionCodeLabelSpan.getStyle().set("text-align", "right");

        Span positionCodeValueSpan = new Span(positionDTO.getCode());
        positionCodeValueSpan.getStyle().setFontWeight("bold");

        Span positionNameLabelSpan = new Span("Name");
        positionNameLabelSpan.getStyle().set("text-align", "right");

        Span positionNameValueSpan = new Span(positionDTO.getName());
        positionNameValueSpan.getStyle().setFontWeight("bold");

        Span createdByLabelSpan = new Span("Created by");
        createdByLabelSpan.getStyle().set("text-align", "right");

        Span createdByValueSpan = new Span(positionDTO.getCreatedBy());
        createdByValueSpan.getStyle().setFontWeight("bold");

        Span dateCreatedLabelSpan = new Span("Date created");
        dateCreatedLabelSpan.getStyle().set("text-align", "right");

        Span dateCreatedValueSpan = new Span(dateTimeFormatter.format(positionDTO.getDateAndTimeCreated()));
        dateCreatedValueSpan.getStyle().setFontWeight("bold");

        Span updatedByLabelSpan = new Span("Updated by");
        updatedByLabelSpan.getStyle().set("text-align", "right");

        Span updatedByValueSpan = new Span(positionDTO.getUpdatedBy());
        updatedByValueSpan.getStyle().setFontWeight("bold");

        Span dateUpdatedLabelSpan = new Span("Date updated");
        dateUpdatedLabelSpan.getStyle().set("text-align", "right");

        Span dateUpdatedValueSpan = new Span(dateTimeFormatter.format(positionDTO.getDateAndTimeUpdated()));
        dateUpdatedValueSpan.getStyle().setFontWeight("bold");

        positionDetailsLayout.add(positionCodeLabelSpan,
                positionCodeValueSpan,
                positionNameLabelSpan,
                positionNameValueSpan,
                createdByLabelSpan,
                createdByValueSpan,
                dateCreatedLabelSpan,
                dateCreatedValueSpan,
                updatedByLabelSpan,
                updatedByValueSpan,
                dateUpdatedLabelSpan,
                dateUpdatedValueSpan);
        positionDetailsLayout.setWidth("720px");
    }
}
