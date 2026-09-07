package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.FacilityOpeningHour;
import com.fitnesusp.usp_gym.model.FacilityRentalStatus;

import com.fitnesusp.usp_gym.repository.FacilityBlockoutRepository;
import com.fitnesusp.usp_gym.repository.FacilityOpeningHourRepository;
import com.fitnesusp.usp_gym.repository.FacilityRentalRepository;
import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.service.MemberService;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.model.MembershipStatus;
import com.fitnesusp.usp_gym.model.StudentVerificationStatus;

import java.time.LocalDate;

import com.fitnesusp.usp_gym.service.FacilityRentalService;
import com.fitnesusp.usp_gym.service.SportsFacilityService;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class RenterRentalController {


    private final FacilityRentalService rentalService;
    private final SportsFacilityService facilityService;
    private final FacilityRentalRepository facilityRentalRepository;
    private final FacilityBlockoutRepository blockoutRepository;
    private final FacilityOpeningHourRepository openingHourRepository;
    private final MemberService memberService;


    public RenterRentalController(
            FacilityRentalService rentalService,
            SportsFacilityService facilityService,
            FacilityRentalRepository facilityRentalRepository,
            FacilityBlockoutRepository blockoutRepository,
            FacilityOpeningHourRepository openingHourRepository,
            MemberService memberService) {

        this.rentalService = rentalService;
        this.facilityService = facilityService;
        this.facilityRentalRepository = facilityRentalRepository;
        this.blockoutRepository = blockoutRepository;
        this.openingHourRepository = openingHourRepository;
        this.memberService = memberService;
    }



    // =====================================================
    // VIEW MY FACILITY RENTALS
    // =====================================================

    @GetMapping("/renter/rentals")
    public String myRentals(
            Principal principal,
            Model model) {


        model.addAttribute(
                "rentals",
                rentalService.getRentalsForRenter(
                        principal.getName()
                )
        );


        return "renter-rentals";
    }



    // =====================================================
    // SHOW RENTAL REQUEST FORM
    // =====================================================

    @GetMapping("/renter/rental-request/new")
    public String showRentalRequestForm(
            @RequestParam(required = false)
            Long facilityId,
            Model model) {


        model.addAttribute(
                "facilities",
                facilityService.getActiveFacilities()
        );


        model.addAttribute(
                "selectedFacilityId",
                facilityId
        );


        return "renter-rental-request";
    }



    // =====================================================
    // SUBMIT FACILITY RENTAL REQUEST
    // =====================================================

    @PostMapping("/renter/rental-request")
    public String submitRentalRequest(

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

            Principal principal,

            RedirectAttributes redirectAttributes) {


        // =====================================================
        // CHECK VALID DATE AND TIME
        // =====================================================

        if (startDateTime == null
                || endDateTime == null) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Please select a valid booking date and time."
            );

            return "redirect:/renter/rentals";
        }



        // =====================================================
        // PREVENT PAST BOOKINGS
        // =====================================================

        if (!startDateTime.isAfter(
                LocalDateTime.now()
        )) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Bookings cannot be made for a past date or time."
            );

            return "redirect:/renter/rentals";
        }



        // =====================================================
        // END TIME MUST BE AFTER START TIME
        // =====================================================

        if (!endDateTime.isAfter(
                startDateTime
        )) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "The booking end time must be after the start time."
            );

            return "redirect:/renter/rentals";
        }



        // =====================================================
        // STANDARD BOOKING MUST BE EXACTLY 1 HOUR
        // =====================================================

        LocalDateTime requiredEndTime =
                startDateTime.plusHours(1);


        if (!endDateTime.equals(
                requiredEndTime
        )) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Standard facility bookings must be exactly 1 hour."
            );

            return "redirect:/renter/rentals";
        }



        // =====================================================
        // BOOKING MUST START AND END ON SAME DAY
        // =====================================================

        if (!startDateTime
                .toLocalDate()
                .equals(
                        endDateTime.toLocalDate()
                )) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Facility bookings must start and end on the same day."
            );

            return "redirect:/renter/rentals";
        }

