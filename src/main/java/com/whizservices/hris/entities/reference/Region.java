package com.whizservices.hris.entities.reference;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "sg_hris_ref_region")
public class Region implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "psg_code", nullable = false)
    private Long psgCode;

    @Column(name = "region_description", length = 250, nullable = false)
    private String regionDescription;

    @Column(name = "region_code", nullable = false)
    private Long regionCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPsgCode() {
        return psgCode;
    }

    public void setPsgCode(Long psgCode) {
        this.psgCode = psgCode;
    }

    public String getRegionDescription() {
        return regionDescription;
    }

    public void setRegionDescription(String regionDescription) {
        this.regionDescription = regionDescription;
    }

    public Long getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Long regionCode) {
        this.regionCode = regionCode;
    }
}
