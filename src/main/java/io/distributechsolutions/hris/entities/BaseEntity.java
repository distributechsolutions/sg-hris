package io.distributechsolutions.hris.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_by", length = 25, nullable = false)
    private String createdBy;

    @Column(name = "date_and_time_created", nullable = false)
    private LocalDateTime dateAndTimeCreated;

    @Column(name = "updated_by", length = 25, nullable = false)
    private String updatedBy;

    @Column(name = "date_and_time_updated", nullable = false)
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
