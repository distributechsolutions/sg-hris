package com.whizservices.hris.dtos.reference;

public class BarangayDTO {
    private Long id;
    private Long barangayCode;
    private String barangayDescription;
    private Long regionCode;
    private Long provinceCode;
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
