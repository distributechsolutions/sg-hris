package io.softwaregarage.hris.profile.dtos;

import io.softwaregarage.hris.commons.BaseDTO;
import io.softwaregarage.hris.admin.dtos.BarangayDTO;
import io.softwaregarage.hris.admin.dtos.MunicipalityDTO;
import io.softwaregarage.hris.admin.dtos.ProvinceDTO;
import io.softwaregarage.hris.admin.dtos.RegionDTO;

public class AddressProfileDTO extends BaseDTO {
    private EmployeeProfileDTO employeeProfileDTO;
    private String addressType;
    private String addressDetail;
    private String streetName;
    private BarangayDTO barangayDTO;
    private MunicipalityDTO municipalityDTO;
    private ProvinceDTO provinceDTO;
    private RegionDTO regionDTO;
    private Integer postalCode;

    public EmployeeProfileDTO getEmployeeDTO() {
        return employeeProfileDTO;
    }

    public void setEmployeeDTO(EmployeeProfileDTO employeeProfileDTO) {
        this.employeeProfileDTO = employeeProfileDTO;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public BarangayDTO getBarangayDTO() {
        return barangayDTO;
    }

    public void setBarangayDTO(BarangayDTO barangayDTO) {
        this.barangayDTO = barangayDTO;
    }

    public MunicipalityDTO getMunicipalityDTO() {
        return municipalityDTO;
    }

    public void setMunicipalityDTO(MunicipalityDTO municipalityDTO) {
        this.municipalityDTO = municipalityDTO;
    }

    public ProvinceDTO getProvinceDTO() {
        return provinceDTO;
    }

    public void setProvinceDTO(ProvinceDTO provinceDTO) {
        this.provinceDTO = provinceDTO;
    }

    public RegionDTO getRegionDTO() {
        return regionDTO;
    }

    public void setRegionDTO(RegionDTO regionDTO) {
        this.regionDTO = regionDTO;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }
}
