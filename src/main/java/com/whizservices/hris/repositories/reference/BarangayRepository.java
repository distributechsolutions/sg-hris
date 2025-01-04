package com.whizservices.hris.repositories.reference;

import com.whizservices.hris.entities.reference.Barangay;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BarangayRepository extends JpaRepository<Barangay, Long> {
    @Query("SELECT b FROM Barangay b WHERE b.municipalityCode = :param")
    List<Barangay> findBarangaysByMunicipalityCode(@Param("param") Long municipalityCode);
}
