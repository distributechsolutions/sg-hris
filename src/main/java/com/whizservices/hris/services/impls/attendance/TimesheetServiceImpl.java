package com.whizservices.hris.services.impls.attendance;

import com.whizservices.hris.dtos.attendance.TimesheetDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.entities.attendance.Timesheet;
import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.repositories.attendance.TimesheetRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.impls.profile.EmployeeServiceImpl;
import com.whizservices.hris.services.attendance.TimesheetService;
import com.whizservices.hris.services.profile.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TimesheetServiceImpl implements TimesheetService {
    private final Logger logger = LoggerFactory.getLogger(TimesheetServiceImpl.class);
    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;

    public TimesheetServiceImpl(TimesheetRepository timesheetRepository,
                                EmployeeRepository employeeRepository) {
        this.timesheetRepository = timesheetRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(TimesheetDTO object) {
        Timesheet timesheet;
        String logMessage;

        if (object.getId() == null) {
            timesheet = new Timesheet();
            timesheet.setCreatedBy(object.getCreatedBy());
            timesheet.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's timesheet record is successfully created.";
        } else {
            timesheet = timesheetRepository.findById(object.getId()).get();
            logMessage = String.format("Employee's timesheet record with id %s is successfully updated.", object.getId());
        }

        timesheet.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        timesheet.setLogDate(object.getLogDate());
        timesheet.setLogTimeIn(object.getLogTimeIn());
        timesheet.setLogTimeOut(object.getLogTimeOut());
        timesheet.setShiftSchedule(object.getShiftSchedule());
        timesheet.setExceptionRemarks(object.getExceptionRemarks());
        timesheet.setLeaveRemarks(object.getLeaveRemarks());
        timesheet.setRegularWorkedHours(object.getRegularWorkedHours());
        timesheet.setOvertimeWorkedHours(object.getOvertimeWorkedHours());
        timesheet.setTotalWorkedHours(object.getTotalWorkedHours());
        timesheet.setStatus(object.getStatus());
        timesheet.setUpdatedBy(object.getUpdatedBy());
        timesheet.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        timesheetRepository.save(timesheet);
        logger.info(logMessage);
    }

    @Override
    public TimesheetDTO getById(UUID id) {
        logger.info(String.format("Retrieving employee's timesheet record with UUID %s", id));

        Timesheet timesheet = timesheetRepository.getReferenceById(id);
        Employee employee = employeeRepository.getReferenceById(timesheet.getEmployee().getId());

        TimesheetDTO timesheetDTO = new TimesheetDTO();
        timesheetDTO.setId(timesheet.getId());
        timesheetDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employee.getId()));
        timesheetDTO.setLogDate(timesheet.getLogDate());
        timesheetDTO.setLogTimeIn(timesheet.getLogTimeIn());
        timesheetDTO.setLogTimeOut(timesheet.getLogTimeOut());
        timesheetDTO.setShiftSchedule(timesheet.getShiftSchedule());
        timesheetDTO.setExceptionRemarks(timesheet.getExceptionRemarks());
        timesheetDTO.setLeaveRemarks(timesheet.getLeaveRemarks());
        timesheetDTO.setRegularWorkedHours(timesheet.getRegularWorkedHours());
        timesheetDTO.setOvertimeWorkedHours(timesheet.getOvertimeWorkedHours());
        timesheetDTO.setTotalWorkedHours(timesheet.getTotalWorkedHours());
        timesheetDTO.setStatus(timesheet.getStatus());
        timesheetDTO.setCreatedBy(timesheet.getCreatedBy());
        timesheetDTO.setDateAndTimeUpdated(timesheet.getDateAndTimeUpdated());
        timesheetDTO.setUpdatedBy(timesheet.getUpdatedBy());
        timesheetDTO.setDateAndTimeUpdated(timesheet.getDateAndTimeUpdated());

        logger.info(String.format("Employee's timesheet record with id %s is successfully retrieved.", timesheet.getId()));

        return timesheetDTO;
    }

    @Override
    public void delete(TimesheetDTO object) {
        if (object.getId() != null) {
            logger.warn("You are about to delete the employee's timesheet record permanently.");

            Timesheet timesheet = timesheetRepository.getReferenceById(object.getId());
            timesheetRepository.delete(timesheet);

            logger.info(String.format("Employee's timesheet record with id %s is deleted.", object.getId()));
        }
    }

    @Override
    public List<TimesheetDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's timesheet records from the database.");
        List<Timesheet> timesheets = timesheetRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's timesheet records has successfully retrieved.");
        List<TimesheetDTO> timesheetDTOList = new ArrayList<>();

        if (!timesheets.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (Timesheet timesheet : timesheets) {
                TimesheetDTO timesheetDTO = new TimesheetDTO();

                timesheetDTO.setId(timesheet.getId());
                timesheetDTO.setEmployeeDTO(employeeService.getById(timesheet.getEmployee().getId()));
                timesheetDTO.setLogDate(timesheet.getLogDate());
                timesheetDTO.setLogTimeIn(timesheet.getLogTimeIn());
                timesheetDTO.setLogTimeOut(timesheet.getLogTimeOut());
                timesheetDTO.setShiftSchedule(timesheet.getShiftSchedule());
                timesheetDTO.setExceptionRemarks(timesheet.getExceptionRemarks());
                timesheetDTO.setLeaveRemarks(timesheet.getLeaveRemarks());
                timesheetDTO.setRegularWorkedHours(timesheet.getRegularWorkedHours());
                timesheetDTO.setOvertimeWorkedHours(timesheet.getOvertimeWorkedHours());
                timesheetDTO.setTotalWorkedHours(timesheet.getTotalWorkedHours());
                timesheetDTO.setStatus(timesheet.getStatus());
                timesheetDTO.setCreatedBy(timesheet.getCreatedBy());
                timesheetDTO.setDateAndTimeCreated(timesheet.getDateAndTimeCreated());
                timesheetDTO.setUpdatedBy(timesheet.getUpdatedBy());
                timesheetDTO.setDateAndTimeUpdated(timesheet.getDateAndTimeUpdated());

                timesheetDTOList.add(timesheetDTO);
            }

            logger.info(String.format("%s records have successfully retrieved.", timesheetDTOList.size()));
        }

        return timesheetDTOList;
    }

    @Override
    public List<TimesheetDTO> findByParameter(String param) {
        List<TimesheetDTO> timesheetDTOList = new ArrayList<>();
        List<Timesheet> timesheetList = null;

        if (param != null && !param.isEmpty()) {
            logger.info("Retrieving employee's timesheet records from the database.");
            timesheetList = timesheetRepository.findTimesheetByStringParameter(param);

            if (timesheetList != null && !timesheetList.isEmpty()) {
                logger.info("Employee's timesheet records has successfully retrieved.");

                EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

                for (Timesheet timesheet : timesheetList) {
                    TimesheetDTO timesheetDTO = new TimesheetDTO();

                    timesheetDTO.setId(timesheet.getId());
                    timesheetDTO.setEmployeeDTO(employeeService.getById(timesheet.getEmployee().getId()));
                    timesheetDTO.setLogDate(timesheet.getLogDate());
                    timesheetDTO.setLogTimeIn(timesheet.getLogTimeIn());
                    timesheetDTO.setLogTimeOut(timesheet.getLogTimeOut());
                    timesheetDTO.setShiftSchedule(timesheet.getShiftSchedule());
                    timesheetDTO.setExceptionRemarks(timesheet.getExceptionRemarks());
                    timesheetDTO.setLeaveRemarks(timesheet.getLeaveRemarks());
                    timesheetDTO.setRegularWorkedHours(timesheet.getRegularWorkedHours());
                    timesheetDTO.setOvertimeWorkedHours(timesheet.getOvertimeWorkedHours());
                    timesheetDTO.setTotalWorkedHours(timesheet.getTotalWorkedHours());
                    timesheetDTO.setStatus(timesheet.getStatus());
                    timesheetDTO.setCreatedBy(timesheet.getCreatedBy());
                    timesheetDTO.setDateAndTimeCreated(timesheet.getDateAndTimeCreated());
                    timesheetDTO.setUpdatedBy(timesheet.getUpdatedBy());
                    timesheetDTO.setDateAndTimeUpdated(timesheet.getDateAndTimeUpdated());

                    timesheetDTOList.add(timesheetDTO);
                }

                logger.info(String.format("%s records have successfully retrieved.", timesheetDTOList.size()));
            }
        }

        return timesheetDTOList;
    }

    @Override
    public List<TimesheetDTO> findByEmployeeDTO(EmployeeDTO employeeDTO) {
        List<TimesheetDTO> timesheetDTOList = new ArrayList<>();
        List<Timesheet> timesheetList = null;

        if (employeeDTO != null) {
            logger.info("Retrieving employee's timesheet records from the database.");
            timesheetList = timesheetRepository.findTimesheetByEmployee(employeeRepository.getById(employeeDTO.getId()));

            if (!timesheetList.isEmpty()) {
                logger.info("Employee's timesheet records has successfully retrieved.");
                EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

                for (Timesheet timesheet : timesheetList) {
                    TimesheetDTO timesheetDTO = new TimesheetDTO();

                    timesheetDTO.setId(timesheet.getId());
                    timesheetDTO.setEmployeeDTO(employeeService.getById(timesheet.getEmployee().getId()));
                    timesheetDTO.setLogDate(timesheet.getLogDate());
                    timesheetDTO.setLogTimeIn(timesheet.getLogTimeIn());
                    timesheetDTO.setLogTimeOut(timesheet.getLogTimeOut());
                    timesheetDTO.setShiftSchedule(timesheet.getShiftSchedule());
                    timesheetDTO.setExceptionRemarks(timesheet.getExceptionRemarks());
                    timesheetDTO.setLeaveRemarks(timesheet.getLeaveRemarks());
                    timesheetDTO.setRegularWorkedHours(timesheet.getRegularWorkedHours());
                    timesheetDTO.setOvertimeWorkedHours(timesheet.getOvertimeWorkedHours());
                    timesheetDTO.setTotalWorkedHours(timesheet.getTotalWorkedHours());
                    timesheetDTO.setStatus(timesheet.getStatus());
                    timesheetDTO.setCreatedBy(timesheet.getCreatedBy());
                    timesheetDTO.setDateAndTimeCreated(timesheet.getDateAndTimeCreated());
                    timesheetDTO.setUpdatedBy(timesheet.getUpdatedBy());
                    timesheetDTO.setDateAndTimeUpdated(timesheet.getDateAndTimeUpdated());

                    timesheetDTOList.add(timesheetDTO);
                }

                logger.info(String.format("%s records have successfully retrieved.", timesheetDTOList.size()));
            }
        }

        return timesheetDTOList;
    }
}
