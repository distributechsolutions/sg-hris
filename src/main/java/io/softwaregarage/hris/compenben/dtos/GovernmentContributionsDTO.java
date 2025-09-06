package io.softwaregarage.hris.compenben.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

import java.math.BigDecimal;

public class GovernmentContributionsDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private BigDecimal sssContributionAmount;
    private BigDecimal hdmfContributionAmount;
    private BigDecimal philhealthContributionAmount;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
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
