package io.distributechsolutions.hris.services.reference;

import io.distributechsolutions.hris.dtos.reference.ProvinceDTO;
import io.distributechsolutions.hris.dtos.reference.RegionDTO;

import java.util.List;

public interface ProvinceService {
    ProvinceDTO getById(Long id);
    List<ProvinceDTO> getAll(int page, int pageSize);
    List<ProvinceDTO> getProvinceByRegion(RegionDTO regionDTO);
}
