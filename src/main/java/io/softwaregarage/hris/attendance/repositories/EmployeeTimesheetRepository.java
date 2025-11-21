package io.softwaregarage.hris.attendance.repositories;

import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import io.softwaregarage.hris.attendance.entities.EmployeeTimesheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EmployeeTimesheetRepository extends JpaRepository<EmployeeTimesheet, UUID> {
    @Query(value = """
                   SELECT et.*
                   FROM sg_hris_employee_timesheet et
                   JOIN sg_hris_employee_profile ep ON et.employee_id = ep.id
                   WHERE UNACCENT(LOWER(ep.first_name)) LIKE LOWER(CONCAT('%', :param, '%'))
                   OR UNACCENT(LOWER(ep.middle_name)) LIKE LOWER(CONCAT('%', :param, '%'))
                   OR UNACCENT(LOWER(ep.last_name)) LIKE LOWER(CONCAT('%', :param, '%'))
                   OR UNACCENT(LOWER(et.shift_schedule)) LIKE LOWER(CONCAT('%', :param, '%'))
                   OR UNACCENT(LOWER(et.log_detail)) LIKE LOWER(CONCAT('%', :param, '%'))
                   OR UNACCENT(LOWER(et.status)) LIKE LOWER(CONCAT('%', :param, '%'))
                   OR TO_CHAR(et.log_date, 'YYYY-MM-DD') LIKE CONCAT('%', :param, '%')
                   OR TO_CHAR(et.log_time, 'HH24:MI:SS') LIKE CONCAT('%', :param, '%')
                   """,
           nativeQuery = true)
    List<EmployeeTimesheet> findTimesheetByStringParameter(@Param("param") String param);

    @Query("SELECT et FROM EmployeeTimesheet et WHERE et.employeeProfile = :employeeParam")
    List<EmployeeTimesheet> findTimesheetByEmployee(@Param("employeeParam") EmployeeProfile employeeProfile);

    @Query("""
           SELECT et
           FROM EmployeeTimesheet et
           WHERE et.employeeProfile = :employeeParam
             AND et.logDate BETWEEN :startDate AND :endDate
           ORDER BY et.logDate ASC
           """)
    List<EmployeeTimesheet> findTimesheetByEmployeeAndLogDateRange(@Param("employeeParam") EmployeeProfile employeeProfile,
                                                                   @Param("startDate") LocalDate startDate,
                                                                   @Param("endDate") LocalDate endDate);

    @Query("""
           SELECT et
           FROM EmployeeTimesheet et
           WHERE et.logDate BETWEEN :startDate AND :endDate
           ORDER BY et.employeeProfile.lastName ASC, et.logDate ASC
           """)
    List<EmployeeTimesheet> findTimesheetByLogDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
