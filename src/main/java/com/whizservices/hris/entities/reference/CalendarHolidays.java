package com.whizservices.hris.entities.reference;

import com.whizservices.hris.entities.BaseEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "wsi_calendar_holidays")
public class CalendarHolidays extends BaseEntity {

    @Column(name = "holiday_description", length = 150, nullable = false)
    private String holidayDescription;

    @Column(name = "holiday_year", nullable = false)
    private Integer holidayYear;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "holiday_type", length = 150, nullable = false)
    private String holidayType;

    public String getHolidayDescription() {
        return holidayDescription;
    }

    public void setHolidayDescription(String holidayDescription) {
        this.holidayDescription = holidayDescription;
    }

    public Integer getHolidayYear() {
        return holidayYear;
    }

    public void setHolidayYear(Integer holidayYear) {
        this.holidayYear = holidayYear;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }
}
