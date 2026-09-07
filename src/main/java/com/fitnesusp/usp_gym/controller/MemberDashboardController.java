package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Booking;
import com.fitnesusp.usp_gym.model.BookingStatus;
import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.repository.BookingRepository;
import com.fitnesusp.usp_gym.repository.MemberRepository;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberDashboardController {

    private final MemberRepository memberRepository;
    private final BookingRepository bookingRepository;


    public MemberDashboardController(
            MemberRepository memberRepository,
            BookingRepository bookingRepository) {

        this.memberRepository = memberRepository;
        this.bookingRepository = bookingRepository;
    }


    @GetMapping("/member/dashboard")
    public String memberDashboard(
            Authentication authentication,
            Model model) {

        String username =
                authentication.getName();


        Member member =
                memberRepository
                        .findByAppUserUsername(username)
                        .orElse(null);


        if (member == null) {

            return "redirect:/login";

        }


        // =========================================
        // MEMBER BOOKINGS
        // =========================================

        List<Booking> bookings =
                bookingRepository
                        .findByMember_IdOrderByFitnessClass_DateAscFitnessClass_StartTimeAsc(
                                member.getId()
                        );


        long activeBookings =
                bookings.stream()
                        .filter(booking ->
                                booking.getStatus()
                                        == BookingStatus.BOOKED
                        )
                        .count();


        // =========================================
        // SEND DATA TO DASHBOARD
        // =========================================

        model.addAttribute(
                "member",
                member
        );

        model.addAttribute(
                "totalBookings",
                bookings.size()
        );

        model.addAttribute(
                "activeBookings",
                activeBookings
        );


        return "member-dashboard";
    }
}