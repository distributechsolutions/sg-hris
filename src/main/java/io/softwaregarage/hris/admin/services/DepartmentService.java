package io.softwaregarage.hris.admin.services;

import io.softwaregarage.hris.admin.dtos.DepartmentDTO;
import io.softwaregarage.hris.admin.dtos.GroupDTO;
import io.softwaregarage.hris.commons.BaseService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DepartmentService extends BaseService<DepartmentDTO> {
    @Transactional
    List<DepartmentDTO> getDepartmentsByGroup(GroupDTO groupDTO);
}
