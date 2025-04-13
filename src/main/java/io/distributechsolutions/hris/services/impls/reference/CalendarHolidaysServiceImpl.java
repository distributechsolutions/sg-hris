package io.distributechsolutions.hris.services.impls.reference;

import io.distributechsolutions.hris.dtos.reference.CalendarHolidaysDTO;
import io.distributechsolutions.hris.entities.reference.CalendarHolidays;
import io.distributechsolutions.hris.repositories.reference.CalendarHolidaysRepository;
import io.distributechsolutions.hris.services.reference.CalendarHolidaysService;

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
public class CalendarHolidaysServiceImpl implements CalendarHolidaysService {
    private final Logger logger = LoggerFactory.getLogger(CalendarHolidaysServiceImpl.class);
    private final CalendarHolidaysRepository calendarHolidaysRepository;

    public CalendarHolidaysServiceImpl(CalendarHolidaysRepository calendarHolidaysRepository) {
        this.calendarHolidaysRepository = calendarHolidaysRepository;
    }

    @Override
    public void saveOrUpdate(CalendarHolidaysDTO object) {
        logger.info("Getting the calendar holiday data transfer object.");
        logger.info("Preparing the calendar holiday object to be saved in the database.");

        CalendarHolidays calendarHolidays;
        String logMessage;

        if (object.getId() != null) {
            calendarHolidays = calendarHolidaysRepository.getReferenceById(object.getId());
            logMessage = "Calendar holiday record with ID ".concat(object.getId().toString()).concat(" has successfully updated.");
        } else {
            calendarHolidays = new CalendarHolidays();
            calendarHolidays.setCreatedBy(object.getCreatedBy());
            calendarHolidays.setDateAndTimeCreated(LocalDateTime.now(ZoneId.of("Asia/Manila")));
            logMessage = "A new calendar holiday has successfully saved in the database.";
        }

        calendarHolidays.setHolidayType(object.getHolidayType());
        calendarHolidays.setHolidayDescription(object.getHolidayDescription());
        calendarHolidays.setHolidayYear(object.getHolidayYear());
        calendarHolidays.setHolidayDate(object.getHolidayDate());
        calendarHolidays.setUpdatedBy(object.getUpdatedBy());
        calendarHolidays.setDateAndTimeUpdated(LocalDateTime.now(ZoneId.of("Asia/Manila")));

        calendarHolidaysRepository.save(calendarHolidays);
        logger.info(logMessage);
    }

    @Override
    public CalendarHolidaysDTO getById(UUID id) {
        logger.info("Getting calendar holiday record with ID ".concat(id.toString()).concat(" from the database."));
        CalendarHolidays calendarHolidays = calendarHolidaysRepository.getReferenceById(id);

        logger.info("Calendar holiday record with ID ".concat(id.toString()).concat(" has successfully retrieved."));
        CalendarHolidaysDTO calendarHolidaysDTO = new CalendarHolidaysDTO();
        calendarHolidaysDTO.setId(calendarHolidays.getId());
        calendarHolidaysDTO.setHolidayType(calendarHolidays.getHolidayType());
        calendarHolidaysDTO.setHolidayDescription(calendarHolidays.getHolidayDescription());
        calendarHolidaysDTO.setHolidayYear(calendarHolidays.getHolidayYear());
        calendarHolidaysDTO.setHolidayDate(calendarHolidays.getHolidayDate());
        calendarHolidaysDTO.setCreatedBy(calendarHolidays.getCreatedBy());
        calendarHolidaysDTO.setDateAndTimeCreated(calendarHolidays.getDateAndTimeCreated());
        calendarHolidaysDTO.setUpdatedBy(calendarHolidays.getUpdatedBy());
        calendarHolidaysDTO.setDateAndTimeUpdated(calendarHolidays.getDateAndTimeUpdated());

        logger.info("Calendar holiday data transfer object has successfully returned.");
        return calendarHolidaysDTO;
    }

