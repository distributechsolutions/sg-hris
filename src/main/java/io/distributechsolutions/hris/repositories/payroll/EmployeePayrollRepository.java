package io.distributechsolutions.hris.repositories.payroll;

import io.distributechsolutions.hris.entities.payroll.EmployeePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, UUID> {
}
