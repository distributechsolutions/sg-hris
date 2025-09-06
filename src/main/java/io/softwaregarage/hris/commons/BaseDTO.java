 package io.softwaregarage.hris.commons;

import java.time.LocalDateTime;
import java.util.UUID;

public class BaseDTO {
    private UUID id;
    private String createdBy;
    private LocalDateTime dateAndTimeCreated;
    private String updatedBy;
    private LocalDateTime dateAndTimeUpdated;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getDateAndTimeCreated() {
        return dateAndTimeCreated;
    }

    public void setDateAndTimeCreated(LocalDateTime dateAndTimeCreated) {
        this.dateAndTimeCreated = dateAndTimeCreated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getDateAndTimeUpdated() {
        return dateAndTimeUpdated;
    }

    public void setDateAndTimeUpdated(LocalDateTime dateAndTimeUpdated) {
        this.dateAndTimeUpdated = dateAndTimeUpdated;
    }
}
