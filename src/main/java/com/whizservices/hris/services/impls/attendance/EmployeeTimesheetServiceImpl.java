package com.whizservices.hris.services.impls.attendance;

import com.whizservices.hris.dtos.attendance.EmployeeTimesheetDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.entities.attendance.EmployeeTimesheet;
import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.repositories.attendance.EmployeeShiftScheduleRepository;
import com.whizservices.hris.repositories.attendance.EmployeeTimesheetRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.attendance.EmployeeShiftScheduleService;
import com.whizservices.hris.services.impls.profile.EmployeeServiceImpl;
import com.whizservices.hris.services.attendance.EmployeeTimesheetService;
import com.whizservices.hris.services.profile.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeTimesheetServiceImpl implements EmployeeTimesheetService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeTimesheetServiceImpl.class);
    private final EmployeeTimesheetRepository employeeTimesheetRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeShiftScheduleRepository employeeShiftScheduleRepository;
    private final EmployeeService employeeService;
    private final EmployeeShiftScheduleService employeeShiftScheduleService;

    public EmployeeTimesheetServiceImpl(EmployeeTimesheetRepository employeeTimesheetRepository,
                                        EmployeeRepository employeeRepository,
                                        EmployeeShiftScheduleRepository employeeShiftScheduleRepository,
                                        EmployeeService employeeService,
                                        EmployeeShiftScheduleService employeeShiftScheduleService) {
        this.employeeTimesheetRepository = employeeTimesheetRepository;
        this.employeeRepository = employeeRepository;
        this.employeeShiftScheduleRepository = employeeShiftScheduleRepository;
        this.employeeService = employeeService;
        this.employeeShiftScheduleService = employeeShiftScheduleService;
    }

    @Override
    public void saveOrUpdate(EmployeeTimesheetDTO object) {
        EmployeeTimesheet employeeTimesheet;
        String logMessage;

        if (object.getId() == null) {
            employeeTimesheet = new EmployeeTimesheet();
            employeeTimesheet.setCreatedBy(object.getCreatedBy());
            employeeTimesheet.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's timesheet record is successfully created.";
        } else {
            employeeTimesheet = employeeTimesheetRepository.findById(object.getId()).get();
            logMessage = String.format("Employee's timesheet record with id %s is successfully updated.", object.getId());
        }

        employeeTimesheet.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        employeeTimesheet.setLogDate(object.getLogDate());
        employeeTimesheet.setLogTime(object.getLogTime());
        employeeTimesheet.setLogDetail(object.getLogDetail());
        employeeTimesheet.setLogImage(object.getLogImage());
        employeeTimesheet.setShiftSchedule(employeeShiftScheduleRepository.getReferenceById(object.getShiftScheduleDTO().getId()));
        employeeTimesheet.setStatus(object.getStatus());
        employeeTimesheet.setUpdatedBy(object.getUpdatedBy());
        employeeTimesheet.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeeTimesheetRepository.save(employeeTimesheet);
        logger.info(logMessage);
    }

    @Override
    public EmployeeTimesheetDTO getById(UUID id) {
        logger.info(String.format("Retrieving employee's timesheet record with UUID %s", id));

        EmployeeTimesheet employeeTimesheet = employeeTimesheetRepository.getReferenceById(id);
        Employee employee = employeeRepository.getReferenceById(employeeTimesheet.getEmployee().getId());

        EmployeeTimesheetDTO employeeTimesheetDTO = new EmployeeTimesheetDTO();

        employeeTimesheetDTO.setEmployeeDTO(employeeService.getById(employeeTimesheet.getId()));
        employeeTimesheetDTO.setLogDate(employeeTimesheet.getLogDate());
        employeeTimesheetDTO.setLogTime(employeeTimesheet.getLogTime());
        employeeTimesheetDTO.setLogDetail(employeeTimesheet.getLogDetail());
        employeeTimesheetDTO.setLogImage(employeeTimesheet.getLogImage());
        employeeTimesheetDTO.setShiftScheduleDTO(employeeShiftScheduleService.getById(employeeTimesheet.getShiftSchedule().getId()));
        employeeTimesheetDTO.setStatus(employeeTimesheet.getStatus());
        employeeTimesheetDTO.setCreatedBy(employeeTimesheet.getCreatedBy());
        employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());
        employeeTimesheetDTO.setUpdatedBy(employeeTimesheet.getUpdatedBy());
        employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());

        logger.info(String.format("Employee's timesheet record with id %s is successfully retrieved.", employeeTimesheet.getId()));

        return employeeTimesheetDTO;
    }

    @Override
    public void delete(EmployeeTimesheetDTO object) {
        if (object.getId() != null) {
            logger.warn("You are about to delete the employee's timesheet record permanently.");

            EmployeeTimesheet employeeTimesheet = employeeTimesheetRepository.getReferenceById(object.getId());
            employeeTimesheetRepository.delete(employeeTimesheet);

            logger.info(String.format("Employee's timesheet record with id %s is deleted.", object.getId()));
        }
    }

    @Override
    public List<EmployeeTimesheetDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's timesheet records from the database.");
        List<EmployeeTimesheet> employeeTimesheets = employeeTimesheetRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's timesheet records has successfully retrieved.");
        List<EmployeeTimesheetDTO> employeeTimesheetDTOList = new ArrayList<>();

        if (!employeeTimesheets.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeTimesheet employeeTimesheet : employeeTimesheets) {
                EmployeeTimesheetDTO employeeTimesheetDTO = new EmployeeTimesheetDTO();

                employeeTimesheetDTO.setEmployeeDTO(employeeService.getById(employeeTimesheet.getId()));
                employeeTimesheetDTO.setLogDate(employeeTimesheet.getLogDate());
                employeeTimesheetDTO.setLogTime(employeeTimesheet.getLogTime());
                employeeTimesheetDTO.setLogDetail(employeeTimesheet.getLogDetail());
                employeeTimesheetDTO.setLogImage(employeeTimesheet.getLogImage());
                employeeTimesheetDTO.setShiftScheduleDTO(employeeShiftScheduleService.getById(employeeTimesheet.getShiftSchedule().getId()));
                employeeTimesheetDTO.setStatus(employeeTimesheet.getStatus());
                employeeTimesheetDTO.setCreatedBy(employeeTimesheet.getCreatedBy());
                employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());
                employeeTimesheetDTO.setUpdatedBy(employeeTimesheet.getUpdatedBy());
                employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());

                employeeTimesheetDTOList.add(employeeTimesheetDTO);
            }

            logger.info(String.format("%s records have successfully retrieved.", employeeTimesheetDTOList.size()));
        }

        return employeeTimesheetDTOList;
    }

    @Override
    public List<EmployeeTimesheetDTO> findByParameter(String param) {
        List<EmployeeTimesheetDTO> employeeTimesheetDTOList = new ArrayList<>();
        List<EmployeeTimesheet> employeeTimesheetList = null;

        if (param != null && !param.isEmpty()) {
            logger.info("Retrieving employee's timesheet records from the database.");
            employeeTimesheetList = employeeTimesheetRepository.findTimesheetByStringParameter(param);

            if (employeeTimesheetList != null && !employeeTimesheetList.isEmpty()) {
                logger.info("Employee's timesheet records has successfully retrieved.");

                EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

                for (EmployeeTimesheet employeeTimesheet : employeeTimesheetList) {
                    EmployeeTimesheetDTO employeeTimesheetDTO = new EmployeeTimesheetDTO();

                    employeeTimesheetDTO.setEmployeeDTO(employeeService.getById(employeeTimesheet.getId()));
                    employeeTimesheetDTO.setLogDate(employeeTimesheet.getLogDate());
                    employeeTimesheetDTO.setLogTime(employeeTimesheet.getLogTime());
                    employeeTimesheetDTO.setLogDetail(employeeTimesheet.getLogDetail());
                    employeeTimesheetDTO.setLogImage(employeeTimesheet.getLogImage());
                    employeeTimesheetDTO.setShiftScheduleDTO(employeeShiftScheduleService.getById(employeeTimesheet.getShiftSchedule().getId()));
                    employeeTimesheetDTO.setStatus(employeeTimesheet.getStatus());
                    employeeTimesheetDTO.setCreatedBy(employeeTimesheet.getCreatedBy());
                    employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());
                    employeeTimesheetDTO.setUpdatedBy(employeeTimesheet.getUpdatedBy());
                    employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());

                    employeeTimesheetDTOList.add(employeeTimesheetDTO);
                }

                logger.info(String.format("%s records have successfully retrieved.", employeeTimesheetDTOList.size()));
            }
        }

        return employeeTimesheetDTOList;
    }

    @Override
    public List<EmployeeTimesheetDTO> findByEmployeeDTO(EmployeeDTO employeeDTO) {
        List<EmployeeTimesheetDTO> employeeTimesheetDTOLinkedList = new LinkedList<>();
        List<EmployeeTimesheet> employeeTimesheetList = null;

        if (employeeDTO != null) {
            logger.info("Retrieving employee's timesheet records from the database.");
            employeeTimesheetList = employeeTimesheetRepository.findTimesheetByEmployee(employeeRepository.getById(employeeDTO.getId()));

            if (!employeeTimesheetList.isEmpty()) {
                // Convert list to linked list to retain the order of the result.
                LinkedList<EmployeeTimesheet> employeeTimesheetLinkedList = new LinkedList<>(employeeTimesheetList);

                logger.info("Employee's timesheet records has successfully retrieved.");
                EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

                for (EmployeeTimesheet employeeTimesheet : employeeTimesheetLinkedList) {
                    EmployeeTimesheetDTO employeeTimesheetDTO = new EmployeeTimesheetDTO();

                    employeeTimesheetDTO.setEmployeeDTO(employeeService.getById(employeeTimesheet.getId()));
                    employeeTimesheetDTO.setLogDate(employeeTimesheet.getLogDate());
                    employeeTimesheetDTO.setLogTime(employeeTimesheet.getLogTime());
                    employeeTimesheetDTO.setLogDetail(employeeTimesheet.getLogDetail());
                    employeeTimesheetDTO.setLogImage(employeeTimesheet.getLogImage());
                    employeeTimesheetDTO.setShiftScheduleDTO(employeeShiftScheduleService.getById(employeeTimesheet.getShiftSchedule().getId()));
                    employeeTimesheetDTO.setStatus(employeeTimesheet.getStatus());
                    employeeTimesheetDTO.setCreatedBy(employeeTimesheet.getCreatedBy());
                    employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());
                    employeeTimesheetDTO.setUpdatedBy(employeeTimesheet.getUpdatedBy());
                    employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());

                    employeeTimesheetDTOLinkedList.add(employeeTimesheetDTO);
                }

                logger.info(String.format("%s records have successfully retrieved.", employeeTimesheetDTOLinkedList.size()));
            }
        }

        return employeeTimesheetDTOLinkedList;
    }

    @Override
    public List<EmployeeTimesheetDTO> findByLogDateRange(LocalDate startDate, LocalDate endDate) {
        List<EmployeeTimesheetDTO> employeeTimesheetDTOList = new ArrayList<>();
        List<EmployeeTimesheet> employeeTimesheetList = null;

        if (startDate != null && endDate != null) {
            logger.info("Retrieving employee's timesheet records from the database.");
            employeeTimesheetList = employeeTimesheetRepository.findTimesheetByLogDateRange(startDate, endDate);

            if (!employeeTimesheetList.isEmpty()) {
                logger.info("Employee's timesheet records has successfully retrieved.");
                EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

                for (EmployeeTimesheet employeeTimesheet : employeeTimesheetList) {
                    EmployeeTimesheetDTO employeeTimesheetDTO = new EmployeeTimesheetDTO();

                    employeeTimesheetDTO.setEmployeeDTO(employeeService.getById(employeeTimesheet.getId()));
                    employeeTimesheetDTO.setLogDate(employeeTimesheet.getLogDate());
                    employeeTimesheetDTO.setLogTime(employeeTimesheet.getLogTime());
                    employeeTimesheetDTO.setLogDetail(employeeTimesheet.getLogDetail());
                    employeeTimesheetDTO.setLogImage(employeeTimesheet.getLogImage());
                    employeeTimesheetDTO.setShiftScheduleDTO(employeeShiftScheduleService.getById(employeeTimesheet.getShiftSchedule().getId()));
                    employeeTimesheetDTO.setStatus(employeeTimesheet.getStatus());
                    employeeTimesheetDTO.setCreatedBy(employeeTimesheet.getCreatedBy());
                    employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());
                    employeeTimesheetDTO.setUpdatedBy(employeeTimesheet.getUpdatedBy());
                    employeeTimesheetDTO.setDateAndTimeUpdated(employeeTimesheet.getDateAndTimeUpdated());

                    employeeTimesheetDTOList.add(employeeTimesheetDTO);
                }

                logger.info(String.format("%s records have successfully retrieved.", employeeTimesheetDTOList.size()));
            }
        }

        return employeeTimesheetDTOList;
    }
}
