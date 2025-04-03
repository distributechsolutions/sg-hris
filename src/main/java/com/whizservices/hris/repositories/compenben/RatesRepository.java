package com.whizservices.hris.repositories.compenben;

import com.whizservices.hris.entities.compenben.Rates;

import com.whizservices.hris.entities.profile.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RatesRepository extends JpaRepository<Rates, UUID> {
    @Query("""
           SELECT r FROM Rates r
           WHERE LOWER(r.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(r.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(r.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(r.rateType) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<Rates> findByStringParameter(@Param("param") String param);

    @Query("SELECT r FROM Rates r WHERE r.employee = :employee")
    Rates findByEmployee(@Param("employee") Employee employee);
}
