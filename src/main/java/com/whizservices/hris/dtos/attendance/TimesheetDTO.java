package com.whizservices.hris.dtos.attendance;

import com.whizservices.hris.dtos.BaseDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimesheetDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private LocalDate logDate;
    private LocalTime logTimeIn;
    private LocalTime logTimeOut;
    private String shiftSchedule;
    private String exceptionRemarks;
    private String leaveRemarks;
    private LocalTime regularWorkedHours;
    private LocalTime overtimeWorkedHours;
    private LocalTime totalWorkedHours;
    private String status;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
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
