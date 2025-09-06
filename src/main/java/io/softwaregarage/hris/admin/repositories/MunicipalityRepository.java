package io.softwaregarage.hris.admin.repositories;

import io.softwaregarage.hris.admin.entities.Municipality;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    @Query("SELECT m FROM Municipality m WHERE m.provinceCode = :param")
    List<Municipality> findMunicipalitiesByProvinceCode(@Param("param") Long provinceCode);
}
