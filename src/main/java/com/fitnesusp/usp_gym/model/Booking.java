/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
/**
 *
 * @author valeriy
 */

@Entity
@Table(
    name = "bookings",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_member_fitness_class",
            columnNames = {
                "member_id",
                "fitness_class_id"
            }
        )
    }
)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(
        name = "member_id",
        nullable = false
    )
    private Member member;


    @ManyToOne(optional = false)
    @JoinColumn(
        name = "fitness_class_id",
        nullable = false
    )
    private FitnessClass fitnessClass;


    @Column(
        name = "booking_date",
        nullable = false
    )
    private LocalDateTime bookingDate;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;


    public Booking() {
    }


    public Long getId() {
        return id;
    }


    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }


    public FitnessClass getFitnessClass() {
        return fitnessClass;
    }

    public void setFitnessClass(
            FitnessClass fitnessClass) {

        this.fitnessClass = fitnessClass;
    }


    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(
            LocalDateTime bookingDate) {

        this.bookingDate = bookingDate;
    }


    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(
            BookingStatus status) {

        this.status = status;
    }
}
