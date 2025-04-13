package io.distributechsolutions.hris.repositories.reference;

import io.distributechsolutions.hris.entities.reference.Municipality;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    @Query("SELECT m FROM Municipality m WHERE m.provinceCode = :param")
    List<Municipality> findMunicipalitiesByProvinceCode(@Param("param") Long provinceCode);
}
