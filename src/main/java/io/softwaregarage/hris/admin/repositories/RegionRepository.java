package io.softwaregarage.hris.admin.repositories;

import io.softwaregarage.hris.admin.entities.Region;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
