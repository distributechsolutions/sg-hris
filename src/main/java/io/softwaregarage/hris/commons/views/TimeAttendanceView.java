package io.softwaregarage.hris.commons.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

import org.vaadin.vcamera.VCamera;

@PermitAll
@PageTitle("Time Attendance")
@Route(value = "time-attendance", layout = MainLayout.class)
@JsModule("clockAndDate.js")
public class TimeAttendanceView extends VerticalLayout {

    public TimeAttendanceView() {
        this.setSizeFull();
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        this.add(this.buildCameraTimeInAndOut());

        // Call the JavaScript function every second to update the clock
        UI.getCurrent().getPage().executeJs("setInterval(() => updateClock(), 1000);");
    }

    public Component buildCameraTimeInAndOut() {
        // HTML element to display the clock
        Html clockHtml = new Html("<div id='clock' style='font-size: 26px; text-align: center;'></div>");

        VCamera vcamera = new VCamera();
        vcamera.setId("video");
        vcamera.openCamera();
        vcamera.getStyle().setWidth("320px");

        Button timeInAndOutButton = new Button("Time In / Time Out");
        timeInAndOutButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_PRIMARY);
        timeInAndOutButton.setWidth("320px");

        VerticalLayout cameraTimeInAndOutLayout = new VerticalLayout();
        cameraTimeInAndOutLayout.setAlignItems(Alignment.CENTER);
        cameraTimeInAndOutLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        cameraTimeInAndOutLayout.add(clockHtml, vcamera, timeInAndOutButton);

        return cameraTimeInAndOutLayout;
    }
}
