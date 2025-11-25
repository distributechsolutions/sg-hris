package io.softwaregarage.hris.payroll.entities;

import io.softwaregarage.hris.commons.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sg_hris_tax_rates")
public class TaxRates extends BaseEntity {
    @Column(name = "tax_year", nullable = false)
    private Long taxYear;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "lower_bound_amount", nullable = false)
    private BigDecimal lowerBoundAmount;

    @Column(name = "upper_bound_amount")
    private BigDecimal upperBoundAmount;

    @Column(name = "base_tax", nullable = false)
    private BigDecimal baseTax;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "is_active_tax_rate", nullable = false)
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
