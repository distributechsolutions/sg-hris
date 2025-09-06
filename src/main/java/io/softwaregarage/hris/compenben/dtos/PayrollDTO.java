package io.softwaregarage.hris.compenben.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PayrollDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private LocalDate cutOffFromDate;
    private LocalDate cutOffToDate;
    private String payrollFrequency;
    private BigDecimal basicPayAmount;
    private BigDecimal allowancePayAmount;
    private BigDecimal absentDeductionAmount;
    private BigDecimal lateOrUndertimeDeductionAmount;
    private BigDecimal restDayOvertimePayAmount;
    private BigDecimal nightDifferentialPayAmount;
    private BigDecimal leavePayAmount;
    private BigDecimal regularHolidayPayAmount;
    private BigDecimal specialHolidayPayAmount;
    private BigDecimal adjustmentPayAmount;
    private BigDecimal totalGrossPayAmount;
    private BigDecimal sssDeductionAmount;
    private BigDecimal hdmfDeductionAmount;
    private BigDecimal philhealthDeductionAmount;
    private BigDecimal withholdingTaxDeductionAmount;
    private BigDecimal totalLoanDeductionAmount;
    private BigDecimal otherDeductionAmount;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
    }

    public LocalDate getCutOffFromDate() {
        return cutOffFromDate;
    }

    public void setCutOffFromDate(LocalDate cutOffFromDate) {
        this.cutOffFromDate = cutOffFromDate;
    }

    public String getPayrollFrequency() {
        return payrollFrequency;
    }

    public void setPayrollFrequency(String payrollFrequency) {
        this.payrollFrequency = payrollFrequency;
    }

    public LocalDate getCutOffToDate() {
        return cutOffToDate;
    }

    public void setCutOffToDate(LocalDate cutOffToDate) {
        this.cutOffToDate = cutOffToDate;
    }

    public BigDecimal getBasicPayAmount() {
        return basicPayAmount;
    }

    public void setBasicPayAmount(BigDecimal basicPayAmount) {
        this.basicPayAmount = basicPayAmount;
    }

    public BigDecimal getAllowancePayAmount() {
        return allowancePayAmount;
    }

    public void setAllowancePayAmount(BigDecimal allowancePayAmount) {
        this.allowancePayAmount = allowancePayAmount;
    }

    public BigDecimal getAbsentDeductionAmount() {
        return absentDeductionAmount;
    }

    public void setAbsentDeductionAmount(BigDecimal absentDeductionAmount) {
        this.absentDeductionAmount = absentDeductionAmount;
    }

    public BigDecimal getLateOrUndertimeDeductionAmount() {
        return lateOrUndertimeDeductionAmount;
    }

    public void setLateOrUndertimeDeductionAmount(BigDecimal lateOrUndertimeDeductionAmount) {
        this.lateOrUndertimeDeductionAmount = lateOrUndertimeDeductionAmount;
    }

    public BigDecimal getRestDayOvertimePayAmount() {
        return restDayOvertimePayAmount;
    }

    public void setRestDayOvertimePayAmount(BigDecimal restDayOvertimePayAmount) {
        this.restDayOvertimePayAmount = restDayOvertimePayAmount;
    }

    public BigDecimal getNightDifferentialPayAmount() {
        return nightDifferentialPayAmount;
    }

    public void setNightDifferentialPayAmount(BigDecimal nightDifferentialPayAmount) {
        this.nightDifferentialPayAmount = nightDifferentialPayAmount;
    }

    public BigDecimal getLeavePayAmount() {
        return leavePayAmount;
    }

    public void setLeavePayAmount(BigDecimal leavePayAmount) {
        this.leavePayAmount = leavePayAmount;
    }

    public BigDecimal getRegularHolidayPayAmount() {
        return regularHolidayPayAmount;
    }

    public void setRegularHolidayPayAmount(BigDecimal regularHolidayPayAmount) {
        this.regularHolidayPayAmount = regularHolidayPayAmount;
    }

    public BigDecimal getSpecialHolidayPayAmount() {
        return specialHolidayPayAmount;
    }

    public void setSpecialHolidayPayAmount(BigDecimal specialHolidayPayAmount) {
        this.specialHolidayPayAmount = specialHolidayPayAmount;
    }

    public BigDecimal getAdjustmentPayAmount() {
        return adjustmentPayAmount;
    }

    public void setAdjustmentPayAmount(BigDecimal adjustmentPayAmount) {
        this.adjustmentPayAmount = adjustmentPayAmount;
    }

    public BigDecimal getTotalGrossPayAmount() {
        return totalGrossPayAmount;
    }

    public void setTotalGrossPayAmount(BigDecimal totalGrossPayAmount) {
        this.totalGrossPayAmount = totalGrossPayAmount;
    }

    public BigDecimal getSssDeductionAmount() {
        return sssDeductionAmount;
    }

    public void setSssDeductionAmount(BigDecimal sssDeductionAmount) {
        this.sssDeductionAmount = sssDeductionAmount;
    }

    public BigDecimal getHdmfDeductionAmount() {
        return hdmfDeductionAmount;
    }

    public void setHdmfDeductionAmount(BigDecimal hdmfDeductionAmount) {
        this.hdmfDeductionAmount = hdmfDeductionAmount;
    }

    public BigDecimal getPhilhealthDeductionAmount() {
        return philhealthDeductionAmount;
    }

    public void setPhilhealthDeductionAmount(BigDecimal philhealthDeductionAmount) {
        this.philhealthDeductionAmount = philhealthDeductionAmount;
    }

    public BigDecimal getWithholdingTaxDeductionAmount() {
        return withholdingTaxDeductionAmount;
    }

    public void setWithholdingTaxDeductionAmount(BigDecimal withholdingTaxDeductionAmount) {
        this.withholdingTaxDeductionAmount = withholdingTaxDeductionAmount;
    }

    public BigDecimal getTotalLoanDeductionAmount() {
        return totalLoanDeductionAmount;
    }

    public void setTotalLoanDeductionAmount(BigDecimal totalLoanDeductionAmount) {
        this.totalLoanDeductionAmount = totalLoanDeductionAmount;
    }

    public BigDecimal getOtherDeductionAmount() {
        return otherDeductionAmount;
    }

    public void setOtherDeductionAmount(BigDecimal otherDeductionAmount) {
        this.otherDeductionAmount = otherDeductionAmount;
    }
}
