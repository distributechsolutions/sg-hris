package io.distributechsolutions.hris.services.compenben;

import io.distributechsolutions.hris.dtos.compenben.LeaveBenefitsDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LeaveBenefitsService extends BaseService<LeaveBenefitsDTO> {
    @Transactional
    List<LeaveBenefitsDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
