package com.whizservices.hris.services.impls.payroll;

import com.whizservices.hris.dtos.payroll.EmployeePayrollDTO;
import com.whizservices.hris.entities.payroll.EmployeePayroll;
import com.whizservices.hris.repositories.payroll.EmployeePayrollRepository;
import com.whizservices.hris.repositories.profile.EmployeeRepository;
import com.whizservices.hris.services.impls.profile.EmployeeServiceImpl;
import com.whizservices.hris.services.payroll.EmployeePayrollService;
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
public class EmployeePayrollServiceImpl implements EmployeePayrollService {
    private final Logger logger = LoggerFactory.getLogger(EmployeePayrollServiceImpl.class);
    private final EmployeePayrollRepository employeePayrollRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeePayrollServiceImpl(EmployeePayrollRepository employeePayrollRepository,
                                      EmployeeRepository employeeRepository) {
        this.employeePayrollRepository = employeePayrollRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void saveOrUpdate(EmployeePayrollDTO object) {
        logger.info("Getting the employee payroll data transfer object.");
        logger.info("Preparing the employee payroll object to be saved in the database.");

        EmployeePayroll employeePayroll;
        String logMessage;

        if (object.getId() != null) {
            employeePayroll = employeePayrollRepository.getReferenceById(object.getId());
            logMessage = "Employee payroll record with ID ".concat(object.getId().toString()).concat(" has successfully updated.");
        } else {
            employeePayroll = new EmployeePayroll();
            employeePayroll.setCreatedBy(object.getCreatedBy());
            employeePayroll.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "A new employee payroll record has successfully saved in the database.";
        }

        employeePayroll.setEmployee(employeeRepository.getReferenceById(object.getEmployeeDTO().getId()));
        employeePayroll.setCutOffFromDate(object.getCutOffFromDate());
        employeePayroll.setCutOffToDate(object.getCutOffToDate());
        employeePayroll.setPayrollFrequency(object.getPayrollFrequency());
        employeePayroll.setBasicPayAmount(object.getBasicPayAmount());
        employeePayroll.setAllowancePayAmount(object.getAllowancePayAmount());
        employeePayroll.setAbsentDeductionAmount(object.getAbsentDeductionAmount());
        employeePayroll.setLateOrUndertimeDeductionAmount(object.getLateOrUndertimeDeductionAmount());
        employeePayroll.setRestDayOvertimePayAmount(object.getRestDayOvertimePayAmount());
        employeePayroll.setNightDifferentialPayAmount(object.getNightDifferentialPayAmount());
        employeePayroll.setLeavePayAmount(object.getLeavePayAmount());
        employeePayroll.setRegularHolidayPayAmount(object.getRegularHolidayPayAmount());
        employeePayroll.setSpecialHolidayPayAmount(object.getSpecialHolidayPayAmount());
        employeePayroll.setAdjustmentPayAmount(object.getAdjustmentPayAmount());
        employeePayroll.setTotalGrossPayAmount(object.getTotalGrossPayAmount());
        employeePayroll.setSssDeductionAmount(object.getSssDeductionAmount());
        employeePayroll.setHdmfDeductionAmount(object.getHdmfDeductionAmount());
        employeePayroll.setPhilhealthDeductionAmount(object.getPhilhealthDeductionAmount());
        employeePayroll.setWithholdingTaxDeductionAmount(object.getWithholdingTaxDeductionAmount());
        employeePayroll.setTotalLoanDeductionAmount(object.getTotalLoanDeductionAmount());
        employeePayroll.setOtherDeductionAmount(object.getOtherDeductionAmount());
        employeePayroll.setUpdatedBy(object.getUpdatedBy());
        employeePayroll.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        employeePayrollRepository.save(employeePayroll);
        logger.info(logMessage);
    }

    @Override
    public EmployeePayrollDTO getById(UUID id) {
        logger.info("Getting employee payroll record with ID ".concat(id.toString()).concat(" from the database."));
        EmployeePayroll employeePayroll = employeePayrollRepository.getReferenceById(id);

        logger.info("Employee record with ID ".concat(id.toString()).concat(" has successfully retrieved."));
        EmployeePayrollDTO employeePayrollDTO = new EmployeePayrollDTO();

        employeePayrollDTO.setId(employeePayroll.getId());
        employeePayrollDTO.setEmployeeDTO(new EmployeeServiceImpl(employeeRepository).getById(employeePayroll.getEmployee().getId()));
        employeePayrollDTO.setCutOffFromDate(employeePayroll.getCutOffFromDate());
        employeePayrollDTO.setCutOffToDate(employeePayroll.getCutOffToDate());
        employeePayrollDTO.setPayrollFrequency(employeePayroll.getPayrollFrequency());
        employeePayrollDTO.setBasicPayAmount(employeePayroll.getBasicPayAmount());
        employeePayrollDTO.setAllowancePayAmount(employeePayroll.getAllowancePayAmount());
        employeePayrollDTO.setAbsentDeductionAmount(employeePayroll.getAbsentDeductionAmount());
        employeePayrollDTO.setLateOrUndertimeDeductionAmount(employeePayroll.getLateOrUndertimeDeductionAmount());
        employeePayrollDTO.setRestDayOvertimePayAmount(employeePayroll.getRestDayOvertimePayAmount());
        employeePayrollDTO.setNightDifferentialPayAmount(employeePayroll.getNightDifferentialPayAmount());
        employeePayrollDTO.setLeavePayAmount(employeePayroll.getLeavePayAmount());
        employeePayrollDTO.setRegularHolidayPayAmount(employeePayroll.getRegularHolidayPayAmount());
        employeePayrollDTO.setSpecialHolidayPayAmount(employeePayroll.getSpecialHolidayPayAmount());
        employeePayrollDTO.setAdjustmentPayAmount(employeePayroll.getAdjustmentPayAmount());
        employeePayrollDTO.setTotalGrossPayAmount(employeePayroll.getTotalGrossPayAmount());
        employeePayrollDTO.setSssDeductionAmount(employeePayroll.getSssDeductionAmount());
        employeePayrollDTO.setHdmfDeductionAmount(employeePayroll.getHdmfDeductionAmount());
        employeePayrollDTO.setPhilhealthDeductionAmount(employeePayroll.getPhilhealthDeductionAmount());
        employeePayrollDTO.setWithholdingTaxDeductionAmount(employeePayroll.getWithholdingTaxDeductionAmount());
        employeePayrollDTO.setTotalLoanDeductionAmount(employeePayroll.getTotalLoanDeductionAmount());
        employeePayrollDTO.setOtherDeductionAmount(employeePayroll.getOtherDeductionAmount());
        employeePayrollDTO.setCreatedBy(employeePayroll.getCreatedBy());
        employeePayrollDTO.setDateAndTimeCreated(employeePayroll.getDateAndTimeCreated());
        employeePayrollDTO.setUpdatedBy(employeePayroll.getUpdatedBy());
        employeePayrollDTO.setDateAndTimeUpdated(employeePayroll.getDateAndTimeUpdated());

        logger.info("Employee payroll data transfer object has successfully returned.");
        return employeePayrollDTO;
    }

    @Override
    public void delete(EmployeePayrollDTO object) {
        logger.warn("You are about to delete an employee payroll record. Doing this will permanently erase in the database.");

        EmployeePayroll employeePayroll = employeePayrollRepository.getReferenceById(object.getId());
        employeePayrollRepository.delete(employeePayroll);

        logger.info("Employee payroll record with ID ".concat(object.getId().toString()).concat(" has successfully deleted in the database."));
    }

    @Override
    public List<EmployeePayrollDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee payroll records from the database.");
        List<EmployeePayroll> employeePayrollList = employeePayrollRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee payroll records successfully retrieved.");
        List<EmployeePayrollDTO> employeePayrollDTOList = new ArrayList<>();

        if (!employeePayrollList.isEmpty()) {
            EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

            for (EmployeePayroll employeePayroll : employeePayrollList) {
                EmployeePayrollDTO employeePayrollDTO = new EmployeePayrollDTO();
                employeePayrollDTO.setId(employeePayroll.getId());
                employeePayrollDTO.setEmployeeDTO(employeeService.getById(employeePayroll.getEmployee().getId()));
                employeePayrollDTO.setCutOffFromDate(employeePayroll.getCutOffFromDate());
                employeePayrollDTO.setCutOffToDate(employeePayroll.getCutOffToDate());
                employeePayrollDTO.setPayrollFrequency(employeePayroll.getPayrollFrequency());
                employeePayrollDTO.setBasicPayAmount(employeePayroll.getBasicPayAmount());
                employeePayrollDTO.setAllowancePayAmount(employeePayroll.getAllowancePayAmount());
                employeePayrollDTO.setAbsentDeductionAmount(employeePayroll.getAbsentDeductionAmount());
                employeePayrollDTO.setLateOrUndertimeDeductionAmount(employeePayroll.getLateOrUndertimeDeductionAmount());
                employeePayrollDTO.setRestDayOvertimePayAmount(employeePayroll.getRestDayOvertimePayAmount());
                employeePayrollDTO.setNightDifferentialPayAmount(employeePayroll.getNightDifferentialPayAmount());
                employeePayrollDTO.setLeavePayAmount(employeePayroll.getLeavePayAmount());
                employeePayrollDTO.setRegularHolidayPayAmount(employeePayroll.getRegularHolidayPayAmount());
                employeePayrollDTO.setSpecialHolidayPayAmount(employeePayroll.getSpecialHolidayPayAmount());
                employeePayrollDTO.setAdjustmentPayAmount(employeePayroll.getAdjustmentPayAmount());
                employeePayrollDTO.setTotalGrossPayAmount(employeePayroll.getTotalGrossPayAmount());
                employeePayrollDTO.setSssDeductionAmount(employeePayroll.getSssDeductionAmount());
                employeePayrollDTO.setHdmfDeductionAmount(employeePayroll.getHdmfDeductionAmount());
                employeePayrollDTO.setPhilhealthDeductionAmount(employeePayroll.getPhilhealthDeductionAmount());
                employeePayrollDTO.setWithholdingTaxDeductionAmount(employeePayroll.getWithholdingTaxDeductionAmount());
                employeePayrollDTO.setTotalLoanDeductionAmount(employeePayroll.getTotalLoanDeductionAmount());
                employeePayrollDTO.setOtherDeductionAmount(employeePayroll.getOtherDeductionAmount());
                employeePayrollDTO.setCreatedBy(employeePayroll.getCreatedBy());
                employeePayrollDTO.setDateAndTimeCreated(employeePayroll.getDateAndTimeCreated());
                employeePayrollDTO.setUpdatedBy(employeePayroll.getUpdatedBy());
                employeePayrollDTO.setDateAndTimeUpdated(employeePayroll.getDateAndTimeUpdated());

                employeePayrollDTOList.add(employeePayrollDTO);
            }

            logger.info(String.valueOf(employeePayrollList.size()).concat(" record(s) found."));
        }

        return employeePayrollDTOList;
    }

    @Override
    public List<EmployeePayrollDTO> findByParameter(String param) {
        return List.of();
    }
}
