package io.softwaregarage.hris.compenben.repositories;

import io.softwaregarage.hris.compenben.entities.Rates;

import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RatesRepository extends JpaRepository<Rates, UUID> {
    @Query("""
           SELECT r FROM Rates r
           WHERE LOWER(r.employeeProfile.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(r.employeeProfile.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(r.employeeProfile.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(r.rateType) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<Rates> findByStringParameter(@Param("param") String param);

    @Query("SELECT r FROM Rates r WHERE r.employeeProfile = :employeeProfile")
    Rates findByEmployee(@Param("employeeProfile") EmployeeProfile employeeProfile);
}
