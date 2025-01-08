package com.whizservices.hris.services.compenben;

import com.whizservices.hris.dtos.compenben.AllowanceDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;

import java.math.BigDecimal;

public interface AllowanceService extends BaseService<AllowanceDTO> {
    BigDecimal getSumOfAllowanceByEmployeeDTO(EmployeeDTO employeeDTO);
}
