package io.softwaregarage.hris.profile.repositories;

import io.softwaregarage.hris.profile.entities.EmployeeProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, UUID> {

    @Query("""
           SELECT ep FROM EmployeeProfile ep WHERE
           LOWER(ep.employeeNumber) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(ep.lastName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(ep.firstName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(ep.middleName) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(ep.gender) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<EmployeeProfile> findEmployeesByParameter(@Param("param") String param);

    @Query(value = """
            SELECT shep.* FROM sg_hris_employee_profile shep
            RIGHT JOIN sg_hris_user_account sgua
            ON shep.id = sgua.employee_id
            WHERE sgua.role IN ('ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_HR_SUPERVISOR', 'ROLE_SUPERVISOR')
            """, nativeQuery = true)
    List<EmployeeProfile> findEmployeesWhoAreApprovers();
}
