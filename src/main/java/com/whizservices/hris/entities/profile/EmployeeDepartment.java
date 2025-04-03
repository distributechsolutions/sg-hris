package com.whizservices.hris.entities.profile;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.admin.Department;
import jakarta.persistence.*;

@Entity
@Table(name = "sg_hris_employee_department")
public class EmployeeDepartment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;

    @Column(name = "is_current_department", nullable = false)
    private boolean currentDepartment;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
