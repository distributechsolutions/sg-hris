package io.softwaregarage.hris.attendance.services;

import io.softwaregarage.hris.attendance.dtos.EmployeeLeaveFilingDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeLeaveFilingService extends BaseService<EmployeeLeaveFilingDTO> {
    @Transactional
    List<EmployeeLeaveFilingDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);

    @Transactional
    List<EmployeeLeaveFilingDTO> getByLeaveStatusAndAssignedApproverEmployeeDTO(String leaveStatus, EmployeeProfileDTO assignedApproverEmployeeProfileDTO);
}
