package io.softwaregarage.hris.attendance.repositories;

import io.softwaregarage.hris.attendance.entities.EmployeeShiftSchedule;

import io.softwaregarage.hris.profile.entities.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeShiftScheduleRepository extends JpaRepository<EmployeeShiftSchedule, UUID> {
    @Query("""
           SELECT ess 
           FROM EmployeeShiftSchedule ess
           WHERE ess.employeeProfile = :param
           ORDER BY ess.dateAndTimeUpdated ASC
           """)
    List<EmployeeShiftSchedule> findByEmployee(@Param("param") EmployeeProfile employeeProfile);
}
