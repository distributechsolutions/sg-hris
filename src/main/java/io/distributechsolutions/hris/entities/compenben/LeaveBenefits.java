package io.distributechsolutions.hris.entities.compenben;

import io.distributechsolutions.hris.entities.BaseEntity;
import io.distributechsolutions.hris.entities.profile.Employee;
import jakarta.persistence.*;

@Entity
@Table(name = "sg_hris_leave_benefits")
public class LeaveBenefits extends BaseEntity {
    @Column(name = "leave_code", length = 50, nullable = false, unique = true)
    private String leaveCode;

    @Column(name = "leave_type", length = 50, nullable = false)
    private String leaveType;

    @Column(name = "leave_for_year", nullable = false)
    private Integer leaveForYear;

    @Column(name = "leave_count", nullable = false)
    private Integer leaveCount;

    @Column(name = "is_leave_active", nullable = false)
    private boolean leaveActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
