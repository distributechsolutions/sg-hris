package com.whizservices.hris.repositories.reference;

import com.whizservices.hris.entities.reference.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    @Query("""
           SELECT d FROM Department d
           WHERE LOWER(d.code) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(d.name) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<Department> findByStringParameter(@Param("param") String parameter);
}
