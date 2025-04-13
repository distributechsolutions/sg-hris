package io.distributechsolutions.hris.services.info;

import io.distributechsolutions.hris.dtos.info.AddressInfoDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddressInfoService extends BaseService<AddressInfoDTO> {
    @Transactional
    List<AddressInfoDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
