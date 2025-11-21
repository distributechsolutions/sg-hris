package io.softwaregarage.hris.attendance.repositories;

import io.softwaregarage.hris.attendance.entities.EmployeeLeaveFiling;
import io.softwaregarage.hris.profile.entities.EmployeeProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeLeaveFilingRepository extends JpaRepository<EmployeeLeaveFiling, UUID> {
    @Query("SELECT elf FROM EmployeeLeaveFiling elf WHERE elf.leaveBenefits.employeeProfile = :param ORDER BY elf.leaveDateAndTimeFrom DESC")
    List<EmployeeLeaveFiling> findByEmployee(@Param("param") EmployeeProfile employeeProfile);

    @Query("""
           SELECT elf FROM EmployeeLeaveFiling elf
           WHERE elf.leaveStatus = :leaveStatusParam
           AND elf.assignedApproverEmployeeProfile = :employeeParam
           ORDER BY elf.leaveDateAndTimeFrom ASC
           """)
    List<EmployeeLeaveFiling> findByStatusAndAssignedApproverEmployee(@Param("leaveStatusParam") String leaveStatus, @Param("employeeParam") EmployeeProfile employeeProfile);

    @Query("""
           SELECT elf FROM EmployeeLeaveFiling elf
           WHERE LOWER(elf.leaveBenefits.employeeProfile.firstName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveBenefits.employeeProfile.lastName) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveBenefits.leaveCode) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveBenefits.leaveType) LIKE LOWER(CONCAT('%', :param, '%'))
           OR LOWER(elf.leaveStatus) LIKE LOWER(CONCAT('%', :param, '%'))
           """)
    List<EmployeeLeaveFiling> findByStringParameter(@Param("param") String parameter);
}
