package io.softwaregarage.hris.attendance.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

import java.time.LocalTime;

public class EmployeeShiftScheduleDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private EmployeeProfileDTO assignedApproverEmployeeProfileDTO;
    private LocalTime startTime;
    private LocalTime endTime;
    private String shiftSchedule;
    private Integer shiftHours;
    private String shiftScheduledDays;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;
    private Boolean activeShift;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
    }

    public EmployeeProfileDTO getAssignedApproverEmployeeProfileDTO() {
        return assignedApproverEmployeeProfileDTO;
    }

    public void setAssignedApproverEmployeeProfileDTO(EmployeeProfileDTO assignedApproverEmployeeProfileDTO) {
        this.assignedApproverEmployeeProfileDTO = assignedApproverEmployeeProfileDTO;
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
