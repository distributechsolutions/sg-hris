package io.distributechsolutions.hris.views.profile;

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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.*;

import com.vaadin.flow.server.StreamResource;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDocumentDTO;
import io.distributechsolutions.hris.services.profile.EmployeeDocumentService;
import io.distributechsolutions.hris.services.profile.EmployeeService;
import io.distributechsolutions.hris.utils.SecurityUtil;
import io.distributechsolutions.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
        "ROLE_HR_MANAGER",
        "ROLE_HR_SUPERVISOR",
        "ROLE_HR_EMPLOYEE"})
@PageTitle("Employee Document Form")
@Route(value = "employee-document-form", layout = MainLayout.class)
public class EmployeeDocumentFormView extends Div implements HasUrlParameter<String> {
    @Resource private final EmployeeDocumentService employeeDocumentService;
    @Resource private final EmployeeService employeeService;

    private EmployeeDocumentDTO employeeDocumentDTO;
    private EmployeeDTO employeeDTO;
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
    private final Grid<EmployeeDocumentDTO> employeeDocumentDTOGrid = new Grid<>(EmployeeDocumentDTO.class, false);

    public EmployeeDocumentFormView(EmployeeDocumentService employeeDocumentService,
                                    EmployeeService employeeService) {
        this.employeeDocumentService = employeeDocumentService;
        this.employeeService = employeeService;

        this.add(employeeDocumentDTOFormLayout, employeeDocumentDTOGrid);
        this.getStyle().setPadding("20px");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        if (parameter != null) {
            parameterId = UUID.fromString(parameter);
            employeeDTO = employeeService.getById(parameterId);
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
                                      "Others");
        documentTypeComboBox.setRequired(true);
        documentTypeComboBox.setRequiredIndicatorVisible(true);

        // START - Single file upload section.
        Button uploadEmployeeFilesButton = new Button("Upload");
        uploadEmployeeFilesButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        MemoryBuffer memoryBuffer = new MemoryBuffer();

        employeeDocumentUpload = new Upload(memoryBuffer);
        employeeDocumentUpload.setId("upload-employee-file");
        employeeDocumentUpload.setDropAllowed(true);
        employeeDocumentUpload.setAcceptedFileTypes(".jpg", ".jpeg", ".png", ".pdf");
        employeeDocumentUpload.setUploadButton(uploadEmployeeFilesButton);
        employeeDocumentUpload.addSucceededListener(succeededEvent -> {
            try {
                this.setFileName(memoryBuffer.getFileName());
                this.setFileType(memoryBuffer.getFileData().getMimeType());
                this.setImageBytes(memoryBuffer.getInputStream().readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getDocumentType).setHeader("Document Type");
        employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getFileName).setHeader("File Name");
        employeeDocumentDTOGrid.addColumn(new LocalDateRenderer<>(EmployeeDocumentDTO::getExpirationDate, "MMM dd, yyyy")).setHeader("Expiration Date");
        employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getRemarks).setHeader("Remarks");
        employeeDocumentDTOGrid.addColumn(EmployeeDocumentDTO::getFileType).setHeader("File Type");
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
                employeeDocumentDTO = employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().get();

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

        if (employeeDocumentDTO == null) {
            employeeDocumentDTO = new EmployeeDocumentDTO();
            employeeDocumentDTO.setEmployeeDTO(employeeDTO);
            employeeDocumentDTO.setFileName(this.getFileName());
            employeeDocumentDTO.setFileData(this.getImageBytes());
            employeeDocumentDTO.setFileType(this.getFileType());
            employeeDocumentDTO.setCreatedBy(loggedInUser);
        }

        employeeDocumentDTO.setDocumentType(documentTypeComboBox.getValue());
        employeeDocumentDTO.setRemarks(remarksTextField.getValue());
        employeeDocumentDTO.setExpirationDate(documentExpirationDatePicker.getValue());
        employeeDocumentDTO.setUpdatedBy(loggedInUser);

        employeeDocumentService.saveOrUpdate(employeeDocumentDTO);

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
        employeeDocumentDTO = employeeDocumentDTOGrid.getSelectionModel().getFirstSelectedItem().get();

        documentTypeComboBox.setValue(employeeDocumentDTO.getDocumentType());
        remarksTextField.setValue(employeeDocumentDTO.getRemarks());
        documentExpirationDatePicker.setValue(employeeDocumentDTO.getExpirationDate());
    }

    private void updateEmployeeDocumentDTOGrid() {
        employeeDocumentDTOGrid.setItems(employeeDocumentService.getByEmployeeDTO(employeeDTO));
    }
}
