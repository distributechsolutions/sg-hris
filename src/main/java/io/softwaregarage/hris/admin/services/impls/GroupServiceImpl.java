package io.softwaregarage.hris.admin.services.impls;

import io.softwaregarage.hris.admin.dtos.GroupDTO;
import io.softwaregarage.hris.admin.entities.Group;
import io.softwaregarage.hris.admin.repositories.GroupRepository;
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
public class GroupServiceImpl implements GroupService {
    private final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public void saveOrUpdate(GroupDTO object) {
        Group group;
        String logMessage;

        if (object.getId() != null) {
            group = groupRepository.getReferenceById(object.getId());
            logMessage = "Group record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            group = new Group();
            group.setCreatedBy(object.getCreatedBy());
            group.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Group record is successfully created.";
        }

        group.setCode(object.getCode());
        group.setName(object.getName());
        group.setDescription(object.getDescription());
        group.setUpdatedBy(object.getUpdatedBy());
        group.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        groupRepository.save(group);
        logger.info(logMessage);
    }

    @Override
    public GroupDTO getById(UUID id) {
        logger.info("Retrieving group record with UUID ".concat(id.toString()));

        Group group = groupRepository.getReferenceById(id);
        GroupDTO groupDTO = new GroupDTO();

        groupDTO.setId(group.getId());
        groupDTO.setCode(group.getCode());
        groupDTO.setName(group.getName());
        groupDTO.setDescription(group.getDescription());
        groupDTO.setCreatedBy(group.getCreatedBy());
        groupDTO.setDateAndTimeCreated(group.getDateAndTimeCreated());
        groupDTO.setUpdatedBy(group.getUpdatedBy());
        groupDTO.setDateAndTimeUpdated(group.getDateAndTimeUpdated());

        logger.info("Group record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return groupDTO;
    }

    @Override
    public void delete(GroupDTO object) {
        if (object != null) {
            logger.warn("You are about to delete a group record permanently.");

            String id = object.getId().toString();
            Group group = groupRepository.getReferenceById(object.getId());
            groupRepository.delete(group);

            logger.info("Group record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<GroupDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving group records from the database.");
        List<Group> groupList = groupRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Group records successfully retrieved.");
        List<GroupDTO> groupDTOList = new ArrayList<>();

        if (!groupList.isEmpty()) {
            for (Group group : groupList) {
                GroupDTO groupDTO = new GroupDTO();

                groupDTO.setId(group.getId());
                groupDTO.setCode(group.getCode());
                groupDTO.setName(group.getName());
                groupDTO.setDescription(group.getDescription());
                groupDTO.setCreatedBy(group.getCreatedBy());
                groupDTO.setDateAndTimeCreated(group.getDateAndTimeCreated());
                groupDTO.setUpdatedBy(group.getUpdatedBy());
                groupDTO.setDateAndTimeUpdated(group.getDateAndTimeUpdated());

                groupDTOList.add(groupDTO);
            }

            logger.info(String.valueOf(groupList.size()).concat(" record(s) found."));
        }

        return groupDTOList;
    }

    @Override
    public List<GroupDTO> findByParameter(String param) {
        return List.of();
    }
}
