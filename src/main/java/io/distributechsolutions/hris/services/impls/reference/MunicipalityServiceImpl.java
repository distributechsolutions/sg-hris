package io.distributechsolutions.hris.services.impls.reference;

import io.distributechsolutions.hris.dtos.reference.MunicipalityDTO;
import io.distributechsolutions.hris.dtos.reference.ProvinceDTO;
import io.distributechsolutions.hris.entities.reference.Municipality;
import io.distributechsolutions.hris.repositories.reference.MunicipalityRepository;
import io.distributechsolutions.hris.services.reference.MunicipalityService;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MunicipalityServiceImpl implements MunicipalityService {
    private final MunicipalityRepository municipalityRepository;

    public MunicipalityServiceImpl(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    @Override
    public MunicipalityDTO getById(Long id) {
        MunicipalityDTO municipalityDTO = null;

        if (id != null) {
            Municipality municipality = municipalityRepository.getReferenceById(id);
            municipalityDTO = new MunicipalityDTO();

            municipalityDTO.setId(municipality.getId());
            municipalityDTO.setPsgCode(municipality.getPsgCode());
            municipalityDTO.setMunicipalityDescription(municipality.getMunicipalityDescription());
            municipalityDTO.setProvinceCode(municipality.getProvinceCode());
            municipalityDTO.setRegionCode(municipality.getRegionCode());
        }

        return municipalityDTO;
    }

    @Override
    public List<MunicipalityDTO> getAll(int page, int pageSize) {
        List<MunicipalityDTO> municipalityDTOList = new ArrayList<>();
        List<Municipality> municipalityList = municipalityRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        if (!municipalityList.isEmpty()) {
            for (Municipality municipality : municipalityList) {
                MunicipalityDTO municipalityDTO = getMunicipalityDTO(municipality);

                municipalityDTOList.add(municipalityDTO);
            }
        }

        return municipalityDTOList;
    }

    @Override
    public List<MunicipalityDTO> getMunicipalityByProvince(ProvinceDTO provinceDTO) {
        List<Municipality> municipalityList;
        List<MunicipalityDTO> municipalityDTOList = new ArrayList<>();

        if (provinceDTO != null) {
            municipalityList = municipalityRepository.findMunicipalitiesByProvinceCode(provinceDTO.getProvinceCode());

            if (!municipalityList.isEmpty()) {
                for (Municipality municipality : municipalityList) {
                    MunicipalityDTO municipalityDTO = getMunicipalityDTO(municipality);
                    municipalityDTOList.add(municipalityDTO);
                }
            }
        }

        return municipalityDTOList;
    }

    private static MunicipalityDTO getMunicipalityDTO(Municipality municipality) {
        MunicipalityDTO municipalityDTO = new MunicipalityDTO();

        municipalityDTO.setId(municipality.getId());
        municipalityDTO.setPsgCode(municipality.getPsgCode());
        municipalityDTO.setMunicipalityDescription(municipality.getMunicipalityDescription());
        municipalityDTO.setRegionCode(municipality.getRegionCode());
        municipalityDTO.setProvinceCode(municipality.getProvinceCode());
        municipalityDTO.setMunicipalityCode(municipality.getMunicipalityCode());

        return municipalityDTO;
    }
}
