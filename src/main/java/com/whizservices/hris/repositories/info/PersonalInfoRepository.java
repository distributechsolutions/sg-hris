package com.whizservices.hris.repositories.info;

import com.whizservices.hris.entities.info.PersonalInfo;
import com.whizservices.hris.entities.profile.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, UUID> {
    @Query("SELECT pi FROM PersonalInfo pi WHERE pi.employee = :param")
    PersonalInfo findByEmployee(@Param("param") Employee employee);
}
