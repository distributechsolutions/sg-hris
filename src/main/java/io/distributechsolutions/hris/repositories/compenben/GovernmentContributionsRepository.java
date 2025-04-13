package io.distributechsolutions.hris.repositories.compenben;

import io.distributechsolutions.hris.entities.compenben.GovernmentContributions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GovernmentContributionsRepository extends JpaRepository<GovernmentContributions, UUID> {
    @Query("""
           SELECT gc FROM GovernmentContributions gc
           WHERE LOWER(gc.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(gc.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(gc.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<GovernmentContributions> findByStringParameter(@Param("param") String param);
}
