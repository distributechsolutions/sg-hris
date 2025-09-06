package io.softwaregarage.hris.profile.services.impls;

import io.softwaregarage.hris.profile.dtos.PersonalProfileDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;
import io.softwaregarage.hris.profile.entities.PersonalProfile;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.profile.repositories.PersonalProfileRepository;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.profile.services.PersonalProfileService;
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
public class PersonalProfileServiceImpl implements PersonalProfileService {
    private final Logger logger = LoggerFactory.getLogger(PersonalProfileServiceImpl.class);

    private final PersonalProfileRepository personalProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public PersonalProfileServiceImpl(PersonalProfileRepository personalProfileRepository,
                                      EmployeeProfileRepository employeeProfileRepository) {
        this.personalProfileRepository = personalProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public void saveOrUpdate(PersonalProfileDTO object) {
        PersonalProfile personalProfile;
        String logMessage;

        if (object.getId() != null) {
            personalProfile = personalProfileRepository.getReferenceById(object.getId());
            logMessage = "Personal record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            personalProfile = new PersonalProfile();
            personalProfile.setCreatedBy(object.getCreatedBy());
            personalProfile.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Personal record is successfully created.";
        }

        personalProfile.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        personalProfile.setDateOfBirth(object.getDateOfBirth());
        personalProfile.setPlaceOfBirth(object.getPlaceOfBirth());
        personalProfile.setMaritalStatus(object.getMaritalStatus());
        personalProfile.setMaidenName(object.getMaidenName());
        personalProfile.setSpouseName(object.getSpouseName());
        personalProfile.setContactNumber(object.getContactNumber());
        personalProfile.setEmailAddress(object.getEmailAddress());
        personalProfile.setTaxIdentificationNumber(object.getTaxIdentificationNumber());
        personalProfile.setSssNumber(object.getSssNumber());
        personalProfile.setHdmfNumber(object.getHdmfNumber());
        personalProfile.setPhilhealthNumber(object.getPhilhealthNumber());
        personalProfile.setUpdatedBy(object.getUpdatedBy());
        personalProfile.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        personalProfileRepository.save(personalProfile);
        logger.info(logMessage);
    }

    @Override
    public PersonalProfileDTO getById(UUID id) {
        logger.info("Retrieving personal record with UUID ".concat(id.toString()));

        PersonalProfile personalProfile = personalProfileRepository.getReferenceById(id);
        PersonalProfileDTO personalProfileDTO = new PersonalProfileDTO();

        personalProfileDTO.setId(personalProfile.getId());
        personalProfileDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(personalProfile.getEmployee().getId()));
        personalProfileDTO.setDateOfBirth(personalProfile.getDateOfBirth());
        personalProfileDTO.setPlaceOfBirth(personalProfile.getPlaceOfBirth());
        personalProfileDTO.setMaritalStatus(personalProfile.getMaritalStatus());
        personalProfileDTO.setMaidenName(personalProfile.getMaidenName());
        personalProfileDTO.setSpouseName(personalProfile.getSpouseName());
        personalProfileDTO.setContactNumber(personalProfile.getContactNumber());
        personalProfileDTO.setEmailAddress(personalProfile.getEmailAddress());
        personalProfileDTO.setTaxIdentificationNumber(personalProfile.getTaxIdentificationNumber());
        personalProfileDTO.setSssNumber(personalProfile.getSssNumber());
        personalProfileDTO.setHdmfNumber(personalProfile.getHdmfNumber());
        personalProfileDTO.setPhilhealthNumber(personalProfile.getPhilhealthNumber());
        personalProfileDTO.setCreatedBy(personalProfile.getCreatedBy());
        personalProfileDTO.setDateAndTimeCreated(personalProfile.getDateAndTimeCreated());
        personalProfileDTO.setUpdatedBy(personalProfile.getUpdatedBy());
        personalProfileDTO.setDateAndTimeUpdated(personalProfile.getDateAndTimeUpdated());

        return personalProfileDTO;
    }

