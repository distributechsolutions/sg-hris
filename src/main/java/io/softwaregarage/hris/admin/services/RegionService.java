package io.softwaregarage.hris.admin.services;

import io.softwaregarage.hris.admin.dtos.RegionDTO;

import java.util.List;

public interface RegionService {
    RegionDTO getById(Long id);
    List<RegionDTO> findAllRegions();
}
