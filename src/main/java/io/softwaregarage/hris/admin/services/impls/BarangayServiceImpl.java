package io.softwaregarage.hris.admin.services.impls;

import io.softwaregarage.hris.admin.dtos.BarangayDTO;
import io.softwaregarage.hris.admin.dtos.MunicipalityDTO;
import io.softwaregarage.hris.admin.entities.Barangay;
import io.softwaregarage.hris.admin.repositories.BarangayRepository;
import io.softwaregarage.hris.admin.services.BarangayService;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarangayServiceImpl implements BarangayService {
    private final BarangayRepository barangayRepository;

    public BarangayServiceImpl(BarangayRepository barangayRepository) {
        this.barangayRepository = barangayRepository;
    }

    @Override
    public BarangayDTO getById(Long id) {
        BarangayDTO barangayDTO = null;

        if (id != null) {
            Barangay barangay = barangayRepository.getReferenceById(id);

            barangayDTO = new BarangayDTO();

            barangayDTO.setId(barangay.getId());
            barangayDTO.setBarangayCode(barangay.getBarangayCode());
            barangayDTO.setBarangayDescription(barangay.getBarangayDescription());
            barangayDTO.setMunicipalityCode(barangay.getMunicipalityCode());
            barangayDTO.setProvinceCode(barangay.getProvinceCode());
            barangayDTO.setRegionCode(barangay.getRegionCode());
        }

        return barangayDTO;
    }

    @Override
    public List<BarangayDTO> getAll(int page, int pageSize) {
        List<BarangayDTO> barangayDTOList = new ArrayList<>();
        List<Barangay> barangayList = barangayRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        if (!barangayList.isEmpty()) {
            for (Barangay barangay : barangayList) {
                BarangayDTO barangayDTO = new BarangayDTO();
                barangayDTO.setId(barangay.getId());
                barangayDTO.setBarangayCode(barangay.getBarangayCode());
                barangayDTO.setBarangayDescription(barangay.getBarangayDescription());
                barangayDTO.setMunicipalityCode(barangay.getMunicipalityCode());
                barangayDTO.setProvinceCode(barangayDTO.getProvinceCode());
                barangayDTO.setRegionCode(barangayDTO.getRegionCode());

                barangayDTOList.add(barangayDTO);
            }
        }

        return barangayDTOList;
    }

    @Override
    public List<BarangayDTO> getBarangayByMunicipality(MunicipalityDTO municipalityDTO) {
        List<Barangay> barangayList;
        List<BarangayDTO> barangayDTOList = new ArrayList<>();

        if (municipalityDTO != null) {
            barangayList = barangayRepository.findBarangaysByMunicipalityCode(municipalityDTO.getMunicipalityCode());

            if (!barangayList.isEmpty()) {
                for (Barangay barangay : barangayList) {
                    BarangayDTO barangayDTO = getBarangayDTO(barangay);
                    barangayDTOList.add(barangayDTO);
                }
            }
        }

        return barangayDTOList;
    }

    private static BarangayDTO getBarangayDTO(Barangay barangay) {
        BarangayDTO barangayDTO = new BarangayDTO();

        barangayDTO.setId(barangay.getId());
        barangayDTO.setBarangayCode(barangay.getBarangayCode());
        barangayDTO.setBarangayDescription(barangay.getBarangayDescription());
        barangayDTO.setRegionCode(barangay.getRegionCode());
        barangayDTO.setProvinceCode(barangay.getProvinceCode());
        barangayDTO.setMunicipalityCode(barangay.getMunicipalityCode());

        return barangayDTO;
    }
}
