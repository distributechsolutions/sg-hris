package io.distributechsolutions.hris.repositories.profile;

import io.distributechsolutions.hris.entities.profile.EmployeeDepartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeDepartmentRepository extends JpaRepository<EmployeeDepartment, UUID> {
    @Query("""
           SELECT ed FROM EmployeeDepartment ed
           WHERE LOWER(ed.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ed.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ed.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ed.department.code) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ed.department.name) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<EmployeeDepartment> findByStringParameter(@Param("param") String parameter);

    @Query("SELECT ed FROM EmployeeDepartment ed WHERE ed.currentDepartment = :param")
    List<EmployeeDepartment> findByBooleanParameter(@Param("param") boolean param);
}
