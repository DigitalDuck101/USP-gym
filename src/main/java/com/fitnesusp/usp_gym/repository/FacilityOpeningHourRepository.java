package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.FacilityOpeningHour;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityOpeningHourRepository
        extends JpaRepository<FacilityOpeningHour, Long> {

    List<FacilityOpeningHour>
    findByFacility_IdOrderByDayOfWeekAsc(
            Long facilityId
    );

    Optional<FacilityOpeningHour>
    findByFacility_IdAndDayOfWeek(
            Long facilityId,
            DayOfWeek dayOfWeek
    );

    boolean existsByFacility_IdAndDayOfWeek(
            Long facilityId,
            DayOfWeek dayOfWeek
    );
}