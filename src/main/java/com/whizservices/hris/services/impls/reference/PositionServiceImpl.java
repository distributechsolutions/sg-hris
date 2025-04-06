package com.whizservices.hris.services.impls.reference;

import com.whizservices.hris.dtos.reference.PositionDTO;
import com.whizservices.hris.entities.reference.Position;
import com.whizservices.hris.repositories.reference.PositionRepository;
import com.whizservices.hris.services.reference.PositionService;
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
public class PositionServiceImpl implements PositionService {
    private final Logger logger = LoggerFactory.getLogger(PositionServiceImpl.class);
    private final PositionRepository positionRepository;

    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public void saveOrUpdate(PositionDTO object) {
        Position position;
        String logMessage;

        if (object.getId() != null) {
            position = positionRepository.getReferenceById(object.getId());
            logMessage = "Position record with id ".concat(object.getId().toString()).concat(" is successfully updated.");
        } else {
            position = new Position();
            position.setCreatedBy(object.getCreatedBy());
            position.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "Position record is successfully created.";
        }


        position.setCode(object.getCode());
        position.setName(object.getName());
        position.setUpdatedBy(object.getUpdatedBy());
        position.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        positionRepository.save(position);
        logger.info(logMessage);
    }

    @Override
    public PositionDTO getById(UUID id) {
        logger.info("Retrieving position record with UUID ".concat(id.toString()));

        Position position = positionRepository.getReferenceById(id);
        PositionDTO positionDTO = new PositionDTO();

        positionDTO.setId(position.getId());
        positionDTO.setCode(position.getCode());
        positionDTO.setName(position.getName());
        positionDTO.setCreatedBy(position.getCreatedBy());
        positionDTO.setDateAndTimeCreated(position.getDateAndTimeCreated());
        positionDTO.setUpdatedBy(position.getUpdatedBy());
        positionDTO.setDateAndTimeUpdated(position.getDateAndTimeUpdated());

        logger.info("Position record with id ".concat(id.toString()).concat(" is successfully retrieved."));

        return positionDTO;
    }

    @Override
    public void delete(PositionDTO object) {
        if (object != null) {
            logger.warn("You are about to delete a position record permanently.");

            String id = object.getId().toString();
            Position position = positionRepository.getReferenceById(object.getId());
            positionRepository.delete(position);

            logger.info("Position record with id ".concat(id).concat(" is successfully deleted."));
        }
    }

    @Override
    public List<PositionDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving position records from the database.");
        List<Position> positionList = positionRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Position records successfully retrieved.");
        List<PositionDTO> positionDTOList = new ArrayList<>();

        if (!positionList.isEmpty()) {
            PositionService positionService = new PositionServiceImpl(positionRepository);

            for (Position position : positionList) {
                PositionDTO positionDTO = new PositionDTO();

                positionDTO.setId(position.getId());
                positionDTO.setCode(position.getCode());
                positionDTO.setName(position.getName());
                positionDTO.setCreatedBy(position.getCreatedBy());
                positionDTO.setDateAndTimeCreated(position.getDateAndTimeCreated());
                positionDTO.setUpdatedBy(position.getUpdatedBy());
                positionDTO.setDateAndTimeUpdated(position.getDateAndTimeUpdated());

                positionDTOList.add(positionDTO);
            }

            logger.info(String.valueOf(positionList.size()).concat(" record(s) found."));
        }

        return positionDTOList;
    }

    @Override
    public List<PositionDTO> findByParameter(String param) {
        logger.info("Retrieving position records with search parameter '%".concat(param).concat("%' from the database."));

        List<PositionDTO> positionDTOList = new ArrayList<>();
        List<Position> positionList = positionRepository.findByStringParameter(param);

        if (!positionList.isEmpty()) {
            logger.info("Position records with parameter '%".concat(param).concat("%' has successfully retrieved."));

            for (Position position : positionList) {
                PositionDTO positionDTO = new PositionDTO();

                positionDTO.setId(position.getId());
                positionDTO.setCode(position.getCode());
                positionDTO.setName(position.getName());
                positionDTO.setCreatedBy(position.getCreatedBy());
                positionDTO.setDateAndTimeCreated(position.getDateAndTimeCreated());
                positionDTO.setUpdatedBy(position.getUpdatedBy());
                positionDTO.setDateAndTimeUpdated(position.getDateAndTimeUpdated());

                positionDTOList.add(positionDTO);
            }
        }

        return positionDTOList;
    }
}
