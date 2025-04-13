package io.distributechsolutions.hris.services.compenben;

import io.distributechsolutions.hris.dtos.compenben.RatesDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;

public interface RatesService extends BaseService<RatesDTO> {
    RatesDTO findByEmployeeDTO(EmployeeDTO employeeDTO);
}
