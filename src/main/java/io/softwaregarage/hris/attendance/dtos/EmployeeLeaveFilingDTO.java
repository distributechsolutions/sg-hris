package io.softwaregarage.hris.attendance.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.compenben.dtos.LeaveBenefitsDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

import java.time.LocalDateTime;

public class EmployeeLeaveFilingDTO extends BaseDTO {
    private LeaveBenefitsDTO leaveBenefitsDTO;
    private EmployeeProfileDTO assignedApproverEmployeeProfileDTO;
    private LocalDateTime leaveDateAndTimeFrom;
    private LocalDateTime leaveDateAndTimeTo;
    private Integer leaveCount;
    private String remarks;
    private String leaveStatus;

    public LeaveBenefitsDTO getLeaveBenefitsDTO() {
        return leaveBenefitsDTO;
    }

    public void setLeaveBenefitsDTO(LeaveBenefitsDTO leaveBenefitsDTO) {
        this.leaveBenefitsDTO = leaveBenefitsDTO;
    }

    public EmployeeProfileDTO getAssignedApproverEmployeeDTO() {
        return assignedApproverEmployeeProfileDTO;
    }

    public void setAssignedApproverEmployeeDTO(EmployeeProfileDTO assignedApproverEmployeeProfileDTO) {
        this.assignedApproverEmployeeProfileDTO = assignedApproverEmployeeProfileDTO;
    }

    public LocalDateTime getLeaveDateAndTimeFrom() {
        return leaveDateAndTimeFrom;
    }

    public void setLeaveDateAndTimeFrom(LocalDateTime leaveDateAndTimeFrom) {
        this.leaveDateAndTimeFrom = leaveDateAndTimeFrom;
    }

    public LocalDateTime getLeaveDateAndTimeTo() {
        return leaveDateAndTimeTo;
    }

    public void setLeaveDateAndTimeTo(LocalDateTime leaveDateAndTimeTo) {
        this.leaveDateAndTimeTo = leaveDateAndTimeTo;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
}
