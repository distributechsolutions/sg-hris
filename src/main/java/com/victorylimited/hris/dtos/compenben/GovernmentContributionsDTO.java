package com.victorylimited.hris.dtos.compenben;

import com.victorylimited.hris.dtos.BaseDTO;
import com.victorylimited.hris.dtos.profile.EmployeeDTO;

import java.math.BigDecimal;

public class GovernmentContributionsDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private BigDecimal sssContributionAmount;
    private BigDecimal hdmfContributionAmount;
    private BigDecimal philhealthContributionAmount;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public BigDecimal getSssContributionAmount() {
        return sssContributionAmount;
    }

    public void setSssContributionAmount(BigDecimal sssContributionAmount) {
        this.sssContributionAmount = sssContributionAmount;
    }

    public BigDecimal getHdmfContributionAmount() {
        return hdmfContributionAmount;
    }

    public void setHdmfContributionAmount(BigDecimal hdmfContributionAmount) {
        this.hdmfContributionAmount = hdmfContributionAmount;
    }

    public BigDecimal getPhilhealthContributionAmount() {
        return philhealthContributionAmount;
    }

    public void setPhilhealthContributionAmount(BigDecimal philhealthContributionAmount) {
        this.philhealthContributionAmount = philhealthContributionAmount;
    }
}
