package io.softwaregarage.hris.compenben.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

public class LeaveBenefitsDTO extends BaseDTO {
    private String leaveCode;
    private String leaveType;
    private Integer leaveForYear;
    private Integer leaveCount;
    private boolean leaveActive;
    private EmployeeProfileDTO employeeProfileDTO;

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getLeaveForYear() {
        return leaveForYear;
    }

    public void setLeaveForYear(Integer leaveForYear) {
        this.leaveForYear = leaveForYear;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }

    public boolean isLeaveActive() {
        return leaveActive;
    }

    public void setLeaveActive(boolean leaveActive) {
        this.leaveActive = leaveActive;
    }

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
    }
}
