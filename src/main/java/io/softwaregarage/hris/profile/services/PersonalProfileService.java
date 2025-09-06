package io.softwaregarage.hris.profile.services;

import io.softwaregarage.hris.profile.dtos.PersonalProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;
import org.springframework.transaction.annotation.Transactional;

public interface PersonalProfileService extends BaseService<PersonalProfileDTO> {
    @Transactional
    PersonalProfileDTO getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
