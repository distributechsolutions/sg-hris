package com.whizservices.hris.services.impls.attendance;

import com.whizservices.hris.dtos.attendance.EmployeeShiftScheduleDTO;
import com.whizservices.hris.entities.attendance.EmployeeShiftSchedule;
import com.whizservices.hris.repositories.attendance.EmployeeShiftScheduleRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.attendance.EmployeeShiftScheduleService;
import com.whizservices.hris.services.impls.profile.EmployeeServiceImpl;
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
public class EmployeeShiftScheduleServiceImpl implements EmployeeShiftScheduleService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeShiftScheduleServiceImpl.class);
    private EmployeeShiftScheduleRepository employeeShiftScheduleRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeShiftScheduleServiceImpl(EmployeeShiftScheduleRepository employeeShiftScheduleRepository,
                                            EmployeeRepository employeeRepository) {
        this.employeeShiftScheduleRepository = employeeShiftScheduleRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(EmployeeShiftScheduleDTO object) {
        EmployeeShiftSchedule employeeShiftSchedule;
        String logMessage;

        if (object.getId() != null) {
            employeeShiftSchedule = employeeShiftScheduleRepository.getReferenceById(object.getId());
            logMessage = "Employee's shift record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            employeeShiftSchedule = new EmployeeShiftSchedule();
            employeeShiftSchedule.setCreatedBy(object.getCreatedBy());
            employeeShiftSchedule.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's shift record is successfully created.";
        }

        employeeShiftSchedule.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        employeeShiftSchedule.setShiftSchedule(object.getShiftSchedule());
        employeeShiftSchedule.setShiftHours(object.getShiftHours());
        employeeShiftSchedule.setShiftScheduledDays(object.getShiftScheduledDays());
        employeeShiftSchedule.setShiftStartTime(object.getShiftStartTime());
        employeeShiftSchedule.setShiftEndTime(object.getShiftEndTime());
        employeeShiftSchedule.setActiveShift(object.isActiveShift());
        employeeShiftSchedule.setUpdatedBy(object.getUpdatedBy());
        employeeShiftSchedule.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeeShiftScheduleRepository.save(employeeShiftSchedule);
        logger.info(logMessage);
    }

    @Override
    public EmployeeShiftScheduleDTO getById(UUID id) {
        logger.info("Retrieving employee's shift record with UUID ".concat(id.toString()));

        EmployeeShiftSchedule employeeShiftSchedule = employeeShiftScheduleRepository.getReferenceById(id);
        EmployeeShiftScheduleDTO employeeShiftScheduleDTO = new EmployeeShiftScheduleDTO();

        employeeShiftScheduleDTO.setId(employeeShiftSchedule.getId());
        employeeShiftScheduleDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employeeShiftSchedule.getEmployee().getId()));
        employeeShiftScheduleDTO.setShiftSchedule(employeeShiftSchedule.getShiftSchedule());
        employeeShiftScheduleDTO.setShiftHours(employeeShiftSchedule.getShiftHours());
        employeeShiftScheduleDTO.setShiftScheduledDays(employeeShiftSchedule.getShiftScheduledDays());
        employeeShiftScheduleDTO.setShiftStartTime(employeeShiftSchedule.getShiftStartTime());
        employeeShiftScheduleDTO.setShiftEndTime(employeeShiftSchedule.getShiftEndTime());
        employeeShiftScheduleDTO.setActiveShift(employeeShiftSchedule.isActiveShift());
        employeeShiftScheduleDTO.setCreatedBy(employeeShiftSchedule.getCreatedBy());
        employeeShiftScheduleDTO.setDateAndTimeCreated(employeeShiftSchedule.getDateAndTimeCreated());
        employeeShiftScheduleDTO.setUpdatedBy(employeeShiftSchedule.getUpdatedBy());
        employeeShiftScheduleDTO.setDateAndTimeUpdated(employeeShiftSchedule.getDateAndTimeUpdated());

        logger.info("Employee's shift record with id ".concat(id.toString()).concat(" is successfully retrieved."));
        return employeeShiftScheduleDTO;
    }

    @Override
    public void delete(EmployeeShiftScheduleDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's shift record permanently.");

            String id = object.getId().toString();
            EmployeeShiftSchedule employeeShiftSchedule = employeeShiftScheduleRepository.getReferenceById(object.getId());
            employeeShiftScheduleRepository.delete(employeeShiftSchedule);

            logger.info("Employee's shift record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<EmployeeShiftScheduleDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee shift from the database.");
        List<EmployeeShiftSchedule> employeeShiftScheduleList = employeeShiftScheduleRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee shift successfully retrieved.");
        List<EmployeeShiftScheduleDTO> employeeShiftScheduleDTOList = new ArrayList<>();

        if (!employeeShiftScheduleList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeShiftSchedule employeeShiftSchedule : employeeShiftScheduleList) {
                EmployeeShiftScheduleDTO employeeShiftScheduleDTO = new EmployeeShiftScheduleDTO();

                employeeShiftScheduleDTO.setId(employeeShiftSchedule.getId());
                employeeShiftScheduleDTO.setEmployeeDTO(employeeService.getById(employeeShiftSchedule.getEmployee().getId()));
                employeeShiftScheduleDTO.setShiftSchedule(employeeShiftSchedule.getShiftSchedule());
                employeeShiftScheduleDTO.setShiftHours(employeeShiftSchedule.getShiftHours());
                employeeShiftScheduleDTO.setShiftScheduledDays(employeeShiftSchedule.getShiftScheduledDays());
                employeeShiftScheduleDTO.setShiftStartTime(employeeShiftSchedule.getShiftStartTime());
                employeeShiftScheduleDTO.setShiftEndTime(employeeShiftSchedule.getShiftEndTime());
                employeeShiftScheduleDTO.setActiveShift(employeeShiftSchedule.isActiveShift());
                employeeShiftScheduleDTO.setCreatedBy(employeeShiftSchedule.getCreatedBy());
                employeeShiftScheduleDTO.setDateAndTimeCreated(employeeShiftSchedule.getDateAndTimeCreated());
                employeeShiftScheduleDTO.setUpdatedBy(employeeShiftSchedule.getUpdatedBy());
                employeeShiftScheduleDTO.setDateAndTimeUpdated(employeeShiftSchedule.getDateAndTimeUpdated());

                employeeShiftScheduleDTOList.add(employeeShiftScheduleDTO);
            }

            logger.info(String.valueOf(employeeShiftScheduleList.size()).concat(" record(s) found."));
        }

        return employeeShiftScheduleDTOList;
    }

    @Override
    public List<EmployeeShiftScheduleDTO> findByParameter(String param) {
        return List.of();
    }
}
