package io.softwaregarage.hris.profile.services;

import io.softwaregarage.hris.profile.dtos.AddressProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddressProfileService extends BaseService<AddressProfileDTO> {
    @Transactional
    List<AddressProfileDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
