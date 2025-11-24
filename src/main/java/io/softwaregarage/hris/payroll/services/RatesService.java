package io.softwaregarage.hris.payroll.services;

import io.softwaregarage.hris.payroll.dtos.RatesDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;

public interface RatesService extends BaseService<RatesDTO> {
    RatesDTO findByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
