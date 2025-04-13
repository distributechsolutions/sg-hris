package io.distributechsolutions.hris.services.impls.attendance;

import io.distributechsolutions.hris.dtos.attendance.EmployeeLeaveFilingDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.entities.attendance.EmployeeLeaveFiling;
import io.distributechsolutions.hris.repositories.compenben.LeaveBenefitsRepository;
import io.distributechsolutions.hris.repositories.attendance.EmployeeLeaveFilingRepository;
import io.distributechsolutions.hris.repositories.profile.EmployeeRepository;
import io.distributechsolutions.hris.services.compenben.LeaveBenefitsService;
import io.distributechsolutions.hris.services.attendance.EmployeeLeaveFilingService;
import io.distributechsolutions.hris.services.impls.compenben.LeaveBenefitsServiceImpl;
import io.distributechsolutions.hris.services.impls.profile.EmployeeServiceImpl;
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
public class EmployeeLeaveFilingServiceImpl implements EmployeeLeaveFilingService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeLeaveFilingServiceImpl.class);

    private final EmployeeLeaveFilingRepository employeeLeaveFilingRepository;
    private final LeaveBenefitsRepository leaveBenefitsRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeLeaveFilingServiceImpl(EmployeeLeaveFilingRepository employeeLeaveFilingRepository,
                                          LeaveBenefitsRepository leaveBenefitsRepository,
                                          EmployeeRepository employeeRepository) {
        this.employeeLeaveFilingRepository = employeeLeaveFilingRepository;
        this.leaveBenefitsRepository = leaveBenefitsRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(EmployeeLeaveFilingDTO object) {
        EmployeeLeaveFiling employeeLeaveFiling;
        String logMessage;

        if (object.getId() != null) {
            employeeLeaveFiling = employeeLeaveFilingRepository.getReferenceById(object.getId());
            logMessage = "Employee's leave filing record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            employeeLeaveFiling = new EmployeeLeaveFiling();
            employeeLeaveFiling.setCreatedBy(object.getCreatedBy());
            employeeLeaveFiling.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's leave filing record is successfully created.";
        }

        employeeLeaveFiling.setLeaveBenefits(leaveBenefitsRepository.getReferenceById(object.getLeaveBenefitsDTO().getId()));
        employeeLeaveFiling.setAssignedApproverEmployee(employeeRepository.getReferenceById(object.getAssignedApproverEmployeeDTO().getId()));
        employeeLeaveFiling.setLeaveDateAndTimeFrom(object.getLeaveDateAndTimeFrom());
        employeeLeaveFiling.setLeaveDateAndTimeTo(object.getLeaveDateAndTimeTo());
        employeeLeaveFiling.setLeaveCount(object.getLeaveCount());
        employeeLeaveFiling.setRemarks(object.getRemarks());
        employeeLeaveFiling.setLeaveStatus(object.getLeaveStatus());
        employeeLeaveFiling.setUpdatedBy(object.getUpdatedBy());
        employeeLeaveFiling.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeeLeaveFilingRepository.save(employeeLeaveFiling);
        logger.info(logMessage);
    }

    @Override
    public EmployeeLeaveFilingDTO getById(UUID id) {
        logger.info("Retrieving employee's leave filing record with UUID ".concat(id.toString()));

        EmployeeLeaveFiling employeeLeaveFiling = employeeLeaveFilingRepository.getReferenceById(id);
        EmployeeLeaveFilingDTO employeeLeaveFilingDTO = new EmployeeLeaveFilingDTO();

        employeeLeaveFilingDTO.setId(employeeLeaveFiling.getId());
        employeeLeaveFilingDTO.setLeaveBenefitsDTO(new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository).getById(employeeLeaveFiling.getLeaveBenefits().getId()));
        employeeLeaveFilingDTO.setAssignedApproverEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employeeLeaveFiling.getAssignedApproverEmployee().getId()));
        employeeLeaveFilingDTO.setLeaveDateAndTimeFrom(employeeLeaveFiling.getLeaveDateAndTimeFrom());
        employeeLeaveFilingDTO.setLeaveDateAndTimeTo(employeeLeaveFiling.getLeaveDateAndTimeTo());
        employeeLeaveFilingDTO.setLeaveCount(employeeLeaveFiling.getLeaveCount());
        employeeLeaveFilingDTO.setRemarks(employeeLeaveFiling.getRemarks());
        employeeLeaveFilingDTO.setLeaveStatus(employeeLeaveFiling.getLeaveStatus());
        employeeLeaveFilingDTO.setCreatedBy(employeeLeaveFiling.getCreatedBy());
        employeeLeaveFilingDTO.setDateAndTimeCreated(employeeLeaveFiling.getDateAndTimeCreated());
        employeeLeaveFilingDTO.setUpdatedBy(employeeLeaveFiling.getUpdatedBy());
        employeeLeaveFilingDTO.setDateAndTimeUpdated(employeeLeaveFiling.getDateAndTimeUpdated());

        logger.info("Employee's leave filing record with id ".concat(id.toString()).concat(" is successfully retrieved."));
        return employeeLeaveFilingDTO;
    }

    @Override
    public void delete(EmployeeLeaveFilingDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's leave filing record permanently.");

            String id = object.getId().toString();
            EmployeeLeaveFiling employeeLeaveFiling = employeeLeaveFilingRepository.getReferenceById(object.getId());
            employeeLeaveFilingRepository.delete(employeeLeaveFiling);

            logger.info("Employee's leave filing record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<EmployeeLeaveFilingDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee leave filings from the database.");
        List<EmployeeLeaveFiling> employeeLeaveFilingList = employeeLeaveFilingRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee leave filings successfully retrieved.");
        List<EmployeeLeaveFilingDTO> employeeLeaveFilingDTOList = new ArrayList<>();

        if (!employeeLeaveFilingList.isEmpty()) {
            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeLeaveFiling employeeLeaveFiling : employeeLeaveFilingList) {
                EmployeeLeaveFilingDTO employeeLeaveFilingDTO = new EmployeeLeaveFilingDTO();

                employeeLeaveFilingDTO.setId(employeeLeaveFiling.getId());
                employeeLeaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(employeeLeaveFiling.getLeaveBenefits().getId()));
                employeeLeaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(employeeLeaveFiling.getAssignedApproverEmployee().getId()));
                employeeLeaveFilingDTO.setLeaveDateAndTimeFrom(employeeLeaveFiling.getLeaveDateAndTimeFrom());
                employeeLeaveFilingDTO.setLeaveDateAndTimeTo(employeeLeaveFiling.getLeaveDateAndTimeTo());
                employeeLeaveFilingDTO.setLeaveCount(employeeLeaveFiling.getLeaveCount());
                employeeLeaveFilingDTO.setRemarks(employeeLeaveFiling.getRemarks());
                employeeLeaveFilingDTO.setLeaveStatus(employeeLeaveFiling.getLeaveStatus());
                employeeLeaveFilingDTO.setCreatedBy(employeeLeaveFiling.getCreatedBy());
                employeeLeaveFilingDTO.setDateAndTimeCreated(employeeLeaveFiling.getDateAndTimeCreated());
                employeeLeaveFilingDTO.setUpdatedBy(employeeLeaveFiling.getUpdatedBy());
                employeeLeaveFilingDTO.setDateAndTimeUpdated(employeeLeaveFiling.getDateAndTimeUpdated());

                employeeLeaveFilingDTOList.add(employeeLeaveFilingDTO);
            }

            logger.info(String.valueOf(employeeLeaveFilingList.size()).concat(" record(s) found."));
        }

        return employeeLeaveFilingDTOList;
    }

    @Override
    public List<EmployeeLeaveFilingDTO> findByParameter(String param) {
        logger.info("Retrieving employee leave filings with search parameter '%".concat(param).concat("%' from the database."));

        List<EmployeeLeaveFilingDTO> employeeLeaveFilingDTOList = new ArrayList<>();
        List<EmployeeLeaveFiling> employeeLeaveFilingList = employeeLeaveFilingRepository.findByStringParameter(param);

        if (!employeeLeaveFilingList.isEmpty()) {
            logger.info("Employee leave filings with parameter '%".concat(param).concat("%' has successfully retrieved."));

            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeLeaveFiling employeeLeaveFiling : employeeLeaveFilingList) {
                EmployeeLeaveFilingDTO employeeLeaveFilingDTO = new EmployeeLeaveFilingDTO();

                employeeLeaveFilingDTO.setId(employeeLeaveFiling.getId());
                employeeLeaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(employeeLeaveFiling.getLeaveBenefits().getId()));
                employeeLeaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(employeeLeaveFiling.getAssignedApproverEmployee().getId()));
                employeeLeaveFilingDTO.setLeaveDateAndTimeFrom(employeeLeaveFiling.getLeaveDateAndTimeFrom());
                employeeLeaveFilingDTO.setLeaveDateAndTimeTo(employeeLeaveFiling.getLeaveDateAndTimeTo());
                employeeLeaveFilingDTO.setLeaveCount(employeeLeaveFiling.getLeaveCount());
                employeeLeaveFilingDTO.setRemarks(employeeLeaveFiling.getRemarks());
                employeeLeaveFilingDTO.setLeaveStatus(employeeLeaveFiling.getLeaveStatus());
                employeeLeaveFilingDTO.setCreatedBy(employeeLeaveFiling.getCreatedBy());
                employeeLeaveFilingDTO.setDateAndTimeCreated(employeeLeaveFiling.getDateAndTimeCreated());
                employeeLeaveFilingDTO.setUpdatedBy(employeeLeaveFiling.getUpdatedBy());
                employeeLeaveFilingDTO.setDateAndTimeUpdated(employeeLeaveFiling.getDateAndTimeUpdated());

                employeeLeaveFilingDTOList.add(employeeLeaveFilingDTO);
            }

            logger.info(String.valueOf(employeeLeaveFilingList.size()).concat(" record(s) found."));
        }

        return employeeLeaveFilingDTOList;
    }

    @Override
    public List<EmployeeLeaveFilingDTO> getByEmployeeDTO(EmployeeDTO employeeDTO) {
        logger.info("Retrieving employee leave filings with UUID ".concat(employeeDTO.getId().toString()).concat(" from the database."));

        List<EmployeeLeaveFilingDTO> employeeLeaveFilingDTOList = new ArrayList<>();
        List<EmployeeLeaveFiling> employeeLeaveFilingList = employeeLeaveFilingRepository.findByEmployee(employeeRepository.getReferenceById(employeeDTO.getId()));

        if (!employeeLeaveFilingList.isEmpty()) {
            logger.info("Employee leave filings with UUID ".concat(employeeDTO.getId().toString()).concat(" has successfully retrieved."));

            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeLeaveFiling employeeLeaveFiling : employeeLeaveFilingList) {
                EmployeeLeaveFilingDTO employeeLeaveFilingDTO = new EmployeeLeaveFilingDTO();

                employeeLeaveFilingDTO.setId(employeeLeaveFiling.getId());
                employeeLeaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(employeeLeaveFiling.getLeaveBenefits().getId()));
                employeeLeaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(employeeLeaveFiling.getAssignedApproverEmployee().getId()));
                employeeLeaveFilingDTO.setLeaveDateAndTimeFrom(employeeLeaveFiling.getLeaveDateAndTimeFrom());
                employeeLeaveFilingDTO.setLeaveDateAndTimeTo(employeeLeaveFiling.getLeaveDateAndTimeTo());
                employeeLeaveFilingDTO.setLeaveCount(employeeLeaveFiling.getLeaveCount());
                employeeLeaveFilingDTO.setRemarks(employeeLeaveFiling.getRemarks());
                employeeLeaveFilingDTO.setLeaveStatus(employeeLeaveFiling.getLeaveStatus());
                employeeLeaveFilingDTO.setCreatedBy(employeeLeaveFiling.getCreatedBy());
                employeeLeaveFilingDTO.setDateAndTimeCreated(employeeLeaveFiling.getDateAndTimeCreated());
                employeeLeaveFilingDTO.setUpdatedBy(employeeLeaveFiling.getUpdatedBy());
                employeeLeaveFilingDTO.setDateAndTimeUpdated(employeeLeaveFiling.getDateAndTimeUpdated());

                employeeLeaveFilingDTOList.add(employeeLeaveFilingDTO);
            }

            logger.info(String.valueOf(employeeLeaveFilingList.size()).concat(" record(s) found."));
        }

        return employeeLeaveFilingDTOList;
    }

    @Override
    public List<EmployeeLeaveFilingDTO> getByLeaveStatusAndAssignedApproverEmployeeDTO(String leaveStatus, EmployeeDTO assignedApproverEmployeeDTO) {
        logger.info("Retrieving assigned leave filings approvals with UUID ".concat(assignedApproverEmployeeDTO.getId().toString()).concat(" from the database."));

        List<EmployeeLeaveFilingDTO> employeeLeaveFilingDTOList = new ArrayList<>();
        List<EmployeeLeaveFiling> employeeLeaveFilingList = employeeLeaveFilingRepository.findByStatusAndAssignedApproverEmployee(leaveStatus, employeeRepository.getReferenceById(assignedApproverEmployeeDTO.getId()));

        if (!employeeLeaveFilingList.isEmpty()) {
            logger.info("Assigned leave filings for approval with UUID ".concat(assignedApproverEmployeeDTO.getId().toString()).concat(" has successfully retrieved."));

            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeeLeaveFiling employeeLeaveFiling : employeeLeaveFilingList) {
                EmployeeLeaveFilingDTO employeeLeaveFilingDTO = new EmployeeLeaveFilingDTO();

                employeeLeaveFilingDTO.setId(employeeLeaveFiling.getId());
                employeeLeaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(employeeLeaveFiling.getLeaveBenefits().getId()));
                employeeLeaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(employeeLeaveFiling.getAssignedApproverEmployee().getId()));
                employeeLeaveFilingDTO.setLeaveDateAndTimeFrom(employeeLeaveFiling.getLeaveDateAndTimeFrom());
                employeeLeaveFilingDTO.setLeaveDateAndTimeTo(employeeLeaveFiling.getLeaveDateAndTimeTo());
                employeeLeaveFilingDTO.setLeaveCount(employeeLeaveFiling.getLeaveCount());
                employeeLeaveFilingDTO.setRemarks(employeeLeaveFiling.getRemarks());
                employeeLeaveFilingDTO.setLeaveStatus(employeeLeaveFiling.getLeaveStatus());
                employeeLeaveFilingDTO.setCreatedBy(employeeLeaveFiling.getCreatedBy());
                employeeLeaveFilingDTO.setDateAndTimeCreated(employeeLeaveFiling.getDateAndTimeCreated());
                employeeLeaveFilingDTO.setUpdatedBy(employeeLeaveFiling.getUpdatedBy());
                employeeLeaveFilingDTO.setDateAndTimeUpdated(employeeLeaveFiling.getDateAndTimeUpdated());

                employeeLeaveFilingDTOList.add(employeeLeaveFilingDTO);
            }

            logger.info(String.valueOf(employeeLeaveFilingList.size()).concat(" record(s) found."));
        }

        return employeeLeaveFilingDTOList;
    }
}
