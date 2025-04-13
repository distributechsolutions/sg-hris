package io.distributechsolutions.hris.repositories.info;

import io.distributechsolutions.hris.entities.info.DependentInfo;
import io.distributechsolutions.hris.entities.profile.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DependentInfoRepository extends JpaRepository<DependentInfo, UUID> {
    @Query("SELECT di FROM DependentInfo di WHERE di.employee = :param")
    List<DependentInfo> findByEmployee(@Param("param") Employee employee);
}
