package io.distributechsolutions.hris.dtos.attendance;

import io.distributechsolutions.hris.dtos.BaseDTO;
import io.distributechsolutions.hris.dtos.compenben.LeaveBenefitsDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;

import java.time.LocalDateTime;

public class EmployeeLeaveFilingDTO extends BaseDTO {
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
