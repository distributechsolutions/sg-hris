package com.whizservices.hris.services.info;

import com.whizservices.hris.dtos.info.DependentInfoDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DependentInfoService extends BaseService<DependentInfoDTO> {
    @Transactional
    List<DependentInfoDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
