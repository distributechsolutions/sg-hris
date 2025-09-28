package io.softwaregarage.hris.profile.entities;

import io.softwaregarage.hris.commons.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "sg_hris_employee_profile")
public class EmployeeProfile extends BaseEntity {
    @Column(name = "employee_number", length = 10, nullable = false, unique = true)
    private String employeeNumber;

    @Column(name = "last_name", length = 35, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 35, nullable = false)
    private String firstName;

    @Column(name = "middle_name", length = 35)
    private String middleName;

    @Column(name = "suffix", length = 5)
    private String suffix;

    @Column(name = "gender", length = 10, nullable = false)
    private String gender;

    @Column(name = "date_hired", nullable = false)
    private LocalDate dateHired;

    @Column(name = "employment_type", length = 25, nullable = false)
    private String employmentType;

    @Column(name = "contract_duration", length = 25, nullable = false)
    private String contractDuration;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", length = 25, nullable = false)
    private String status;

    public EmployeeProfile() {
    }

    public EmployeeProfile(String employeeNumber,
                           String lastName,
                           String firstName,
                           String middleName,
                           String suffix,
                           String gender,
                           LocalDate dateHired,
                           String employmentType,
                           String contractDuration,
                           LocalDate startDate,
                           LocalDate endDate,
                           String status) {
        this.employeeNumber = employeeNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.suffix = suffix;
        this.gender = gender;
        this.dateHired = dateHired;
        this.employmentType = employmentType;
        this.contractDuration = contractDuration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateHired() {
        return dateHired;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getContractDuration() {
        return contractDuration;
    }

    public void setContractDuration(String contractDuration) {
        this.contractDuration = contractDuration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
