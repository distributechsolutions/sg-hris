package com.whizservices.hris.repositories.profile;

import com.whizservices.hris.entities.profile.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("""
           SELECT e FROM Employee e WHERE
           LOWER(e.employeeNumber) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.lastName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.firstName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.middleName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(e.gender) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<Employee> findEmployeesByParameter(@Param("param") String param);

    @Query(value = """
            SELECT she.* FROM sg_hris_employee she
            RIGHT JOIN sg_hris_user_account sgua
            ON she.id = sgua.employee_id
            WHERE sgua.role IN ('ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_HR_SUPERVISOR', 'ROLE_SUPERVISOR')
            """, nativeQuery = true)
    List<Employee> findEmployeesWhoAreApprovers();
}
