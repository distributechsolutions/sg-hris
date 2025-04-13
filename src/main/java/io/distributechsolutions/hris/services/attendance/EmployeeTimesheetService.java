package io.distributechsolutions.hris.services.attendance;

import io.distributechsolutions.hris.dtos.attendance.EmployeeTimesheetDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.services.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTimesheetService extends BaseService<EmployeeTimesheetDTO> {
    @Transactional
    List<EmployeeTimesheetDTO> findByEmployeeDTO(EmployeeDTO employeeDTO);

    @Transactional
    List<EmployeeTimesheetDTO> findByLogDateRange(LocalDate startDate, LocalDate endDate);
}
