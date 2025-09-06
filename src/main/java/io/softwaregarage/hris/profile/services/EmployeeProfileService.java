package io.softwaregarage.hris.profile.services;

import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;

import java.util.List;

public interface EmployeeProfileService extends BaseService<EmployeeProfileDTO> {
    List<EmployeeProfileDTO> getEmployeesWhoAreApprovers();
}
