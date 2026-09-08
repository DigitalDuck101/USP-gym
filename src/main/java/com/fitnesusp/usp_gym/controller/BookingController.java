package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.model.MembershipStatus;
import com.fitnesusp.usp_gym.model.StudentVerificationStatus;

import com.fitnesusp.usp_gym.service.BookingService;
import com.fitnesusp.usp_gym.service.FitnessClassService;
import com.fitnesusp.usp_gym.service.MemberService;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/member")
public class BookingController {


    private final BookingService bookingService;

    private final FitnessClassService fitnessClassService;

    private final MemberService memberService;


    public BookingController(
            BookingService bookingService,
            FitnessClassService fitnessClassService,
            MemberService memberService) {

        this.bookingService = bookingService;
        this.fitnessClassService = fitnessClassService;
        this.memberService = memberService;
    }



    // ==========================================
    // AVAILABLE FITNESS CLASSES
    // ==========================================

    @GetMapping("/classes")
    public String availableClasses(
            Principal principal,
            Model model) {

        Member member =
                memberService.getMemberByUsername(
                        principal.getName()
                );


        model.addAttribute(
                "member",
                member
        );


        model.addAttribute(
                "fitnessClasses",
                fitnessClassService.getUpcomingClasses()
        );


        return "member-classes";
    }



    // ==========================================
    // BOOK FITNESS CLASS
    // ==========================================

    @PostMapping("/book/{classId}")
    public String bookClass(
            @PathVariable Long classId,
            Principal principal,
            RedirectAttributes redirectAttributes) {


        try {


            // ==========================================
            // GET CURRENT MEMBER
            // ==========================================

            Member member =
                    memberService.getMemberByUsername(
                            principal.getName()
                    );



            // ==========================================
            // STUDENT REGISTRATION CHECK
            // ==========================================

            if (member.getMemberType()
                    == MemberType.STUDENT) {


                if (member.getVerificationStatus()
                        != StudentVerificationStatus.APPROVED) {

                    throw new IllegalStateException(
                            "Your student registration must be approved "
                                    + "before you can book a fitness class."
                    );
                }
            }



            // ==========================================
            // RENTER / COMMUNITY MEMBER CHECK
            // ==========================================

            if (member.getMemberType()
                    == MemberType.RENTER) {


                MembershipStatus membershipStatus =
                        member.getMembershipStatus();


                // Membership not yet verified
                if (membershipStatus == null) {

                    throw new IllegalStateException(
                            "Your membership has not been verified. "
                                    + "Please contact the administrator."
                    );
                }


                // Membership must be ACTIVE
                if (membershipStatus
                        != MembershipStatus.ACTIVE) {

                    throw new IllegalStateException(
                            "An active membership is required "
                                    + "to book a fitness class. "
                                    + "Your current membership status is "
                                    + membershipStatus
                                    + "."
                    );
                }


                // Check expiry date
                if (member.getMembershipExpiryDate() != null
                        &&
                        member.getMembershipExpiryDate()
                                .isBefore(
                                        LocalDate.now()
                                )) {

                    throw new IllegalStateException(
                            "Your membership has expired. "
                                    + "Please renew your membership "
                                    + "before booking a fitness class."
                    );
                }
            }



            // ==========================================
            // BOOK CLASS
            // ==========================================

            bookingService.bookClass(
                    principal.getName(),
                    classId
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Class booked successfully!"
            );


        } catch (IllegalStateException e) {


            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/member/classes";
    }



    // ==========================================
    // MY BOOKINGS
    // ==========================================

    @GetMapping("/bookings")
    public String myBookings(
            Principal principal,
            Model model) {


        Member member =
                memberService.getMemberByUsername(
                        principal.getName()
                );


        model.addAttribute(
                "member",
                member
        );


        model.addAttribute(
                "bookings",
                bookingService.getBookingsForMember(
                        principal.getName()
                )
        );


        return "member-bookings";
    }



    // ==========================================
    // CANCEL BOOKING
    // ==========================================

    @PostMapping("/bookings/cancel/{bookingId}")
    public String cancelBooking(
            @PathVariable Long bookingId,
            Principal principal,
            RedirectAttributes redirectAttributes) {


        try {


            bookingService.cancelBooking(
                    principal.getName(),
                    bookingId
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Booking cancelled successfully!"
            );


        } catch (IllegalStateException e) {


            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/member/bookings";
    }
}