package io.softwaregarage.hris.payroll.dtos;

import io.softwaregarage.hris.commons.BaseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TaxRatesDTO extends BaseDTO {
    private Long taxYear;
    private LocalDate effectiveDate;
    private BigDecimal lowerBoundAmount;
    private BigDecimal upperBoundAmount;
    private BigDecimal baseTax;
    private BigDecimal rate;
    private Boolean activeTaxRate;

    public Long getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(Long taxYear) {
        this.taxYear = taxYear;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getLowerBoundAmount() {
        return lowerBoundAmount;
    }

    public void setLowerBoundAmount(BigDecimal lowerBoundAmount) {
        this.lowerBoundAmount = lowerBoundAmount;
    }

    public BigDecimal getUpperBoundAmount() {
        return upperBoundAmount;
    }

    public void setUpperBoundAmount(BigDecimal upperBoundAmount) {
        this.upperBoundAmount = upperBoundAmount;
    }

    public BigDecimal getBaseTax() {
        return baseTax;
    }

    public void setBaseTax(BigDecimal baseTax) {
        this.baseTax = baseTax;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Boolean isActiveTaxRate() {
        return activeTaxRate;
    }

    public void setActiveTaxRate(Boolean activeTaxRate) {
        this.activeTaxRate = activeTaxRate;
    }
}
