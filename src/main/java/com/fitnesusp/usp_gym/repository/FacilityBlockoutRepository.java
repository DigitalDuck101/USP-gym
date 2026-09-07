package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.FacilityBlockout;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacilityBlockoutRepository
        extends JpaRepository<FacilityBlockout, Long> {


    // Display all block-out periods
    List<FacilityBlockout>
    findAllByOrderByStartDateTimeAsc();


    // Check whether a requested booking overlaps
    // an administrator block-out period
    @Query("""
        SELECT b
        FROM FacilityBlockout b
        WHERE b.facility.id = :facilityId
          AND b.startDateTime < :endDateTime
          AND b.endDateTime > :startDateTime
    """)
    List<FacilityBlockout> findConflictingBlockouts(
            @Param("facilityId")
            Long facilityId,

            @Param("startDateTime")
            LocalDateTime startDateTime,

            @Param("endDateTime")
            LocalDateTime endDateTime
    );
}