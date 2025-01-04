package com.whizservices.hris.services.reference;

import com.whizservices.hris.dtos.reference.RegionDTO;

import java.util.List;

public interface RegionService {
    RegionDTO getById(Long id);
    List<RegionDTO> findAllRegions();
}
