package com.whizservices.hris.repositories.attendance;

import com.whizservices.hris.entities.attendance.EmployeeShiftSchedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeShiftScheduleRepository extends JpaRepository<EmployeeShiftSchedule, UUID> {
}
