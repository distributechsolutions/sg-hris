package com.whizservices.hris.repositories.reference;

import com.whizservices.hris.entities.reference.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PositionRepository extends JpaRepository<Position, UUID> {
    @Query("""
           SELECT p FROM Position p
           WHERE LOWER(p.code) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(p.name) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<Position> findByStringParameter(@Param("param") String parameter);
}
