package io.softwaregarage.hris.profile.dtos;

import io.softwaregarage.hris.commons.BaseDTO;

import java.time.LocalDate;

public class EmployeeProfileDTO extends BaseDTO {
    private String employeeNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private String suffix;
    private String gender;
    private LocalDate dateHired;
    private LocalDate dateResigned;
    private String employmentType;
    private String contractDuration;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public EmployeeProfileDTO() {
    }

    public EmployeeProfileDTO(String employeeNumber,
                              String lastName,
                              String firstName,
                              String middleName,
                              String suffix,
                              String gender,
                              LocalDate dateHired,
                              LocalDate dateResigned,
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
        this.dateResigned = dateResigned;
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

    public LocalDate getDateResigned() {
        return dateResigned;
    }

    public void setDateResigned(LocalDate dateResigned) {
        this.dateResigned = dateResigned;
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

    public String getEmployeeFullName() {
        return this.lastName.concat(this.suffix != null ? this.suffix : "")
                            .concat(", ")
                            .concat(this.firstName)
                            .concat(" ")
                            .concat(this.middleName);
    }
}
