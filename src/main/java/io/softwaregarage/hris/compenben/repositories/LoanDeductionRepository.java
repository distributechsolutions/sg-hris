package io.softwaregarage.hris.compenben.repositories;

import io.softwaregarage.hris.compenben.entities.LoanDeduction;

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
           OR LOWER(ld.employeeProfile.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ld.employeeProfile.middleName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(ld.employeeProfile.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<LoanDeduction> findByStringParameter(@Param("param") String param);
}
