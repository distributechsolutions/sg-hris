package com.whizservices.hris.services.impls.info;

import com.whizservices.hris.dtos.info.PersonalInfoDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.entities.info.PersonalInfo;
import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.repositories.info.PersonalInfoRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.impls.profile.EmployeeServiceImpl;
import com.whizservices.hris.services.info.PersonalInfoService;
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
public class PersonalInfoServiceImpl implements PersonalInfoService {
    private final Logger logger = LoggerFactory.getLogger(PersonalInfoServiceImpl.class);

    private final PersonalInfoRepository personalInfoRepository;
    private final EmployeeRepository employeeRepository;

    public PersonalInfoServiceImpl(PersonalInfoRepository personalInfoRepository,
                                   EmployeeRepository employeeRepository) {
        this.personalInfoRepository = personalInfoRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(PersonalInfoDTO object) {
        PersonalInfo personalInfo;
        String logMessage;

        if (object.getId() != null) {
            personalInfo = personalInfoRepository.getReferenceById(object.getId());
            logMessage = "Personal record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            personalInfo = new PersonalInfo();
            personalInfo.setCreatedBy(object.getCreatedBy());
            personalInfo.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Personal record is successfully created.";
        }

        personalInfo.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        personalInfo.setDateOfBirth(object.getDateOfBirth());
        personalInfo.setPlaceOfBirth(object.getPlaceOfBirth());
        personalInfo.setMaritalStatus(object.getMaritalStatus());
        personalInfo.setMaidenName(object.getMaidenName());
        personalInfo.setSpouseName(object.getSpouseName());
        personalInfo.setContactNumber(object.getContactNumber());
        personalInfo.setEmailAddress(object.getEmailAddress());
        personalInfo.setTaxIdentificationNumber(object.getTaxIdentificationNumber());
        personalInfo.setSssNumber(object.getSssNumber());
        personalInfo.setHdmfNumber(object.getHdmfNumber());
        personalInfo.setPhilhealthNumber(object.getPhilhealthNumber());
        personalInfo.setUpdatedBy(object.getUpdatedBy());
        personalInfo.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        personalInfoRepository.save(personalInfo);
        logger.info(logMessage);
    }

    @Override
    public PersonalInfoDTO getById(UUID id) {
        logger.info("Retrieving personal record with UUID ".concat(id.toString()));

        PersonalInfo personalInfo = personalInfoRepository.getReferenceById(id);
        PersonalInfoDTO personalInfoDTO = new PersonalInfoDTO();

        personalInfoDTO.setId(personalInfo.getId());
        personalInfoDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(personalInfo.getEmployee().getId()));
        personalInfoDTO.setDateOfBirth(personalInfo.getDateOfBirth());
        personalInfoDTO.setPlaceOfBirth(personalInfo.getPlaceOfBirth());
        personalInfoDTO.setMaritalStatus(personalInfo.getMaritalStatus());
        personalInfoDTO.setMaidenName(personalInfo.getMaidenName());
        personalInfoDTO.setSpouseName(personalInfo.getSpouseName());
        personalInfoDTO.setContactNumber(personalInfo.getContactNumber());
        personalInfoDTO.setEmailAddress(personalInfo.getEmailAddress());
        personalInfoDTO.setTaxIdentificationNumber(personalInfo.getTaxIdentificationNumber());
        personalInfoDTO.setSssNumber(personalInfo.getSssNumber());
        personalInfoDTO.setHdmfNumber(personalInfo.getHdmfNumber());
        personalInfoDTO.setPhilhealthNumber(personalInfo.getPhilhealthNumber());
        personalInfoDTO.setCreatedBy(personalInfo.getCreatedBy());
        personalInfoDTO.setDateAndTimeCreated(personalInfo.getDateAndTimeCreated());
        personalInfoDTO.setUpdatedBy(personalInfo.getUpdatedBy());
        personalInfoDTO.setDateAndTimeUpdated(personalInfo.getDateAndTimeUpdated());

        return personalInfoDTO;
    }

