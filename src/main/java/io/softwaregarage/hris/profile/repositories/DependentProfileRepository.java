package io.softwaregarage.hris.profile.repositories;

import io.softwaregarage.hris.profile.entities.DependentProfile;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DependentProfileRepository extends JpaRepository<DependentProfile, UUID> {
    @Query("SELECT dp FROM DependentProfile dp WHERE dp.employeeProfile = :param")
    List<DependentProfile> findByEmployee(@Param("param") EmployeeProfile employeeProfile);
}
