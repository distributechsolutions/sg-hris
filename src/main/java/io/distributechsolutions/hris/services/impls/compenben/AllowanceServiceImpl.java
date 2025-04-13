package io.distributechsolutions.hris.services.impls.compenben;

import io.distributechsolutions.hris.dtos.compenben.AllowanceDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.entities.compenben.Allowance;
import io.distributechsolutions.hris.repositories.compenben.AllowanceRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeRepository;
import io.distributechsolutions.hris.services.compenben.AllowanceService;
import io.distributechsolutions.hris.services.impls.profile.EmployeeServiceImpl;
import io.distributechsolutions.hris.services.profile.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AllowanceServiceImpl implements AllowanceService {
    private final Logger logger = LoggerFactory.getLogger(AllowanceServiceImpl.class);

    private final AllowanceRepository allowanceRepository;
    private final EmployeeRepository employeeRepository;

    public AllowanceServiceImpl(AllowanceRepository allowanceRepository,
                                EmployeeRepository employeeRepository) {
        this.allowanceRepository = allowanceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(AllowanceDTO object) {
        Allowance allowance;
        String logMessage;

        if (object.getId() != null) {
            allowance = allowanceRepository.getReferenceById(object.getId());
            logMessage = "Employee's allowance record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            allowance = new Allowance();
            allowance.setCreatedBy(object.getCreatedBy());
            allowance.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's allowance record is successfully created.";
        }

        allowance.setAllowanceCode(object.getAllowanceCode());
        allowance.setAllowanceType(object.getAllowanceType());
        allowance.setAllowanceAmount(object.getAllowanceAmount());
        allowance.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        allowance.setUpdatedBy(object.getUpdatedBy());
        allowance.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        allowanceRepository.save(allowance);
        logger.info(logMessage);
    }

    @Override
    public AllowanceDTO getById(UUID id) {
        logger.info("Retrieving employee's allowance record with UUID ".concat(id.toString()));

        Allowance allowance = allowanceRepository.getReferenceById(id);
        AllowanceDTO allowanceDTO = new AllowanceDTO();

        allowanceDTO.setId(allowance.getId());
        allowanceDTO.setAllowanceCode(allowance.getAllowanceCode());
        allowanceDTO.setAllowanceType(allowance.getAllowanceType());
        allowanceDTO.setAllowanceAmount(allowance.getAllowanceAmount());
        allowanceDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(allowance.getEmployee().getId()));
        allowanceDTO.setCreatedBy(allowance.getCreatedBy());
        allowanceDTO.setDateAndTimeCreated(allowance.getDateAndTimeCreated());
        allowanceDTO.setUpdatedBy(allowance.getUpdatedBy());
        allowanceDTO.setDateAndTimeUpdated(allowance.getDateAndTimeUpdated());

        logger.info("Employee's allowance record with id ".concat(id.toString()).concat(" is successfully retrieved."));
        return allowanceDTO;
    }

    @Override
    public void delete(AllowanceDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's allowance record permanently.");

            String id = object.getId().toString();
            Allowance allowance = allowanceRepository.getReferenceById(object.getId());
            allowanceRepository.delete(allowance);

            logger.info("Employee's allowance record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<AllowanceDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's allowance from the database.");
        List<Allowance> allowanceList = allowanceRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's allowance successfully retrieved.");
        List<AllowanceDTO> allowanceDTOList = new ArrayList<>();

        if (!allowanceList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (Allowance allowance : allowanceList) {
                AllowanceDTO allowanceDTO = new AllowanceDTO();

                allowanceDTO.setId(allowance.getId());
                allowanceDTO.setAllowanceCode(allowance.getAllowanceCode());
                allowanceDTO.setAllowanceType(allowance.getAllowanceType());
                allowanceDTO.setAllowanceAmount(allowance.getAllowanceAmount());
                allowanceDTO.setEmployeeDTO(employeeService.getById(allowance.getEmployee().getId()));
                allowanceDTO.setCreatedBy(allowance.getCreatedBy());
                allowanceDTO.setDateAndTimeCreated(allowance.getDateAndTimeCreated());
                allowanceDTO.setUpdatedBy(allowance.getUpdatedBy());
                allowanceDTO.setDateAndTimeUpdated(allowance.getDateAndTimeUpdated());

                allowanceDTOList.add(allowanceDTO);
            }

            logger.info(String.valueOf(allowanceList.size()).concat(" record(s) found."));
        }

        return allowanceDTOList;
    }

    @Override
    public List<AllowanceDTO> findByParameter(String param) {
        logger.info("Retrieving employee's allowance with search parameter '%".concat(param).concat("%' from the database."));

        List<Allowance> allowanceList = allowanceRepository.findByStringParameter(param);
        List<AllowanceDTO> allowanceDTOList = new ArrayList<>();

        if (!allowanceList.isEmpty()) {
            logger.info("Employee's allowance with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (Allowance allowance : allowanceList) {
                AllowanceDTO allowanceDTO = new AllowanceDTO();

                allowanceDTO.setId(allowance.getId());
                allowanceDTO.setAllowanceCode(allowance.getAllowanceCode());
                allowanceDTO.setAllowanceType(allowance.getAllowanceType());
                allowanceDTO.setAllowanceAmount(allowance.getAllowanceAmount());
                allowanceDTO.setEmployeeDTO(employeeService.getById(allowance.getEmployee().getId()));
                allowanceDTO.setCreatedBy(allowance.getCreatedBy());
                allowanceDTO.setDateAndTimeCreated(allowance.getDateAndTimeCreated());
                allowanceDTO.setUpdatedBy(allowance.getUpdatedBy());
                allowanceDTO.setDateAndTimeUpdated(allowance.getDateAndTimeUpdated());

                allowanceDTOList.add(allowanceDTO);
            }

            logger.info(String.valueOf(allowanceList.size()).concat(" record(s) found."));
        }

        return allowanceDTOList;
    }

    @Override
    public BigDecimal getSumOfAllowanceByEmployeeDTO(EmployeeDTO employeeDTO) {
        Object sumOfAllowanceByEmployeeDTO = allowanceRepository.findSumOfAllowanceByEmployee(employeeRepository.getReferenceById(employeeDTO.getId()));
        BigDecimal sumOfAllowance = new BigDecimal(sumOfAllowanceByEmployeeDTO != null ? sumOfAllowanceByEmployeeDTO.toString() : "0.00");
        return sumOfAllowance;
    }
}
