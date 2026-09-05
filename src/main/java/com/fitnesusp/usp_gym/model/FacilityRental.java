/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 *
 * @author svetik
 */

@Entity
@Table(name = "facility_rentals")
public class FacilityRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(
            name = "facility_id",
            nullable = false
    )
    private SportsFacility facility;


    @ManyToOne(optional = false)
    @JoinColumn(
            name = "renter_id",
            nullable = false
    )
    private Member renter;


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


    @Column(nullable = false)
    private String purpose;
    
    @Column(name = "admin_comment", length = 1000)
    private String adminComment;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityRentalStatus status;


    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;


    public FacilityRental() {
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


    public Member getRenter() {
        return renter;
    }

    public void setRenter(Member renter) {
        this.renter = renter;
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


    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public FacilityRentalStatus getStatus() {
        return status;
    }

    public void setStatus(
            FacilityRentalStatus status) {

        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }
    
    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }
}
