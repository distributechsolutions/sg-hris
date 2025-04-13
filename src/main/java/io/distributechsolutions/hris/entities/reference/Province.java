package io.distributechsolutions.hris.entities.reference;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "sg_hris_ref_province")
public class Province implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "psg_code", nullable = false)
    private Long psgCode;

    @Column(name = "province_description", length = 250, nullable = false)
    private String provinceDescription;

    @Column(name = "region_code", nullable = false)
    private Long regionCode;

    @Column(name = "province_code", nullable = false)
    private Long provinceCode;

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

    public String getProvinceDescription() {
        return provinceDescription;
    }

    public void setProvinceDescription(String provinceDescription) {
        this.provinceDescription = provinceDescription;
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
}