    @Override
    public void delete(CalendarHolidaysDTO object) {
        logger.warn("You are about to delete a calendar holiday record. Doing this will permanently erase in the database.");

        CalendarHolidays calendarHolidays = calendarHolidaysRepository.getReferenceById(object.getId());
        calendarHolidaysRepository.delete(calendarHolidays);

        logger.info("Calendar holiday record with ID ".concat(object.getId().toString()).concat(" has successfully deleted in the database."));
    }

    @Override
    public List<CalendarHolidaysDTO> getAll(int page, int pageSize) {
        logger.info("Retrieving calendar holiday records from the database.");
        List<CalendarHolidays> calendarHolidaysList = calendarHolidaysRepository.findAll(PageRequest.of(page, pageSize)).stream().toList();

        logger.info("Calendar holiday records successfully retrieved.");
        List<CalendarHolidaysDTO> calendarHolidaysDTOList = new ArrayList<>();

        if (!calendarHolidaysList.isEmpty()) {
            for (CalendarHolidays calendarHolidays : calendarHolidaysList) {
                CalendarHolidaysDTO calendarHolidaysDTO = new CalendarHolidaysDTO();
                calendarHolidaysDTO.setId(calendarHolidays.getId());
                calendarHolidaysDTO.setHolidayType(calendarHolidays.getHolidayType());
                calendarHolidaysDTO.setHolidayDescription(calendarHolidays.getHolidayDescription());
                calendarHolidaysDTO.setHolidayYear(calendarHolidays.getHolidayYear());
                calendarHolidaysDTO.setHolidayDate(calendarHolidays.getHolidayDate());
                calendarHolidaysDTO.setCreatedBy(calendarHolidays.getCreatedBy());
                calendarHolidaysDTO.setDateAndTimeCreated(calendarHolidays.getDateAndTimeCreated());
                calendarHolidaysDTO.setUpdatedBy(calendarHolidays.getUpdatedBy());
                calendarHolidaysDTO.setDateAndTimeUpdated(calendarHolidays.getDateAndTimeUpdated());

                calendarHolidaysDTOList.add(calendarHolidaysDTO);
            }

            logger.info(String.valueOf(calendarHolidaysList.size()).concat(" record(s) found."));
        }

        return calendarHolidaysDTOList;
    }

    @Override
    public List<CalendarHolidaysDTO> findByParameter(String param) {
        logger.info("Retrieving calendar holiday records from the database.");
        List<CalendarHolidays> calendarHolidaysList = calendarHolidaysRepository.findByStringParameter(param);

        logger.info("Calendar holiday records successfully retrieved.");
        List<CalendarHolidaysDTO> calendarHolidaysDTOList = new ArrayList<>();

        if (!calendarHolidaysList.isEmpty()) {
            for (CalendarHolidays calendarHolidays : calendarHolidaysList) {
                CalendarHolidaysDTO calendarHolidaysDTO = new CalendarHolidaysDTO();
                calendarHolidaysDTO.setId(calendarHolidays.getId());
                calendarHolidaysDTO.setHolidayType(calendarHolidays.getHolidayType());
                calendarHolidaysDTO.setHolidayDescription(calendarHolidays.getHolidayDescription());
                calendarHolidaysDTO.setHolidayYear(calendarHolidays.getHolidayYear());
                calendarHolidaysDTO.setHolidayDate(calendarHolidays.getHolidayDate());
                calendarHolidaysDTO.setCreatedBy(calendarHolidays.getCreatedBy());
                calendarHolidaysDTO.setDateAndTimeCreated(calendarHolidays.getDateAndTimeCreated());
                calendarHolidaysDTO.setUpdatedBy(calendarHolidays.getUpdatedBy());
                calendarHolidaysDTO.setDateAndTimeUpdated(calendarHolidays.getDateAndTimeUpdated());

                calendarHolidaysDTOList.add(calendarHolidaysDTO);
            }

            logger.info(String.valueOf(calendarHolidaysList.size()).concat(" record(s) found."));
        }

        return calendarHolidaysDTOList;
    }
}
