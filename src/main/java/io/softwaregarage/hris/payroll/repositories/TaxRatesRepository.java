package io.softwaregarage.hris.payroll.repositories;

import io.softwaregarage.hris.payroll.entities.TaxRates;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaxRatesRepository extends JpaRepository<TaxRates, UUID> {
}
