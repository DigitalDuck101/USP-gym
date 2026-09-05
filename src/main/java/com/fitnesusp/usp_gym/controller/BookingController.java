/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Member;

import com.fitnesusp.usp_gym.service.BookingService;
import com.fitnesusp.usp_gym.service.FitnessClassService;
import com.fitnesusp.usp_gym.service.MemberService;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 *
 * @author svetik
 */



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


        Member member =  memberService.getMemberByUsername( principal.getName());
        model.addAttribute( "member", member);
        model.addAttribute( "fitnessClasses", fitnessClassService.getUpcomingClasses());


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


            bookingService.bookClass( principal.getName(), classId );


            redirectAttributes.addFlashAttribute("successMessage", "Class booked successfully!" );


        } catch (IllegalStateException e) {


            redirectAttributes.addFlashAttribute( "errorMessage", e.getMessage());

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


        Member member = memberService.getMemberByUsername( principal.getName());


        model.addAttribute( "member", member );
        model.addAttribute( "bookings", bookingService.getBookingsForMember( principal.getName()));


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

            bookingService.cancelBooking( principal.getName(), bookingId );
            redirectAttributes.addFlashAttribute( "successMessage", "Booking cancelled successfully!");


        } catch (IllegalStateException e) {


            redirectAttributes.addFlashAttribute( "errorMessage", e.getMessage());

        }


        return "redirect:/member/bookings";
    }
}