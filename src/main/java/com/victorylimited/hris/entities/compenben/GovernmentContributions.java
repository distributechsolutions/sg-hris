package com.victorylimited.hris.entities.compenben;

import com.victorylimited.hris.entities.BaseEntity;
import com.victorylimited.hris.entities.profile.Employee;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vlh_government_contributions")
public class GovernmentContributions extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "sss_contribution_amount", nullable = false)
    private BigDecimal sssContrbutionAmount;

    @Column(name = "hdmf_contribution_amount", nullable = false)
    private BigDecimal hdmfContrbutionAmount;

    @Column(name = "philhealth_contribution_amount", nullable = false)
    private BigDecimal philhealthContributionAmount;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
