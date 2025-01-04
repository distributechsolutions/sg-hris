package com.whizservices.hris.services.reference;

import com.whizservices.hris.dtos.reference.MunicipalityDTO;
import com.whizservices.hris.dtos.reference.ProvinceDTO;

import java.util.List;

public interface MunicipalityService {
    MunicipalityDTO getById(Long id);
    List<MunicipalityDTO> getAll(int page, int pageSize);
    List<MunicipalityDTO> getMunicipalityByProvince(ProvinceDTO provinceDTO);
}
