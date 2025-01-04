package com.victorylimited.hris.entities.compenben;

import com.victorylimited.hris.entities.BaseEntity;
import com.victorylimited.hris.entities.profile.Employee;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vlh_rates")
public class Rates extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "monthly_rate", nullable = false)
    private BigDecimal monthlyRate;

    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;

    @Column(name = "hourly_rate", nullable = false)
    private BigDecimal hourlyRate;

    @Column(name = "overtime_hourly_rate", nullable = false)
    private BigDecimal overtimeHourlyRate;

    @Column(name = "late_hourly_rate", nullable = false)
    private BigDecimal lateHourlyRate;

    @Column(name = "absent_daily_rate", nullable = false)
    private BigDecimal absentDailyRate;

    @Column(name = "is_current_rates", nullable = false)
    private boolean currentRates;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
