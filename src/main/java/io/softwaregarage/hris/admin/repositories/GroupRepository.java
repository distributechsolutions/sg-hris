package io.softwaregarage.hris.admin.repositories;

import io.softwaregarage.hris.admin.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
}
