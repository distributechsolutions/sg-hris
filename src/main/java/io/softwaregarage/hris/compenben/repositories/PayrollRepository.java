package io.softwaregarage.hris.compenben.repositories;

import io.softwaregarage.hris.compenben.entities.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PayrollRepository extends JpaRepository<Payroll, UUID> {
}
