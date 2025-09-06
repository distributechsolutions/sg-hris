package io.softwaregarage.hris.admin.dtos;

import io.softwaregarage.hris.commons.BaseDTO;

public class DepartmentDTO extends BaseDTO {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
