package io.distributechsolutions.hris.services.impls.compenben;

import io.distributechsolutions.hris.dtos.compenben.LeaveBenefitsDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.entities.compenben.LeaveBenefits;
import io.distributechsolutions.hris.repositories.compenben.LeaveBenefitsRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeRepository;
import io.distributechsolutions.hris.services.compenben.LeaveBenefitsService;
import io.distributechsolutions.hris.services.impls.profile.EmployeeServiceImpl;
import io.distributechsolutions.hris.services.profile.EmployeeService;
import io.distributechsolutions.hris.utils.StringUtil;

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
public class LeaveBenefitsServiceImpl implements LeaveBenefitsService {
    private final Logger logger = LoggerFactory.getLogger(LeaveBenefitsServiceImpl.class);

    private final LeaveBenefitsRepository leaveBenefitsRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveBenefitsServiceImpl(LeaveBenefitsRepository leaveBenefitsRepository,
                                    EmployeeRepository employeeRepository) {
        this.leaveBenefitsRepository = leaveBenefitsRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(LeaveBenefitsDTO object) {
        LeaveBenefits leaveBenefits;
        String logMessage;

        if (object.getId() != null) {
            leaveBenefits = leaveBenefitsRepository.getReferenceById(object.getId());
            logMessage = "Employee's leave benefit record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            leaveBenefits = new LeaveBenefits();
            leaveBenefits.setCreatedBy(object.getCreatedBy());
            leaveBenefits.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's leave benefit record is successfully created.";
        }

        leaveBenefits.setLeaveCode(object.getLeaveCode());
        leaveBenefits.setLeaveType(object.getLeaveType());
        leaveBenefits.setLeaveForYear(object.getLeaveForYear());
        leaveBenefits.setLeaveCount(object.getLeaveCount());
        leaveBenefits.setLeaveActive(object.isLeaveActive());
        leaveBenefits.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        leaveBenefits.setUpdatedBy(object.getUpdatedBy());
        leaveBenefits.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        leaveBenefitsRepository.save(leaveBenefits);
        logger.info(logMessage);
    }

    @Override
    public LeaveBenefitsDTO getById(UUID id) {
        logger.info("Retrieving employee's leave benefit record with UUID ".concat(id.toString()));

        LeaveBenefits leaveBenefits = leaveBenefitsRepository.getReferenceById(id);
        LeaveBenefitsDTO leaveBenefitsDTO = new LeaveBenefitsDTO();

        leaveBenefitsDTO.setId(leaveBenefits.getId());
        leaveBenefitsDTO.setLeaveCode(leaveBenefits.getLeaveCode());
        leaveBenefitsDTO.setLeaveType(leaveBenefits.getLeaveType());
        leaveBenefitsDTO.setLeaveForYear(leaveBenefits.getLeaveForYear());
        leaveBenefitsDTO.setLeaveCount(leaveBenefits.getLeaveCount());
        leaveBenefitsDTO.setLeaveActive(leaveBenefits.isLeaveActive());
        leaveBenefitsDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(leaveBenefits.getEmployee().getId()));
        leaveBenefitsDTO.setCreatedBy(leaveBenefits.getCreatedBy());
        leaveBenefitsDTO.setDateAndTimeCreated(leaveBenefits.getDateAndTimeCreated());
        leaveBenefitsDTO.setUpdatedBy(leaveBenefits.getUpdatedBy());
        leaveBenefitsDTO.setDateAndTimeUpdated(leaveBenefits.getDateAndTimeUpdated());

        logger.info("Employee's leave benefit record with id ".concat(id.toString()).concat(" is successfully retrieved."));
        return leaveBenefitsDTO;
    }

    @Override
    public void delete(LeaveBenefitsDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's leave benefit record permanently.");

            String id = object.getId().toString();
            LeaveBenefits leaveBenefits = leaveBenefitsRepository.getReferenceById(object.getId());
            leaveBenefitsRepository.delete(leaveBenefits);

            logger.info("Employee's leave benefit record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<LeaveBenefitsDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee's leave benefits from the database.");
        List<LeaveBenefits> leaveBenefitsList = leaveBenefitsRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee's leave benefits successfully retrieved.");
        List<LeaveBenefitsDTO> leaveBenefitsDTOList = new ArrayList<>();

        if (!leaveBenefitsList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LeaveBenefits leaveBenefits : leaveBenefitsList) {
                LeaveBenefitsDTO leaveBenefitsDTO = new LeaveBenefitsDTO();

                leaveBenefitsDTO.setId(leaveBenefits.getId());
                leaveBenefitsDTO.setLeaveCode(leaveBenefits.getLeaveCode());
                leaveBenefitsDTO.setLeaveType(leaveBenefits.getLeaveType());
                leaveBenefitsDTO.setLeaveForYear(leaveBenefits.getLeaveForYear());
                leaveBenefitsDTO.setLeaveCount(leaveBenefits.getLeaveCount());
                leaveBenefitsDTO.setLeaveActive(leaveBenefits.isLeaveActive());
                leaveBenefitsDTO.setEmployeeDTO(employeeService.getById(leaveBenefits.getEmployee().getId()));
                leaveBenefitsDTO.setCreatedBy(leaveBenefits.getCreatedBy());
                leaveBenefitsDTO.setDateAndTimeCreated(leaveBenefits.getDateAndTimeCreated());
                leaveBenefitsDTO.setUpdatedBy(leaveBenefits.getUpdatedBy());
                leaveBenefitsDTO.setDateAndTimeUpdated(leaveBenefits.getDateAndTimeUpdated());

                leaveBenefitsDTOList.add(leaveBenefitsDTO);
            }

            logger.info(String.valueOf(leaveBenefitsList.size()).concat(" record(s) found."));
        }

