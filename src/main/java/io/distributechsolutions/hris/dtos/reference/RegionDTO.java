package io.distributechsolutions.hris.dtos.reference;

public class RegionDTO {
    private Long id;
    private Long psgCode;
    private String regionDescription;
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
