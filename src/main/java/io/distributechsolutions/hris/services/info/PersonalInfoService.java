package io.distributechsolutions.hris.services.info;

import io.distributechsolutions.hris.dtos.info.PersonalInfoDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

public interface PersonalInfoService extends BaseService<PersonalInfoDTO> {
    @Transactional
    PersonalInfoDTO getByEmployeeDTO(EmployeeDTO employeeDTO);
}
