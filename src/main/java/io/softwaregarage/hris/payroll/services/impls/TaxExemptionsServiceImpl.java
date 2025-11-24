package io.softwaregarage.hris.payroll.services.impls;

import io.softwaregarage.hris.payroll.dtos.TaxExemptionsDTO;
import io.softwaregarage.hris.payroll.entities.TaxExemptions;
import io.softwaregarage.hris.payroll.repositories.TaxExemptionsRepository;
import io.softwaregarage.hris.payroll.services.TaxExemptionsService;
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
public class TaxExemptionsServiceImpl implements TaxExemptionsService {
    private final Logger logger = LoggerFactory.getLogger(TaxExemptionsServiceImpl.class);

    private final TaxExemptionsRepository taxExemptionsRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeProfileService employeeProfileService;

    public TaxExemptionsServiceImpl(TaxExemptionsRepository taxExemptionsRepository,
                                    EmployeeProfileRepository employeeProfileRepository,
                                    EmployeeProfileService employeeProfileService) {
        this.taxExemptionsRepository = taxExemptionsRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeProfileService = employeeProfileService;
    }

    @Override
    public void saveOrUpdate(TaxExemptionsDTO object) {
        TaxExemptions taxExemptions;
        String logMessage;

        if (object.getId() != null) {
            taxExemptions = taxExemptionsRepository.getReferenceById(object.getId());
            logMessage = "Employee's tax exemption record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            taxExemptions = new TaxExemptions();
            taxExemptions.setCreatedBy(object.getCreatedBy());
            taxExemptions.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's tax exemption record is successfully created.";
        }

        taxExemptions.setEmployeeProfile(employeeProfileRepository.getReferenceById(object.getEmployeeProfileDTO().getId()));
        taxExemptions.setTaxExemptionPercentage(object.getTaxExemptionPercentage());
        taxExemptions.setActiveTaxExemption(object.isActiveTaxExemption());
        taxExemptions.setUpdatedBy(object.getUpdatedBy());
        taxExemptions.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        taxExemptionsRepository.save(taxExemptions);
        logger.info(logMessage);
    }

    @Override
    public TaxExemptionsDTO getById(UUID id) {
        logger.info("Retrieving employee's tax exemption record with UUID ".concat(id.toString()));

        TaxExemptions taxExemptions = taxExemptionsRepository.getReferenceById(id);
        TaxExemptionsDTO taxExemptionsDTO = new TaxExemptionsDTO();

        taxExemptionsDTO.setId(taxExemptions.getId());
        taxExemptionsDTO.setEmployeeProfileDTO(employeeProfileService.getById(taxExemptions.getEmployeeProfile().getId()));
        taxExemptionsDTO.setTaxExemptionPercentage(taxExemptions.getTaxExemptionPercentage());
        taxExemptionsDTO.setActiveTaxExemption(taxExemptions.isActiveTaxExemption());
        taxExemptionsDTO.setCreatedBy(taxExemptions.getCreatedBy());
        taxExemptionsDTO.setDateAndTimeCreated(taxExemptions.getDateAndTimeCreated());
        taxExemptionsDTO.setUpdatedBy(taxExemptions.getUpdatedBy());
        taxExemptionsDTO.setDateAndTimeUpdated(taxExemptions.getDateAndTimeUpdated());

        logger.info("Employee's tax exemption record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return taxExemptionsDTO;
    }

    @Override
    public void delete(TaxExemptionsDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's tax exemption record permanently.");

            String id = object.getId().toString();
            TaxExemptions taxExemptions = taxExemptionsRepository.getReferenceById(object.getId());
            taxExemptionsRepository.delete(taxExemptions);

            logger.info("Employee's tax exemptions record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<TaxExemptionsDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's tax exemptions records from the database.");
        List<TaxExemptions> taxExemptionsList = taxExemptionsRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's tax exemptions records successfully retrieved.");
        List<TaxExemptionsDTO> taxExemptionsDTOList = new ArrayList<>();

        if (!taxExemptionsList.isEmpty()) {
            for (TaxExemptions taxExemptions : taxExemptionsList) {
                TaxExemptionsDTO taxExemptionsDTO = new TaxExemptionsDTO();

                taxExemptionsDTO.setId(taxExemptions.getId());
                taxExemptionsDTO.setEmployeeProfileDTO(employeeProfileService.getById(taxExemptions.getEmployeeProfile().getId()));
                taxExemptionsDTO.setTaxExemptionPercentage(taxExemptions.getTaxExemptionPercentage());
                taxExemptionsDTO.setActiveTaxExemption(taxExemptions.isActiveTaxExemption());
                taxExemptionsDTO.setCreatedBy(taxExemptions.getCreatedBy());
                taxExemptionsDTO.setDateAndTimeCreated(taxExemptions.getDateAndTimeCreated());
                taxExemptionsDTO.setUpdatedBy(taxExemptions.getUpdatedBy());
                taxExemptionsDTO.setDateAndTimeUpdated(taxExemptions.getDateAndTimeUpdated());

                taxExemptionsDTOList.add(taxExemptionsDTO);
            }

            logger.info(String.valueOf(taxExemptionsList.size()).concat(" record(s) found."));
        }

        return taxExemptionsDTOList;
    }

    @Override
    public List<TaxExemptionsDTO> findByParameter(String param) {
        return List.of();
    }
}
