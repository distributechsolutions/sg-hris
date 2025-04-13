package io.distributechsolutions.hris.repositories.reference;

import io.distributechsolutions.hris.entities.reference.CalendarHolidays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CalendarHolidaysRepository extends JpaRepository<CalendarHolidays, UUID> {
    @Query("""
           SELECT ch FROM CalendarHolidays ch WHERE
           LOWER(ch.holidayType) LIKE LOWER(CONCAT('%', :param, '%')) OR
           LOWER(ch.holidayDescription) LIKE LOWER(CONCAT('%', :param, '%')) OR
           CAST(ch.holidayYear AS STRING) LIKE LOWER(CONCAT('%', :param, '%')) 
           """)
    List<CalendarHolidays> findByStringParameter(@Param("param") String parameter);
}
