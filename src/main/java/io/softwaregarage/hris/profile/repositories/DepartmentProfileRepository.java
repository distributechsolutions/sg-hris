package io.softwaregarage.hris.profile.repositories;

import io.softwaregarage.hris.profile.entities.DepartmentProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DepartmentProfileRepository extends JpaRepository<DepartmentProfile, UUID> {
    @Query("""
           SELECT dp FROM DepartmentProfile dp
           WHERE LOWER(dp.employeeProfile.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(dp.employeeProfile.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(dp.employeeProfile.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(dp.department.code) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(dp.department.name) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<DepartmentProfile> findByStringParameter(@Param("param") String parameter);

    @Query("SELECT dp FROM DepartmentProfile dp WHERE dp.currentDepartment = :param")
    List<DepartmentProfile> findByBooleanParameter(@Param("param") boolean param);
}
