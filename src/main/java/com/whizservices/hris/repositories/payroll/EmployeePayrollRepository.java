package com.whizservices.hris.repositories.payroll;

import com.whizservices.hris.entities.payroll.EmployeePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, UUID> {
}
