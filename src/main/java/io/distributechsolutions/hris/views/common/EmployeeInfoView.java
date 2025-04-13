package io.distributechsolutions.hris.views.common;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.distributechsolutions.hris.services.admin.UserService;
import io.distributechsolutions.hris.services.info.AddressInfoService;
import io.distributechsolutions.hris.services.info.DependentInfoService;
import io.distributechsolutions.hris.services.info.PersonalInfoService;
import io.distributechsolutions.hris.services.profile.EmployeeService;
import io.distributechsolutions.hris.services.reference.BarangayService;
import io.distributechsolutions.hris.services.reference.MunicipalityService;
import io.distributechsolutions.hris.services.reference.ProvinceService;
import io.distributechsolutions.hris.services.reference.RegionService;
import io.distributechsolutions.hris.views.MainLayout;

import io.distributechsolutions.hris.views.info.AddressInfoForm;
import io.distributechsolutions.hris.views.info.DependentInfoForm;
import io.distributechsolutions.hris.views.info.PersonalInfoForm;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Employee Information")
@Route(value = "employee-information", layout = MainLayout.class)
public class EmployeeInfoView extends Div {
    @Resource private final PersonalInfoService personalInfoService;
    @Resource private final AddressInfoService addressInfoService;
    @Resource private final DependentInfoService dependentInfoService;
    @Resource private final UserService userService;
    @Resource private final EmployeeService employeeService;
    @Resource private final RegionService regionService;
    @Resource private final ProvinceService provinceService;
    @Resource private final MunicipalityService municipalityService;
    @Resource private final BarangayService barangayService;

    private TabSheet infoTabSheets = new TabSheet();

    public EmployeeInfoView(PersonalInfoService personalInfoService,
                            AddressInfoService addressInfoService,
                            DependentInfoService dependentInfoService,
                            UserService userService,
                            EmployeeService employeeService,
                            RegionService regionService,
                            ProvinceService provinceService,
                            MunicipalityService municipalityService,
                            BarangayService barangayService) {
        this.personalInfoService = personalInfoService;
        this.addressInfoService = addressInfoService;
        this.dependentInfoService = dependentInfoService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.regionService = regionService;
        this.provinceService = provinceService;
        this.municipalityService = municipalityService;
        this.barangayService = barangayService;

        this.setWidth("100%");
        this.createInfoTabSheets();
        this.add(infoTabSheets);
    }

    public void createInfoTabSheets() {
        PersonalInfoForm personalInfoForm = new PersonalInfoForm(personalInfoService,
                                                                 userService,
                                                                 employeeService);
        AddressInfoForm addressInfoForm = new AddressInfoForm(addressInfoService,
                                                              userService,
                                                              employeeService,
                                                              regionService,
                                                              provinceService,
                                                              municipalityService,
                                                              barangayService);
        DependentInfoForm dependentInfoForm = new DependentInfoForm(dependentInfoService,
                                                                    userService,
                                                                    employeeService);

        infoTabSheets.add("Personal", personalInfoForm);
        infoTabSheets.add("Addresses", addressInfoForm);
        infoTabSheets.add("Dependents", dependentInfoForm);
    }

}
