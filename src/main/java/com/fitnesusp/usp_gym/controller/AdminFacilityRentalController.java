package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.FacilityOpeningHour;
import com.fitnesusp.usp_gym.model.FacilityRental;
import com.fitnesusp.usp_gym.model.FacilityRentalStatus;

import com.fitnesusp.usp_gym.repository.FacilityBlockoutRepository;
import com.fitnesusp.usp_gym.repository.FacilityOpeningHourRepository;
import com.fitnesusp.usp_gym.repository.FacilityRentalRepository;

import com.fitnesusp.usp_gym.service.FacilityRentalService;
import com.fitnesusp.usp_gym.service.MemberService;
import com.fitnesusp.usp_gym.service.SportsFacilityService;
import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.model.MembershipStatus;
import com.fitnesusp.usp_gym.model.StudentVerificationStatus;

import java.time.LocalDate;

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
@RequestMapping("/admin/rentals")
public class AdminFacilityRentalController {


    private final FacilityRentalService rentalService;

    private final MemberService memberService;

    private final SportsFacilityService facilityService;

    private final FacilityRentalRepository facilityRentalRepository;

    private final FacilityBlockoutRepository blockoutRepository;

    private final FacilityOpeningHourRepository openingHourRepository;


    public AdminFacilityRentalController(
            FacilityRentalService rentalService,
            MemberService memberService,
            SportsFacilityService facilityService,
            FacilityBlockoutRepository blockoutRepository,
            FacilityRentalRepository facilityRentalRepository,
            FacilityOpeningHourRepository openingHourRepository) {

        this.rentalService =
                rentalService;

        this.memberService =
                memberService;

        this.facilityService =
                facilityService;

        this.facilityRentalRepository =
                facilityRentalRepository;

        this.blockoutRepository =
                blockoutRepository;

        this.openingHourRepository =
                openingHourRepository;
    }



    // =====================================================
    // VIEW ALL FACILITY RENTALS
    // =====================================================

    @GetMapping
    public String showRentals(
            Model model) {

        model.addAttribute(
                "rentals",
                rentalService.getAllRentals()
        );

        return "facility-rentals";
    }



    // =====================================================
    // SHOW ADMIN CREATE RENTAL FORM
    // =====================================================

    @GetMapping("/new")
    public String showNewRentalForm(
            Model model) {

        model.addAttribute(
                "renters",
                memberService.getAllRenters()
        );

        model.addAttribute(
                "facilities",
                facilityService.getActiveFacilities()
        );

        return "facility-rental-form";
    }



    // =====================================================
    // ADMIN CREATE RENTAL
    // =====================================================

