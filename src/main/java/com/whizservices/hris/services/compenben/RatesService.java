package com.whizservices.hris.services.compenben;

import com.whizservices.hris.dtos.compenben.RatesDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;

public interface RatesService extends BaseService<RatesDTO> {
    RatesDTO findByEmployeeDTO(EmployeeDTO employeeDTO);
}
