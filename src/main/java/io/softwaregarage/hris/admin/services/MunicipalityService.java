package io.softwaregarage.hris.admin.services;

import io.softwaregarage.hris.admin.dtos.MunicipalityDTO;
import io.softwaregarage.hris.admin.dtos.ProvinceDTO;

import java.util.List;

public interface MunicipalityService {
    MunicipalityDTO getById(Long id);
    List<MunicipalityDTO> getAll(int page, int pageSize);
    List<MunicipalityDTO> getMunicipalityByProvince(ProvinceDTO provinceDTO);
}
