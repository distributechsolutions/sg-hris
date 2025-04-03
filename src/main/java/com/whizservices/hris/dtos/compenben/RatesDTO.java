package com.whizservices.hris.dtos.compenben;

import com.whizservices.hris.dtos.BaseDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;

import java.math.BigDecimal;

public class RatesDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private String rateType;
    private BigDecimal basicCompensationRate;
    private BigDecimal dailyCompensationRate;
    private BigDecimal hourlyCompensationRate;
    private BigDecimal overtimeHourlyCompensationRate;
    private BigDecimal lateHourlyDeductionRate;
    private BigDecimal dailyAbsentDeductionRate;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
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
