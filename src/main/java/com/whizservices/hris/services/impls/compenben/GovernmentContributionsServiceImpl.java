package com.whizservices.hris.services.impls.compenben;

import com.whizservices.hris.dtos.compenben.AllowanceDTO;
import com.whizservices.hris.dtos.compenben.GovernmentContributionsDTO;
import com.whizservices.hris.entities.compenben.GovernmentContributions;
import com.whizservices.hris.repositories.compenben.GovernmentContributionsRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.compenben.GovernmentContributionsService;
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
public class GovernmentContributionsServiceImpl implements GovernmentContributionsService {
    private final Logger logger = LoggerFactory.getLogger(GovernmentContributionsServiceImpl.class);

    private final GovernmentContributionsRepository governmentContributionsRepository;
    private final EmployeeRepository employeeRepository;

    public GovernmentContributionsServiceImpl(GovernmentContributionsRepository governmentContributionsRepository,
                                              EmployeeRepository employeeRepository) {
        this.governmentContributionsRepository = governmentContributionsRepository;
        this.employeeRepository = employeeRepository;
    }


    @Override
    public void saveOrUpdate(GovernmentContributionsDTO object) {
        GovernmentContributions governmentContributions;
        String logMessage;

        if (object.getId() != null) {
            governmentContributions = governmentContributionsRepository.getReferenceById(object.getId());
            logMessage = "Employee's government contributions record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            governmentContributions = new GovernmentContributions();
            governmentContributions.setCreatedBy(object.getCreatedBy());
            governmentContributions.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's government contributions record is successfully created.";
        }

        governmentContributions.setSssContrbutionAmount(object.getSssContributionAmount());
        governmentContributions.setHdmfContrbutionAmount(object.getHdmfContributionAmount());
        governmentContributions.setPhilhealthContributionAmount(object.getPhilhealthContributionAmount());
        governmentContributions.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        governmentContributions.setUpdatedBy(object.getUpdatedBy());
        governmentContributions.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        governmentContributionsRepository.save(governmentContributions);
        logger.info(logMessage);
    }

    @Override
    public GovernmentContributionsDTO getById(UUID id) {
        logger.info("Retrieving employee's government contributions record with UUID ".concat(id.toString()));

        GovernmentContributions governmentContributions = governmentContributionsRepository.getById(id);
        GovernmentContributionsDTO governmentContributionsDTO = new GovernmentContributionsDTO();

        governmentContributionsDTO.setId(governmentContributions.getId());
        governmentContributionsDTO.setSssContributionAmount(governmentContributions.getSssContrbutionAmount());
        governmentContributionsDTO.setHdmfContributionAmount(governmentContributions.getHdmfContrbutionAmount());
        governmentContributionsDTO.setPhilhealthContributionAmount(governmentContributions.getPhilhealthContributionAmount());
        governmentContributionsDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(governmentContributions.getEmployee().getId()));
        governmentContributionsDTO.setCreatedBy(governmentContributions.getCreatedBy());
        governmentContributionsDTO.setDateAndTimeCreated(governmentContributions.getDateAndTimeCreated());
        governmentContributionsDTO.setUpdatedBy(governmentContributions.getUpdatedBy());
        governmentContributionsDTO.setDateAndTimeUpdated(governmentContributions.getDateAndTimeUpdated());

        logger.info("Employee's government contriibutions record with id ".concat(id.toString()).concat(" is successfully retrieved."));
        return governmentContributionsDTO;
    }

    @Override
    public void delete(GovernmentContributionsDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's government contributions record permanently.");

            String id = object.getId().toString();
            GovernmentContributions governmentContributions = governmentContributionsRepository.getReferenceById(object.getId());
            governmentContributionsRepository.delete(governmentContributions);

            logger.info("Employee's allowance record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<GovernmentContributionsDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's government contributions from the database.");
        List<GovernmentContributions> governmentContributionsList = governmentContributionsRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's government contributions successfully retrieved.");
        List<GovernmentContributionsDTO> governmentContributionsDTOList = new ArrayList<>();

        if (!governmentContributionsList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (GovernmentContributions governmentContributions : governmentContributionsList) {
                GovernmentContributionsDTO governmentContributionsDTO = new GovernmentContributionsDTO();

                governmentContributionsDTO.setId(governmentContributions.getId());
                governmentContributionsDTO.setSssContributionAmount(governmentContributions.getSssContrbutionAmount());
                governmentContributionsDTO.setHdmfContributionAmount(governmentContributions.getHdmfContrbutionAmount());
                governmentContributionsDTO.setPhilhealthContributionAmount(governmentContributions.getPhilhealthContributionAmount());
                governmentContributionsDTO.setEmployeeDTO(employeeService.getById(governmentContributions.getEmployee().getId()));
                governmentContributionsDTO.setCreatedBy(governmentContributions.getCreatedBy());
                governmentContributionsDTO.setDateAndTimeCreated(governmentContributions.getDateAndTimeCreated());
                governmentContributionsDTO.setUpdatedBy(governmentContributions.getUpdatedBy());
                governmentContributionsDTO.setDateAndTimeUpdated(governmentContributions.getDateAndTimeUpdated());

                governmentContributionsDTOList.add(governmentContributionsDTO);
            }

            logger.info(String.valueOf(governmentContributionsList.size()).concat(" record(s) found."));
        }

        return governmentContributionsDTOList;
    }

    @Override
    public List<GovernmentContributionsDTO> findByParameter(String param) {
        logger.info("Retrieving employee's government contributions with search parameter '%".concat(param).concat("%' from the database."));

        List<GovernmentContributions> governmentContributionsList = governmentContributionsRepository.findByStringParameter(param);
        List<GovernmentContributionsDTO> governmentContributionsDTOList = new ArrayList<>();

        if (!governmentContributionsList.isEmpty()) {
            logger.info("Employee's government contributions with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (GovernmentContributions governmentContributions : governmentContributionsList) {
                AllowanceDTO allowanceDTO = new AllowanceDTO();

                GovernmentContributionsDTO governmentContributionsDTO = new GovernmentContributionsDTO();

                governmentContributionsDTO.setId(governmentContributions.getId());
                governmentContributionsDTO.setSssContributionAmount(governmentContributions.getSssContrbutionAmount());
                governmentContributionsDTO.setHdmfContributionAmount(governmentContributions.getHdmfContrbutionAmount());
                governmentContributionsDTO.setPhilhealthContributionAmount(governmentContributions.getPhilhealthContributionAmount());
                governmentContributionsDTO.setEmployeeDTO(employeeService.getById(governmentContributions.getEmployee().getId()));
                governmentContributionsDTO.setCreatedBy(governmentContributions.getCreatedBy());
                governmentContributionsDTO.setDateAndTimeCreated(governmentContributions.getDateAndTimeCreated());
                governmentContributionsDTO.setUpdatedBy(governmentContributions.getUpdatedBy());
                governmentContributionsDTO.setDateAndTimeUpdated(governmentContributions.getDateAndTimeUpdated());

                governmentContributionsDTOList.add(governmentContributionsDTO);
            }

            logger.info(String.valueOf(governmentContributionsList.size()).concat(" record(s) found."));
        }

        return governmentContributionsDTOList;
    }
}
