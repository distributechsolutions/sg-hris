package io.softwaregarage.hris.profile.services;

import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.dtos.DocumentProfileDTO;
import io.softwaregarage.hris.commons.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentProfileService extends BaseService<DocumentProfileDTO> {
    @Transactional
    List<DocumentProfileDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
