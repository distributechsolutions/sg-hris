package io.distributechsolutions.hris.services.reference;

import io.distributechsolutions.hris.dtos.reference.MunicipalityDTO;
import io.distributechsolutions.hris.dtos.reference.ProvinceDTO;

import java.util.List;

public interface MunicipalityService {
    MunicipalityDTO getById(Long id);
    List<MunicipalityDTO> getAll(int page, int pageSize);
    List<MunicipalityDTO> getMunicipalityByProvince(ProvinceDTO provinceDTO);
}
