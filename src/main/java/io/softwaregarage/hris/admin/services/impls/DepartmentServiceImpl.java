package io.softwaregarage.hris.admin.services.impls;

import io.softwaregarage.hris.admin.dtos.DepartmentDTO;
import io.softwaregarage.hris.admin.dtos.GroupDTO;
import io.softwaregarage.hris.admin.entities.Department;
import io.softwaregarage.hris.admin.repositories.DepartmentRepository;
import io.softwaregarage.hris.admin.repositories.GroupRepository;
import io.softwaregarage.hris.admin.services.DepartmentService;
import io.softwaregarage.hris.admin.services.GroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 GroupRepository groupRepository,
                                 GroupService groupService) {
        this.departmentRepository = departmentRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    @Override
    public void saveOrUpdate(DepartmentDTO object) {
        Department department;
        String logMessage;

        if (object.getId() != null) {
            department = departmentRepository.getReferenceById(object.getId());
            logMessage = "Department record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            department = new Department();
            department.setCreatedBy(object.getCreatedBy());
            department.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Department record is successfully created.";
        }

        department.setCode(object.getCode());
        department.setName(object.getName());
        department.setGroup(groupRepository.getReferenceById(object.getGroupDTO().getId()));
        department.setUpdatedBy(object.getUpdatedBy());
        department.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        departmentRepository.save(department);
        logger.info(logMessage);
    }

    @Override
    public DepartmentDTO getById(UUID id) {
        logger.info("Retrieving department record with UUID ".concat(id.toString()));

        Department department = departmentRepository.getReferenceById(id);
        DepartmentDTO departmentDTO = new DepartmentDTO();

        departmentDTO.setId(department.getId());
        departmentDTO.setCode(department.getCode());
        departmentDTO.setName(department.getName());
        departmentDTO.setGroupDTO(groupService.getById(department.getGroup().getId()));
        departmentDTO.setCreatedBy(department.getCreatedBy());
        departmentDTO.setDateAndTimeCreated(department.getDateAndTimeCreated());
        departmentDTO.setUpdatedBy(department.getUpdatedBy());
        departmentDTO.setDateAndTimeUpdated(department.getDateAndTimeUpdated());

        logger.info("Department record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return departmentDTO;
    }

    @Override
    public void delete(DepartmentDTO object) {
        if (object != null) {
            logger.warn("You are about to delete a department record permanently.");

            String id = object.getId().toString();
            Department department = departmentRepository.getReferenceById(object.getId());
            departmentRepository.delete(department);

            logger.info("Department record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<DepartmentDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving department records from the database.");
        List<Department> departmentList = departmentRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Department records successfully retrieved.");
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();

        if (!departmentList.isEmpty()) {
            for (Department department : departmentList) {
                DepartmentDTO departmentDTO = new DepartmentDTO();

                departmentDTO.setId(department.getId());
                departmentDTO.setCode(department.getCode());
                departmentDTO.setName(department.getName());
                departmentDTO.setGroupDTO(groupService.getById(department.getGroup().getId()));
                departmentDTO.setCreatedBy(department.getCreatedBy());
                departmentDTO.setDateAndTimeCreated(department.getDateAndTimeCreated());
                departmentDTO.setUpdatedBy(department.getUpdatedBy());
                departmentDTO.setDateAndTimeUpdated(department.getDateAndTimeUpdated());

                departmentDTOList.add(departmentDTO);
            }

            logger.info(String.valueOf(departmentList.size()).concat(" record(s) found."));
        }

        return departmentDTOList;
    }

    @Override
    public List<DepartmentDTO> findByParameter(String param) {
        logger.info("Retrieving department records with search parameter '%".concat(param).concat("%' from the database."));

        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        List<Department> departmentList = departmentRepository.findByStringParameter(param);

        if (!departmentList.isEmpty()) {
            logger.info("Department records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            for (Department department : departmentList) {
                DepartmentDTO departmentDTO = new DepartmentDTO();

                departmentDTO.setId(department.getId());
                departmentDTO.setCode(department.getCode());
                departmentDTO.setName(department.getName());
                departmentDTO.setGroupDTO(groupService.getById(department.getGroup().getId()));
                departmentDTO.setCreatedBy(department.getCreatedBy());
                departmentDTO.setDateAndTimeCreated(department.getDateAndTimeCreated());
                departmentDTO.setUpdatedBy(department.getUpdatedBy());
                departmentDTO.setDateAndTimeUpdated(department.getDateAndTimeUpdated());

                departmentDTOList.add(departmentDTO);
            }
        }

        return departmentDTOList;
    }

    @Override
    public List<DepartmentDTO> getDepartmentsByGroup(GroupDTO groupDTO) {
        logger.info("Retrieving department records from group '".concat(groupDTO.getName()).concat("' in the database."));

        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        List<Department> departmentList = departmentRepository.findByGroup(groupRepository.getReferenceById(groupDTO.getId()));

        if (!departmentList.isEmpty()) {
            logger.info("Department records from group '".concat(groupDTO.getName()).concat("' has successfully retrieved."));

            for (Department department : departmentList) {
                DepartmentDTO departmentDTO = new DepartmentDTO();

                departmentDTO.setId(department.getId());
                departmentDTO.setCode(department.getCode());
                departmentDTO.setName(department.getName());
                departmentDTO.setGroupDTO(groupService.getById(department.getGroup().getId()));
                departmentDTO.setCreatedBy(department.getCreatedBy());
                departmentDTO.setDateAndTimeCreated(department.getDateAndTimeCreated());
                departmentDTO.setUpdatedBy(department.getUpdatedBy());
                departmentDTO.setDateAndTimeUpdated(department.getDateAndTimeUpdated());

                departmentDTOList.add(departmentDTO);
            }
        }

        return departmentDTOList;
    }
}
