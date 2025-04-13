package io.distributechsolutions.hris.services.compenben;

import io.distributechsolutions.hris.dtos.compenben.AllowanceDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;

import java.math.BigDecimal;

public interface AllowanceService extends BaseService<AllowanceDTO> {
    BigDecimal getSumOfAllowanceByEmployeeDTO(EmployeeDTO employeeDTO);
}
