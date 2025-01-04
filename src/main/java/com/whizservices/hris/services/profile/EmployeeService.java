package com.whizservices.hris.services.profile;

import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;

import java.util.List;

public interface EmployeeService extends BaseService<EmployeeDTO> {
    EmployeeDTO getEmployeeByBiometricId(String biometricId);
    List<EmployeeDTO> getEmployeesWhoAreApprovers();
}
