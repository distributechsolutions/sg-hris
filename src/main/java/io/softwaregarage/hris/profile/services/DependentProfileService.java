package io.softwaregarage.hris.profile.services;

import io.softwaregarage.hris.profile.dtos.DependentProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DependentProfileService extends BaseService<DependentProfileDTO> {
    @Transactional
    List<DependentProfileDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
