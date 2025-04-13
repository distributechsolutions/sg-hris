package io.distributechsolutions.hris.repositories.attendance;

import io.distributechsolutions.hris.entities.attendance.EmployeeLeaveFiling;
import io.distributechsolutions.hris.entities.profile.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeLeaveFilingRepository extends JpaRepository<EmployeeLeaveFiling, UUID> {
    @Query("SELECT elf FROM EmployeeLeaveFiling elf WHERE elf.leaveBenefits.employee = :param ORDER BY elf.leaveDateAndTimeFrom DESC")
    List<EmployeeLeaveFiling> findByEmployee(@Param("param") Employee employee);

    @Query("""
           SELECT elf FROM EmployeeLeaveFiling elf 
           WHERE elf.leaveStatus = :leaveStatusParam
           AND elf.assignedApproverEmployee = :employeeParam 
           ORDER BY elf.leaveDateAndTimeFrom ASC
           """)
    List<EmployeeLeaveFiling> findByStatusAndAssignedApproverEmployee(@Param("leaveStatusParam") String leaveStatus, @Param("employeeParam") Employee employee);

    @Query("""
           SELECT elf FROM EmployeeLeaveFiling elf
           WHERE LOWER(elf.leaveBenefits.employee.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveBenefits.employee.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveBenefits.leaveCode) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveBenefits.leaveType) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveStatus) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<EmployeeLeaveFiling> findByStringParameter(@Param("param") String parameter);
}
