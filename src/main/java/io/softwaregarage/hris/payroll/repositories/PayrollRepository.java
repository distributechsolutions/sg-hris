package io.softwaregarage.hris.payroll.repositories;

import io.softwaregarage.hris.payroll.entities.Payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PayrollRepository extends JpaRepository<Payroll, UUID> {
}
