package io.softwaregarage.hris.admin.dtos;

import io.softwaregarage.hris.commons.BaseDTO;

import java.time.LocalDate;

public class CalendarHolidaysDTO extends BaseDTO {
    private String holidayDescription;
    private Integer holidayYear;
    private LocalDate holidayDate;
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
