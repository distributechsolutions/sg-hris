package io.softwaregarage.hris.admin.services;

import io.softwaregarage.hris.admin.dtos.BarangayDTO;
import io.softwaregarage.hris.admin.dtos.MunicipalityDTO;

import java.util.List;

public interface BarangayService {
    BarangayDTO getById(Long id);
    List<BarangayDTO> getAll(int page, int pageSize);
    List<BarangayDTO> getBarangayByMunicipality(MunicipalityDTO municipalityDTO);
}
