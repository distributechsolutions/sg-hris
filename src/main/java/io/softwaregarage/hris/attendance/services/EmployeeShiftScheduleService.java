package io.softwaregarage.hris.attendance.services;

import io.softwaregarage.hris.attendance.dtos.EmployeeShiftScheduleDTO;
import io.softwaregarage.hris.commons.BaseService;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmployeeShiftScheduleService extends BaseService<EmployeeShiftScheduleDTO> {
    @Transactional
    List<EmployeeShiftScheduleDTO> getEmployeeShiftScheduleByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO);
}
