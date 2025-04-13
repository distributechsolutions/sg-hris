package io.distributechsolutions.hris.repositories.attendance;

import io.distributechsolutions.hris.entities.attendance.EmployeeShiftSchedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeShiftScheduleRepository extends JpaRepository<EmployeeShiftSchedule, UUID> {
}
