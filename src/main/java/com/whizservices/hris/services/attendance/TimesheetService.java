package com.whizservices.hris.services.attendance;

import com.whizservices.hris.dtos.attendance.TimesheetDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;

import java.util.List;

public interface TimesheetService extends BaseService<TimesheetDTO> {
    List<TimesheetDTO> findByEmployeeDTO(EmployeeDTO employeeDTO);
}
