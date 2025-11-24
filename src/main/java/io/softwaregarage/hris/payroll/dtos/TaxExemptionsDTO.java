package io.softwaregarage.hris.payroll.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.profile.dtos.EmployeeProfileDTO;

import java.math.BigDecimal;

public class TaxExemptionsDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private Double taxExemptionPercentage;
    private Boolean activeTaxExemption;

    public EmployeeProfileDTO getEmployeeProfileDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeProfileDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
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
