package io.softwaregarage.hris.profile.repositories;

import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.profile.entities.DocumentProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentProfileRepository extends JpaRepository<DocumentProfile, UUID> {
    @Query("SELECT dp FROM DocumentProfile dp WHERE dp.employeeProfile = :param")
    List<DocumentProfile> getByEmployee(@Param("param") EmployeeProfile employeeProfile);

    @Query("SELECT dp FROM DocumentProfile dp WHERE dp.employeeProfile = :param AND dp.documentType = 'ID Picture'")
    DocumentProfile getIDPictureByEmployeeProfile(@Param("param") EmployeeProfile employeeProfile);
}
