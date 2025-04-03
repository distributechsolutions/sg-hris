package com.whizservices.hris.services.impls.profile;

import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
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
public class EmployeeServiceImpl implements EmployeeService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(EmployeeDTO object) {
        logger.info("Getting the employee data transfer object.");
        logger.info("Preparing the employee object to be saved in the database.");

        Employee employee;
        String logMessage;

        if (object.getId() != null) {
            employee = employeeRepository.getReferenceById(object.getId());
            logMessage = "Employee record with ID ".concat(object.getId().toString()).concat(" has successfully updated.");
        } else {
            employee = new Employee();
            employee.setCreatedBy(object.getCreatedBy());
            employee.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "A new employee record has successfully saved in the database.";
        }

        employee.setEmployeeNumber(object.getEmployeeNumber());
        employee.setLastName(object.getLastName());
        employee.setFirstName(object.getFirstName());
        employee.setMiddleName(object.getMiddleName());
        employee.setSuffix(object.getSuffix());
        employee.setGender(object.getGender());
        employee.setDateHired(object.getDateHired());
        employee.setUpdatedBy(object.getUpdatedBy());
        employee.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeeRepository.save(employee);
        logger.info(logMessage);
    }

    @Override
    public EmployeeDTO getById(UUID id) {
        logger.info("Getting employee record with ID ".concat(id.toString()).concat(" from the database."));
        Employee employee = employeeRepository.getReferenceById(id);

        logger.info("Employee record with ID ".concat(id.toString()).concat(" has successfully retrieved."));
        EmployeeDTO employeeDTO = getEmployeeDTO(employee);

        logger.info("Employee data transfer object has successfully returned.");
        return employeeDTO;
    }

    @Override
    public void delete(EmployeeDTO object) {
        logger.warn("You are about to delete an employee record. Doing this will permanently erase in the database.");

        Employee employee = employeeRepository.getReferenceById(object.getId());
        employeeRepository.delete(employee);

        logger.info("Employee record with ID ".concat(object.getId().toString()).concat(" has successfully deleted in the database."));
    }

    @Override
    public List<EmployeeDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee records from the database.");
        List<Employee> employeeList = employeeRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee records successfully retrieved.");
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        if (!employeeList.isEmpty()) {
            for (Employee employee : employeeList) {
                EmployeeDTO employeeDTO = getEmployeeDTO(employee);
                employeeDTOList.add(employeeDTO);
            }

            logger.info(String.valueOf(employeeList.size()).concat(" record(s) found."));
        }

        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> findByParameter(String param) {
        logger.info("Retrieving employee records with search parameter '%".concat(param).concat("%' from the database."));
        List<Employee> employeeList = employeeRepository.findEmployeesByParameter(param);

        logger.info("Employee records with parameter '%".concat(param).concat("%' has successfully retrieved."));
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        if (!employeeList.isEmpty()) {
            for (Employee employee : employeeList) {
                EmployeeDTO employeeDTO = getEmployeeDTO(employee);
                employeeDTOList.add(employeeDTO);
            }

            logger.info(String.valueOf(employeeList.size()).concat(" record(s) found."));
        }

        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> getEmployeesWhoAreApprovers() {
        logger.info("Retrieving employees who are approvers from the database.");
        List<Employee> employeeList = employeeRepository.findEmployeesWhoAreApprovers();

        logger.info("Employees who are approvers were successfully retrieved.");
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        if (!employeeList.isEmpty()) {
            for (Employee employee : employeeList) {
                EmployeeDTO employeeDTO = getEmployeeDTO(employee);
                employeeDTOList.add(employeeDTO);
            }

            logger.info(String.valueOf(employeeList.size()).concat(" record(s) found."));
        }

        return employeeDTOList;
    }

    /**
     * Returns the employee data transfer object where values comes from the employee object.
     * @param employee - The employee object that contains values from the database.
     * @return The employee data transfer object.
     */
    private static EmployeeDTO getEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setId(employee.getId());
        employeeDTO.setEmployeeNumber(employee.getEmployeeNumber());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setMiddleName(employee.getMiddleName());
        employeeDTO.setSuffix(employee.getSuffix());
        employeeDTO.setGender(employee.getGender());
        employeeDTO.setDateHired(employee.getDateHired());
        employeeDTO.setCreatedBy(employee.getCreatedBy());
        employeeDTO.setDateAndTimeCreated(employee.getDateAndTimeCreated());
        employeeDTO.setUpdatedBy(employee.getUpdatedBy());
        employeeDTO.setDateAndTimeUpdated(employee.getDateAndTimeUpdated());

        return employeeDTO;
    }
}
