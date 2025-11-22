package io.softwaregarage.hris.admin.entities;

import io.softwaregarage.hris.commons.BaseEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "sg_hris_department")
public class Department extends BaseEntity {
    @Column(name = "department_code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "department_name", length = 35, nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
