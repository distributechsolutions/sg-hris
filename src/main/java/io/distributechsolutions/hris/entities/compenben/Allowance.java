package io.distributechsolutions.hris.entities.compenben;

import io.distributechsolutions.hris.entities.BaseEntity;
import io.distributechsolutions.hris.entities.profile.Employee;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sg_hris_allowance_benefits")
public class Allowance extends BaseEntity {
    @Column(name = "allowance_code", length = 50, nullable = false, unique = true)
    private String allowanceCode;

    @Column(name = "allowance_type", length = 50, nullable = false)
    private String allowanceType;

    @Column(name = "allowance_amount", nullable = false)
    private BigDecimal allowanceAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    public String getAllowanceCode() {
        return allowanceCode;
    }

    public void setAllowanceCode(String allowanceCode) {
        this.allowanceCode = allowanceCode;
    }

    public String getAllowanceType() {
        return allowanceType;
    }

    public void setAllowanceType(String allowanceType) {
        this.allowanceType = allowanceType;
    }

    public BigDecimal getAllowanceAmount() {
        return allowanceAmount;
    }

    public void setAllowanceAmount(BigDecimal allowanceAmount) {
        this.allowanceAmount = allowanceAmount;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
