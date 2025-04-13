package io.distributechsolutions.hris.services.impls.profile;

import io.distributechsolutions.hris.dtos.profile.EmployeeDepartmentDTO;
import io.distributechsolutions.hris.entities.profile.EmployeeDepartment;
import io.distributechsolutions.hris.repositories.reference.DepartmentRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeDepartmentRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeRepository;
import io.distributechsolutions.hris.services.reference.DepartmentService;
import io.distributechsolutions.hris.services.impls.reference.DepartmentServiceImpl;
import io.distributechsolutions.hris.services.profile.EmployeeDepartmentService;
import io.distributechsolutions.hris.services.profile.EmployeeService;

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
public class EmployeeDepartmentServiceImpl implements EmployeeDepartmentService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeDepartmentServiceImpl.class);

    private final EmployeeDepartmentRepository employeeDepartmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeDepartmentServiceImpl(EmployeeDepartmentRepository employeeDepartmentRepository,
                                         EmployeeRepository employeeRepository,
                                         DepartmentRepository departmentRepository) {
        this.employeeDepartmentRepository = employeeDepartmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void saveOrUpdate(EmployeeDepartmentDTO object) {
        EmployeeDepartment employeeDepartment;
        String logMessage;

        if (object.getId() != null) {
            employeeDepartment = employeeDepartmentRepository.getReferenceById(object.getId());
            logMessage = "Employee's department record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            employeeDepartment = new EmployeeDepartment();
            employeeDepartment.setCreatedBy(object.getCreatedBy());
            employeeDepartment.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's department record is successfully created.";
        }

        employeeDepartment.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        employeeDepartment.setDepartment(departmentRepository.getReferenceById(object.getDepartmentDTO().getId()));
        employeeDepartment.setCurrentDepartment(object.isCurrentDepartment());
        employeeDepartment.setUpdatedBy(object.getUpdatedBy());
        employeeDepartment.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeeDepartmentRepository.save(employeeDepartment);
        logger.info(logMessage);
    }

    @Override
    public EmployeeDepartmentDTO getById(UUID id) {
        logger.info("Retrieving employee's department record with UUID ".concat(id.toString()));

        EmployeeDepartment employeeDepartment = employeeDepartmentRepository.getReferenceById(id);
        EmployeeDepartmentDTO employeeDepartmentDTO = new EmployeeDepartmentDTO();

        employeeDepartmentDTO.setId(employeeDepartment.getId());
        employeeDepartmentDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employeeDepartment.getEmployee().getId()));
        employeeDepartmentDTO.setDepartmentDTO(new DepartmentServiceImpl(departmentRepository).getById(employeeDepartment.getDepartment().getId()));
        employeeDepartmentDTO.setCurrentDepartment(employeeDepartment.isCurrentDepartment());
        employeeDepartmentDTO.setCreatedBy(employeeDepartment.getCreatedBy());
        employeeDepartmentDTO.setDateAndTimeCreated(employeeDepartment.getDateAndTimeCreated());
        employeeDepartmentDTO.setUpdatedBy(employeeDepartment.getUpdatedBy());
        employeeDepartmentDTO.setDateAndTimeUpdated(employeeDepartment.getDateAndTimeUpdated());

        logger.info("Employee's department record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return employeeDepartmentDTO;
    }

    @Override
    public void delete(EmployeeDepartmentDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's department record permanently.");

            String id = object.getId().toString();
            EmployeeDepartment employeeDepartment = employeeDepartmentRepository.getReferenceById(object.getId());
            employeeDepartmentRepository.delete(employeeDepartment);

            logger.info("Employee's department record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<EmployeeDepartmentDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's department records from the database.");
        List<EmployeeDepartment> employeeDepartmentList = employeeDepartmentRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's department records successfully retrieved.");
        List<EmployeeDepartmentDTO> employeeDepartmentDTOList = new ArrayList<>();

        if (!employeeDepartmentList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
            DepartmentService positionService = new DepartmentServiceImpl(departmentRepository);

            for (EmployeeDepartment employeeDepartment : employeeDepartmentList) {
                EmployeeDepartmentDTO employeeDepartmentDTO = new EmployeeDepartmentDTO();

                employeeDepartmentDTO.setId(employeeDepartment.getId());
                employeeDepartmentDTO.setEmployeeDTO(employeeService.getById(employeeDepartment.getEmployee().getId()));
                employeeDepartmentDTO.setDepartmentDTO(positionService.getById(employeeDepartment.getDepartment().getId()));
                employeeDepartmentDTO.setCurrentDepartment(employeeDepartment.isCurrentDepartment());
                employeeDepartmentDTO.setCreatedBy(employeeDepartment.getCreatedBy());
                employeeDepartmentDTO.setDateAndTimeCreated(employeeDepartment.getDateAndTimeCreated());
                employeeDepartmentDTO.setUpdatedBy(employeeDepartment.getUpdatedBy());
                employeeDepartmentDTO.setDateAndTimeUpdated(employeeDepartment.getDateAndTimeUpdated());

                employeeDepartmentDTOList.add(employeeDepartmentDTO);
            }

            logger.info(String.valueOf(employeeDepartmentList.size()).concat(" record(s) found."));
        }

        return employeeDepartmentDTOList;
    }

    @Override
    public List<EmployeeDepartmentDTO> findByParameter(String param) {
        List<EmployeeDepartmentDTO> employeeDepartmentDTOList = new ArrayList<>();
        List<EmployeeDepartment> employeeDepartmentList = null;

        logger.info("Retrieving employee's department records with search parameter '%".concat(param).concat("%' from the database."));

        if (param.equalsIgnoreCase("Yes") || param.equalsIgnoreCase("No")) {
            employeeDepartmentList = employeeDepartmentRepository.findByBooleanParameter(param.equalsIgnoreCase("Yes"));
        } else {
            employeeDepartmentList = employeeDepartmentRepository.findByStringParameter(param);
        }

        if (!employeeDepartmentList.isEmpty()) {
            logger.info("Employee's department records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
            DepartmentService positionService = new DepartmentServiceImpl(departmentRepository);

            for (EmployeeDepartment employeeDepartment : employeeDepartmentList) {
                EmployeeDepartmentDTO employeeDepartmentDTO = new EmployeeDepartmentDTO();

                employeeDepartmentDTO.setId(employeeDepartment.getId());
                employeeDepartmentDTO.setEmployeeDTO(employeeService.getById(employeeDepartment.getEmployee().getId()));
                employeeDepartmentDTO.setDepartmentDTO(positionService.getById(employeeDepartment.getDepartment().getId()));
                employeeDepartmentDTO.setCurrentDepartment(employeeDepartment.isCurrentDepartment());
                employeeDepartmentDTO.setCreatedBy(employeeDepartment.getCreatedBy());
                employeeDepartmentDTO.setDateAndTimeCreated(employeeDepartment.getDateAndTimeCreated());
                employeeDepartmentDTO.setUpdatedBy(employeeDepartment.getUpdatedBy());
                employeeDepartmentDTO.setDateAndTimeUpdated(employeeDepartment.getDateAndTimeUpdated());

                employeeDepartmentDTOList.add(employeeDepartmentDTO);
            }
        }

        return employeeDepartmentDTOList;
    }
}
