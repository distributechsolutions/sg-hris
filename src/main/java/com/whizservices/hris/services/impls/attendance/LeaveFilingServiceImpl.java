package com.whizservices.hris.services.impls.attendance;

import com.whizservices.hris.dtos.attendance.LeaveFilingDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;
import com.whizservices.hris.entities.attendance.LeaveFiling;
import com.whizservices.hris.repositories.compenben.LeaveBenefitsRepository;
import com.whizservices.hris.repositories.attendance.LeaveFilingRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.compenben.LeaveBenefitsService;
import com.whizservices.hris.services.attendance.LeaveFilingService;
import com.whizservices.hris.services.impls.compenben.LeaveBenefitsServiceImpl;
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
public class LeaveFilingServiceImpl implements LeaveFilingService {
    private final Logger logger = LoggerFactory.getLogger(LeaveFilingServiceImpl.class);

    private final LeaveFilingRepository leaveFilingRepository;
    private final LeaveBenefitsRepository leaveBenefitsRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveFilingServiceImpl(LeaveFilingRepository leaveFilingRepository,
                                  LeaveBenefitsRepository leaveBenefitsRepository,
                                  EmployeeRepository employeeRepository) {
        this.leaveFilingRepository = leaveFilingRepository;
        this.leaveBenefitsRepository = leaveBenefitsRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(LeaveFilingDTO object) {
        LeaveFiling leaveFiling;
        String logMessage;

        if (object.getId() != null) {
            leaveFiling = leaveFilingRepository.getReferenceById(object.getId());
            logMessage = "Employee's leave filing record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            leaveFiling = new LeaveFiling();
            leaveFiling.setCreatedBy(object.getCreatedBy());
            leaveFiling.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Employee's leave filing record is successfully created.";
        }

        leaveFiling.setLeaveBenefits(leaveBenefitsRepository.getReferenceById(object.getLeaveBenefitsDTO().getId()));
        leaveFiling.setAssignedApproverEmployee(employeeRepository.getReferenceById(object.getAssignedApproverEmployeeDTO().getId()));
        leaveFiling.setLeaveDateAndTimeFrom(object.getLeaveDateAndTimeFrom());
        leaveFiling.setLeaveDateAndTimeTo(object.getLeaveDateAndTimeTo());
        leaveFiling.setLeaveCount(object.getLeaveCount());
        leaveFiling.setRemarks(object.getRemarks());
        leaveFiling.setLeaveStatus(object.getLeaveStatus());
        leaveFiling.setUpdatedBy(object.getUpdatedBy());
        leaveFiling.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        leaveFilingRepository.save(leaveFiling);
        logger.info(logMessage);
    }

    @Override
    public LeaveFilingDTO getById(UUID id) {
        logger.info("Retrieving employee's leave filing record with UUID ".concat(id.toString()));

        LeaveFiling leaveFiling = leaveFilingRepository.getReferenceById(id);
        LeaveFilingDTO leaveFilingDTO = new LeaveFilingDTO();

        leaveFilingDTO.setId(leaveFiling.getId());
        leaveFilingDTO.setLeaveBenefitsDTO(new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository).getById(leaveFiling.getLeaveBenefits().getId()));
        leaveFilingDTO.setAssignedApproverEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(leaveFiling.getAssignedApproverEmployee().getId()));
        leaveFilingDTO.setLeaveDateAndTimeFrom(leaveFiling.getLeaveDateAndTimeFrom());
        leaveFilingDTO.setLeaveDateAndTimeTo(leaveFiling.getLeaveDateAndTimeTo());
        leaveFilingDTO.setLeaveCount(leaveFiling.getLeaveCount());
        leaveFilingDTO.setRemarks(leaveFiling.getRemarks());
        leaveFilingDTO.setLeaveStatus(leaveFiling.getLeaveStatus());
        leaveFilingDTO.setCreatedBy(leaveFiling.getCreatedBy());
        leaveFilingDTO.setDateAndTimeCreated(leaveFiling.getDateAndTimeCreated());
        leaveFilingDTO.setUpdatedBy(leaveFiling.getUpdatedBy());
        leaveFilingDTO.setDateAndTimeUpdated(leaveFiling.getDateAndTimeUpdated());

