package io.softwaregarage.hris.profile.services.impls;

import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
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
public class EmployeeProfileServiceImpl implements EmployeeProfileService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeProfileServiceImpl.class);
    private final EmployeeProfileRepository employeeProfileRepository;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository employeeProfileRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public void saveOrUpdate(EmployeeProfileDTO object) {
        logger.info("Getting the employee data transfer object.");
        logger.info("Preparing the employee object to be saved in the database.");

        EmployeeProfile employeeProfile;
        String logMessage;

        if (object.getId() != null) {
            employeeProfile = employeeProfileRepository.getReferenceById(object.getId());
            logMessage = "Employee record with ID ".concat(object.getId().toString()).concat(" has successfully updated.");
        } else {
            employeeProfile = new EmployeeProfile();
            employeeProfile.setCreatedBy(object.getCreatedBy());
            employeeProfile.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "A new employee record has successfully saved in the database.";
        }

        employeeProfile.setEmployeeNumber(object.getEmployeeNumber());
        employeeProfile.setLastName(object.getLastName());
        employeeProfile.setFirstName(object.getFirstName());
        employeeProfile.setMiddleName(object.getMiddleName());
        employeeProfile.setSuffix(object.getSuffix());
        employeeProfile.setGender(object.getGender());
        employeeProfile.setDateHired(object.getDateHired());
        employeeProfile.setEmploymentType(object.getEmploymentType());
        employeeProfile.setContractDuration(object.getContractDuration());
        employeeProfile.setStartDate(object.getStartDate());
        employeeProfile.setEndDate(object.getEndDate());
        employeeProfile.setStatus(object.getStatus());
        employeeProfile.setUpdatedBy(object.getUpdatedBy());
        employeeProfile.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeeProfileRepository.save(employeeProfile);
        logger.info(logMessage);
    }

    @Override
    public EmployeeProfileDTO getById(UUID id) {
        logger.info("Getting employee record with ID ".concat(id.toString()).concat(" from the database."));
        EmployeeProfile employeeProfile = employeeProfileRepository.getReferenceById(id);

        logger.info("Employee record with ID ".concat(id.toString()).concat(" has successfully retrieved."));
        EmployeeProfileDTO employeeProfileDTO = getEmployeeDTO(employeeProfile);

        logger.info("Employee data transfer object has successfully returned.");
        return employeeProfileDTO;
    }

    @Override
    public void delete(EmployeeProfileDTO object) {
        logger.warn("You are about to delete an employee record. Doing this will permanently erase in the database.");

        EmployeeProfile employeeProfile = employeeProfileRepository.getReferenceById(object.getId());
        employeeProfileRepository.delete(employeeProfile);

        logger.info("Employee record with ID ".concat(object.getId().toString()).concat(" has successfully deleted in the database."));
    }

    @Override
    public List<EmployeeProfileDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee records from the database.");
        List<EmployeeProfile> employeeProfileList = employeeProfileRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee records successfully retrieved.");
        List<EmployeeProfileDTO> employeeProfileDTOList = new ArrayList<>();

        if (!employeeProfileList.isEmpty()) {
            for (EmployeeProfile employeeProfile : employeeProfileList) {
                EmployeeProfileDTO employeeProfileDTO = getEmployeeDTO(employeeProfile);
                employeeProfileDTOList.add(employeeProfileDTO);
            }

            logger.info(String.valueOf(employeeProfileList.size()).concat(" record(s) found."));
        }

        return employeeProfileDTOList;
    }

    @Override
    public List<EmployeeProfileDTO> findByParameter(String param) {
        logger.info("Retrieving employee records with search parameter '%".concat(param).concat("%' from the database."));
        List<EmployeeProfile> employeeProfileList = employeeProfileRepository.findEmployeesByParameter(param);

        logger.info("Employee records with parameter '%".concat(param).concat("%' has successfully retrieved."));
        List<EmployeeProfileDTO> employeeProfileDTOList = new ArrayList<>();

        if (!employeeProfileList.isEmpty()) {
            for (EmployeeProfile employeeProfile : employeeProfileList) {
                EmployeeProfileDTO employeeProfileDTO = getEmployeeDTO(employeeProfile);
                employeeProfileDTOList.add(employeeProfileDTO);
            }

            logger.info(String.valueOf(employeeProfileList.size()).concat(" record(s) found."));
        }

        return employeeProfileDTOList;
    }

    @Override
    public List<EmployeeProfileDTO> getEmployeesWhoAreApprovers() {
        logger.info("Retrieving employees who are approvers from the database.");
        List<EmployeeProfile> employeeProfileList = employeeProfileRepository.findEmployeesWhoAreApprovers();

        logger.info("Employees who are approvers were successfully retrieved.");
        List<EmployeeProfileDTO> employeeProfileDTOList = new ArrayList<>();

        if (!employeeProfileList.isEmpty()) {
            for (EmployeeProfile employeeProfile : employeeProfileList) {
                EmployeeProfileDTO employeeProfileDTO = getEmployeeDTO(employeeProfile);
                employeeProfileDTOList.add(employeeProfileDTO);
            }

            logger.info(String.valueOf(employeeProfileList.size()).concat(" record(s) found."));
        }

        return employeeProfileDTOList;
    }

    @Override
    public List<EmployeeProfileDTO> findEmployeesWhoseContractIsNearlyExpired() {
        logger.info("Retrieving contracts that are nearly expire from the database.");
        List<EmployeeProfile> employeeProfileList = employeeProfileRepository.findEmployeesWhoseContractIsNearlyExpired();

        logger.info("Contracts that are nearly expire were successfully retrieved.");
        List<EmployeeProfileDTO> employeeProfileDTOList = new ArrayList<>();

        if (!employeeProfileList.isEmpty()) {
            for (EmployeeProfile employeeProfile : employeeProfileList) {
                EmployeeProfileDTO employeeProfileDTO = getEmployeeDTO(employeeProfile);
                employeeProfileDTOList.add(employeeProfileDTO);
            }

            logger.info(String.valueOf(employeeProfileList.size()).concat(" record(s) found."));
        }

        return employeeProfileDTOList;
    }

    @Override
    public List<EmployeeProfileDTO> findEmployeesWhoseContractIsExpired() {
        logger.info("Retrieving contracts that are expired from the database.");
        List<EmployeeProfile> employeeProfileList = employeeProfileRepository.findEmployeesWhoseContractIsExpired();

        logger.info("Contracts that are expired were successfully retrieved.");
        List<EmployeeProfileDTO> employeeProfileDTOList = new ArrayList<>();

        if (!employeeProfileList.isEmpty()) {
            for (EmployeeProfile employeeProfile : employeeProfileList) {
                EmployeeProfileDTO employeeProfileDTO = getEmployeeDTO(employeeProfile);
                employeeProfileDTOList.add(employeeProfileDTO);
            }

            logger.info(String.valueOf(employeeProfileList.size()).concat(" record(s) found."));
        }

        return employeeProfileDTOList;
    }

    /**
     * Returns the employee data transfer object where values comes from the employee object.
     * @param employeeProfile - The employee object that contains values from the database.
     * @return The employee data transfer object.
     */
    private static EmployeeProfileDTO getEmployeeDTO(EmployeeProfile employeeProfile) {
        EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();

        employeeProfileDTO.setId(employeeProfile.getId());
        employeeProfileDTO.setEmployeeNumber(employeeProfile.getEmployeeNumber());
        employeeProfileDTO.setLastName(employeeProfile.getLastName());
        employeeProfileDTO.setFirstName(employeeProfile.getFirstName());
        employeeProfileDTO.setMiddleName(employeeProfile.getMiddleName());
        employeeProfileDTO.setSuffix(employeeProfile.getSuffix());
        employeeProfileDTO.setGender(employeeProfile.getGender());
        employeeProfileDTO.setDateHired(employeeProfile.getDateHired());
        employeeProfileDTO.setEmploymentType(employeeProfile.getEmploymentType());
        employeeProfileDTO.setContractDuration(employeeProfile.getContractDuration());
        employeeProfileDTO.setStartDate(employeeProfile.getStartDate());
        employeeProfileDTO.setEndDate(employeeProfile.getEndDate());
        employeeProfileDTO.setStatus(employeeProfile.getStatus());
        employeeProfileDTO.setCreatedBy(employeeProfile.getCreatedBy());
        employeeProfileDTO.setDateAndTimeCreated(employeeProfile.getDateAndTimeCreated());
        employeeProfileDTO.setUpdatedBy(employeeProfile.getUpdatedBy());
        employeeProfileDTO.setDateAndTimeUpdated(employeeProfile.getDateAndTimeUpdated());

        return employeeProfileDTO;
    }
}
