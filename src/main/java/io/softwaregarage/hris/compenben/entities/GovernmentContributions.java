package io.softwaregarage.hris.compenben.entities;

import io.softwaregarage.hris.commons.BaseEntity;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sg_hris_government_contributions")
public class GovernmentContributions extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, unique = true)
    private EmployeeProfile employeeProfile;

    @Column(name = "sss_contribution_amount", nullable = false)
    private BigDecimal sssContrbutionAmount;

    @Column(name = "hdmf_contribution_amount", nullable = false)
    private BigDecimal hdmfContrbutionAmount;

    @Column(name = "philhealth_contribution_amount", nullable = false)
    private BigDecimal philhealthContributionAmount;

    public EmployeeProfile getEmployee() {
        return employeeProfile;
    }

    public void setEmployee(EmployeeProfile employeeProfile) {
        this.employeeProfile = employeeProfile;
    }

    public BigDecimal getSssContrbutionAmount() {
        return sssContrbutionAmount;
    }

    public void setSssContrbutionAmount(BigDecimal sssContrbutionAmount) {
        this.sssContrbutionAmount = sssContrbutionAmount;
    }

    public BigDecimal getHdmfContrbutionAmount() {
        return hdmfContrbutionAmount;
    }

    public void setHdmfContrbutionAmount(BigDecimal hdmfContrbutionAmount) {
        this.hdmfContrbutionAmount = hdmfContrbutionAmount;
    }

    public BigDecimal getPhilhealthContributionAmount() {
        return philhealthContributionAmount;
    }

    public void setPhilhealthContributionAmount(BigDecimal philhealthContributionAmount) {
        this.philhealthContributionAmount = philhealthContributionAmount;
    }
}
