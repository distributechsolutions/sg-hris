package com.whizservices.hris.dtos.attendance;

import com.whizservices.hris.dtos.BaseDTO;
import com.whizservices.hris.dtos.compenben.LeaveBenefitsDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;

import java.time.LocalDateTime;

public class LeaveFilingDTO extends BaseDTO {
    private LeaveBenefitsDTO leaveBenefitsDTO;
    private EmployeeDTO assignedApproverEmployeeDTO;
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

    public EmployeeDTO getAssignedApproverEmployeeDTO() {
        return assignedApproverEmployeeDTO;
    }

    public void setAssignedApproverEmployeeDTO(EmployeeDTO assignedApproverEmployeeDTO) {
        this.assignedApproverEmployeeDTO = assignedApproverEmployeeDTO;
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
