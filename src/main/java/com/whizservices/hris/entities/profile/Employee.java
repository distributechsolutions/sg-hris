package com.whizservices.hris.entities.profile;

import com.whizservices.hris.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "wsi_employee")
public class Employee extends BaseEntity {
    @Column(name = "employee_number", length = 10, nullable = false, unique = true)
    private String employeeNumber;

    @Column(name = "biometrics_number", length = 10, nullable = false, unique = true)
    private String biometricsNumber;

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

    @Column(name = "atm_account_number", length = 15, nullable = false)
    private String atmAccountNumber;

    public Employee() {
    }

    public Employee(String employeeNumber,
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
}