// =====================================================
// CHECK MEMBER BOOKING ELIGIBILITY
// =====================================================

        Member member =
                memberService.getMemberByUsername(
                        principal.getName()
                );


// =====================================================
// STUDENT REGISTRATION VERIFICATION
// =====================================================

        if (member.getMemberType() == MemberType.STUDENT) {

            if (member.getVerificationStatus()
                    != StudentVerificationStatus.APPROVED) {

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Your student registration must be approved before you can make a facility booking."
                );

                return "redirect:/renter/rentals";
            }
        }


// =====================================================
// COMMUNITY / RENTER MEMBERSHIP VERIFICATION
// =====================================================

        if (member.getMemberType() == MemberType.RENTER) {

            MembershipStatus status =
                    member.getMembershipStatus();


            // Missing membership information
            if (status == null) {

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Your membership has not been verified. Please contact the administrator."
                );

                return "redirect:/renter/rentals";
            }


            // Membership must be ACTIVE
            if (status != MembershipStatus.ACTIVE) {

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "An active membership is required to make a facility booking. "
                                + "Your current membership status is "
                                + status
                                + "."
                );

                return "redirect:/renter/rentals";
            }


            // Check expiry date
            if (member.getMembershipExpiryDate() != null
                    &&
                    member.getMembershipExpiryDate()
                            .isBefore(LocalDate.now())) {

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Your membership has expired. Please renew your membership before making a booking."
                );

                return "redirect:/renter/rentals";
            }
        }

        // =====================================================
        // CHECK FACILITY OPENING HOURS
        // =====================================================

        FacilityOpeningHour openingHour =
                openingHourRepository
                        .findByFacility_IdAndDayOfWeek(
                                facilityId,
                                startDateTime.getDayOfWeek()
                        )
                        .orElse(null);


        // No opening-hour record means facility is closed
        if (openingHour == null) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This facility is closed on the selected day."
            );

            return "redirect:/renter/rentals";
        }



        boolean startsTooEarly =
                startDateTime
                        .toLocalTime()
                        .isBefore(
                                openingHour.getOpeningTime()
                        );


        boolean endsTooLate =
                endDateTime
                        .toLocalTime()
                        .isAfter(
                                openingHour.getClosingTime()
                        );


        if (startsTooEarly || endsTooLate) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "This booking is outside the facility opening hours. "
                            + "Opening hours are "
                            + openingHour.getOpeningTime()
                            + " to "
                            + openingHour.getClosingTime()
                            + "."
            );

            return "redirect:/renter/rentals";
        }



        // =====================================================
        // CHECK PURPOSE
        // =====================================================

        if (purpose == null
                || purpose.trim().isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Please enter the purpose of the facility booking."
            );

            return "redirect:/renter/rentals";
        }



        try {


            // =====================================================
            // CHECK ADMIN FACILITY BLOCK-OUT
            // =====================================================

            boolean blocked =
                    !blockoutRepository
                            .findConflictingBlockouts(
                                    facilityId,
                                    startDateTime,
                                    endDateTime
                            )
                            .isEmpty();


            if (blocked) {

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "This facility is unavailable during the selected time. "
                                + "Please choose another booking time."
                );

                return "redirect:/renter/rentals";
            }



            // =====================================================
            // CHECK EXISTING CONFIRMED BOOKING
            // =====================================================

            boolean hasConflict =
                    !facilityRentalRepository
                            .findConflictingRentals(
                                    facilityId,
                                    FacilityRentalStatus.CONFIRMED,
                                    startDateTime,
                                    endDateTime
                            )
                            .isEmpty();


            if (hasConflict) {

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "This facility is already booked during the selected time. "
                                + "Please choose another time."
                );

                return "redirect:/renter/rentals";
            }



            // =====================================================
            // CREATE PENDING RENTAL REQUEST
            // =====================================================

            rentalService.submitRentalRequest(
                    principal.getName(),
                    facilityId,
                    startDateTime,
                    endDateTime,
                    purpose.trim()
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Rental request sent to administrator."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/renter/rentals";
    }
}