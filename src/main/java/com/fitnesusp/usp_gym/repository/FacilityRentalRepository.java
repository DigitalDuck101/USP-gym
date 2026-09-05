/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.FacilityRental;
import com.fitnesusp.usp_gym.model.FacilityRentalStatus;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author valeriy
 */

public interface FacilityRentalRepository
        extends JpaRepository<FacilityRental, Long> {
    
    List<FacilityRental>
        findAllByOrderByStartDateTimeAsc();

    List<FacilityRental>
        findByRenter_IdOrderByStartDateTimeAsc(
                Long renterId
        );


    @Query("""
        SELECT r
        FROM FacilityRental r
        WHERE r.facility.id = :facilityId
          AND r.status = :status
          AND r.startDateTime < :endDateTime
          AND r.endDateTime > :startDateTime
    """)
    List<FacilityRental> findConflictingRentals(
            @Param("facilityId")
            Long facilityId,

            @Param("status")
            FacilityRentalStatus status,

            @Param("startDateTime")
            LocalDateTime startDateTime,

            @Param("endDateTime")
            LocalDateTime endDateTime
    );
    
    List<FacilityRental>
    findByStatusOrderByCreatedAtAsc(
        FacilityRentalStatus status
    );
}
