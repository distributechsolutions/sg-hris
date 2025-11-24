package io.softwaregarage.hris.payroll.entities;

import io.softwaregarage.hris.commons.BaseEntity;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sg_hris_rates")
public class Rates extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeProfile employeeProfile;

    @Column(name = "rate_type", length = 50, nullable = false)
    private String rateType;

    @Column(name = "basic_compensation_rate", nullable = false)
    private BigDecimal basicCompensationRate;

    @Column(name = "daily_compensation_rate", nullable = false)
    private BigDecimal dailyCompensationRate;

    @Column(name = "hourly_compensation_rate", nullable = false)
    private BigDecimal hourlyCompensationRate;

    @Column(name = "overtime_hourly_compensation_rate", nullable = false)
    private BigDecimal overtimeHourlyCompensationRate;

    @Column(name = "late_hourly_deduction_rate", nullable = false)
    private BigDecimal lateHourlyDeductionRate;

    @Column(name = "daily_absent_deduction_rate", nullable = false)
    private BigDecimal dailyAbsentDeductionRate;

    public EmployeeProfile getEmployee() {
        return employeeProfile;
    }

    public void setEmployee(EmployeeProfile employeeProfile) {
        this.employeeProfile = employeeProfile;
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
