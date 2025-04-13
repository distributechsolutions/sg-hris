package io.distributechsolutions.hris.services.reference;

import io.distributechsolutions.hris.dtos.reference.BarangayDTO;
import io.distributechsolutions.hris.dtos.reference.MunicipalityDTO;

import java.util.List;

public interface BarangayService {
    BarangayDTO getById(Long id);
    List<BarangayDTO> getAll(int page, int pageSize);
    List<BarangayDTO> getBarangayByMunicipality(MunicipalityDTO municipalityDTO);
}
