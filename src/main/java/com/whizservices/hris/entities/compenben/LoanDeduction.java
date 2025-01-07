package com.whizservices.hris.entities.compenben;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.profile.Employee;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "wsi_loan_deductions")
public class LoanDeduction extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "loan_type", length = 100, nullable = false)
    private String loanType;

    @Column(name = "loan_description", length = 150, nullable = false)
    private String loanDescription;

    @Column(name = "loan_amount", nullable = false)
    private BigDecimal loanAmount;

    @Column(name = "loan_start_date", nullable = false)
    private LocalDate loanStartDate;

    @Column(name = "loan_end_date", nullable = false)
    private LocalDate loanEndDate;

    @Column(name = "monthly_deduction")
    private BigDecimal monthlyDeduction;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanDescription() {
        return loanDescription;
    }

    public void setLoanDescription(String loanDescription) {
        this.loanDescription = loanDescription;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(LocalDate loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public LocalDate getLoanEndDate() {
        return loanEndDate;
    }

    public void setLoanEndDate(LocalDate loanEndDate) {
        this.loanEndDate = loanEndDate;
    }

    public BigDecimal getMonthlyDeduction() {
        return monthlyDeduction;
    }

    public void setMonthlyDeduction(BigDecimal monthlyDeduction) {
        this.monthlyDeduction = monthlyDeduction;
    }
}
