package io.softwaregarage.hris.attendance.services;

import io.softwaregarage.hris.attendance.dtos.EmployeeTimesheetDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.commons.BaseService;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTimesheetService extends BaseService<EmployeeTimesheetDTO> {
    @Transactional
    List<EmployeeTimesheetDTO> findByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);

    @Transactional
    List<EmployeeTimesheetDTO> findTimesheetByEmployeeAndLogDate(EmployeeProfileDTO employeeProfileDTO,
                                                                 LocalDate startDate,
                                                                 LocalDate endDate);

    @Transactional
    List<EmployeeTimesheetDTO> findByLogDateRange(LocalDate startDate, LocalDate endDate);
}
