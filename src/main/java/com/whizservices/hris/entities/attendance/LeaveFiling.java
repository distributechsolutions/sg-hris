package com.whizservices.hris.entities.attendance;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.compenben.LeaveBenefits;
import com.whizservices.hris.entities.profile.Employee;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wsi_leave_filing")
public class LeaveFiling extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_benefits_id", referencedColumnName = "id", nullable = false)
    private LeaveBenefits leaveBenefits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_approver_employee_id", referencedColumnName = "id", nullable = false)
    private Employee assignedApproverEmployee;

    @Column(name = "leave_date_and_time_from", nullable = false)
    private LocalDateTime leaveDateAndTimeFrom;

    @Column(name = "leave_date_and_time_to", nullable = false)
    private LocalDateTime leaveDateAndTimeTo;

    @Column(name = "leave_count", nullable = false)
    private Integer leaveCount;

    @Column(name = "remarks", length = 150, nullable = false)
    private String remarks;

    @Column(name = "leave_status", length = 150, nullable = false)
    private String leaveStatus;

    public LeaveBenefits getLeaveBenefits() {
        return leaveBenefits;
    }

    public void setLeaveBenefits(LeaveBenefits leaveBenefits) {
        this.leaveBenefits = leaveBenefits;
    }

    public Employee getAssignedApproverEmployee() {
        return assignedApproverEmployee;
    }

    public void setAssignedApproverEmployee(Employee assignedApproverEmployee) {
        this.assignedApproverEmployee = assignedApproverEmployee;
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
