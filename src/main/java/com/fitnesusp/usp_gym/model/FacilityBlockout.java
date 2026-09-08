package com.fitnesusp.usp_gym.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "facility_blockouts")
public class FacilityBlockout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(
            name = "facility_id",
            nullable = false
    )
    private SportsFacility facility;


    @Column(
            name = "start_date_time",
            nullable = false
    )
    private LocalDateTime startDateTime;


    @Column(
            name = "end_date_time",
            nullable = false
    )
    private LocalDateTime endDateTime;


    @Column(
            length = 500
    )
    private String reason;


    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;


    public FacilityBlockout() {
    }


    @PrePersist
    public void onCreate() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }


    public Long getId() {
        return id;
    }


    public SportsFacility getFacility() {
        return facility;
    }


    public void setFacility(
            SportsFacility facility) {

        this.facility = facility;
    }


    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }


    public void setStartDateTime(
            LocalDateTime startDateTime) {

        this.startDateTime = startDateTime;
    }


    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }


    public void setEndDateTime(
            LocalDateTime endDateTime) {

        this.endDateTime = endDateTime;
    }


    public String getReason() {
        return reason;
    }


    public void setReason(
            String reason) {

        this.reason = reason;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }
}