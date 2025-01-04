package com.whizservices.hris.repositories.info;

import com.whizservices.hris.entities.info.AddressInfo;
import com.whizservices.hris.entities.profile.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AddressInfoRepository extends JpaRepository<AddressInfo, UUID> {
    @Query("SELECT ai FROM AddressInfo ai WHERE ai.employee = :param")
    List<AddressInfo> findByEmployee(@Param("param") Employee employee);
}
