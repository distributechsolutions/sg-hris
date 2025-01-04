package com.whizservices.hris.dtos.profile;

import com.whizservices.hris.dtos.BaseDTO;

import java.time.LocalDate;

public class EmployeeDTO extends BaseDTO {
    private String employeeNumber;
    private String biometricsNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private String suffix;
    private String gender;
    private LocalDate dateHired;
    private String atmAccountNumber;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String employeeNumber,
                    String biometricsNumber,
                    String lastName,
                    String firstName,
                    String middleName,
                    String suffix,
                    String gender,
                    LocalDate dateHired,
                    String atmAccountNumber) {
        this.employeeNumber = employeeNumber;
        this.biometricsNumber = biometricsNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.suffix = suffix;
        this.gender = gender;
        this.dateHired = dateHired;
        this.atmAccountNumber = atmAccountNumber;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getBiometricsNumber() {
        return biometricsNumber;
    }

    public void setBiometricsNumber(String biometricsNumber) {
        this.biometricsNumber = biometricsNumber;
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

    public String getAtmAccountNumber() {
        return atmAccountNumber;
    }

    public void setAtmAccountNumber(String atmAccountNumber) {
        this.atmAccountNumber = atmAccountNumber;
    }

    public String getEmployeeFullName() {
        return this.lastName.concat(this.suffix != null ? this.suffix : "")
                            .concat(", ")
                            .concat(this.firstName)
                            .concat(" ")
                            .concat(this.middleName);
    }
}