        logger.info("Employee's leave filing record with id ".concat(id.toString()).concat(" is successfully retrieved."));
        return leaveFilingDTO;
    }

    @Override
    public void delete(LeaveFilingDTO object) {
        if (object != null) {
            logger.warn("You are about to delete the employee's leave filing record permanently.");

            String id = object.getId().toString();
            LeaveFiling leaveFiling = leaveFilingRepository.getReferenceById(object.getId());
            leaveFilingRepository.delete(leaveFiling);

            logger.info("Employee's leave filing record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<LeaveFilingDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee leave filings from the database.");
        List<LeaveFiling> leaveFilingList = leaveFilingRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee leave filings successfully retrieved.");
        List<LeaveFilingDTO> leaveFilingDTOList = new ArrayList<>();

        if (!leaveFilingList.isEmpty()) {
            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LeaveFiling leaveFiling : leaveFilingList) {
                LeaveFilingDTO leaveFilingDTO = new LeaveFilingDTO();

                leaveFilingDTO.setId(leaveFiling.getId());
                leaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(leaveFiling.getLeaveBenefits().getId()));
                leaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(leaveFiling.getAssignedApproverEmployee().getId()));
                leaveFilingDTO.setLeaveDateAndTimeFrom(leaveFiling.getLeaveDateAndTimeFrom());
                leaveFilingDTO.setLeaveDateAndTimeTo(leaveFiling.getLeaveDateAndTimeTo());
                leaveFilingDTO.setLeaveCount(leaveFiling.getLeaveCount());
                leaveFilingDTO.setRemarks(leaveFiling.getRemarks());
                leaveFilingDTO.setLeaveStatus(leaveFiling.getLeaveStatus());
                leaveFilingDTO.setCreatedBy(leaveFiling.getCreatedBy());
                leaveFilingDTO.setDateAndTimeCreated(leaveFiling.getDateAndTimeCreated());
                leaveFilingDTO.setUpdatedBy(leaveFiling.getUpdatedBy());
                leaveFilingDTO.setDateAndTimeUpdated(leaveFiling.getDateAndTimeUpdated());

                leaveFilingDTOList.add(leaveFilingDTO);
            }

            logger.info(String.valueOf(leaveFilingList.size()).concat(" record(s) found."));
        }

        return leaveFilingDTOList;
    }

    @Override
    public List<LeaveFilingDTO> findByParameter(String param) {
        logger.info("Retrieving employee leave filings with search parameter '%".concat(param).concat("%' from the database."));

        List<LeaveFilingDTO> leaveFilingDTOList = new ArrayList<>();
        List<LeaveFiling> leaveFilingList = leaveFilingRepository.findByStringParameter(param);

        if (!leaveFilingList.isEmpty()) {
            logger.info("Employee leave filings with parameter '%".concat(param).concat("%' has successfully retrieved."));

            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LeaveFiling leaveFiling : leaveFilingList) {
                LeaveFilingDTO leaveFilingDTO = new LeaveFilingDTO();

                leaveFilingDTO.setId(leaveFiling.getId());
                leaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(leaveFiling.getLeaveBenefits().getId()));
                leaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(leaveFiling.getAssignedApproverEmployee().getId()));
                leaveFilingDTO.setLeaveDateAndTimeFrom(leaveFiling.getLeaveDateAndTimeFrom());
                leaveFilingDTO.setLeaveDateAndTimeTo(leaveFiling.getLeaveDateAndTimeTo());
                leaveFilingDTO.setLeaveCount(leaveFiling.getLeaveCount());
                leaveFilingDTO.setRemarks(leaveFiling.getRemarks());
                leaveFilingDTO.setLeaveStatus(leaveFiling.getLeaveStatus());
                leaveFilingDTO.setCreatedBy(leaveFiling.getCreatedBy());
                leaveFilingDTO.setDateAndTimeCreated(leaveFiling.getDateAndTimeCreated());
                leaveFilingDTO.setUpdatedBy(leaveFiling.getUpdatedBy());
                leaveFilingDTO.setDateAndTimeUpdated(leaveFiling.getDateAndTimeUpdated());

                leaveFilingDTOList.add(leaveFilingDTO);
            }

            logger.info(String.valueOf(leaveFilingList.size()).concat(" record(s) found."));
        }

        return leaveFilingDTOList;
    }

    @Override
    public List<LeaveFilingDTO> getByEmployeeDTO(EmployeeDTO employeeDTO) {
        logger.info("Retrieving employee leave filings with UUID ".concat(employeeDTO.getId().toString()).concat(" from the database."));

        List<LeaveFilingDTO> leaveFilingDTOList = new ArrayList<>();
        List<LeaveFiling> leaveFilingList = leaveFilingRepository.findByEmployee(employeeRepository.getReferenceById(employeeDTO.getId()));

        if (!leaveFilingList.isEmpty()) {
            logger.info("Employee leave filings with UUID ".concat(employeeDTO.getId().toString()).concat(" has successfully retrieved."));

            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LeaveFiling leaveFiling : leaveFilingList) {
                LeaveFilingDTO leaveFilingDTO = new LeaveFilingDTO();

                leaveFilingDTO.setId(leaveFiling.getId());
                leaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(leaveFiling.getLeaveBenefits().getId()));
                leaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(leaveFiling.getAssignedApproverEmployee().getId()));
                leaveFilingDTO.setLeaveDateAndTimeFrom(leaveFiling.getLeaveDateAndTimeFrom());
                leaveFilingDTO.setLeaveDateAndTimeTo(leaveFiling.getLeaveDateAndTimeTo());
                leaveFilingDTO.setLeaveCount(leaveFiling.getLeaveCount());
                leaveFilingDTO.setRemarks(leaveFiling.getRemarks());
                leaveFilingDTO.setLeaveStatus(leaveFiling.getLeaveStatus());
                leaveFilingDTO.setCreatedBy(leaveFiling.getCreatedBy());
                leaveFilingDTO.setDateAndTimeCreated(leaveFiling.getDateAndTimeCreated());
                leaveFilingDTO.setUpdatedBy(leaveFiling.getUpdatedBy());
                leaveFilingDTO.setDateAndTimeUpdated(leaveFiling.getDateAndTimeUpdated());

                leaveFilingDTOList.add(leaveFilingDTO);
            }

            logger.info(String.valueOf(leaveFilingList.size()).concat(" record(s) found."));
        }

        return leaveFilingDTOList;
    }

    @Override
    public List<LeaveFilingDTO> getByLeaveStatusAndAssignedApproverEmployeeDTO(String leaveStatus, EmployeeDTO assignedApproverEmployeeDTO) {
        logger.info("Retrieving assigned leave filings approvals with UUID ".concat(assignedApproverEmployeeDTO.getId().toString()).concat(" from the database."));

        List<LeaveFilingDTO> leaveFilingDTOList = new ArrayList<>();
        List<LeaveFiling> leaveFilingList = leaveFilingRepository.findByStatusAndAssignedApproverEmployee(leaveStatus, employeeRepository.getReferenceById(assignedApproverEmployeeDTO.getId()));

        if (!leaveFilingList.isEmpty()) {
            logger.info("Assigned leave filings for approval with UUID ".concat(assignedApproverEmployeeDTO.getId().toString()).concat(" has successfully retrieved."));

            LeaveBenefitsService leaveBenefitsService = new LeaveBenefitsServiceImpl(leaveBenefitsRepository, employeeRepository);
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (LeaveFiling leaveFiling : leaveFilingList) {
                LeaveFilingDTO leaveFilingDTO = new LeaveFilingDTO();

                leaveFilingDTO.setId(leaveFiling.getId());
                leaveFilingDTO.setLeaveBenefitsDTO(leaveBenefitsService.getById(leaveFiling.getLeaveBenefits().getId()));
                leaveFilingDTO.setAssignedApproverEmployeeDTO(employeeService.getById(leaveFiling.getAssignedApproverEmployee().getId()));
                leaveFilingDTO.setLeaveDateAndTimeFrom(leaveFiling.getLeaveDateAndTimeFrom());
                leaveFilingDTO.setLeaveDateAndTimeTo(leaveFiling.getLeaveDateAndTimeTo());
                leaveFilingDTO.setLeaveCount(leaveFiling.getLeaveCount());
                leaveFilingDTO.setRemarks(leaveFiling.getRemarks());
                leaveFilingDTO.setLeaveStatus(leaveFiling.getLeaveStatus());
                leaveFilingDTO.setCreatedBy(leaveFiling.getCreatedBy());
                leaveFilingDTO.setDateAndTimeCreated(leaveFiling.getDateAndTimeCreated());
                leaveFilingDTO.setUpdatedBy(leaveFiling.getUpdatedBy());
                leaveFilingDTO.setDateAndTimeUpdated(leaveFiling.getDateAndTimeUpdated());

                leaveFilingDTOList.add(leaveFilingDTO);
            }

            logger.info(String.valueOf(leaveFilingList.size()).concat(" record(s) found."));
        }

        return leaveFilingDTOList;
    }
}
