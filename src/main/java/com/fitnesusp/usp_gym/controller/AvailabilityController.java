package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.FacilityRentalStatus;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.ResponseBody;
import com.fitnesusp.usp_gym.model.FacilityRentalStatus;
import com.fitnesusp.usp_gym.repository.FacilityRentalRepository;
import com.fitnesusp.usp_gym.repository.SportsFacilityRepository;
import com.fitnesusp.usp_gym.repository.FacilityBlockoutRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AvailabilityController {

    private final SportsFacilityRepository sportsFacilityRepository;
    private final FacilityRentalRepository facilityRentalRepository;
    private final FacilityBlockoutRepository blockoutRepository;


    public AvailabilityController(
            SportsFacilityRepository sportsFacilityRepository,
            FacilityRentalRepository facilityRentalRepository,
            FacilityBlockoutRepository blockoutRepository) {

        this.sportsFacilityRepository =
                sportsFacilityRepository;

        this.facilityRentalRepository =
                facilityRentalRepository;

        this.blockoutRepository =
                blockoutRepository;
    }


    @GetMapping("/member/availability")
    public String availability(Model model) {

        // Only active facilities
        model.addAttribute(
                "facilities",
                sportsFacilityRepository
                        .findByActiveTrueOrderByNameAsc()
        );


        // Only confirmed rentals count as booked
        model.addAttribute(
                "confirmedRentals",
                facilityRentalRepository
                        .findByStatusOrderByCreatedAtAsc(
                                FacilityRentalStatus.CONFIRMED
                        )
        );


        return "member-availability";
    }
    @GetMapping("/member/availability/events")
    @ResponseBody
    public List<Map<String, Object>> availabilityEvents() {

        List<Map<String, Object>> events =
                new ArrayList<>();


        // =====================================================
        // CONFIRMED BOOKINGS
        // =====================================================

        facilityRentalRepository
                .findByStatusOrderByCreatedAtAsc(
                        FacilityRentalStatus.CONFIRMED
                )
                .forEach(rental -> {

                    events.add(
                            Map.of(
                                    "id",
                                    rental.getId(),

                                    "facilityId",
                                    rental.getFacility().getId(),

                                    "facility",
                                    rental.getFacility().getName(),

                                    "start",
                                    rental.getStartDateTime().toString(),

                                    "end",
                                    rental.getEndDateTime().toString(),

                                    "eventType",
                                    "BOOKING"
                            )
                    );

                });


        // =====================================================
        // FACILITY BLOCK-OUTS
        // =====================================================

        blockoutRepository
                .findAllByOrderByStartDateTimeAsc()
                .forEach(blockout -> {

                    events.add(
                            Map.of(
                                    "id",
                                    blockout.getId(),

                                    "facilityId",
                                    blockout.getFacility().getId(),

                                    "facility",
                                    blockout.getFacility().getName(),

                                    "start",
                                    blockout.getStartDateTime().toString(),

                                    "end",
                                    blockout.getEndDateTime().toString(),

                                    "eventType",
                                    "BLOCKOUT"
                            )
                    );

                });


        return events;
    }
}