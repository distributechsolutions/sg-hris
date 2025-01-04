package com.whizservices.hris.services.info;

import com.whizservices.hris.dtos.info.PersonalInfoDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

public interface PersonalInfoService extends BaseService<PersonalInfoDTO> {
    @Transactional
    PersonalInfoDTO getByEmployeeDTO(EmployeeDTO employeeDTO);
}
