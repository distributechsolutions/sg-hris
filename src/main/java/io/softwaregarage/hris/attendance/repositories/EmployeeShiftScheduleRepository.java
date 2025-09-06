package io.softwaregarage.hris.attendance.repositories;

import io.softwaregarage.hris.attendance.entities.EmployeeShiftSchedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeShiftScheduleRepository extends JpaRepository<EmployeeShiftSchedule, UUID> {
}
