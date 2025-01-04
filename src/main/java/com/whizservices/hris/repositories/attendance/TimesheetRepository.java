package com.whizservices.hris.repositories.attendance;

import com.whizservices.hris.entities.profile.Employee;
import com.whizservices.hris.entities.attendance.Timesheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TimesheetRepository extends JpaRepository<Timesheet, UUID> {
    @Query("""
           SELECT t FROM Timesheet t
           WHERE LOWER(t.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(t.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(t.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(t.shiftSchedule) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(t.status) like LOWER(CONCAT('%', :param, '%'))
           OR LOWER(TO_CHAR(t.logDate, 'YYYY-MM-DD')) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(TO_CHAR(t.logTimeIn, 'YYYY-MM-DD')) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(TO_CHAR(t.logTimeOut, 'YYYY-MM-DD')) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<Timesheet> findTimesheetByStringParameter(@Param("param") String param);

    @Query("SELECT t FROM Timesheet t WHERE t.employee = :employeeParam")
    List<Timesheet> findTimesheetByEmployee(@Param("employeeParam") Employee employee);
}
