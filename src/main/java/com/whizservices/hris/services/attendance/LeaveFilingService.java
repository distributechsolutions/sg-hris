package com.whizservices.hris.services.attendance;

import com.whizservices.hris.dtos.attendance.LeaveFilingDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LeaveFilingService extends BaseService<LeaveFilingDTO> {
    @Transactional
    List<LeaveFilingDTO> getByEmployeeDTO(EmployeeDTO employeeDTO);

    @Transactional
    List<LeaveFilingDTO> getByLeaveStatusAndAssignedApproverEmployeeDTO(String leaveStatus, EmployeeDTO assignedApproverEmployeeDTO);
}
