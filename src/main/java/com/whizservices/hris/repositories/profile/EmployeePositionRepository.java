package com.whizservices.hris.repositories.profile;

import com.whizservices.hris.entities.profile.EmployeePosition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, UUID> {
    @Query("""
           SELECT ep FROM EmployeePosition ep
           WHERE LOWER(ep.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ep.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ep.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ep.position.code) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ep.position.name) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<EmployeePosition> findByStringParameter(@Param("param") String parameter);

    @Query("SELECT ep FROM EmployeePosition ep WHERE ep.currentPosition = :param")
    List<EmployeePosition> findByBooleanParameter(@Param("param") boolean param);
}
