package com.whizservices.hris.dtos.compenben;

import com.whizservices.hris.dtos.BaseDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;

import java.math.BigDecimal;

public class RatesDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private BigDecimal monthlyRate;
    private BigDecimal dailyRate;
    private BigDecimal hourlyRate;
    private BigDecimal overtimeHourlyRate;
    private BigDecimal lateHourlyRate;
    private BigDecimal absentDailyRate;
    private boolean currentRates;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getOvertimeHourlyRate() {
        return overtimeHourlyRate;
    }

    public void setOvertimeHourlyRate(BigDecimal overtimeHourlyRate) {
        this.overtimeHourlyRate = overtimeHourlyRate;
    }

    public BigDecimal getLateHourlyRate() {
        return lateHourlyRate;
    }

    public void setLateHourlyRate(BigDecimal lateHourlyRate) {
        this.lateHourlyRate = lateHourlyRate;
    }

    public BigDecimal getAbsentDailyRate() {
        return absentDailyRate;
    }

    public void setAbsentDailyRate(BigDecimal absentDailyRate) {
        this.absentDailyRate = absentDailyRate;
    }

    public boolean isCurrentRates() {
        return currentRates;
    }

    public void setCurrentRates(boolean currentRates) {
        this.currentRates = currentRates;
    }
}
