package com.whizservices.hris.services.profile;

import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.dtos.profile.EmployeeDocumentDTO;
import com.whizservices.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeDocumentService extends BaseService<EmployeeDocumentDTO> {
    @Transactional
    List<EmployeeDocumentDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
