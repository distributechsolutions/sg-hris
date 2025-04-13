package io.distributechsolutions.hris.repositories.compenben;

import io.distributechsolutions.hris.entities.compenben.LoanDeduction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoanDeductionRepository extends JpaRepository<LoanDeduction, UUID> {
    @Query("""
           SELECT ld FROM LoanDeduction ld
           WHERE LOWER(ld.loanType) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ld.loanDescription) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ld.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ld.employee.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ld.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<LoanDeduction> findByStringParameter(@Param("param") String param);
}