    @Override
    public void delete(PersonalInfoDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the personal record permanently.");

            String id = object.getId().toString();
            PersonalInfo personalInfo = personalInfoRepository.getReferenceById(object.getId());
            personalInfoRepository.delete(personalInfo);

            logger.info("Personal record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<PersonalInfoDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving personal records from the database.");
        List<PersonalInfo> personalInfoList = personalInfoRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Personal records successfully retrieved.");
        List<PersonalInfoDTO> personalInfoDTOList = new ArrayList<>();

        if (!personalInfoList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (PersonalInfo personalInfo : personalInfoList) {
                PersonalInfoDTO personalInfoDTO = new PersonalInfoDTO();

                personalInfoDTO.setId(personalInfo.getId());
                personalInfoDTO.setEmployeeDTO(employeeService.getById(personalInfo.getEmployee().getId()));
                personalInfoDTO.setDateOfBirth(personalInfo.getDateOfBirth());
                personalInfoDTO.setPlaceOfBirth(personalInfo.getPlaceOfBirth());
                personalInfoDTO.setMaritalStatus(personalInfo.getMaritalStatus());
                personalInfoDTO.setMaidenName(personalInfo.getMaidenName());
                personalInfoDTO.setSpouseName(personalInfo.getSpouseName());
                personalInfoDTO.setContactNumber(personalInfo.getContactNumber());
                personalInfoDTO.setEmailAddress(personalInfo.getEmailAddress());
                personalInfoDTO.setTaxIdentificationNumber(personalInfo.getTaxIdentificationNumber());
                personalInfoDTO.setSssNumber(personalInfo.getSssNumber());
                personalInfoDTO.setHdmfNumber(personalInfo.getHdmfNumber());
                personalInfoDTO.setPhilhealthNumber(personalInfo.getPhilhealthNumber());
                personalInfoDTO.setCreatedBy(personalInfo.getCreatedBy());
                personalInfoDTO.setDateAndTimeCreated(personalInfo.getDateAndTimeCreated());
                personalInfoDTO.setUpdatedBy(personalInfo.getUpdatedBy());
                personalInfoDTO.setDateAndTimeUpdated(personalInfo.getDateAndTimeUpdated());

                personalInfoDTOList.add(personalInfoDTO);
            }

            logger.info(String.valueOf(personalInfoList.size()).concat(" record(s) found."));
        }

        return personalInfoDTOList;
    }

    @Override
    public List<PersonalInfoDTO> findByParameter(String param) {
        return List.of();
    }

    @Override
    public PersonalInfoDTO getByEmployeeDTO(EmployeeDTO employeeDTO) {
        logger.info("Retrieving personal record with employee UUID ".concat(employeeDTO.getId().toString()));

        Employee employee = employeeRepository.getReferenceById(employeeDTO.getId());
        PersonalInfo personalInfo = personalInfoRepository.findByEmployee(employee);
        PersonalInfoDTO personalInfoDTO = null;

        if (personalInfo != null) {
            personalInfoDTO = new PersonalInfoDTO();

            personalInfoDTO.setId(personalInfo.getId());
            personalInfoDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employee.getId()));
            personalInfoDTO.setDateOfBirth(personalInfo.getDateOfBirth());
            personalInfoDTO.setPlaceOfBirth(personalInfo.getPlaceOfBirth());
            personalInfoDTO.setMaritalStatus(personalInfo.getMaritalStatus());
            personalInfoDTO.setMaidenName(personalInfo.getMaidenName());
            personalInfoDTO.setSpouseName(personalInfo.getSpouseName());
            personalInfoDTO.setContactNumber(personalInfo.getContactNumber());
            personalInfoDTO.setEmailAddress(personalInfo.getEmailAddress());
            personalInfoDTO.setTaxIdentificationNumber(personalInfo.getTaxIdentificationNumber());
            personalInfoDTO.setSssNumber(personalInfo.getSssNumber());
            personalInfoDTO.setHdmfNumber(personalInfo.getHdmfNumber());
            personalInfoDTO.setPhilhealthNumber(personalInfo.getPhilhealthNumber());
            personalInfoDTO.setCreatedBy(personalInfo.getCreatedBy());
            personalInfoDTO.setDateAndTimeCreated(personalInfo.getDateAndTimeCreated());
            personalInfoDTO.setUpdatedBy(personalInfo.getUpdatedBy());
            personalInfoDTO.setDateAndTimeUpdated(personalInfo.getDateAndTimeUpdated());
        }

        return personalInfoDTO;
    }
}
