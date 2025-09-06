package io.softwaregarage.hris.compenben.services.impls;

import io.softwaregarage.hris.compenben.dtos.PayrollDTO;
import io.softwaregarage.hris.compenben.entities.Payroll;
import io.softwaregarage.hris.compenben.repositories.PayrollRepository;
import io.softwaregarage.hris.profile.repositories.EmployeeProfileRepository;
import io.softwaregarage.hris.profile.services.impls.EmployeeProfileServiceImpl;
import io.softwaregarage.hris.compenben.services.PayrollService;
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
public class PayrollServiceImpl implements PayrollService {
    private final Logger logger = LoggerFactory.getLogger(PayrollServiceImpl.class);
    private final PayrollRepository payrollRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public PayrollServiceImpl(PayrollRepository payrollRepository,
                              EmployeeProfileRepository employeeProfileRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public void saveOrUpdate(PayrollDTO object) {
        logger.info("Getting the employee payroll data transfer object.");
        logger.info("Preparing the employee payroll object to be saved in the database.");

        Payroll payroll;
        String logMessage;

        if (object.getId() != null) {
            payroll = payrollRepository.getReferenceById(object.getId());
            logMessage = "Employee payroll record with ID ".concat(object.getId().toString()).concat(" has successfully updated.");
        } else {
            payroll = new Payroll();
            payroll.setCreatedBy(object.getCreatedBy());
            payroll.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "A new employee payroll record has successfully saved in the database.";
        }

        payroll.setEmployee(employeeProfileRepository.getReferenceById(object.getEmployeeDTO().getId()));
        payroll.setCutOffFromDate(object.getCutOffFromDate());
        payroll.setCutOffToDate(object.getCutOffToDate());
        payroll.setPayrollFrequency(object.getPayrollFrequency());
        payroll.setBasicPayAmount(object.getBasicPayAmount());
        payroll.setAllowancePayAmount(object.getAllowancePayAmount());
        payroll.setAbsentDeductionAmount(object.getAbsentDeductionAmount());
        payroll.setLateOrUndertimeDeductionAmount(object.getLateOrUndertimeDeductionAmount());
        payroll.setRestDayOvertimePayAmount(object.getRestDayOvertimePayAmount());
        payroll.setNightDifferentialPayAmount(object.getNightDifferentialPayAmount());
        payroll.setLeavePayAmount(object.getLeavePayAmount());
        payroll.setRegularHolidayPayAmount(object.getRegularHolidayPayAmount());
        payroll.setSpecialHolidayPayAmount(object.getSpecialHolidayPayAmount());
        payroll.setAdjustmentPayAmount(object.getAdjustmentPayAmount());
        payroll.setTotalGrossPayAmount(object.getTotalGrossPayAmount());
        payroll.setSssDeductionAmount(object.getSssDeductionAmount());
        payroll.setHdmfDeductionAmount(object.getHdmfDeductionAmount());
        payroll.setPhilhealthDeductionAmount(object.getPhilhealthDeductionAmount());
        payroll.setWithholdingTaxDeductionAmount(object.getWithholdingTaxDeductionAmount());
        payroll.setTotalLoanDeductionAmount(object.getTotalLoanDeductionAmount());
        payroll.setOtherDeductionAmount(object.getOtherDeductionAmount());
        payroll.setUpdatedBy(object.getUpdatedBy());
        payroll.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        payrollRepository.save(payroll);
        logger.info(logMessage);
    }

    @Override
    public PayrollDTO getById(UUID id) {
        logger.info("Getting employee payroll record with ID ".concat(id.toString()).concat(" from the database."));
        Payroll payroll = payrollRepository.getReferenceById(id);

        logger.info("Employee record with ID ".concat(id.toString()).concat(" has successfully retrieved."));
        PayrollDTO payrollDTO = new PayrollDTO();

        payrollDTO.setId(payroll.getId());
        payrollDTO.setEmployeeDTO(new EmployeeProfileServiceImpl(employeeProfileRepository).getById(payroll.getEmployee().getId()));
        payrollDTO.setCutOffFromDate(payroll.getCutOffFromDate());
        payrollDTO.setCutOffToDate(payroll.getCutOffToDate());
        payrollDTO.setPayrollFrequency(payroll.getPayrollFrequency());
        payrollDTO.setBasicPayAmount(payroll.getBasicPayAmount());
        payrollDTO.setAllowancePayAmount(payroll.getAllowancePayAmount());
        payrollDTO.setAbsentDeductionAmount(payroll.getAbsentDeductionAmount());
        payrollDTO.setLateOrUndertimeDeductionAmount(payroll.getLateOrUndertimeDeductionAmount());
        payrollDTO.setRestDayOvertimePayAmount(payroll.getRestDayOvertimePayAmount());
        payrollDTO.setNightDifferentialPayAmount(payroll.getNightDifferentialPayAmount());
        payrollDTO.setLeavePayAmount(payroll.getLeavePayAmount());
        payrollDTO.setRegularHolidayPayAmount(payroll.getRegularHolidayPayAmount());
        payrollDTO.setSpecialHolidayPayAmount(payroll.getSpecialHolidayPayAmount());
        payrollDTO.setAdjustmentPayAmount(payroll.getAdjustmentPayAmount());
        payrollDTO.setTotalGrossPayAmount(payroll.getTotalGrossPayAmount());
        payrollDTO.setSssDeductionAmount(payroll.getSssDeductionAmount());
        payrollDTO.setHdmfDeductionAmount(payroll.getHdmfDeductionAmount());
        payrollDTO.setPhilhealthDeductionAmount(payroll.getPhilhealthDeductionAmount());
        payrollDTO.setWithholdingTaxDeductionAmount(payroll.getWithholdingTaxDeductionAmount());
        payrollDTO.setTotalLoanDeductionAmount(payroll.getTotalLoanDeductionAmount());
        payrollDTO.setOtherDeductionAmount(payroll.getOtherDeductionAmount());
        payrollDTO.setCreatedBy(payroll.getCreatedBy());
        payrollDTO.setDateAndTimeCreated(payroll.getDateAndTimeCreated());
        payrollDTO.setUpdatedBy(payroll.getUpdatedBy());
        payrollDTO.setDateAndTimeUpdated(payroll.getDateAndTimeUpdated());

        logger.info("Employee payroll data transfer object has successfully returned.");
        return payrollDTO;
    }

    @Override
    public void delete(PayrollDTO object) {
        logger.warn("You are about to delete an employee payroll record. Doing this will permanently erase in the database.");

        Payroll payroll = payrollRepository.getReferenceById(object.getId());
        payrollRepository.delete(payroll);

        logger.info("Employee payroll record with ID ".concat(object.getId().toString()).concat(" has successfully deleted in the database."));
    }

    @Override
    public List<PayrollDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving employee payroll records from the database.");
        List<Payroll> payrollList = payrollRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Employee payroll records successfully retrieved.");
        List<PayrollDTO> payrollDTOList = new ArrayList<>();

        if (!payrollList.isEmpty()) {
            EmployeeProfileService employeeProfileService = new EmployeeProfileServiceImpl(employeeProfileRepository);

            for (Payroll payroll : payrollList) {
                PayrollDTO payrollDTO = new PayrollDTO();
                payrollDTO.setId(payroll.getId());
                payrollDTO.setEmployeeDTO(employeeProfileService.getById(payroll.getEmployee().getId()));
                payrollDTO.setCutOffFromDate(payroll.getCutOffFromDate());
                payrollDTO.setCutOffToDate(payroll.getCutOffToDate());
                payrollDTO.setPayrollFrequency(payroll.getPayrollFrequency());
                payrollDTO.setBasicPayAmount(payroll.getBasicPayAmount());
                payrollDTO.setAllowancePayAmount(payroll.getAllowancePayAmount());
                payrollDTO.setAbsentDeductionAmount(payroll.getAbsentDeductionAmount());
                payrollDTO.setLateOrUndertimeDeductionAmount(payroll.getLateOrUndertimeDeductionAmount());
                payrollDTO.setRestDayOvertimePayAmount(payroll.getRestDayOvertimePayAmount());
                payrollDTO.setNightDifferentialPayAmount(payroll.getNightDifferentialPayAmount());
                payrollDTO.setLeavePayAmount(payroll.getLeavePayAmount());
                payrollDTO.setRegularHolidayPayAmount(payroll.getRegularHolidayPayAmount());
                payrollDTO.setSpecialHolidayPayAmount(payroll.getSpecialHolidayPayAmount());
                payrollDTO.setAdjustmentPayAmount(payroll.getAdjustmentPayAmount());
                payrollDTO.setTotalGrossPayAmount(payroll.getTotalGrossPayAmount());
                payrollDTO.setSssDeductionAmount(payroll.getSssDeductionAmount());
                payrollDTO.setHdmfDeductionAmount(payroll.getHdmfDeductionAmount());
                payrollDTO.setPhilhealthDeductionAmount(payroll.getPhilhealthDeductionAmount());
                payrollDTO.setWithholdingTaxDeductionAmount(payroll.getWithholdingTaxDeductionAmount());
                payrollDTO.setTotalLoanDeductionAmount(payroll.getTotalLoanDeductionAmount());
                payrollDTO.setOtherDeductionAmount(payroll.getOtherDeductionAmount());
                payrollDTO.setCreatedBy(payroll.getCreatedBy());
                payrollDTO.setDateAndTimeCreated(payroll.getDateAndTimeCreated());
                payrollDTO.setUpdatedBy(payroll.getUpdatedBy());
                payrollDTO.setDateAndTimeUpdated(payroll.getDateAndTimeUpdated());

                payrollDTOList.add(payrollDTO);
            }

            logger.info(String.valueOf(payrollList.size()).concat(" record(s) found."));
        }

        return payrollDTOList;
    }

    @Override
    public List<PayrollDTO> findByParameter(String param) {
        return List.of();
    }
}
