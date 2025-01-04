package com.whizservices.hris.services.compenben;

import com.whizservices.hris.dtos.compenben.LeaveBenefitsDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LeaveBenefitsService extends BaseService<LeaveBenefitsDTO> {
    @Transactional
    List<LeaveBenefitsDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
