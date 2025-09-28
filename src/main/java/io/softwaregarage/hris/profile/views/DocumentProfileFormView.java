package io.softwaregarage.hris.profile.views;

import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;

import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.dtos.DocumentProfileDTO;
import io.softwaregarage.hris.profile.services.DocumentProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.io.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.vaadin.lineawesome.LineAwesomeIcon;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR",
               "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Document Form")
@Route(value = "employee-document-form", layout = MainLayout.class)
public class DocumentProfileFormView extends Div implements HasUrlParameter<String> {
    @Resource private final DocumentProfileService documentProfileService;
    @Resource private final EmployeeProfileService employeeProfileService;

    private DocumentProfileDTO documentProfileDTO;
    private EmployeeProfileDTO employeeProfileDTO;
    private UUID parameterId;

    // This will hold the value from the uploaded image in the upload component.
    private String fileName, fileType;
    private byte[] imageBytes;

    // Component fields.
    private ComboBox<String> documentTypeComboBox;
    private Upload employeeDocumentUpload;
    private TextField remarksTextField;
    private DatePicker documentExpirationDatePicker;
    private Button viewButton, editButton;

    private final FormLayout employeeDocumentDTOFormLayout = new FormLayout();
    private final Grid<DocumentProfileDTO> employeeDocumentDTOGrid = new Grid<>(DocumentProfileDTO.class, false);

    public DocumentProfileFormView(DocumentProfileService documentProfileService,
                                   EmployeeProfileService employeeProfileService) {
        this.documentProfileService = documentProfileService;
        this.employeeProfileService = employeeProfileService;

        this.add(employeeDocumentDTOFormLayout, employeeDocumentDTOGrid);
        this.getStyle().setPadding("20px");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            employeeProfileDTO = employeeProfileService.getById(parameterId);
        }

        this.buildEmployeeDocumentFormLayout();
        this.buildEmployeeDocumentGrid();
        this.updateEmployeeDocumentDTOGrid();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    private void buildEmployeeDocumentFormLayout() {
        documentTypeComboBox = new ComboBox<>("Document Type");
        documentTypeComboBox.setItems("Birth Certificate",
                                      "Police Clearance",
                                      "NBI Clearance",
                                      "Medical Certificate",
                                      "Transcript of Records",
                                      "Diploma",
                                      "Passport",
                                      "Scanned Government ID",
                                      "ID Picture",
                                      "Others");
        documentTypeComboBox.setRequired(true);
        documentTypeComboBox.setRequiredIndicatorVisible(true);

        // START - Single file upload section.
        Button uploadEmployeeFilesButton = new Button("Upload");
        uploadEmployeeFilesButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        employeeDocumentUpload = new Upload();
        employeeDocumentUpload.setId("upload-employee-file");
        employeeDocumentUpload.setDropAllowed(true);
        employeeDocumentUpload.setAcceptedFileTypes(".jpg", ".jpeg", ".png", ".pdf");
        employeeDocumentUpload.setUploadButton(uploadEmployeeFilesButton);
        employeeDocumentUpload.setUploadHandler(uploadEvent -> {
            try (InputStream inputStream = uploadEvent.getInputStream()) {
                this.setFileName(uploadEvent.getFileName());
                this.setFileType(uploadEvent.getContentType());
                this.setImageBytes(inputStream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read uploaded file", e);
            }

        });

        NativeLabel dropLabel = new NativeLabel("Upload the file here. Accepted file types: .pdf, .png, .jpeg and .jpg");
        dropLabel.setFor(employeeDocumentUpload.getId().isPresent() ? employeeDocumentUpload.getId().get() : "");

        Div uploadDiv = new Div(dropLabel, employeeDocumentUpload);
        uploadDiv.getStyle().set("padding-top", "10px");
        // END - Single file upload section.

        remarksTextField = new TextField("Remarks");
        remarksTextField.setRequired(true);
        remarksTextField.setRequiredIndicatorVisible(true);

        documentExpirationDatePicker = new DatePicker("Expiration Date");
        documentExpirationDatePicker.setMin(LocalDate.of(LocalDate.now().getYear(),
                                                         LocalDate.now().getMonth(),
                                                         LocalDate.now().getDayOfMonth()));
        documentExpirationDatePicker.setRequired(true);
        documentExpirationDatePicker.setRequiredIndicatorVisible(true);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            // Save the data.
            this.saveEmployeeDocumentDTO();

            // Clear the fields.
            this.clearFields();

            // Update the data grid.
            this.updateEmployeeDocumentDTOGrid();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> this.clearFields());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setMaxWidth("768px");
        buttonLayout.setPadding(true);

        employeeDocumentDTOFormLayout.add(documentTypeComboBox,
                                          documentExpirationDatePicker,
                                          uploadDiv,
                                          remarksTextField,
                                          buttonLayout);
        employeeDocumentDTOFormLayout.setColspan(uploadDiv, 2);
        employeeDocumentDTOFormLayout.setColspan(remarksTextField, 2);
        employeeDocumentDTOFormLayout.setColspan(buttonLayout, 2);
        employeeDocumentDTOFormLayout.setWidth("768px");
    }

