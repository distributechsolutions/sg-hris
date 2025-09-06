package io.softwaregarage.hris.compenben.services;

import io.softwaregarage.hris.compenben.dtos.RatesDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;

public interface RatesService extends BaseService<RatesDTO> {
    RatesDTO findByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
