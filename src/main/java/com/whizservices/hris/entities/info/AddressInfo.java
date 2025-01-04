package com.whizservices.hris.entities.info;

import com.whizservices.hris.entities.BaseEntity;
import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.entities.reference.Barangay;
import com.whizservices.hris.entities.reference.Municipality;
import com.whizservices.hris.entities.reference.Province;
import com.whizservices.hris.entities.reference.Region;

import jakarta.persistence.*;

@Entity
@Table(name = "wsi_address_information")
public class AddressInfo extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @Column(name = "address_type", length = 25, nullable = false )
    private String addressType;

    @Column(name = "address_detail", length = 100, nullable = false)
    private String addressDetail;

    @Column(name = "street_name", length = 50, nullable = false)
    private String streetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barangay_id", referencedColumnName = "id", nullable = false)
    private Barangay barangay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", referencedColumnName = "id", nullable = false)
    private Municipality municipality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", referencedColumnName = "id", nullable = false)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    private Region region;

    @Column(name = "postal_code", nullable = false)
    private Integer postalCode;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    public Barangay getBarangay() {
        return barangay;
    }

    public void setBarangay(Barangay barangay) {
        this.barangay = barangay;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }
}