    private void buildEmployeeDocumentGrid() {
        employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getDocumentType).setHeader("Document Type");
        employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getFileName).setHeader("File Name");
        employeeDocumentDTOGrid.addColumn(new LocalDateRenderer<>(DocumentProfileDTO::getExpirationDate, "MMM dd, yyyy")).setHeader("Expiration Date");
        employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getRemarks).setHeader("Remarks");
        employeeDocumentDTOGrid.addColumn(DocumentProfileDTO::getFileType).setHeader("File Type");
        employeeDocumentDTOGrid.addComponentColumn(addressDTO -> this.buildRowToolbar(addressDTO.getFileType().equals("application/pdf") ?
                                                                                      LineAwesomeIcon.FILE_PDF.create() :
                                                                                      LineAwesomeIcon.IMAGES.create()))
                               .setHeader("Action");
        employeeDocumentDTOGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES,
                                                 GridVariant.LUMO_COLUMN_BORDERS,
                                                 GridVariant.LUMO_WRAP_CELL_CONTENT);
        employeeDocumentDTOGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        employeeDocumentDTOGrid.setAllRowsVisible(true);
        employeeDocumentDTOGrid.setEmptyStateText("No documents found.");
    }

    private Component buildRowToolbar(SvgIcon svgIcon) {
        HorizontalLayout rowToolbarLayout = new HorizontalLayout();

        viewButton = new Button();
        viewButton.setTooltipText("View Document");
        viewButton.setIcon(svgIcon);
        viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        viewButton.addClickListener(buttonClickEvent -> {
            if (employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().isPresent()) {
                documentProfileDTO = employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().get();

                byte[] fileData = documentProfileDTO.getFileData();
                String fileName = documentProfileDTO.getFileName();
                String mimeType = documentProfileDTO.getFileType();

                // Create a layout that will hod the viewer components.
                VerticalLayout dialogLayout = new VerticalLayout();
                dialogLayout.setPadding(false);
                dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
                dialogLayout.getStyle().set("width", "768px").set("max-width", "100%");

                if (mimeType.equals("application/pdf")) {
                    VerticalLayout pdfLayout = new VerticalLayout();
                    pdfLayout.setSizeFull();

                    PdfViewer pdfViewer = new PdfViewer();
                    pdfViewer.setSrc(DownloadHandler.fromInputStream(downloadEvent -> {
                        try {
                            return new DownloadResponse(new ByteArrayInputStream(fileData), fileName, mimeType, fileData.length);
                        } catch (Exception e) {
                            return DownloadResponse.error(500);
                        }
                    }));

                    dialogLayout.add(pdfViewer);
                } else {
                    Image imageViewer = new Image();
                    imageViewer.setSrc(downloadHandler -> {
                        try (OutputStream out = downloadHandler.getOutputStream()) {
                            out.write(fileData);
                            downloadHandler.setFileName(fileName);
                            downloadHandler.setContentType(mimeType);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
                    imageViewer.setAlt(fileName);
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

        editButton = new Button();
        editButton.setTooltipText("Edit Document");
        editButton.setIcon(LineAwesomeIcon.PENCIL_ALT_SOLID.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(buttonClickEvent -> this.loadEmployeeDocumentDTO());

        rowToolbarLayout.add(viewButton, editButton);
        rowToolbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rowToolbarLayout.getStyle().set("flex-wrap", "wrap");

        return rowToolbarLayout;
    }

    private void saveEmployeeDocumentDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (documentProfileDTO == null) {
            documentProfileDTO = new DocumentProfileDTO();
            documentProfileDTO.setEmployeeDTO(employeeProfileDTO);
            documentProfileDTO.setFileName(this.getFileName());
            documentProfileDTO.setFileData(this.getImageBytes());
            documentProfileDTO.setFileType(this.getFileType());
            documentProfileDTO.setCreatedBy(loggedInUser);
        }

        documentProfileDTO.setDocumentType(documentTypeComboBox.getValue());
        documentProfileDTO.setRemarks(remarksTextField.getValue());
        documentProfileDTO.setExpirationDate(documentExpirationDatePicker.getValue());
        documentProfileDTO.setUpdatedBy(loggedInUser);

        documentProfileService.saveOrUpdate(documentProfileDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved an employee document.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void clearFields() {
        documentTypeComboBox.clear();
        employeeDocumentUpload.clearFileList();
        remarksTextField.clear();
        documentExpirationDatePicker.clear();
    }

    private void loadEmployeeDocumentDTO() {
        documentProfileDTO = employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().get();

        documentTypeComboBox.setValue(documentProfileDTO.getDocumentType());
        remarksTextField.setValue(documentProfileDTO.getRemarks());
        documentExpirationDatePicker.setValue(documentProfileDTO.getExpirationDate());
    }

    private void updateEmployeeDocumentDTOGrid() {
        employeeDocumentDTOGrid.setItems(documentProfileService.getByEmployeeDTO(employeeProfileDTO));
    }
}
