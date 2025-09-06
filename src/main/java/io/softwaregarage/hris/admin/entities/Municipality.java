package io.softwaregarage.hris.admin.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "sg_hris_ref_municipality")
public class Municipality implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "psg_code", nullable = false)
    private Long psgCode;

    @Column(name = "municipality_description", length = 250, nullable = false)
    private String municipalityDescription;

    @Column(name = "region_code", nullable = false)
    private Long regionCode;

    @Column(name = "province_code", nullable = false)
    private Long provinceCode;

    @Column(name = "municipality_code", nullable = false)
    private Long municipalityCode;

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

    public String getMunicipalityDescription() {
        return municipalityDescription;
    }

    public void setMunicipalityDescription(String municipalityDescription) {
        this.municipalityDescription = municipalityDescription;
    }

    public Long getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Long regionCode) {
        this.regionCode = regionCode;
    }

    public Long getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Long provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Long getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(Long municipalityCode) {
        this.municipalityCode = municipalityCode;
    }
}
