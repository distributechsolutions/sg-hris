package io.distributechsolutions.hris.dtos.attendance;

import io.distributechsolutions.hris.dtos.BaseDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;

import java.time.LocalTime;

public class EmployeeShiftScheduleDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private String shiftSchedule;
    private Integer shiftHours;
    private String shiftScheduledDays;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private Boolean activeShift;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
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
