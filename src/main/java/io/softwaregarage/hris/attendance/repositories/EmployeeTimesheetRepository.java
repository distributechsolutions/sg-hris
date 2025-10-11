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
           WHERE unaccent(lower(ep.first_name)) LIKE lower(concat('%', :param, '%'))
              OR unaccent(lower(ep.middle_name)) LIKE lower(concat('%', :param, '%'))
              OR unaccent(lower(ep.last_name)) LIKE lower(concat('%', :param, '%'))
              OR unaccent(lower(et.shift_schedule)) LIKE lower(concat('%', :param, '%'))
              OR unaccent(lower(et.log_detail)) LIKE lower(concat('%', :param, '%'))
              OR unaccent(lower(et.status)) LIKE lower(concat('%', :param, '%'))
              OR to_char(et.log_date, 'YYYY-MM-DD') LIKE concat('%', :param, '%')
              OR to_char(et.log_time, 'HH24:MI:SS') LIKE concat('%', :param, '%')
           """, nativeQuery = true)
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
