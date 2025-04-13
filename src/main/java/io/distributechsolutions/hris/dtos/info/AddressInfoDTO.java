package io.distributechsolutions.hris.dtos.info;

import io.distributechsolutions.hris.dtos.BaseDTO;
import io.distributechsolutions.hris.dtos.profile.EmployeeDTO;
import io.distributechsolutions.hris.dtos.reference.BarangayDTO;
import io.distributechsolutions.hris.dtos.reference.MunicipalityDTO;
import io.distributechsolutions.hris.dtos.reference.ProvinceDTO;
import io.distributechsolutions.hris.dtos.reference.RegionDTO;

public class AddressInfoDTO extends BaseDTO {
    private EmployeeDTO employeeDTO;
    private String addressType;
    private String addressDetail;
    private String streetName;
    private BarangayDTO barangayDTO;
    private MunicipalityDTO municipalityDTO;
    private ProvinceDTO provinceDTO;
    private RegionDTO regionDTO;
    private Integer postalCode;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
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
