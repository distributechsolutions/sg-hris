package com.whizservices.hris.views.compenben;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.*;

import com.whizservices.hris.dtos.compenben.GovernmentContributionsDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.compenben.GovernmentContributionsService;
import com.whizservices.hris.services.profile.EmployeeService;
import com.whizservices.hris.utils.SecurityUtil;
import com.whizservices.hris.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Government Contributions Form")
@Route(value = "government-contributions-form", layout = MainLayout.class)
public class GovernmentContributionsFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource private final GovernmentContributionsService governmentContributionsService;
    @Resource private final EmployeeService employeeService;

    private GovernmentContributionsDTO governmentContributionsDTO;
    private UUID parameterId;

    private final FormLayout governmentContributionsDTOFormLayout = new FormLayout();
    private ComboBox<EmployeeDTO> employeeDTOComboBox;
    private BigDecimalField sssAmountField, hdmfAmountField, philhealthAmountField;

    public GovernmentContributionsFormView(GovernmentContributionsService governmentContributionsService, EmployeeService employeeService) {
        this.governmentContributionsService = governmentContributionsService;
        this.employeeService = employeeService;

        add(governmentContributionsDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, governmentContributionsDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {
        if (s != null) {
            parameterId = UUID.fromString(s);
            governmentContributionsDTO = governmentContributionsService.getById(parameterId);
        }

        buildGovernmentContributionsFormLayout();
    }

    private void buildGovernmentContributionsFormLayout() {
        // Create the query object that will do the pagination of employee records in the combo box component.
        Query<EmployeeDTO, Void> employeeQuery = new Query<>();

        employeeDTOComboBox = new ComboBox<>("Employee");
        employeeDTOComboBox.setItems((employeeDTO, filterString) -> employeeDTO.getEmployeeFullName().toLowerCase().contains(filterString.toLowerCase()),
                                                                                       employeeService.getAll(employeeQuery.getPage(), employeeQuery.getPageSize()));
        employeeDTOComboBox.setItemLabelGenerator(EmployeeDTO::getEmployeeFullName);
        employeeDTOComboBox.setClearButtonVisible(true);
        employeeDTOComboBox.setRequired(true);
        employeeDTOComboBox.setRequiredIndicatorVisible(true);
        if (governmentContributionsDTO != null) employeeDTOComboBox.setValue(governmentContributionsDTO.getEmployeeDTO());

        // Add a prefix div label for each of the decimal fields.
        Div phpPrefix = new Div();
        phpPrefix.setText("PHP");

        sssAmountField = new BigDecimalField("SSS Contribution Amount");
        sssAmountField.setPlaceholder("0.00");
        sssAmountField.setRequired(true);
        sssAmountField.setRequiredIndicatorVisible(true);
        sssAmountField.setPrefixComponent(phpPrefix);
        if (governmentContributionsDTO != null) sssAmountField.setValue(governmentContributionsDTO.getSssContributionAmount());

        hdmfAmountField = new BigDecimalField("HDMF Contribution Amount");
        hdmfAmountField.setPlaceholder("0.00");
        hdmfAmountField.setRequired(true);
        hdmfAmountField.setRequiredIndicatorVisible(true);
        hdmfAmountField.setPrefixComponent(phpPrefix);
        if (governmentContributionsDTO != null) hdmfAmountField.setValue(governmentContributionsDTO.getHdmfContributionAmount());

        philhealthAmountField = new BigDecimalField("Philhealth Contribution Amount");
        philhealthAmountField.setPlaceholder("0.00");
        philhealthAmountField.setRequired(true);
        philhealthAmountField.setRequiredIndicatorVisible(true);
        philhealthAmountField.setPrefixComponent(phpPrefix);
        if (governmentContributionsDTO != null) philhealthAmountField.setValue(governmentContributionsDTO.getPhilhealthContributionAmount());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateGovernmentContributionsDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(GovernmentContributionsListView.class));

            Notification notification = Notification.show("Successfully added government contributions.", 5000, Notification.Position.TOP_CENTER);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(GovernmentContributionsListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        governmentContributionsDTOFormLayout.add(employeeDTOComboBox,
                                                 sssAmountField,
                                                 hdmfAmountField,
                                                 philhealthAmountField,
                                                 buttonLayout);
        governmentContributionsDTOFormLayout.setColspan(employeeDTOComboBox, 3);
        governmentContributionsDTOFormLayout.setColspan(buttonLayout, 3);
        governmentContributionsDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateGovernmentContributionsDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            governmentContributionsDTO = governmentContributionsService.getById(parameterId);
        } else {
            governmentContributionsDTO = new GovernmentContributionsDTO();
            governmentContributionsDTO.setCreatedBy(loggedInUser);
        }

        governmentContributionsDTO.setEmployeeDTO(employeeDTOComboBox.getValue());
        governmentContributionsDTO.setSssContributionAmount(sssAmountField.getValue());
        governmentContributionsDTO.setHdmfContributionAmount(hdmfAmountField.getValue());
        governmentContributionsDTO.setPhilhealthContributionAmount(philhealthAmountField.getValue());
        governmentContributionsDTO.setUpdatedBy(loggedInUser);

        governmentContributionsService.saveOrUpdate(governmentContributionsDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a government contribution record.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
}
