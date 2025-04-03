package com.whizservices.hris.services.attendance;

import com.whizservices.hris.dtos.attendance.EmployeeTimesheetDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.services.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTimesheetService extends BaseService<EmployeeTimesheetDTO> {
    @Transactional
    List<EmployeeTimesheetDTO> findByEmployeeDTO(EmployeeDTO employeeDTO);

    @Transactional
    List<EmployeeTimesheetDTO> findByLogDateRange(LocalDate startDate, LocalDate endDate);
}
