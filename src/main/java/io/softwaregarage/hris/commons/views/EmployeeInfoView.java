package io.softwaregarage.hris.commons.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.softwaregarage.hris.admin.services.UserService;
import io.softwaregarage.hris.profile.services.AddressProfileService;
import io.softwaregarage.hris.profile.services.DependentProfileService;
import io.softwaregarage.hris.profile.services.PersonalProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;
import io.softwaregarage.hris.admin.services.BarangayService;
import io.softwaregarage.hris.admin.services.MunicipalityService;
import io.softwaregarage.hris.admin.services.ProvinceService;
import io.softwaregarage.hris.admin.services.RegionService;

import io.softwaregarage.hris.profile.views.AddressProfileFormView;
import io.softwaregarage.hris.profile.views.DependentProfileFormView;
import io.softwaregarage.hris.profile.views.PersonalProfileFormView;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Employee Information")
@Route(value = "employee-information", layout = MainLayout.class)
public class EmployeeInfoView extends Div {
    @Resource private final PersonalProfileService personalProfileService;
    @Resource private final AddressProfileService addressProfileService;
    @Resource private final DependentProfileService dependentProfileService;
    @Resource private final UserService userService;
    @Resource private final EmployeeProfileService employeeProfileService;
    @Resource private final RegionService regionService;
    @Resource private final ProvinceService provinceService;
    @Resource private final MunicipalityService municipalityService;
    @Resource private final BarangayService barangayService;

    private TabSheet infoTabSheets = new TabSheet();

    public EmployeeInfoView(PersonalProfileService personalProfileService,
                            AddressProfileService addressProfileService,
                            DependentProfileService dependentProfileService,
                            UserService userService,
                            EmployeeProfileService employeeProfileService,
                            RegionService regionService,
                            ProvinceService provinceService,
                            MunicipalityService municipalityService,
                            BarangayService barangayService) {
        this.personalProfileService = personalProfileService;
        this.addressProfileService = addressProfileService;
        this.dependentProfileService = dependentProfileService;
        this.userService = userService;
        this.employeeProfileService = employeeProfileService;
        this.regionService = regionService;
        this.provinceService = provinceService;
        this.municipalityService = municipalityService;
        this.barangayService = barangayService;

        this.setWidth("100%");
        this.createInfoTabSheets();
        this.add(infoTabSheets);
    }

    public void createInfoTabSheets() {
        PersonalProfileFormView personalProfileFormView = new PersonalProfileFormView(personalProfileService,
                                                                 userService,
                employeeProfileService);
        AddressProfileFormView addressProfileFormView = new AddressProfileFormView(addressProfileService,
                                                              userService,
                employeeProfileService,
                                                              regionService,
                                                              provinceService,
                                                              municipalityService,
                                                              barangayService);
        DependentProfileFormView dependentProfileFormView = new DependentProfileFormView(dependentProfileService,
                                                                    userService,
                employeeProfileService);

        infoTabSheets.add("Personal", personalProfileFormView);
        infoTabSheets.add("Addresses", addressProfileFormView);
        infoTabSheets.add("Dependents", dependentProfileFormView);
    }

}
