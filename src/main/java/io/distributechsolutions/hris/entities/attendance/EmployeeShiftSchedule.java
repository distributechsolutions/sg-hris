package io.distributechsolutions.hris.entities.attendance;

import io.distributechsolutions.hris.entities.BaseEntity;
import io.distributechsolutions.hris.entities.profile.Employee;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "sg_hris_employee_shift_schedule")
public class EmployeeShiftSchedule extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "shift_schedule", length = 25, nullable = false)
    private String shiftSchedule;

    @Column(name = "shift_hours", nullable = false)
    private Integer shiftHours;

    @Column(name = "shift_scheduled_days", length = 150, nullable = false)
    private String shiftScheduledDays;

    @Column(name = "shift_start_time", nullable = false)
    private LocalTime shiftStartTime;

    @Column(name = "shift_end_time", nullable = false)
    private LocalTime shiftEndTime;

    @Column(name = "is_active_shift", nullable = false)
    private Boolean activeShift;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getShiftSchedule() {
        return shiftSchedule;
    }

    public void setShiftSchedule(String shiftSchedule) {
        this.shiftSchedule = shiftSchedule;
    }

    public Integer getShiftHours() {
        return shiftHours;
    }

    public void setShiftHours(Integer shiftHours) {
        this.shiftHours = shiftHours;
    }

    public String getShiftScheduledDays() {
        return shiftScheduledDays;
    }

    public void setShiftScheduledDays(String shiftScheduledDays) {
        this.shiftScheduledDays = shiftScheduledDays;
    }

    public LocalTime getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(LocalTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(LocalTime shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    public Boolean isActiveShift() {
        return activeShift;
    }

    public void setActiveShift(Boolean activeShift) {
        this.activeShift = activeShift;
    }
}
