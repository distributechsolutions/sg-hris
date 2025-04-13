package io.distributechsolutions.hris.dtos.reference;

public class MunicipalityDTO {
    private Long id;
    private Long psgCode;
    private String municipalityDescription;
    private Long regionCode;
    private Long provinceCode;
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
