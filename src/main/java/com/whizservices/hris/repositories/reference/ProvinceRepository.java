package com.whizservices.hris.repositories.reference;

import com.whizservices.hris.entities.reference.Province;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    @Query("SELECT p FROM Province p WHERE p.regionCode = :param")
    List<Province> findProvincesByRegionCode(@Param("param") Long regionCode);
}
