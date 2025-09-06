package io.softwaregarage.hris.profile.dtos;

import io.softwaregarage.hris.commons.BaseDTO;

public class DepartmentProfileDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private io.softwaregarage.hris.admin.dtos.DepartmentDTO departmentDTO;
    private boolean currentDepartment;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
    }

    public io.softwaregarage.hris.admin.dtos.DepartmentDTO getDepartmentDTO() {
        return departmentDTO;
    }

    public void setDepartmentDTO(io.softwaregarage.hris.admin.dtos.DepartmentDTO departmentDTO) {
        this.departmentDTO = departmentDTO;
    }

    public boolean isCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(boolean currentDepartment) {
        this.currentDepartment = currentDepartment;
    }
}
