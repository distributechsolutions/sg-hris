package com.whizservices.hris.services.info;

import com.whizservices.hris.dtos.info.AddressInfoDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AddressInfoService extends BaseService<AddressInfoDTO> {
    @Transactional
    List<AddressInfoDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
