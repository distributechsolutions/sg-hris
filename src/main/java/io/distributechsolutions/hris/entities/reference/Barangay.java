package io.distributechsolutions.hris.entities.reference;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "sg_hris_ref_barangay")
public class Barangay implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "barangay_code", nullable = false)
    private Long barangayCode;

    @Column(name = "barangay_description", length = 250, nullable = false)
    private String barangayDescription;

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

    public Long getBarangayCode() {
        return barangayCode;
    }

    public void setBarangayCode(Long barangayCode) {
        this.barangayCode = barangayCode;
    }

    public String getBarangayDescription() {
        return barangayDescription;
    }

    public void setBarangayDescription(String barangayDescription) {
        this.barangayDescription = barangayDescription;
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
