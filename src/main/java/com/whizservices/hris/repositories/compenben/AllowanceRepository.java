package com.whizservices.hris.repositories.compenben;

import com.whizservices.hris.entities.compenben.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AllowanceRepository extends JpaRepository<Allowance, UUID> {
    @Query("""
           SELECT a FROM Allowance a
           WHERE LOWER(a.allowanceCode) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(a.allowanceType) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(a.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(a.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(a.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    public List<Allowance> findByStringParameter(@Param("param") String parameter);
}
