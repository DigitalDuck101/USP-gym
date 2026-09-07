package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.FacilityBlockout;
import com.fitnesusp.usp_gym.model.FacilityRentalStatus;
import com.fitnesusp.usp_gym.model.SportsFacility;

import com.fitnesusp.usp_gym.repository.FacilityBlockoutRepository;
import com.fitnesusp.usp_gym.repository.FacilityRentalRepository;
import com.fitnesusp.usp_gym.repository.SportsFacilityRepository;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin/blockouts")
public class FacilityBlockoutController {


    private final FacilityBlockoutRepository blockoutRepository;

    private final SportsFacilityRepository facilityRepository;

    private final FacilityRentalRepository rentalRepository;


    public FacilityBlockoutController(
            FacilityBlockoutRepository blockoutRepository,
            SportsFacilityRepository facilityRepository,
            FacilityRentalRepository rentalRepository) {

        this.blockoutRepository =
                blockoutRepository;

        this.facilityRepository =
                facilityRepository;

        this.rentalRepository =
                rentalRepository;
    }


    // =====================================================
    // VIEW ALL BLOCK-OUT PERIODS
    // =====================================================

    @GetMapping
    public String showBlockouts(
            Model model) {

        model.addAttribute(
                "blockouts",
                blockoutRepository
                        .findAllByOrderByStartDateTimeAsc()
        );

        return "facility-blockouts";
    }


    // =====================================================
    // SHOW CREATE FORM
    // =====================================================

    @GetMapping("/new")
    public String showCreateForm(
            Model model) {

        model.addAttribute(
                "facilities",
                facilityRepository
                        .findByActiveTrueOrderByNameAsc()
        );

        return "facility-blockout-form";
    }


    // =====================================================
    // CREATE BLOCK-OUT
    // =====================================================

    @PostMapping
    public String createBlockout(

            @RequestParam Long facilityId,

            @RequestParam
            @DateTimeFormat(
                    pattern = "yyyy-MM-dd'T'HH:mm"
            )
            LocalDateTime startDateTime,

            @RequestParam
            @DateTimeFormat(
                    pattern = "yyyy-MM-dd'T'HH:mm"
            )
            LocalDateTime endDateTime,

            @RequestParam(required = false)
            String reason,

            RedirectAttributes redirectAttributes) {


        try {


            // ---------------------------------------------
            // FACILITY MUST EXIST
            // ---------------------------------------------

            SportsFacility facility =
                    facilityRepository
                            .findById(facilityId)
                            .orElseThrow(
                                    () ->
                                            new IllegalStateException(
                                                    "Sports facility not found."
                                            )
                            );


            // ---------------------------------------------
            // VALIDATE TIMES
            // ---------------------------------------------

            if (startDateTime == null
                    || endDateTime == null) {

                throw new IllegalStateException(
                        "Please enter a valid start and end time."
                );
            }


            if (!endDateTime.isAfter(
                    startDateTime)) {

                throw new IllegalStateException(
                        "Block-out end time must be after the start time."
                );
            }


            // ---------------------------------------------
            // CHECK EXISTING BLOCK-OUT
            // ---------------------------------------------

            boolean blockoutConflict =
                    !blockoutRepository
                            .findConflictingBlockouts(
                                    facilityId,
                                    startDateTime,
                                    endDateTime
                            )
                            .isEmpty();


            if (blockoutConflict) {

                throw new IllegalStateException(
                        "This facility already has a block-out period during the selected time."
                );
            }


            // ---------------------------------------------
            // CHECK CONFIRMED BOOKINGS
            // ---------------------------------------------

            boolean bookingConflict =
                    !rentalRepository
                            .findConflictingRentals(
                                    facilityId,
                                    FacilityRentalStatus.CONFIRMED,
                                    startDateTime,
                                    endDateTime
                            )
                            .isEmpty();


            if (bookingConflict) {

                throw new IllegalStateException(
                        "Cannot block this period because the facility already has a confirmed booking."
                );
            }


            // ---------------------------------------------
            // SAVE BLOCK-OUT
            // ---------------------------------------------

            FacilityBlockout blockout =
                    new FacilityBlockout();


            blockout.setFacility(
                    facility
            );

            blockout.setStartDateTime(
                    startDateTime
            );

            blockout.setEndDateTime(
                    endDateTime
            );

            blockout.setReason(
                    reason
            );


            blockoutRepository.save(
                    blockout
            );


            redirectAttributes
                    .addFlashAttribute(
                            "successMessage",
                            "Facility block-out period created successfully."
                    );


        } catch (IllegalStateException e) {


            redirectAttributes
                    .addFlashAttribute(
                            "errorMessage",
                            e.getMessage()
                    );
        }


        return "redirect:/admin/blockouts";
    }


    // =====================================================
    // DELETE BLOCK-OUT
    // =====================================================

    @PostMapping("/delete/{id}")
    public String deleteBlockout(

            @PathVariable Long id,

            RedirectAttributes redirectAttributes) {


        if (!blockoutRepository.existsById(id)) {

            redirectAttributes
                    .addFlashAttribute(
                            "errorMessage",
                            "Block-out period not found."
                    );

            return "redirect:/admin/blockouts";
        }


        blockoutRepository.deleteById(id);


        redirectAttributes
                .addFlashAttribute(
                        "successMessage",
                        "Block-out period removed successfully."
                );


        return "redirect:/admin/blockouts";
    }
}