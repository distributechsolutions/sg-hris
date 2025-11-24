package io.softwaregarage.hris.payroll.repositories;

import io.softwaregarage.hris.payroll.entities.TaxExemptions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaxExemptionsRepository extends JpaRepository<TaxExemptions, UUID> {
}
