package io.distributechsolutions.hris.repositories.compenben;

import io.distributechsolutions.hris.entities.compenben.Allowance;
import io.distributechsolutions.hris.entities.profile.Employee;
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

    @Query("SELECT SUM(a.allowanceAmount) FROM Allowance a WHERE a.employee = :param")
    public Object findSumOfAllowanceByEmployee(@Param("param") Employee employee);
}
