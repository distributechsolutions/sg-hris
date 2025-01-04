package com.whizservices.hris.services.impls.reference;

import com.whizservices.hris.dtos.reference.RegionDTO;
import com.whizservices.hris.entities.reference.Region;
import com.whizservices.hris.repositories.reference.RegionRepository;
import com.whizservices.hris.services.reference.RegionService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;

    public RegionServiceImpl(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Override
    public RegionDTO getById(Long id) {
        RegionDTO regionDTO = null;

        if (id != null) {
            Region region = regionRepository.getReferenceById(id);
            regionDTO = new RegionDTO();

            regionDTO.setId(region.getId());
            regionDTO.setPsgCode(region.getPsgCode());
            regionDTO.setRegionDescription(region.getRegionDescription());
            regionDTO.setRegionCode(region.getRegionCode());
        }

        return regionDTO;
    }

    @Override
    public List<RegionDTO> findAllRegions() {
        List<Region> regionList = regionRepository.findAll();
        List<RegionDTO> regionDTOList = new ArrayList<>();

        if (!regionList.isEmpty()) {
            for (Region region : regionList) {
                RegionDTO regionDTO = new RegionDTO();

                regionDTO.setId(region.getId());
                regionDTO.setPsgCode(region.getPsgCode());
                regionDTO.setRegionDescription(region.getRegionDescription());
                regionDTO.setRegionCode(region.getRegionCode());

                regionDTOList.add(regionDTO);
            }
        }

        return regionDTOList;
    }
}