    @Override
    public void delete(PersonalProfileDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the personal record permanently.");

            String id = object.getId().toString();
            PersonalProfile personalProfile = personalProfileRepository.getReferenceById(object.getId());
            personalProfileRepository.delete(personalProfile);

            logger.info("Personal record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<PersonalProfileDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving personal records from the database.");
        List<PersonalProfile> personalProfileList = personalProfileRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Personal records successfully retrieved.");
        List<PersonalProfileDTO> personalProfileDTOList = new ArrayList<>();

        if (!personalProfileList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (PersonalProfile personalProfile : personalProfileList) {
                PersonalProfileDTO personalProfileDTO = new PersonalProfileDTO();

                personalProfileDTO.setId(personalProfile.getId());
                personalProfileDTO.setEmployeeDTO(employeeProfileService.getById(personalProfile.getEmployee().getId()));
                personalProfileDTO.setDateOfBirth(personalProfile.getDateOfBirth());
                personalProfileDTO.setPlaceOfBirth(personalProfile.getPlaceOfBirth());
                personalProfileDTO.setMaritalStatus(personalProfile.getMaritalStatus());
                personalProfileDTO.setMaidenName(personalProfile.getMaidenName());
                personalProfileDTO.setSpouseName(personalProfile.getSpouseName());
                personalProfileDTO.setContactNumber(personalProfile.getContactNumber());
                personalProfileDTO.setEmailAddress(personalProfile.getEmailAddress());
                personalProfileDTO.setTaxIdentificationNumber(personalProfile.getTaxIdentificationNumber());
                personalProfileDTO.setSssNumber(personalProfile.getSssNumber());
                personalProfileDTO.setHdmfNumber(personalProfile.getHdmfNumber());
                personalProfileDTO.setPhilhealthNumber(personalProfile.getPhilhealthNumber());
                personalProfileDTO.setCreatedBy(personalProfile.getCreatedBy());
                personalProfileDTO.setDateAndTimeCreated(personalProfile.getDateAndTimeCreated());
                personalProfileDTO.setUpdatedBy(personalProfile.getUpdatedBy());
                personalProfileDTO.setDateAndTimeUpdated(personalProfile.getDateAndTimeUpdated());

                personalProfileDTOList.add(personalProfileDTO);
            }

            logger.info(String.valueOf(personalProfileList.size()).concat(" record(s) found."));
        }

        return personalProfileDTOList;
    }

    @Override
    public List<PersonalProfileDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public PersonalProfileDTO getByEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        logger.info("Retrieving personal record with employee UUID ".concat(employeeProfileDTO.getId().toString()));

        EmployeeProfile employeeProfile = employeeProfileRepository.getReferenceById(employeeProfileDTO.getId());
        PersonalProfile personalProfile = personalProfileRepository.findByEmployee(employeeProfile);
        PersonalProfileDTO personalProfileDTO = null;

        if (personalProfile != null) {
            personalProfileDTO = new PersonalProfileDTO();

            personalProfileDTO.setId(personalProfile.getId());
            personalProfileDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(employeeProfile.getId()));
            personalProfileDTO.setDateOfBirth(personalProfile.getDateOfBirth());
            personalProfileDTO.setPlaceOfBirth(personalProfile.getPlaceOfBirth());
            personalProfileDTO.setMaritalStatus(personalProfile.getMaritalStatus());
            personalProfileDTO.setMaidenName(personalProfile.getMaidenName());
            personalProfileDTO.setSpouseName(personalProfile.getSpouseName());
            personalProfileDTO.setContactNumber(personalProfile.getContactNumber());
            personalProfileDTO.setEmailAddress(personalProfile.getEmailAddress());
            personalProfileDTO.setTaxIdentificationNumber(personalProfile.getTaxIdentificationNumber());
            personalProfileDTO.setSssNumber(personalProfile.getSssNumber());
            personalProfileDTO.setHdmfNumber(personalProfile.getHdmfNumber());
            personalProfileDTO.setPhilhealthNumber(personalProfile.getPhilhealthNumber());
            personalProfileDTO.setCreatedBy(personalProfile.getCreatedBy());
            personalProfileDTO.setDateAndTimeCreated(personalProfile.getDateAndTimeCreated());
            personalProfileDTO.setUpdatedBy(personalProfile.getUpdatedBy());
            personalProfileDTO.setDateAndTimeUpdated(personalProfile.getDateAndTimeUpdated());
        }

        return personalProfileDTO;
    }
}
