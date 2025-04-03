package com.whizservices.hris.views.profile;

import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.*;

import com.vaadin.flow.server.StreamResource;
import com.whizservices.hris.dtos.info.AddressInfoDTO;
import com.whizservices.hris.dtos.info.DependentInfoDTO;
import com.whizservices.hris.dtos.info.PersonalInfoDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.dtos.profile.EmployeeDocumentDTO;
import com.whizservices.hris.services.info.AddressInfoService;
import com.whizservices.hris.services.info.DependentInfoService;
import com.whizservices.hris.services.info.PersonalInfoService;
import com.whizservices.hris.services.profile.EmployeeDocumentService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Details")
@Route(value = "employee-details", layout = MainLayout.class)
public class EmployeeDetailsView extends Div implements HasUrlParameter<String> {
    @Resource private final EmployeeService employeeService;
    @Resource private final PersonalInfoService personalInfoService;
    @Resource private final AddressInfoService addressInfoService;
    @Resource private final DependentInfoService dependentInfoService;
    @Resource private final EmployeeDocumentService employeeDocumentService;

    private EmployeeDTO employeeDTO;
    private PersonalInfoDTO personalInfoDTO;
    private List<AddressInfoDTO> addressInfoDTOList;
    private List<DependentInfoDTO> dependentInfoDTOList;
    private List<EmployeeDocumentDTO> employeeDocumentDTOList;

    private final FormLayout employeeDetailsLayout = new FormLayout();
    private final Div personalInfoDiv = new Div();
    private final Div addressInfoDiv = new Div();
    private final Div dependentInfoDiv = new Div();
    private final Div employeeDocumentDiv = new Div();

    private final Grid<AddressInfoDTO> addressInfoDTOGrid = new Grid<>(AddressInfoDTO.class, false);
    private final Grid<DependentInfoDTO> dependentInfoDTOGrid = new Grid<>(DependentInfoDTO.class, false);
    private final Grid<EmployeeDocumentDTO> employeeDocumentDTOGrid = new Grid<>(EmployeeDocumentDTO.class, false);

    private TabSheet employeeInformationTabSheets = new TabSheet();


    enum MessageLevel {
        INFO,
        SUCCESS,
        WARNING,
        DANGER
    }

    public EmployeeDetailsView(EmployeeService employeeService,
                               PersonalInfoService personalInfoService,
                               AddressInfoService addressInfoService,
                               DependentInfoService dependentInfoService,
                               EmployeeDocumentService employeeDocumentService) {
        this.employeeService = employeeService;
        this.personalInfoService = personalInfoService;
        this.addressInfoService = addressInfoService;
        this.dependentInfoService = dependentInfoService;
        this.employeeDocumentService = employeeDocumentService;

        setSizeFull();
        add(employeeDetailsLayout, employeeInformationTabSheets);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            employeeDTO = employeeService.getById(parameterId);
        }

