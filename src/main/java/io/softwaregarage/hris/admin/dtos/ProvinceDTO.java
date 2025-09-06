package io.softwaregarage.hris.admin.dtos;

public class ProvinceDTO {
    private Long id;
    private Long psgCode;
    private String provinceDescription;
    private Long regionCode;
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
