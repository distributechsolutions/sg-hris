package io.softwaregarage.hris.payroll.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

import java.math.BigDecimal;

public class RatesDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private String rateType;
    private BigDecimal basicCompensationRate;
    private BigDecimal dailyCompensationRate;
    private BigDecimal hourlyCompensationRate;
    private BigDecimal overtimeHourlyCompensationRate;
    private BigDecimal lateHourlyDeductionRate;
    private BigDecimal dailyAbsentDeductionRate;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getBasicCompensationRate() {
        return basicCompensationRate;
    }

    public void setBasicCompensationRate(BigDecimal basicCompensationRate) {
        this.basicCompensationRate = basicCompensationRate;
    }

    public BigDecimal getDailyCompensationRate() {
        return dailyCompensationRate;
    }

    public void setDailyCompensationRate(BigDecimal dailyCompensationRate) {
        this.dailyCompensationRate = dailyCompensationRate;
    }

    public BigDecimal getHourlyCompensationRate() {
        return hourlyCompensationRate;
    }

    public void setHourlyCompensationRate(BigDecimal hourlyCompensationRate) {
        this.hourlyCompensationRate = hourlyCompensationRate;
    }

    public BigDecimal getOvertimeHourlyCompensationRate() {
        return overtimeHourlyCompensationRate;
    }

    public void setOvertimeHourlyCompensationRate(BigDecimal overtimeHourlyCompensationRate) {
        this.overtimeHourlyCompensationRate = overtimeHourlyCompensationRate;
    }

    public BigDecimal getLateHourlyDeductionRate() {
        return lateHourlyDeductionRate;
    }

    public void setLateHourlyDeductionRate(BigDecimal lateHourlyDeductionRate) {
        this.lateHourlyDeductionRate = lateHourlyDeductionRate;
    }

    public BigDecimal getDailyAbsentDeductionRate() {
        return dailyAbsentDeductionRate;
    }

    public void setDailyAbsentDeductionRate(BigDecimal dailyAbsentDeductionRate) {
        this.dailyAbsentDeductionRate = dailyAbsentDeductionRate;
    }
}
