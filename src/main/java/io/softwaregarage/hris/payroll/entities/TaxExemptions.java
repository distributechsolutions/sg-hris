package io.softwaregarage.hris.payroll.entities;

import io.softwaregarage.hris.commons.BaseEntity;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;

import jakarta.persistence.*;

@Entity
@Table(name = "sg_hris_tax_exemptions")
public class TaxExemptions extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private EmployeeProfile employeeProfile;

    @Column(name = "tax_exemption_percentage", nullable = false)
    private Double taxExemptionPercentage;

    @Column(name = "is_active_tax_exemption", nullable = false)
    private Boolean activeTaxExemption;

    public EmployeeProfile getEmployeeProfile() {
        return employeeProfile;
    }

    public void setEmployeeProfile(EmployeeProfile employeeProfile) {
        this.employeeProfile = employeeProfile;
    }

    public Double getTaxExemptionPercentage() {
        return taxExemptionPercentage;
    }

    public void setTaxExemptionPercentage(Double taxExemptionPercentage) {
        this.taxExemptionPercentage = taxExemptionPercentage;
    }

    public Boolean isActiveTaxExemption() {
        return activeTaxExemption;
    }

    public void setActiveTaxExemption(Boolean activeTaxExemption) {
        this.activeTaxExemption = activeTaxExemption;
    }
}
