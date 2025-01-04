package com.whizservices.hris.repositories.profile;

import com.whizservices.hris.entities.profile.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("SELECT e FROM Employee e WHERE e.biometricsNumber = :param")
    Employee findByBiometricsNumber(@Param("param") String biometricsNumber);

    @Query("""
           SELECT e FROM Employee e WHERE
           LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.biometricsNumber) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.lastName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.firstName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.middleName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.gender) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<Employee> findEmployeesByParameter(@Param("param") String param);

    @Query(value = """
            SELECT we.* FROM wsi_employee we
            RIGHT JOIN wsi_user_account wua
            ON we.id = wua.employee_id
            WHERE wua.role IN ('ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_HR_SUPERVISOR', 'ROLE_SUPERVISOR')
            """, nativeQuery = true)
    List<Employee> findEmployeesWhoAreApprovers();
}
