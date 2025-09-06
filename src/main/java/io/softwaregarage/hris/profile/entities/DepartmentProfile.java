package io.softwaregarage.hris.profile.entities;

import io.softwaregarage.hris.admin.entities.Department;
import io.softwaregarage.hris.commons.BaseEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "sg_hris_department_profile")
public class DepartmentProfile extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeProfile employeeProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private io.softwaregarage.hris.admin.entities.Department department;

    @Column(name = "is_current_department", nullable = false)
    private boolean currentDepartment;

    public EmployeeProfile getEmployee() {
        return employeeProfile;
    }

    public void setEmployee(EmployeeProfile employeeProfile) {
        this.employeeProfile = employeeProfile;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public boolean isCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(boolean currentDepartment) {
        this.currentDepartment = currentDepartment;
    }
}
