package io.softwaregarage.hris.profile.entities;

import io.softwaregarage.hris.commons.BaseEntity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "sg_hris_personal_profile")
public class PersonalProfile extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, unique = true)
    private EmployeeProfile employeeProfile;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "place_of_birth", length = 150, nullable = false)
    private String placeOfBirth;

    @Column(name = "marital_status", length = 20, nullable = false)
    private String maritalStatus;

    @Column(name = "maiden_name", length = 105)
    private String maidenName;

    @Column(name = "spouse_name", length = 105)
    private String spouseName;

    @Column(name = "contact_number", nullable = false)
    private Long contactNumber;

    @Column(name = "email_address", length = 50, nullable = false)
    private String emailAddress;

    @Column(name = "tax_identification_number", length = 9, nullable = false)
    private String taxIdentificationNumber;

    @Column(name = "sss_number", length = 10, nullable = false)
    private String sssNumber;

    @Column(name = "hdmf_number", length = 12, nullable = false)
    private String hdmfNumber;

    @Column(name = "philhealth_number", length = 12, nullable = false)
    private String philhealthNumber;

    public EmployeeProfile getEmployee() {
        return employeeProfile;
    }

    public void setEmployee(EmployeeProfile employeeProfile) {
        this.employeeProfile = employeeProfile;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public void setSssNumber(String sssNumber) {
        this.sssNumber = sssNumber;
    }

    public String getHdmfNumber() {
        return hdmfNumber;
    }

    public void setHdmfNumber(String hdmfNumber) {
        this.hdmfNumber = hdmfNumber;
    }

    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    public void setPhilhealthNumber(String philhealthNumber) {
        this.philhealthNumber = philhealthNumber;
    }
}
