package com.whizservices.hris.entities.profile;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.reference.Position;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sg_hris_employee_position")
public class EmployeePosition extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", referencedColumnName = "id", nullable = false)
    private Position position;

    @Column(name = "is_current_position", nullable = false)
    private boolean currentPosition;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(boolean currentPosition) {
        this.currentPosition = currentPosition;
    }
}
