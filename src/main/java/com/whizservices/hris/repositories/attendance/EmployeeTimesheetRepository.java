package com.whizservices.hris.repositories.attendance;

import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.entities.attendance.EmployeeTimesheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EmployeeTimesheetRepository extends JpaRepository<EmployeeTimesheet, UUID> {
    @Query("""
           SELECT et
           FROM EmployeeTimesheet et
           WHERE LOWER(et.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
              OR LOWER(et.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
              OR LOWER(et.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
              OR LOWER(et.shiftSchedule) LIKE LOWER(CONCAT('%', :param, '%'))
              OR LOWER(et.logDetail) LIKE LOWER(CONCAT('%', :param, '%'))
              OR LOWER(et.status) LIKE LOWER(CONCAT('%', :param, '%'))
              OR FUNCTION('TO_CHAR', et.logDate, 'YYYY-MM-DD') LIKE CONCAT('%', :param, '%')
              OR FUNCTION('TO_CHAR', et.logTime, 'hh:mm:ss a') LIKE CONCAT('%', :param, '%')
           """)
    List<EmployeeTimesheet> findTimesheetByStringParameter(@Param("param") String param);

    @Query("SELECT et FROM EmployeeTimesheet et WHERE et.employee = :employeeParam")
    List<EmployeeTimesheet> findTimesheetByEmployee(@Param("employeeParam") Employee employee);

    @Query("""
           SELECT et
           FROM EmployeeTimesheet et
           WHERE et.status = 'APPROVED'
             AND et.logDate BETWEEN :startDate AND :endDate
           ORDER BY et.employee.lastName ASC, et.logDate ASC
           """)
    List<EmployeeTimesheet> findTimesheetByLogDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
