package io.softwaregarage.hris.admin.services;

import io.softwaregarage.hris.admin.dtos.ProvinceDTO;
import io.softwaregarage.hris.admin.dtos.RegionDTO;

import java.util.List;

public interface ProvinceService {
    ProvinceDTO getById(Long id);
    List<ProvinceDTO> getAll(int page, int pageSize);
    List<ProvinceDTO> getProvinceByRegion(RegionDTO regionDTO);
}
