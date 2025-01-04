package com.whizservices.hris.dtos.info;

import com.whizservices.hris.dtos.BaseDTO;
import com.whizservices.hris.dtos.profile.EmployeeDTO;

import java.time.LocalDate;

public class DependentInfoDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private String fullName;
    private LocalDate dateOfBirth;
    private Integer age;
    private String relationship;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
