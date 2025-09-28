package io.softwaregarage.hris.profile.views;

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
import io.softwaregarage.hris.profile.dtos.AddressProfileDTO;
import io.softwaregarage.hris.profile.dtos.DependentProfileDTO;
import io.softwaregarage.hris.profile.dtos.PersonalProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.dtos.DocumentProfileDTO;
import io.softwaregarage.hris.profile.services.AddressProfileService;
import io.softwaregarage.hris.profile.services.DependentProfileService;
import io.softwaregarage.hris.profile.services.PersonalProfileService;
import io.softwaregarage.hris.profile.services.DocumentProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.commons.views.MainLayout;

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
public class EmployeeProfileDetailsView extends Div implements HasUrlParameter<String> {
    @Resource private final EmployeeProfileService employeeProfileService;
    @Resource private final PersonalProfileService personalProfileService;
    @Resource private final AddressProfileService addressProfileService;
    @Resource private final DependentProfileService dependentProfileService;
    @Resource private final DocumentProfileService documentProfileService;

    private EmployeeProfileDTO employeeProfileDTO;
    private PersonalProfileDTO personalProfileDTO;
    private List<AddressProfileDTO> addressProfileDTOList;
    private List<DependentProfileDTO> dependentProfileDTOList;
    private List<DocumentProfileDTO> documentProfileDTOList;

    private final FormLayout employeeDetailsLayout = new FormLayout();
    private final Div personalInfoDiv = new Div();
    private final Div addressInfoDiv = new Div();
    private final Div dependentInfoDiv = new Div();
    private final Div employeeDocumentDiv = new Div();

    private final Grid<AddressProfileDTO> addressInfoDTOGrid = new Grid<>(AddressProfileDTO.class, false);
    private final Grid<DependentProfileDTO> dependentInfoDTOGrid = new Grid<>(DependentProfileDTO.class, false);
    private final Grid<DocumentProfileDTO> employeeDocumentDTOGrid = new Grid<>(DocumentProfileDTO.class, false);

    private TabSheet employeeInformationTabSheets = new TabSheet();


    enum MessageLevel {
        INFO,
        SUCCESS,
        WARNING,
        DANGER
    }

    public EmployeeProfileDetailsView(EmployeeProfileService employeeProfileService,
                                      PersonalProfileService personalProfileService,
                                      AddressProfileService addressProfileService,
                                      DependentProfileService dependentProfileService,
                                      DocumentProfileService documentProfileService) {
        this.employeeProfileService = employeeProfileService;
        this.personalProfileService = personalProfileService;
        this.addressProfileService = addressProfileService;
        this.dependentProfileService = dependentProfileService;
        this.documentProfileService = documentProfileService;

        setSizeFull();
        add(employeeDetailsLayout, employeeInformationTabSheets);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            UUID parameterId = UUID.fromString(parameter);
            employeeProfileDTO = employeeProfileService.getById(parameterId);
        }

