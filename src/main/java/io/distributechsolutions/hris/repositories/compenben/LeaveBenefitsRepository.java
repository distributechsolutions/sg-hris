package io.distributechsolutions.hris.repositories.compenben;

import io.distributechsolutions.hris.entities.compenben.LeaveBenefits;
import io.distributechsolutions.hris.entities.profile.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LeaveBenefitsRepository extends JpaRepository<LeaveBenefits, UUID> {
    @Query("""
           SELECT lb FROM LeaveBenefits lb
           WHERE LOWER(lb.leaveCode) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(lb.leaveType) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(lb.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(lb.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<LeaveBenefits> findByStringParameter(@Param("param") String param);

    @Query("""
           SELECT lb FROM LeaveBenefits lb
           WHERE lb.leaveForYear = :param
           OR lb.leaveCount = :param
           """)
    List<LeaveBenefits> findByIntegerParameter(@Param("param") Integer param);

    @Query("SELECT lb FROM LeaveBenefits lb WHERE lb.leaveActive = :param")
    List<LeaveBenefits> findByBooleanParameter(@Param("param") boolean param);

    @Query("SELECT lb FROM LeaveBenefits lb WHERE lb.employee = :param AND lb.leaveActive = true")
    List<LeaveBenefits> findByEmployee(@Param("param") Employee employee);
}
