package io.softwaregarage.hris.profile.services.impls;

import io.softwaregarage.hris.admin.repositories.PositionRepository;
import io.softwaregarage.hris.admin.services.PositionService;
import io.softwaregarage.hris.admin.services.impls.PositionServiceImpl;
import io.softwaregarage.hris.profile.dtos.PositionProfileDTO;
import io.softwaregarage.hris.profile.entities.PositionProfile;
import io.softwaregarage.hris.profile.repositories.PositionProfileRepository;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.profile.services.PositionProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;

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
public class PositionProfileServiceImpl implements PositionProfileService {
    private final Logger logger = LoggerFactory.getLogger(PositionProfileServiceImpl.class);

    private final PositionProfileRepository positionProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final PositionRepository positionRepository;

    public PositionProfileServiceImpl(PositionProfileRepository employeePositionProfileRepository,
                                      EmployeeProfileRepository employeeProfileRepository,
                                      PositionRepository positionRepository) {
        this.positionProfileRepository = employeePositionProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public void saveOrUpdate(PositionProfileDTO object) {
        PositionProfile positionProfile;
        String logMessage;

        if (object.getId() != null) {
            positionProfile = positionProfileRepository.getReferenceById(object.getId());
            logMessage = "Employee's position record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            positionProfile = new PositionProfile();
            positionProfile.setCreatedBy(object.getCreatedBy());
            positionProfile.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's position record is successfully created.";
        }

        positionProfile.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        positionProfile.setPosition(positionRepository.getReferenceById(object.getPositionDTO().getId()));
        positionProfile.setCurrentPosition(object.isCurrentPosition());
        positionProfile.setUpdatedBy(object.getUpdatedBy());
        positionProfile.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        positionProfileRepository.save(positionProfile);
        logger.info(logMessage);
    }

    @Override
    public PositionProfileDTO getById(UUID id) {
        logger.info("Retrieving employee's position record with UUID ".concat(id.toString()));

        PositionProfile positionProfile = positionProfileRepository.getReferenceById(id);
        PositionProfileDTO positionProfileDTO = new PositionProfileDTO();

        positionProfileDTO.setId(positionProfile.getId());
        positionProfileDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(positionProfile.getEmployee().getId()));
        positionProfileDTO.setPositionDTO(new PositionServiceImpl(positionRepository).getById(positionProfile.getPosition().getId()));
        positionProfileDTO.setCurrentPosition(positionProfile.isCurrentPosition());
        positionProfileDTO.setCreatedBy(positionProfile.getCreatedBy());
        positionProfileDTO.setDateAndTimeCreated(positionProfile.getDateAndTimeCreated());
        positionProfileDTO.setUpdatedBy(positionProfile.getUpdatedBy());
        positionProfileDTO.setDateAndTimeUpdated(positionProfile.getDateAndTimeUpdated());

        logger.info("Employee's position record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return positionProfileDTO;
    }

    @Override
    public void delete(PositionProfileDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's position record permanently.");

            String id = object.getId().toString();
            PositionProfile positionProfile = positionProfileRepository.getReferenceById(object.getId());
            positionProfileRepository.delete(positionProfile);

            logger.info("Employee's position record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<PositionProfileDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's position records from the database.");
        List<PositionProfile> positionProfileList = positionProfileRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's position records successfully retrieved.");
        List<PositionProfileDTO> positionProfileDTOList = new ArrayList<>();

        if (!positionProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);
            PositionService positionService = new PositionServiceImpl(positionRepository);

            for (PositionProfile positionProfile : positionProfileList) {
                PositionProfileDTO positionProfileDTO = new PositionProfileDTO();

                positionProfileDTO.setId(positionProfile.getId());
                positionProfileDTO.setEmployeeDTO(employeeProfileService.getById(positionProfile.getEmployee().getId()));
                positionProfileDTO.setPositionDTO(positionService.getById(positionProfile.getPosition().getId()));
                positionProfileDTO.setCurrentPosition(positionProfile.isCurrentPosition());
                positionProfileDTO.setCreatedBy(positionProfile.getCreatedBy());
                positionProfileDTO.setDateAndTimeCreated(positionProfile.getDateAndTimeCreated());
                positionProfileDTO.setUpdatedBy(positionProfile.getUpdatedBy());
                positionProfileDTO.setDateAndTimeUpdated(positionProfile.getDateAndTimeUpdated());

                positionProfileDTOList.add(positionProfileDTO);
            }

            logger.info(String.valueOf(positionProfileList.size()).concat(" record(s) found."));
        }

        return positionProfileDTOList;
    }

    @Override
    public List<PositionProfileDTO> findByParameter(String param) {
        List<PositionProfileDTO> positionProfileDTOList = new ArrayList<>();
        List<PositionProfile> positionProfileList;

        logger.info("Retrieving employee's position records with search parameter '%".concat(param).concat("%' from the database."));

        if (param.equalsIgnoreCase("Yes") || param.equalsIgnoreCase("No")) {
            positionProfileList = positionProfileRepository.findByBooleanParameter(param.equalsIgnoreCase("Yes"));
        } else {
            positionProfileList = positionProfileRepository.findByStringParameter(param);
        }

        if (!positionProfileList.isEmpty()) {
            logger.info("Employee's position records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);
            PositionService positionService = new PositionServiceImpl(positionRepository);

            for (PositionProfile positionProfile : positionProfileList) {
                PositionProfileDTO positionProfileDTO = new PositionProfileDTO();

                positionProfileDTO.setId(positionProfile.getId());
                positionProfileDTO.setEmployeeDTO(employeeProfileService.getById(positionProfile.getEmployee().getId()));
                positionProfileDTO.setPositionDTO(positionService.getById(positionProfile.getPosition().getId()));
                positionProfileDTO.setCurrentPosition(positionProfile.isCurrentPosition());
                positionProfileDTO.setCreatedBy(positionProfile.getCreatedBy());
                positionProfileDTO.setDateAndTimeCreated(positionProfile.getDateAndTimeCreated());
                positionProfileDTO.setUpdatedBy(positionProfile.getUpdatedBy());
                positionProfileDTO.setDateAndTimeUpdated(positionProfile.getDateAndTimeUpdated());

                positionProfileDTOList.add(positionProfileDTO);
            }
        }

        return positionProfileDTOList;
    }
}
