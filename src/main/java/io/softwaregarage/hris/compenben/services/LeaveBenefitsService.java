package io.softwaregarage.hris.compenben.services;

import io.softwaregarage.hris.compenben.dtos.LeaveBenefitsDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LeaveBenefitsService extends BaseService<LeaveBenefitsDTO> {
    @Transactional
    List<LeaveBenefitsDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
