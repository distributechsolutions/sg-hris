package io.softwaregarage.hris.attendance.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmployeeTimesheetDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private LocalDate logDate;
    private LocalTime logTime;
    private String logDetail;
    private byte[] logImage;
    private String status;
    private EmployeeShiftScheduleDTO shiftScheduleDTO;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
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

    public EmployeeShiftScheduleDTO getShiftScheduleDTO() {
        return shiftScheduleDTO;
    }

    public void setShiftScheduleDTO(EmployeeShiftScheduleDTO shiftScheduleDTO) {
        this.shiftScheduleDTO = shiftScheduleDTO;
    }
}
