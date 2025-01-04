package com.whizservices.hris.services.reference;

import com.whizservices.hris.dtos.reference.BarangayDTO;
import com.whizservices.hris.dtos.reference.MunicipalityDTO;

import java.util.List;

public interface BarangayService {
    BarangayDTO getById(Long id);
    List<BarangayDTO> getAll(int page, int pageSize);
    List<BarangayDTO> getBarangayByMunicipality(MunicipalityDTO municipalityDTO);
}
