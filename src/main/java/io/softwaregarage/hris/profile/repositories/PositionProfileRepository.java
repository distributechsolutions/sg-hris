package io.softwaregarage.hris.profile.repositories;

import io.softwaregarage.hris.profile.entities.PositionProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PositionProfileRepository extends JpaRepository<PositionProfile, UUID> {
    @Query("""
           SELECT pp FROM PositionProfile pp
           WHERE LOWER(pp.employeeProfile.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(pp.employeeProfile.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(pp.employeeProfile.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(pp.position.code) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(pp.position.name) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<PositionProfile> findByStringParameter(@Param("param") String parameter);

    @Query("SELECT pp FROM PositionProfile pp WHERE pp.currentPosition = :param")
    List<PositionProfile> findByBooleanParameter(@Param("param") boolean param);
}
