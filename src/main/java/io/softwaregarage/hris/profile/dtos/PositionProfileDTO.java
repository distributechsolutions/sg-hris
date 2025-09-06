package io.softwaregarage.hris.profile.dtos;

import io.softwaregarage.hris.commons.BaseDTO;

public class PositionProfileDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private io.softwaregarage.hris.admin.dtos.PositionDTO positionDTO;
    private boolean currentPosition;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
    }

    public io.softwaregarage.hris.admin.dtos.PositionDTO getPositionDTO() {
        return positionDTO;
    }

    public void setPositionDTO(io.softwaregarage.hris.admin.dtos.PositionDTO positionDTO) {
        this.positionDTO = positionDTO;
    }

    public boolean isCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(boolean currentPosition) {
        this.currentPosition = currentPosition;
    }
}