    @PostMapping
    public String createRental(

            @RequestParam
            Long renterId,

            @RequestParam
            Long facilityId,

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

            @RequestParam
            String purpose,

            RedirectAttributes redirectAttributes) {


        try {

            rentalService.createRental(
                    renterId,
                    facilityId,
                    startDateTime,
                    endDateTime,
                    purpose
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Facility rental created successfully."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/admin/rentals";
    }



    // =====================================================
    // CANCEL RENTAL
    // =====================================================

    @PostMapping("/cancel/{id}")
    public String cancelRental(
            @PathVariable
            Long id,
            RedirectAttributes redirectAttributes) {


        try {

            rentalService.cancelRental(id);


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rental cancelled successfully."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/admin/rentals";
    }



    // =====================================================
    // APPROVE PENDING RENTAL REQUEST
    // =====================================================

    @PostMapping("/approve/{id}")
    public String approveRequest(
            @PathVariable
            Long id,
            RedirectAttributes redirectAttributes) {


        try {

            // =====================================================
            // FIND RENTAL REQUEST
            // =====================================================

            FacilityRental rental =
                    facilityRentalRepository
                            .findById(id)
                            .orElseThrow(
                                    () -> new IllegalStateException(
                                            "Rental request not found."
                                    )
                            );



            // =====================================================
            // ONLY PENDING REQUESTS CAN BE APPROVED
            // =====================================================

            if (rental.getStatus()
                    != FacilityRentalStatus.PENDING) {

                throw new IllegalStateException(
                        "Only pending rental requests can be approved."
                );
            }
            // =====================================================
// CHECK CURRENT MEMBER ELIGIBILITY
// =====================================================

            Member renter =
                    rental.getRenter();


// =====================================================
// STUDENT REGISTRATION CHECK
// =====================================================

            if (renter.getMemberType() == MemberType.STUDENT) {

                if (renter.getVerificationStatus()
                        != StudentVerificationStatus.APPROVED) {

                    throw new IllegalStateException(
                            "Cannot approve this request because "
                                    + "the student's registration is not approved."
                    );
                }
            }


// =====================================================
// COMMUNITY / RENTER MEMBERSHIP CHECK
// =====================================================

            if (renter.getMemberType() == MemberType.RENTER) {

                MembershipStatus membershipStatus =
                        renter.getMembershipStatus();


                if (membershipStatus == null) {

                    throw new IllegalStateException(
                            "Cannot approve this request because "
                                    + "the member's membership has not been verified."
                    );
                }


                if (membershipStatus
                        != MembershipStatus.ACTIVE) {

                    throw new IllegalStateException(
                            "Cannot approve this request because "
                                    + "the member's membership status is "
                                    + membershipStatus
                                    + "."
                    );
                }


                if (renter.getMembershipExpiryDate() != null
                        &&
                        renter.getMembershipExpiryDate()
                                .isBefore(LocalDate.now())) {

                    throw new IllegalStateException(
                            "Cannot approve this request because "
                                    + "the member's membership has expired."
                    );
                }
            }



            // =====================================================
            // BOOKING MUST START AND END ON SAME DAY
            // =====================================================

            if (!rental
                    .getStartDateTime()
                    .toLocalDate()
                    .equals(
                            rental
                                    .getEndDateTime()
                                    .toLocalDate()
                    )) {

                throw new IllegalStateException(
                        "Cannot approve this request because "
                                + "the booking crosses into another day."
                );
            }



            // =====================================================
            // CHECK FACILITY OPENING HOURS
            // =====================================================

            FacilityOpeningHour openingHour =
                    openingHourRepository
                            .findByFacility_IdAndDayOfWeek(
                                    rental
                                            .getFacility()
                                            .getId(),

                                    rental
                                            .getStartDateTime()
                                            .getDayOfWeek()
                            )
                            .orElse(null);



            // No opening hours means facility is closed
            if (openingHour == null) {

                throw new IllegalStateException(
                        "Cannot approve this request because "
                                + "the facility is closed on the selected day."
                );
            }



            boolean startsTooEarly =
                    rental
                            .getStartDateTime()
                            .toLocalTime()
                            .isBefore(
                                    openingHour.getOpeningTime()
                            );


            boolean endsTooLate =
                    rental
                            .getEndDateTime()
                            .toLocalTime()
                            .isAfter(
                                    openingHour.getClosingTime()
                            );


            if (startsTooEarly || endsTooLate) {

                throw new IllegalStateException(
                        "Cannot approve this request because "
                                + "it is outside the facility opening hours. "
                                + "Opening hours are "
                                + openingHour.getOpeningTime()
                                + " to "
                                + openingHour.getClosingTime()
                                + "."
                );
            }



            // =====================================================
            // CHECK ADMIN FACILITY BLOCK-OUT
            // =====================================================

            boolean blocked =
                    !blockoutRepository
                            .findConflictingBlockouts(
                                    rental
                                            .getFacility()
                                            .getId(),

                                    rental.getStartDateTime(),

                                    rental.getEndDateTime()
                            )
                            .isEmpty();


            if (blocked) {

                throw new IllegalStateException(
                        "Cannot approve this request because "
                                + "the facility is unavailable during "
                                + "the selected time."
                );
            }



            // =====================================================
            // CHECK EXISTING CONFIRMED BOOKING
            // =====================================================

            boolean hasConflict =
                    !facilityRentalRepository
                            .findConflictingRentals(
                                    rental
                                            .getFacility()
                                            .getId(),

                                    FacilityRentalStatus.CONFIRMED,

                                    rental.getStartDateTime(),

                                    rental.getEndDateTime()
                            )
                            .isEmpty();


            if (hasConflict) {

                throw new IllegalStateException(
                        "Cannot approve this request because "
                                + "the facility is already booked during "
                                + "the selected time."
                );
            }



            // =====================================================
            // SAFE TO APPROVE
            // =====================================================

            rentalService.approveRequest(id);


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rental request approved."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/admin/rentals";
    }
}