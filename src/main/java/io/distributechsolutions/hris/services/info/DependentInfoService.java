package io.distributechsolutions.hris.services.info;

import io.distributechsolutions.hris.dtos.info.DependentInfoDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DependentInfoService extends BaseService<DependentInfoDTO> {
    @Transactional
    List<DependentInfoDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
