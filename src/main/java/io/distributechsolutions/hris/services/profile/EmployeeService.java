package io.distributechsolutions.hris.services.profile;

import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;

import java.util.List;

public interface EmployeeService extends BaseService<EmployeeDTO> {
    List<EmployeeDTO> getEmployeesWhoAreApprovers();
}