        return leaveBenefitsDTOList;
    }

    @Override
    public List<LeaveBenefitsDTO> findByParameter(String param) {
        List<LeaveBenefitsDTO> leaveBenefitsDTOList = new ArrayList<>();
        List<LeaveBenefits> leaveBenefitsList = null;

        logger.info("Retrieving employee's leave benefits with search parameter '%".concat(param).concat("%' from the database."));

        if (param.equalsIgnoreCase("Yes") || param.equalsIgnoreCase("No")) {
            leaveBenefitsList = leaveBenefitsRepository.findByBooleanParameter(param.equalsIgnoreCase("Yes"));
        } else if (StringUtil.isNumeric(param)) {
            leaveBenefitsList = leaveBenefitsRepository.findByIntegerParameter(Integer.parseInt(param));
        } else {
            leaveBenefitsList = leaveBenefitsRepository.findByStringParameter(param);
        }

        if (!leaveBenefitsList.isEmpty()) {
            logger.info("Employee's leave benefits with parameter '%".concat(param).concat("%' has successfully retrieved."));

            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LeaveBenefits leaveBenefits : leaveBenefitsList) {
                LeaveBenefitsDTO leaveBenefitsDTO = new LeaveBenefitsDTO();

                leaveBenefitsDTO.setId(leaveBenefits.getId());
                leaveBenefitsDTO.setLeaveCode(leaveBenefits.getLeaveCode());
                leaveBenefitsDTO.setLeaveType(leaveBenefits.getLeaveType());
                leaveBenefitsDTO.setLeaveForYear(leaveBenefits.getLeaveForYear());
                leaveBenefitsDTO.setLeaveCount(leaveBenefits.getLeaveCount());
                leaveBenefitsDTO.setLeaveActive(leaveBenefits.isLeaveActive());
                leaveBenefitsDTO.setEmployeeDTO(employeeService.getById(leaveBenefits.getEmployee().getId()));
                leaveBenefitsDTO.setCreatedBy(leaveBenefits.getCreatedBy());
                leaveBenefitsDTO.setDateAndTimeCreated(leaveBenefits.getDateAndTimeCreated());
                leaveBenefitsDTO.setUpdatedBy(leaveBenefits.getUpdatedBy());
                leaveBenefitsDTO.setDateAndTimeUpdated(leaveBenefits.getDateAndTimeUpdated());

                leaveBenefitsDTOList.add(leaveBenefitsDTO);
            }

            logger.info(String.valueOf(leaveBenefitsList.size()).concat(" record(s) found."));
        }

        return leaveBenefitsDTOList;
    }

    @Override
    public List<LeaveBenefitsDTO> getByEmployeeDTO(EmployeeDTO employeeDTO) {
        logger.info("Retrieving employee's leave benefits with UUID ".concat(employeeDTO.getId().toString()).concat(" from the database."));
        List<LeaveBenefits> leaveBenefitsList = leaveBenefitsRepository.findByEmployee(employeeRepository.getReferenceById(employeeDTO.getId()));

        logger.info("Employee's leave benefits has successfully retrieved.");
        List<LeaveBenefitsDTO> leaveBenefitsDTOList = new ArrayList<>();

        if (!leaveBenefitsList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LeaveBenefits leaveBenefits : leaveBenefitsList) {
                LeaveBenefitsDTO leaveBenefitsDTO = new LeaveBenefitsDTO();

                leaveBenefitsDTO.setId(leaveBenefits.getId());
                leaveBenefitsDTO.setLeaveCode(leaveBenefits.getLeaveCode());
                leaveBenefitsDTO.setLeaveType(leaveBenefits.getLeaveType());
                leaveBenefitsDTO.setLeaveForYear(leaveBenefits.getLeaveForYear());
                leaveBenefitsDTO.setLeaveCount(leaveBenefits.getLeaveCount());
                leaveBenefitsDTO.setLeaveActive(leaveBenefits.isLeaveActive());
                leaveBenefitsDTO.setEmployeeDTO(employeeService.getById(leaveBenefits.getEmployee().getId()));
                leaveBenefitsDTO.setCreatedBy(leaveBenefits.getCreatedBy());
                leaveBenefitsDTO.setDateAndTimeCreated(leaveBenefits.getDateAndTimeCreated());
                leaveBenefitsDTO.setUpdatedBy(leaveBenefits.getUpdatedBy());
                leaveBenefitsDTO.setDateAndTimeUpdated(leaveBenefits.getDateAndTimeUpdated());

                leaveBenefitsDTOList.add(leaveBenefitsDTO);
            }

            logger.info(String.valueOf(leaveBenefitsList.size()).concat(" record(s) found."));
        }

        return leaveBenefitsDTOList;
    }
}
