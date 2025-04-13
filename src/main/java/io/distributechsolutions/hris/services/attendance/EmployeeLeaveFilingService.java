package io.distributechsolutions.hris.services.attendance;

import io.distributechsolutions.hris.dtos.attendance.EmployeeLeaveFilingDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeLeaveFilingService extends BaseService<EmployeeLeaveFilingDTO> {
    @Transactional
    List<EmployeeLeaveFilingDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);

    @Transactional
    List<EmployeeLeaveFilingDTO> getByLeaveStatusAndAssignedApproverEmployeeDTO(String leaveStatus, EmployeeDTO assignedApproverEmployeeDTO);
}
