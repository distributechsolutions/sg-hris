package io.softwaregarage.hris.profile.repositories;

import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.profile.entities.PersonalProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PersonalProfileRepository extends JpaRepository<PersonalProfile, UUID> {
    @Query("SELECT pp FROM PersonalProfile pp WHERE pp.employeeProfile = :param")
    PersonalProfile findByEmployee(@Param("param") EmployeeProfile employeeProfile);
}
