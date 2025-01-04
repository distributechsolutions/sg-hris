package com.whizservices.hris.entities.admin;

import com.whizservices.hris.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "wsi_position")
public class Position extends BaseEntity {
    @Column(name = "position_code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "position_name", length = 35, nullable = false, unique = true)
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
