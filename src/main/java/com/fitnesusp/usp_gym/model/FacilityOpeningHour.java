package com.fitnesusp.usp_gym.model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(
        name = "facility_opening_hours",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "facility_id",
                                "day_of_week"
                        }
                )
        }
)
public class FacilityOpeningHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(
            name = "facility_id",
            nullable = false
    )
    private SportsFacility facility;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "day_of_week",
            nullable = false
    )
    private DayOfWeek dayOfWeek;


    @Column(
            name = "opening_time",
            nullable = false
    )
    private LocalTime openingTime;


    @Column(
            name = "closing_time",
            nullable = false
    )
    private LocalTime closingTime;


    public FacilityOpeningHour() {
    }


    public Long getId() {
        return id;
    }


    public SportsFacility getFacility() {
        return facility;
    }


    public void setFacility(
            SportsFacility facility
    ) {
        this.facility = facility;
    }


    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }


    public void setDayOfWeek(
            DayOfWeek dayOfWeek
    ) {
        this.dayOfWeek = dayOfWeek;
    }


    public LocalTime getOpeningTime() {
        return openingTime;
    }


    public void setOpeningTime(
            LocalTime openingTime
    ) {
        this.openingTime = openingTime;
    }


    public LocalTime getClosingTime() {
        return closingTime;
    }


    public void setClosingTime(
            LocalTime closingTime
    ) {
        this.closingTime = closingTime;
    }
}