        buildEmployeeDetailsLayout();
        buildEmployeeInformationTabSheets();
        buildPersonalInfoDiv();
        buildAddressInfoDiv();
        buildDependentInfoDiv();
        buildEmployeeDocumentDiv();
    }

    public void buildEmployeeDetailsLayout() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);

        Span recordIdLabelSpan = new Span("Record ID");
        recordIdLabelSpan.getStyle().set("text-align", "right");

        Span recordIdValueSpan = new Span(employeeProfileDTO.getId().toString());
        recordIdValueSpan.getStyle().setFontWeight("bold");

        Span employeeNoLabelSpan = new Span("Employee No");
        employeeNoLabelSpan.getStyle().set("text-align", "right");

        Span employeeNoValueSpan = new Span(employeeProfileDTO.getEmployeeNumber());
        employeeNoValueSpan.getStyle().setFontWeight("bold");

        Span dateHiredLabelSpan = new Span("Date Hired");
        dateHiredLabelSpan.getStyle().set("text-align", "right");

        Span dateHiredValueSpan = new Span(df.format(employeeProfileDTO.getDateHired()));
        dateHiredValueSpan.getStyle().setFontWeight("bold");

        Span fullNameLabelSpan = new Span("Full Name");
        fullNameLabelSpan.getStyle().set("text-align", "right");

        Span fullNameValueSpan = new Span(employeeProfileDTO.getEmployeeFullName());
        fullNameValueSpan.getStyle().setFontWeight("bold");

        Span genderLabelSpan = new Span("Gender");
        genderLabelSpan.getStyle().set("text-align", "right");

        Span genderValueSpan = new Span(employeeProfileDTO.getGender());
        genderValueSpan.getStyle().setFontWeight("bold");

        Span employmentTypeLabelSpan = new Span("Employment Type");
        employmentTypeLabelSpan.getStyle().set("text-align", "right");

        Span employmentTypeValueSpan = new Span(employeeProfileDTO.getEmploymentType());
        employmentTypeValueSpan.getStyle().setFontWeight("bold");

        Span contractDurationLabelSpan = new Span("Contract Duration");
        contractDurationLabelSpan.getStyle().set("text-align", "right");

        Span contractDurationValueSpan = new Span(employeeProfileDTO.getContractDuration());
        contractDurationValueSpan.getStyle().setFontWeight("bold");

        Span startDateLabelSpan = new Span("Start Date");
        startDateLabelSpan.getStyle().set("text-align", "right");

        Span startDateValueSpan = new Span(df.format(employeeProfileDTO.getStartDate()));
        startDateValueSpan.getStyle().setFontWeight("bold");

        Span endDateLabelSpan = new Span("End Date");
        endDateLabelSpan.getStyle().set("text-align", "right");

        Span endDateValueSpan = new Span(df.format(employeeProfileDTO.getEndDate()));
        endDateValueSpan.getStyle().setFontWeight("bold");

        Span statusLabelSpan = new Span("Status");
        statusLabelSpan.getStyle().set("text-align", "right");

        Span statusValueSpan = new Span(employeeProfileDTO.getStatus());
        statusValueSpan.getStyle().setFontWeight("bold");

        employeeDetailsLayout.add(recordIdLabelSpan,
                                  recordIdValueSpan,
                                  employeeNoLabelSpan,
                                  employeeNoValueSpan,
                                  dateHiredLabelSpan,
                                  dateHiredValueSpan,
                                  fullNameLabelSpan,
                                  fullNameValueSpan,
                                  genderLabelSpan,
                                  genderValueSpan,
                                  employmentTypeLabelSpan,
                                  employmentTypeValueSpan,
                                  contractDurationLabelSpan,
                                  contractDurationValueSpan,
                                  startDateLabelSpan,
                                  startDateValueSpan,
                                  endDateLabelSpan,
                                  endDateValueSpan,
                                  statusLabelSpan,
                                  statusValueSpan);
        employeeDetailsLayout.setWidth("720px");
    }

    private void buildEmployeeInformationTabSheets() {
        employeeInformationTabSheets.add("Personal", personalInfoDiv);
        employeeInformationTabSheets.add("Addresses", addressInfoDiv);
        employeeInformationTabSheets.add("Dependents", dependentInfoDiv);
        employeeInformationTabSheets.add("Documents", employeeDocumentDiv);
    }

    private void buildPersonalInfoDiv() {
        personalProfileDTO = personalProfileService.getByEmployeeDTO(employeeProfileDTO);

        if (personalProfileDTO != null) {
            FormLayout personalInfoFormLayout = new FormLayout();

            Span dateOfBirthLabelSpan = new Span("Date of birth");
            dateOfBirthLabelSpan.getStyle().set("text-align", "right");

            Span dateOfBirthValueSpan = new Span(DateTimeFormatter.ofPattern("MMM dd, yyyy").format(personalProfileDTO.getDateOfBirth()));
            dateOfBirthValueSpan.getStyle().setFontWeight("bold");

            Span placeOfBirthLabelSpan = new Span("Place of birth");
            placeOfBirthLabelSpan.getStyle().set("text-align", "right");

            Span placeOfBirthValueSpan = new Span(personalProfileDTO.getPlaceOfBirth());
            placeOfBirthValueSpan.getStyle().setFontWeight("bold");

            Span maritalStatusLabelSpan = new Span("Marital Status");
            maritalStatusLabelSpan.getStyle().set("text-align", "right");

            Span maritalStatusValueSpan = new Span(personalProfileDTO.getMaritalStatus());
            maritalStatusValueSpan.getStyle().setFontWeight("bold");

            Span spouseLabelSpan = new Span("Spouse Name");
            spouseLabelSpan.getStyle().set("text-align", "right");

            Span spouseValueSpan = new Span(personalProfileDTO.getSpouseName() != null ? personalProfileDTO.getSpouseName() : "");
            spouseValueSpan.getStyle().setFontWeight("bold");

            Span contactNoLabelSpan = new Span("Contact Number");
            contactNoLabelSpan.getStyle().set("text-align", "right");

            Span contactNoValueSpan = new Span(String.valueOf(personalProfileDTO.getContactNumber()));
            contactNoValueSpan.getStyle().setFontWeight("bold");

            Span emailLabelSpan = new Span("Email");
            emailLabelSpan.getStyle().set("text-align", "right");

            Span emailValueSpan = new Span(personalProfileDTO.getEmailAddress());
            emailValueSpan.getStyle().setFontWeight("bold");

            Span tinLabelSpan = new Span("TIN");
            tinLabelSpan.getStyle().set("text-align", "right");

            Span tinValueSpan = new Span(personalProfileDTO.getTaxIdentificationNumber());
            tinValueSpan.getStyle().setFontWeight("bold");

            Span sssLabelSpan = new Span("SSS");
            sssLabelSpan.getStyle().set("text-align", "right");

            Span sssValueSpan = new Span(personalProfileDTO.getSssNumber());
            sssValueSpan.getStyle().setFontWeight("bold");

            Span hdmfLabelSpan = new Span("Pag-Ibig HDMF");
            hdmfLabelSpan.getStyle().set("text-align", "right");

            Span hdmfValueSpan = new Span(personalProfileDTO.getHdmfNumber());
            hdmfValueSpan.getStyle().setFontWeight("bold");

            Span philhealthLabelSpan = new Span("Philhealth");
            philhealthLabelSpan.getStyle().set("text-align", "right");

            Span philhealthValueSpan = new Span(personalProfileDTO.getPhilhealthNumber());
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
            personalInfoFormLayout.setWidth("720px");

            personalInfoDiv.add(personalInfoFormLayout);
        } else {
            Div profileMessageNotification = this.buildNotification("Employee has not yet filled up this information.",
                                                                    EmployeeProfileDetailsView.MessageLevel.INFO,
                                                                    LineAwesomeIcon.INFO_CIRCLE_SOLID.create());

            personalInfoDiv.add(profileMessageNotification);
        }

        personalInfoDiv.getStyle().setPadding("10px");
    }

    private void buildAddressInfoDiv() {
        addressProfileDTOList = addressProfileService.getByEmployeeDTO(employeeProfileDTO);

        if (!addressProfileDTOList.isEmpty()) {
            addressInfoDTOGrid.setItems(addressProfileDTOList);
            addressInfoDTOGrid.addColumn(AddressProfileDTO::getAddressType)
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
            addressInfoDTOGrid.addColumn(AddressProfileDTO::getPostalCode)
                              .setHeader("Postal Code");
            addressInfoDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                GridVariant.LUMO_COLUMN_BORDERS,
                                                GridVariant.LUMO_WRAP_CELL_CONTENT);
            addressInfoDTOGrid.setAllRowsVisible(true);

            addressInfoDiv.add(addressInfoDTOGrid);
        } else {
            Div addressMessageNotification = this.buildNotification("Employee has not yet filled up this information.",
                                                                    EmployeeProfileDetailsView.MessageLevel.INFO,
                                                                    LineAwesomeIcon.INFO_CIRCLE_SOLID.create());

            addressInfoDiv.add(addressMessageNotification);
        }

        addressInfoDiv.getStyle().setPadding("10px");
    }

    private void buildDependentInfoDiv() {
        dependentProfileDTOList = dependentProfileService.getByEmployeeDTO(employeeProfileDTO);

        if (!dependentProfileDTOList.isEmpty()) {
            dependentInfoDTOGrid.setItems(dependentProfileDTOList);
            dependentInfoDTOGrid.addColumn(DependentProfileDTO::getFullName).setHeader("Name");
            dependentInfoDTOGrid.addColumn(new LocalDateRenderer<>(DependentProfileDTO::getDateOfBirth, "MMM dd, yyyy")).setHeader("Date of Birth");
            dependentInfoDTOGrid.addColumn(DependentProfileDTO::getAge).setHeader("Age");
            dependentInfoDTOGrid.addColumn(DependentProfileDTO::getRelationship).setHeader("Relationship");
            dependentInfoDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                  GridVariant.LUMO_COLUMN_BORDERS,
                                                  GridVariant.LUMO_WRAP_CELL_CONTENT);
            dependentInfoDTOGrid.setAllRowsVisible(true);

            dependentInfoDiv.add(dependentInfoDTOGrid);
        } else {
            Div dependentMessageNotification = this.buildNotification("Employee has not yet filled up this information.",
                    EmployeeProfileDetailsView.MessageLevel.INFO,
                    LineAwesomeIcon.INFO_CIRCLE_SOLID.create());

            dependentInfoDiv.add(dependentMessageNotification);
        }

        dependentInfoDiv.getStyle().setPadding("10px");
    }

    private void buildEmployeeDocumentDiv() {
        documentProfileDTOList = documentProfileService.getByEmployeeDTO(employeeProfileDTO);

        if (!documentProfileDTOList.isEmpty()) {
            employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getDocumentType).setHeader("Document Type");
            employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getFileName).setHeader("File Name");
            employeeDocumentDTOGrid.addColumn(new LocalDateRenderer<>(DocumentProfileDTO::getExpirationDate, "MMM dd, yyyy")).setHeader("Expiration Date");
            employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getRemarks).setHeader("Remarks");
            employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getFileType).setHeader("File Type");
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
            employeeDocumentDTOGrid.setItems(documentProfileDTOList);

            employeeDocumentDiv.add(employeeDocumentDTOGrid);
        } else {
            Div employeeDocumentMessageNotification = this.buildNotification("No documents found related to this employee.",
                                                                             EmployeeProfileDetailsView.MessageLevel.INFO,
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
                DocumentProfileDTO documentProfileDTO = employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                // Get the PDF or image data from the selected data row.
                StreamResource dataStreamResource = new StreamResource(documentProfileDTO.getFileName(), () -> new ByteArrayInputStream(documentProfileDTO.getFileData()));

                // Create a PDF viewer component.
                PdfViewer pdfViewer;

                // Create an image viewer.
                Image imageViewer;

                // Create a layout that will hod the viewer components.
                VerticalLayout dialogLayout = new VerticalLayout();
                dialogLayout.setPadding(false);
                dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
                dialogLayout.getStyle().set("width", "768px").set("max-width", "100%");

                if (documentProfileDTO.getFileType().equals("application/pdf")) {
                    pdfViewer = new PdfViewer();
                    pdfViewer.setSrc(dataStreamResource);
                    dialogLayout.add(pdfViewer);
                } else {
                    imageViewer = new Image(dataStreamResource, documentProfileDTO.getFileName());
                    dialogLayout.add(imageViewer);
                }

                // Create a dialog component that will display the streamed resource.
                Dialog pdfDialog = new Dialog();
                pdfDialog.setHeaderTitle(documentProfileDTO.getFileName());
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

    private Div buildNotification(String message, EmployeeProfileDetailsView.MessageLevel messageLevel, SvgIcon svgIcon) {
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
