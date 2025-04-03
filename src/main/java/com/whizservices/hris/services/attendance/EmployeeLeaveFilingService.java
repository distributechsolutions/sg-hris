package com.whizservices.hris.services.attendance;

import com.whizservices.hris.dtos.attendance.EmployeeLeaveFilingDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeLeaveFilingService extends BaseService<EmployeeLeaveFilingDTO> {
    @Transactional
    List<EmployeeLeaveFilingDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);

    @Transactional
    List<EmployeeLeaveFilingDTO> getByLeaveStatusAndAssignedApproverEmployeeDTO(String leaveStatus, EmployeeDTO assignedApproverEmployeeDTO);
}
