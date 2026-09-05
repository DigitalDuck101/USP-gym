/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.Booking;
import com.fitnesusp.usp_gym.model.BookingStatus;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author valeriy
 */

public interface BookingRepository
        extends JpaRepository<Booking, Long> {


    Optional<Booking>
        findByMember_IdAndFitnessClass_Id(
            Long memberId,
            Long fitnessClassId
        );


    long countByFitnessClass_IdAndStatus(
            Long fitnessClassId,
            BookingStatus status
    );


    List<Booking>
        findByMember_IdOrderByFitnessClass_DateAscFitnessClass_StartTimeAsc(
            Long memberId
        );


    void deleteByMember_Id(Long memberId);


    void deleteByFitnessClass_Id(Long fitnessClassId);
    
        Optional<Booking> findByIdAndMember_Id(
                 Long bookingId,
                 Long memberId
        );
    
    List<Booking> findByMember_IdAndStatusAndFitnessClass_Date(
        Long memberId,
        BookingStatus status,
        LocalDate date
    );
}
