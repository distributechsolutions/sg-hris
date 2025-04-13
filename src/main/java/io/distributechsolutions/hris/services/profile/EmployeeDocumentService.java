package io.distributechsolutions.hris.services.profile;

import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDocumentDTO;
import io.distributechsolutions.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeDocumentService extends BaseService<EmployeeDocumentDTO> {
    @Transactional
    List<EmployeeDocumentDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);
}