        buildEmployeeDetailsLayout();
        buildEmployeeInformationTabSheets();
        buildPersonalInfoDiv();
        buildAddressInfoDiv();
        buildDependentInfoDiv();
        buildEmployeeDocumentDiv();
    }

    public void buildEmployeeDetailsLayout() {
        Span recordIdLabelSpan = new Span("Record ID");
        recordIdLabelSpan.getStyle().set("text-align", "right");

        Span recordIdValueSpan = new Span(employeeDTO.getId().toString());
        recordIdValueSpan.getStyle().setFontWeight("bold");

        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(employeeDTO.getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span fullNameLabelSpan = new Span("Full Name");
        fullNameLabelSpan.getStyle().set("text-align", "right");

        String fullName = employeeDTO.getFirstName().concat(" ")
                .concat(employeeDTO.getMiddleName())
                .concat(" ")
                .concat(employeeDTO.getLastName())
                .concat(employeeDTO.getSuffix() != null ? " ".concat(employeeDTO.getSuffix()) : "");

        Span fullNameValueSpan = new Span(fullName);
        fullNameValueSpan.getStyle().setFontWeight("bold");

        Span genderLabelSpan = new Span("Gender");
        genderLabelSpan.getStyle().set("text-align", "right");

        Span genderValueSpan = new Span(employeeDTO.getGender());
        genderValueSpan.getStyle().setFontWeight("bold");

        Span dateHiredLabelSpan = new Span("Date Hired");
        dateHiredLabelSpan.getStyle().set("text-align", "right");

        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        String dateHired = df.format(employeeDTO.getDateHired());

        Span dateHiredValueSpan = new Span(dateHired);
        dateHiredValueSpan.getStyle().setFontWeight("bold");

        employeeDetailsLayout.add(recordIdLabelSpan,
                                  recordIdValueSpan,
                                  employeeNoLabelSpan,
                                  employeeNoValueSpan,
                                  fullNameLabelSpan,
                                  fullNameValueSpan,
                                  genderLabelSpan,
                                  genderValueSpan,
                                  dateHiredLabelSpan,
                                  dateHiredValueSpan);
        employeeDetailsLayout.setWidth("768px");
    }

    private void buildEmployeeInformationTabSheets() {
        employeeInformationTabSheets.add("Personal", personalInfoDiv);
        employeeInformationTabSheets.add("Addresses", addressInfoDiv);
        employeeInformationTabSheets.add("Dependents", dependentInfoDiv);
        employeeInformationTabSheets.add("Documents", employeeDocumentDiv);
    }

    private void buildPersonalInfoDiv() {
        personalInfoDTO = personalInfoService.getByEmployeeDTO(employeeDTO);

        if (personalInfoDTO != null) {
            FormLayout personalInfoFormLayout = new FormLayout();

            Span dateOfBirthLabelSpan = new Span("Date of birth");
            dateOfBirthLabelSpan.getStyle().set("text-align", "right");

            Span dateOfBirthValueSpan = new Span(DateTimeFormatter.ofPattern("MMM dd, yyyy").format(personalInfoDTO.getDateOfBirth()));
            dateOfBirthValueSpan.getStyle().setFontWeight("bold");

            Span placeOfBirthLabelSpan = new Span("Place of birth");
            placeOfBirthLabelSpan.getStyle().set("text-align", "right");

            Span placeOfBirthValueSpan = new Span(personalInfoDTO.getPlaceOfBirth());
            placeOfBirthValueSpan.getStyle().setFontWeight("bold");

            Span maritalStatusLabelSpan = new Span("Marital Status");
            maritalStatusLabelSpan.getStyle().set("text-align", "right");

            Span maritalStatusValueSpan = new Span(personalInfoDTO.getMaritalStatus());
            maritalStatusValueSpan.getStyle().setFontWeight("bold");

            Span spouseLabelSpan = new Span("Spouse Name");
            spouseLabelSpan.getStyle().set("text-align", "right");

            Span spouseValueSpan = new Span(personalInfoDTO.getSpouseName() != null ? personalInfoDTO.getSpouseName() : "");
            spouseValueSpan.getStyle().setFontWeight("bold");

            Span contactNoLabelSpan = new Span("Contact Number");
            contactNoLabelSpan.getStyle().set("text-align", "right");

            Span contactNoValueSpan = new Span(String.valueOf(personalInfoDTO.getContactNumber()));
            contactNoValueSpan.getStyle().setFontWeight("bold");

            Span emailLabelSpan = new Span("Email");
            emailLabelSpan.getStyle().set("text-align", "right");

            Span emailValueSpan = new Span(personalInfoDTO.getEmailAddress());
            emailValueSpan.getStyle().setFontWeight("bold");

            Span tinLabelSpan = new Span("TIN");
            tinLabelSpan.getStyle().set("text-align", "right");

            Span tinValueSpan = new Span(personalInfoDTO.getTaxIdentificationNumber());
            tinValueSpan.getStyle().setFontWeight("bold");

            Span sssLabelSpan = new Span("SSS");
            sssLabelSpan.getStyle().set("text-align", "right");

            Span sssValueSpan = new Span(personalInfoDTO.getSssNumber());
            sssValueSpan.getStyle().setFontWeight("bold");

            Span hdmfLabelSpan = new Span("Pag-Ibig HDMF");
            hdmfLabelSpan.getStyle().set("text-align", "right");

            Span hdmfValueSpan = new Span(personalInfoDTO.getHdmfNumber());
            hdmfValueSpan.getStyle().setFontWeight("bold");

            Span philhealthLabelSpan = new Span("Philhealth");
            philhealthLabelSpan.getStyle().set("text-align", "right");

            Span philhealthValueSpan = new Span(personalInfoDTO.getPhilhealthNumber());
            philhealthValueSpan.getStyle().setFontWeight("bold");

            personalInfoFormLayout.add(dateOfBirthLabelSpan,
                                       dateOfBirthValueSpan,
                                       placeOfBirthLabelSpan,
                                       placeOfBirthValueSpan,
                                       maritalStatusLabelSpan,
                                       maritalStatusValueSpan,
                                       spouseLabelSpan,
                                       spouseValueSpan,
                                       contactNoLabelSpan,
                                       contactNoValueSpan,
                                       emailLabelSpan,
                                       emailValueSpan,
                                       tinLabelSpan,
                                       tinValueSpan,
                                       sssLabelSpan,
                                       sssValueSpan,
                                       hdmfLabelSpan,
                                       hdmfValueSpan,
                                       philhealthLabelSpan,
                                       philhealthValueSpan);
            personalInfoFormLayout.setWidth("768px");

            personalInfoDiv.add(personalInfoFormLayout);
        } else {
            Div profileMessageNotification = this.buildNotification("Employee has not yet filled up this information.",
                                                                    EmployeeDetailsView.MessageLevel.INFO,
                                                                    LineAwesomeIcon.INFO_CIRCLE_SOLID.create());

            personalInfoDiv.add(profileMessageNotification);
        }

        personalInfoDiv.getStyle().setPadding("10px");
    }

    private void buildAddressInfoDiv() {
        addressInfoDTOList = addressInfoService.getByEmployeeDTO(employeeDTO);

        if (!addressInfoDTOList.isEmpty()) {
            addressInfoDTOGrid.setItems(addressInfoDTOList);
            addressInfoDTOGrid.addColumn(AddressInfoDTO::getAddressType)
                              .setHeader("Address Type");
            addressInfoDTOGrid.addColumn(addressDTO -> addressDTO.getAddressDetail()
                                                                 .concat(" ")
                                                                 .concat(addressDTO.getStreetName())
                                                                 .concat(", ")
                                                                 .concat(addressDTO.getBarangayDTO().getBarangayDescription()))
                              .setHeader("Address Details");
            addressInfoDTOGrid.addColumn(addressInfoDTO -> addressInfoDTO.getMunicipalityDTO().getMunicipalityDescription())
                              .setHeader("Municipality");
            addressInfoDTOGrid.addColumn(addressInfoDTO -> addressInfoDTO.getProvinceDTO().getProvinceDescription())
                              .setHeader("Province");
            addressInfoDTOGrid.addColumn(addressInfoDTO -> addressInfoDTO.getRegionDTO().getRegionDescription())
                              .setHeader("Region");
            addressInfoDTOGrid.addColumn(AddressInfoDTO::getPostalCode)
                              .setHeader("Postal Code");
            addressInfoDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                GridVariant.LUMO_COLUMN_BORDERS,
                                                GridVariant.LUMO_WRAP_CELL_CONTENT);
            addressInfoDTOGrid.setAllRowsVisible(true);

            addressInfoDiv.add(addressInfoDTOGrid);
        } else {
            Div addressMessageNotification = this.buildNotification("Employee has not yet filled up this information.",
                                                                    EmployeeDetailsView.MessageLevel.INFO,
                                                                    LineAwesomeIcon.INFO_CIRCLE_SOLID.create());

            addressInfoDiv.add(addressMessageNotification);
        }

        addressInfoDiv.getStyle().setPadding("10px");
    }

    private void buildDependentInfoDiv() {
        dependentInfoDTOList = dependentInfoService.getByEmployeeDTO(employeeDTO);

        if (!dependentInfoDTOList.isEmpty()) {
            dependentInfoDTOGrid.setItems(dependentInfoDTOList);
            dependentInfoDTOGrid.addColumn(DependentInfoDTO::getFullName).setHeader("Name");
            dependentInfoDTOGrid.addColumn(new LocalDateRenderer<>(DependentInfoDTO::getDateOfBirth, "MMM dd, yyyy")).setHeader("Date of Birth");
            dependentInfoDTOGrid.addColumn(DependentInfoDTO::getAge).setHeader("Age");
            dependentInfoDTOGrid.addColumn(DependentInfoDTO::getRelationship).setHeader("Relationship");
            dependentInfoDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                  GridVariant.LUMO_COLUMN_BORDERS,
                                                  GridVariant.LUMO_WRAP_CELL_CONTENT);
            dependentInfoDTOGrid.setAllRowsVisible(true);

            dependentInfoDiv.add(dependentInfoDTOGrid);
        } else {
            Div dependentMessageNotification = this.buildNotification("Employee has not yet filled up this information.",
                    EmployeeDetailsView.MessageLevel.INFO,
                    LineAwesomeIcon.INFO_CIRCLE_SOLID.create());

            dependentInfoDiv.add(dependentMessageNotification);
        }

        dependentInfoDiv.getStyle().setPadding("10px");
    }

    private void buildEmployeeDocumentDiv() {
        employeeDocumentDTOList = employeeDocumentService.getByEmployeeDTO(employeeDTO);

        if (!employeeDocumentDTOList.isEmpty()) {
            employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getDocumentType).setHeader("Document Type");
            employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getFileName).setHeader("File Name");
            employeeDocumentDTOGrid.addColumn(new LocalDateRenderer<>(EmployeeDocumentDTO::getExpirationDate, "MMM dd, yyyy")).setHeader("Expiration Date");
            employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getRemarks).setHeader("Remarks");
            employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getFileType).setHeader("File Type");
            employeeDocumentDTOGrid.addComponentColumn(addressDTO -> this.buildEmployeeDocumentRowToolbar(addressDTO.getFileType().equals("application/pdf") ?
                                                                     LineAwesomeIcon.FILE_PDF.create() :
                                                                     LineAwesomeIcon.IMAGES.create()))
                                   .setHeader("Action");
            employeeDocumentDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                     GridVariant.LUMO_COLUMN_BORDERS,
                                                     GridVariant.LUMO_WRAP_CELL_CONTENT);
            employeeDocumentDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            employeeDocumentDTOGrid.setAllRowsVisible(true);
            employeeDocumentDTOGrid.setEmptyStateText("No documents found.");
            employeeDocumentDTOGrid.setItems(employeeDocumentDTOList);

            employeeDocumentDiv.add(employeeDocumentDTOGrid);
        } else {
            Div employeeDocumentMessageNotification = this.buildNotification("No documents found related to this employee.",
                                                                             EmployeeDetailsView.MessageLevel.INFO,
                                                                             LineAwesomeIcon.INFO_CIRCLE_SOLID.create());

            employeeDocumentDiv.add(employeeDocumentMessageNotification);
        }

        employeeDocumentDiv.getStyle().setPadding("10px");
    }

    private Component buildEmployeeDocumentRowToolbar(SvgIcon svgIcon) {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        Button viewButton = new Button();
        viewButton.setTooltipText("View Document");
        viewButton.setIcon(svgIcon);
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> {
            if (employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                EmployeeDocumentDTO employeeDocumentDTO = employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                // Get the PDF or image data from the selected data row.
                StreamResource dataStreamResource = new StreamResource(employeeDocumentDTO.getFileName(), () -> new ByteArrayInputStream(employeeDocumentDTO.getFileData()));

                // Create a PDF viewer component.
                PdfViewer pdfViewer;

                // Create an image viewer.
                Image imageViewer;

                // Create a layout that will hod the viewer components.
                VerticalLayout dialogLayout = new VerticalLayout();
                dialogLayout.setPadding(false);
                dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
                dialogLayout.getStyle().set("width", "768px").set("max-width", "100%");

                if (employeeDocumentDTO.getFileType().equals("application/pdf")) {
                    pdfViewer = new PdfViewer();
                    pdfViewer.setSrc(dataStreamResource);
                    dialogLayout.add(pdfViewer);
                } else {
                    imageViewer = new Image(dataStreamResource, employeeDocumentDTO.getFileName());
                    dialogLayout.add(imageViewer);
                }

                // Create a dialog component that will display the streamed resource.
                Dialog pdfDialog = new Dialog();
                pdfDialog.setHeaderTitle(employeeDocumentDTO.getFileName());
                pdfDialog.setModal(true);
                pdfDialog.setResizable(true);

                // Create a close button for the dialog.
                Button closeButton = new Button("Close");
                closeButton.addClickListener(buttonClickEvent1 -> {
                    pdfDialog.close();
                });

                pdfDialog.add(dialogLayout);
                pdfDialog.getFooter().add(closeButton);
                pdfDialog.open();
            }
        });

        rowToolbarLayout.add(viewButton);
        rowToolbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private Div buildNotification(String message, EmployeeDetailsView.MessageLevel messageLevel, SvgIcon svgIcon) {
        Div text = new Div(new Text(message));

        HorizontalLayout layout = new HorizontalLayout(svgIcon, text);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        Div notificationDiv = new Div();
        notificationDiv.getStyle().set("padding", "20px")
                .set("border-radius", "3px")
                .set("color", "#fdfefe")
                .set("margin-bottom", "5px");

        // Change the background color based on the message level.
        switch (messageLevel) {
            case INFO -> notificationDiv.getStyle().set("background-color", "#2196F3");
            case SUCCESS -> notificationDiv.getStyle().set("background-color", "#04AA6D");
            case WARNING -> notificationDiv.getStyle().set("background-color", "#ff9800");
            case DANGER -> notificationDiv.getStyle().set("background-color", "#f44336");
        }

        notificationDiv.add(layout);

        return notificationDiv;
    }
}
