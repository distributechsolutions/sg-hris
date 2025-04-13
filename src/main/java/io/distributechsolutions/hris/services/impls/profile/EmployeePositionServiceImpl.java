package io.distributechsolutions.hris.services.impls.profile;

import io.distributechsolutions.hris.dtos.profile.EmployeePositionDTO;
import io.distributechsolutions.hris.entities.profile.EmployeePosition;
import io.distributechsolutions.hris.repositories.reference.PositionRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeePositionRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeRepository;
import io.distributechsolutions.hris.services.reference.PositionService;
import io.distributechsolutions.hris.services.impls.reference.PositionServiceImpl;
import io.distributechsolutions.hris.services.profile.EmployeePositionService;
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
public class EmployeePositionServiceImpl implements EmployeePositionService {
    private final Logger logger = LoggerFactory.getLogger(EmployeePositionServiceImpl.class);

    private final EmployeePositionRepository employeePositionRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    public EmployeePositionServiceImpl(EmployeePositionRepository employeePositionRepository,
                                       EmployeeRepository employeeRepository,
                                       PositionRepository positionRepository) {
        this.employeePositionRepository = employeePositionRepository;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public void saveOrUpdate(EmployeePositionDTO object) {
        EmployeePosition employeePosition;
        String logMessage;

        if (object.getId() != null) {
            employeePosition = employeePositionRepository.getReferenceById(object.getId());
            logMessage = "Employee's position record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            employeePosition = new EmployeePosition();
            employeePosition.setCreatedBy(object.getCreatedBy());
            employeePosition.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's position record is successfully created.";
        }

        employeePosition.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        employeePosition.setPosition(positionRepository.getReferenceById(object.getPositionDTO().getId()));
        employeePosition.setCurrentPosition(object.isCurrentPosition());
        employeePosition.setUpdatedBy(object.getUpdatedBy());
        employeePosition.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeePositionRepository.save(employeePosition);
        logger.info(logMessage);
    }

    @Override
    public EmployeePositionDTO getById(UUID id) {
        logger.info("Retrieving employee's position record with UUID ".concat(id.toString()));

        EmployeePosition employeePosition = employeePositionRepository.getReferenceById(id);
        EmployeePositionDTO employeePositionDTO = new EmployeePositionDTO();

        employeePositionDTO.setId(employeePosition.getId());
        employeePositionDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employeePosition.getEmployee().getId()));
        employeePositionDTO.setPositionDTO(new PositionServiceImpl(positionRepository).getById(employeePosition.getPosition().getId()));
        employeePositionDTO.setCurrentPosition(employeePosition.isCurrentPosition());
        employeePositionDTO.setCreatedBy(employeePosition.getCreatedBy());
        employeePositionDTO.setDateAndTimeCreated(employeePosition.getDateAndTimeCreated());
        employeePositionDTO.setUpdatedBy(employeePosition.getUpdatedBy());
        employeePositionDTO.setDateAndTimeUpdated(employeePosition.getDateAndTimeUpdated());

        logger.info("Employee's position record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return employeePositionDTO;
    }

    @Override
    public void delete(EmployeePositionDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's position record permanently.");

            String id = object.getId().toString();
            EmployeePosition employeePosition = employeePositionRepository.getReferenceById(object.getId());
            employeePositionRepository.delete(employeePosition);

            logger.info("Employee's position record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<EmployeePositionDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's position records from the database.");
        List<EmployeePosition> employeePositionList = employeePositionRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's position records successfully retrieved.");
        List<EmployeePositionDTO> employeePositionDTOList = new ArrayList<>();

        if (!employeePositionList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
            PositionService positionService = new PositionServiceImpl(positionRepository);

            for (EmployeePosition employeePosition : employeePositionList) {
                EmployeePositionDTO employeePositionDTO = new EmployeePositionDTO();

                employeePositionDTO.setId(employeePosition.getId());
                employeePositionDTO.setEmployeeDTO(employeeService.getById(employeePosition.getEmployee().getId()));
                employeePositionDTO.setPositionDTO(positionService.getById(employeePosition.getPosition().getId()));
                employeePositionDTO.setCurrentPosition(employeePosition.isCurrentPosition());
                employeePositionDTO.setCreatedBy(employeePosition.getCreatedBy());
                employeePositionDTO.setDateAndTimeCreated(employeePosition.getDateAndTimeCreated());
                employeePositionDTO.setUpdatedBy(employeePosition.getUpdatedBy());
                employeePositionDTO.setDateAndTimeUpdated(employeePosition.getDateAndTimeUpdated());

                employeePositionDTOList.add(employeePositionDTO);
            }

            logger.info(String.valueOf(employeePositionList.size()).concat(" record(s) found."));
        }

        return employeePositionDTOList;
    }

    @Override
    public List<EmployeePositionDTO> findByParameter(String param) {
        List<EmployeePositionDTO> employeePositionDTOList = new ArrayList<>();
        List<EmployeePosition> employeePositionList = null;

        logger.info("Retrieving employee's position records with search parameter '%".concat(param).concat("%' from the database."));

        if (param.equalsIgnoreCase("Yes") || param.equalsIgnoreCase("No")) {
            employeePositionList = employeePositionRepository.findByBooleanParameter(param.equalsIgnoreCase("Yes"));
        } else {
            employeePositionList = employeePositionRepository.findByStringParameter(param);
        }

        if (!employeePositionList.isEmpty()) {
            logger.info("Employee's position records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
            PositionService positionService = new PositionServiceImpl(positionRepository);

            for (EmployeePosition employeePosition : employeePositionList) {
                EmployeePositionDTO employeePositionDTO = new EmployeePositionDTO();

                employeePositionDTO.setId(employeePosition.getId());
                employeePositionDTO.setEmployeeDTO(employeeService.getById(employeePosition.getEmployee().getId()));
                employeePositionDTO.setPositionDTO(positionService.getById(employeePosition.getPosition().getId()));
                employeePositionDTO.setCurrentPosition(employeePosition.isCurrentPosition());
                employeePositionDTO.setCreatedBy(employeePosition.getCreatedBy());
                employeePositionDTO.setDateAndTimeCreated(employeePosition.getDateAndTimeCreated());
                employeePositionDTO.setUpdatedBy(employeePosition.getUpdatedBy());
                employeePositionDTO.setDateAndTimeUpdated(employeePosition.getDateAndTimeUpdated());

                employeePositionDTOList.add(employeePositionDTO);
            }
        }

        return employeePositionDTOList;
    }
}
