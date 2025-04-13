package io.distributechsolutions.hris.services.reference;

import io.distributechsolutions.hris.dtos.reference.RegionDTO;

import java.util.List;

public interface RegionService {
    RegionDTO getById(Long id);
    List<RegionDTO> findAllRegions();
}
