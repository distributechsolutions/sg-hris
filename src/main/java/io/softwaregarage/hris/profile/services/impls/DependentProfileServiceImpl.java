package io.softwaregarage.hris.profile.services.impls;

import io.softwaregarage.hris.profile.dtos.DependentProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.entities.DependentProfile;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.profile.repositories.DependentProfileRepository;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.profile.services.DependentProfileService;
import io.softwaregarage.hris.profile.services.EmployeeProfileService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DependentProfileServiceImpl implements DependentProfileService {
    private final Logger logger = LoggerFactory.getLogger(DependentProfileService.class);

    private final DependentProfileRepository dependentProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public DependentProfileServiceImpl(DependentProfileRepository dependentProfileRepository,
                                       EmployeeProfileRepository employeeProfileRepository) {
        this.dependentProfileRepository = dependentProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public void saveOrUpdate(DependentProfileDTO object) {
        DependentProfile dependentProfile;
        String logMessage;

        if (object.getId() != null) {
            dependentProfile = dependentProfileRepository.getReferenceById(object.getId());
            logMessage = "Dependent record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            dependentProfile = new DependentProfile();
            dependentProfile.setCreatedBy(object.getCreatedBy());
            dependentProfile.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Dependent record is successfully created.";
        }

        dependentProfile.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        dependentProfile.setFullName(object.getFullName());
        dependentProfile.setDateOfBirth(object.getDateOfBirth());
        dependentProfile.setAge(object.getAge());
        dependentProfile.setRelationship(object.getRelationship());
        dependentProfile.setUpdatedBy(object.getUpdatedBy());
        dependentProfile.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        dependentProfileRepository.save(dependentProfile);
        logger.info(logMessage);
    }

    @Override
    public DependentProfileDTO getById(UUID id) {
        logger.info("Retrieving personnel dependent record with UUID ".concat(id.toString()));

        DependentProfile dependentProfile = dependentProfileRepository.getReferenceById(id);
        DependentProfileDTO dependentProfileDTO = new DependentProfileDTO();

        dependentProfileDTO.setId(dependentProfile.getId());
        dependentProfileDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(dependentProfile.getEmployee().getId()));
        dependentProfileDTO.setFullName(dependentProfile.getFullName());
        dependentProfileDTO.setDateOfBirth(dependentProfile.getDateOfBirth());
        dependentProfileDTO.setAge(dependentProfile.getAge());
        dependentProfileDTO.setRelationship(dependentProfile.getRelationship());
        dependentProfileDTO.setUpdatedBy(dependentProfile.getUpdatedBy());
        dependentProfileDTO.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        return dependentProfileDTO;
    }

    @Override
    public void delete(DependentProfileDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the dependent record permanently.");

            String id = object.getId().toString();
            DependentProfile dependentProfile = dependentProfileRepository.getReferenceById(object.getId());
            dependentProfileRepository.delete(dependentProfile);

            logger.info("Dependent record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<DependentProfileDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving dependent records from the database.");
        List<DependentProfile> dependentProfileList = dependentProfileRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Dependent records successfully retrieved.");
        List<DependentProfileDTO> dependentProfileDTOList = new ArrayList<>();

        if (!dependentProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (DependentProfile dependentProfile : dependentProfileList) {
                DependentProfileDTO dependentProfileDTO = new DependentProfileDTO();

                dependentProfileDTO.setId(dependentProfile.getId());
                dependentProfileDTO.setEmployeeDTO(employeeProfileService.getById(dependentProfile.getEmployee().getId()));
                dependentProfileDTO.setFullName(dependentProfile.getFullName());
                dependentProfileDTO.setDateOfBirth(dependentProfile.getDateOfBirth());
                dependentProfileDTO.setAge(dependentProfile.getAge());
                dependentProfileDTO.setRelationship(dependentProfile.getRelationship());

                dependentProfileDTOList.add(dependentProfileDTO);
            }

            logger.info(String.valueOf(dependentProfileList.size()).concat(" record(s) found."));
        }

        return dependentProfileDTOList;
    }

    @Override
    public List<DependentProfileDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public List<DependentProfileDTO> getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        logger.info("Retrieving dependent records with employee UUID ".concat(employeeProfileDTO.getId().toString()));

        EmployeeProfile employeeProfile = employeeProfileRepository.getReferenceById(employeeProfileDTO.getId());

        List<DependentProfile> dependentProfileList = dependentProfileRepository.findByEmployee(employeeProfile);
        List<DependentProfileDTO> dependentProfileDTOList = new ArrayList<>();

        if (!dependentProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (DependentProfile dependentProfile : dependentProfileList) {
                DependentProfileDTO dependentProfileDTO = new DependentProfileDTO();

                dependentProfileDTO.setId(dependentProfile.getId());
                dependentProfileDTO.setEmployeeDTO(employeeProfileService.getById(dependentProfile.getEmployee().getId()));
                dependentProfileDTO.setFullName(dependentProfile.getFullName());
                dependentProfileDTO.setDateOfBirth(dependentProfile.getDateOfBirth());
                dependentProfileDTO.setAge(dependentProfile.getAge());
                dependentProfileDTO.setRelationship(dependentProfile.getRelationship());

                dependentProfileDTOList.add(dependentProfileDTO);
            }

            logger.info(String.valueOf(dependentProfileList.size()).concat(" record(s) found."));
        }

        return dependentProfileDTOList;
    }
}
