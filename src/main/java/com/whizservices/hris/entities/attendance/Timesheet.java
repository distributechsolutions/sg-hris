package com.whizservices.hris.entities.attendance;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.profile.Employee;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "wsi_employee_timesheet")
public class Timesheet extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "log_time_in")
    private LocalTime logTimeIn;

    @Column(name = "log_time_out")
    private LocalTime logTimeOut;

    @Column(name = "shift_schedule", length = 150, nullable = false)
    private String shiftSchedule;

    @Column(name = "exception_remarks", length = 50)
    private String exceptionRemarks;

    @Column(name = "leave_remarks", length = 50)
    private String leaveRemarks;

    @Column(name = "regular_worked_hours", nullable = false)
    private LocalTime regularWorkedHours;

    @Column(name = "overtime_worked_hours", nullable = false)
    private LocalTime overtimeWorkedHours;

    @Column(name = "total_worked_hours", nullable = false)
    private LocalTime totalWorkedHours;

    @Column(name = "status", length = 25, nullable = false)
    private String status;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public LocalTime getLogTimeIn() {
        return logTimeIn;
    }

    public void setLogTimeIn(LocalTime logTimeIn) {
        this.logTimeIn = logTimeIn;
    }

    public LocalTime getLogTimeOut() {
        return logTimeOut;
    }

    public void setLogTimeOut(LocalTime logTimeOut) {
        this.logTimeOut = logTimeOut;
    }

    public String getShiftSchedule() {
        return shiftSchedule;
    }

    public void setShiftSchedule(String shiftSchedule) {
        this.shiftSchedule = shiftSchedule;
    }

    public String getExceptionRemarks() {
        return exceptionRemarks;
    }

    public void setExceptionRemarks(String exceptionRemarks) {
        this.exceptionRemarks = exceptionRemarks;
    }

    public String getLeaveRemarks() {
        return leaveRemarks;
    }

    public void setLeaveRemarks(String leaveRemarks) {
        this.leaveRemarks = leaveRemarks;
    }

    public LocalTime getRegularWorkedHours() {
        return regularWorkedHours;
    }

    public void setRegularWorkedHours(LocalTime regularWorkedHours) {
        this.regularWorkedHours = regularWorkedHours;
    }

    public LocalTime getOvertimeWorkedHours() {
        return overtimeWorkedHours;
    }

    public void setOvertimeWorkedHours(LocalTime overtimeWorkedHours) {
        this.overtimeWorkedHours = overtimeWorkedHours;
    }

    public LocalTime getTotalWorkedHours() {
        return totalWorkedHours;
    }

    public void setTotalWorkedHours(LocalTime totalWorkedHours) {
        this.totalWorkedHours = totalWorkedHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
