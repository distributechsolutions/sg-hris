package io.softwaregarage.hris.profile.repositories;

import io.softwaregarage.hris.profile.entities.AddressProfile;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AddressProfileRepository extends JpaRepository<AddressProfile, UUID> {
    @Query("SELECT ap FROM AddressProfile ap WHERE ap.employeeProfile = :param")
    List<AddressProfile> findByEmployee(@Param("param") EmployeeProfile employeeProfile);
}
