package com.whizservices.hris.entities.attendance;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.profile.Employee;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sg_hris_employee_timesheet")
public class EmployeeTimesheet extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "timesheet_log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "timesheet_log_time")
    private LocalTime logTime;

    @Column(name = "timesheet_log_detail", length = 10, nullable = false)
    private String logDetail;

    @Lob
    @Column(name = "timesheet_log_image", nullable = false)
    private byte[] logImage;

    @Column(name = "timesheet_status", length = 10, nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_shift_schedule_id", referencedColumnName = "id", nullable = false)
    private EmployeeShiftSchedule shiftSchedule;

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

    public LocalTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalTime logTime) {
        this.logTime = logTime;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public byte[] getLogImage() {
        return logImage;
    }

    public void setLogImage(byte[] logImage) {
        this.logImage = logImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmployeeShiftSchedule getShiftSchedule() {
        return shiftSchedule;
    }

    public void setShiftSchedule(EmployeeShiftSchedule shiftSchedule) {
        this.shiftSchedule = shiftSchedule;
    }
}
