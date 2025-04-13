package io.distributechsolutions.hris.dtos.compenben;

import io.distributechsolutions.hris.dtos.BaseDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;

import java.math.BigDecimal;

public class AllowanceDTO extends BaseDTO {
    private String allowanceCode;
    private String allowanceType;
    private BigDecimal allowanceAmount;
    private EmployeeDTO employeeDTO;

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

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }
}
