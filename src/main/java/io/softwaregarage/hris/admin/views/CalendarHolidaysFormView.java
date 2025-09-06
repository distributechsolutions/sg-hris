package io.softwaregarage.hris.admin.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import io.softwaregarage.hris.admin.dtos.CalendarHolidaysDTO;
import io.softwaregarage.hris.admin.services.CalendarHolidaysService;
import io.softwaregarage.hris.utils.SecurityUtil;
import io.softwaregarage.hris.commons.views.MainLayout;

import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@RolesAllowed({"ROLE_ADMIN",
               "ROLE_HR_MANAGER",
               "ROLE_HR_SUPERVISOR"})
@PageTitle("Calendar Holiday Form")
@Route(value = "calendar-holiday-form", layout = MainLayout.class)
public class CalendarHolidaysFormView extends VerticalLayout implements HasUrlParameter<String> {
    @Resource
    private final CalendarHolidaysService calendarHolidaysService;
    private CalendarHolidaysDTO calendarHolidaysDTO;
    private UUID parameterId;

    private final FormLayout calendarHolidaysDTOFormLayout = new FormLayout();
    private ComboBox<String> holidayTypeComboBox;
    private TextField holidayDescriptionTextField, holidayYearTextField;
    private DatePicker holidayDatePicker;

    public CalendarHolidaysFormView(CalendarHolidaysService calendarHolidaysService) {
        this.calendarHolidaysService = calendarHolidaysService;

        add(calendarHolidaysDTOFormLayout);

        setSizeFull();
        setMargin(true);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, calendarHolidaysDTOFormLayout);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String paramId) {
        if (paramId != null) {
            parameterId = UUID.fromString(paramId);
            calendarHolidaysDTO = calendarHolidaysService.getById(parameterId);
        }

        buildCalendarHolidaysFormLayout();
    }

    private void buildCalendarHolidaysFormLayout() {
        holidayTypeComboBox = new ComboBox<>("Holiday Type");
        holidayTypeComboBox.setItems("Regular Holiday", "Special Working Holiday", "Special Non-Working Holiday");
        holidayTypeComboBox.setClearButtonVisible(true);
        holidayTypeComboBox.setRequired(true);
        holidayTypeComboBox.setRequiredIndicatorVisible(true);
        if (calendarHolidaysDTO != null) holidayTypeComboBox.setValue(calendarHolidaysDTO.getHolidayType());

        holidayDescriptionTextField = new TextField("Holiday Description");
        holidayDescriptionTextField.setClearButtonVisible(true);
        holidayDescriptionTextField.setRequired(true);
        holidayDescriptionTextField.setRequiredIndicatorVisible(true);
        if (calendarHolidaysDTO != null) holidayDescriptionTextField.setValue(calendarHolidaysDTO.getHolidayDescription());

        holidayYearTextField = new TextField("Holiday Year");
        holidayYearTextField.setAllowedCharPattern("[0-9]");
        holidayYearTextField.setMinLength(4);
        holidayYearTextField.setMaxLength(4);
        holidayYearTextField.setClearButtonVisible(true);
        holidayYearTextField.setRequired(true);
        holidayYearTextField.setRequiredIndicatorVisible(true);
        if (calendarHolidaysDTO != null) holidayYearTextField.setValue(String.valueOf(calendarHolidaysDTO.getHolidayYear()));

        holidayDatePicker = new DatePicker("Holiday Date");
        holidayDatePicker.setMin(LocalDate.of(Calendar.YEAR, 1, 1));
        holidayDatePicker.setRequired(true);
        holidayDatePicker.setRequiredIndicatorVisible(true);
        if (calendarHolidaysDTO != null) holidayDatePicker.setValue(calendarHolidaysDTO.getHolidayDate());

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(buttonClickEvent -> {
            saveOrUpdateCalendarHolidaysDTO();
            saveButton.getUI().ifPresent(ui -> ui.navigate(CalendarHolidaysListView.class));
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(buttonClickEvent -> cancelButton.getUI().ifPresent(ui -> ui.navigate(CalendarHolidaysListView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        buttonLayout.setMaxWidth("720px");
        buttonLayout.setPadding(true);

        calendarHolidaysDTOFormLayout.add(holidayTypeComboBox,
                                          holidayDescriptionTextField,
                                          holidayYearTextField,
                                          holidayDatePicker,
                                          buttonLayout);
        calendarHolidaysDTOFormLayout.setColspan(buttonLayout, 2);
        calendarHolidaysDTOFormLayout.setMaxWidth("720px");
    }

    private void saveOrUpdateCalendarHolidaysDTO() {
        String loggedInUser = Objects.requireNonNull(SecurityUtil.getAuthenticatedUser()).getUsername();

        if (parameterId != null) {
            calendarHolidaysDTO = calendarHolidaysService.getById(parameterId);
        } else {
            calendarHolidaysDTO = new CalendarHolidaysDTO();
            calendarHolidaysDTO.setCreatedBy(loggedInUser);
        }

        calendarHolidaysDTO.setHolidayType(holidayTypeComboBox.getValue());
        calendarHolidaysDTO.setHolidayDescription(holidayDescriptionTextField.getValue());
        calendarHolidaysDTO.setHolidayYear(Integer.valueOf(holidayYearTextField.getValue()));
        calendarHolidaysDTO.setHolidayDate(holidayDatePicker.getValue());
        calendarHolidaysDTO.setUpdatedBy(loggedInUser);

        calendarHolidaysService.saveOrUpdate(calendarHolidaysDTO);

        // Show notification message.
        Notification notification = Notification.show("You have successfully saved a calendar holiday reference.",  5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
