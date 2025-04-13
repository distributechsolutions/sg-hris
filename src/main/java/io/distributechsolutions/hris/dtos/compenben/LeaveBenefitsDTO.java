package io.distributechsolutions.hris.dtos.compenben;

import io.distributechsolutions.hris.dtos.BaseDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;

public class LeaveBenefitsDTO extends BaseDTO {
    private String leaveCode;
    private String leaveType;
    private Integer leaveForYear;
    private Integer leaveCount;
    private boolean leaveActive;
    private EmployeeDTO employeeDTO;

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

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }
}
