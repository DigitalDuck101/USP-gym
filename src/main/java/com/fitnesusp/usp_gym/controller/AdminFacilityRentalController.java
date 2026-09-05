/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.service.FacilityRentalService;
import com.fitnesusp.usp_gym.service.MemberService;
import com.fitnesusp.usp_gym.service.SportsFacilityService;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * @author svetik
 */

@Controller
@RequestMapping("/admin/rentals")
public class AdminFacilityRentalController {

    private final FacilityRentalService rentalService;
    private final MemberService memberService;
    private final SportsFacilityService facilityService;


    public AdminFacilityRentalController(
            FacilityRentalService rentalService,
            MemberService memberService,
            SportsFacilityService facilityService) {

        this.rentalService = rentalService;
        this.memberService = memberService;
        this.facilityService = facilityService;
    }


    @GetMapping
    public String showRentals(
            Model model) {

        model.addAttribute( "rentals",  rentalService.getAllRentals());

        return "facility-rentals";
    }


    @GetMapping("/new")
    public String showNewRentalForm(
            Model model) {

        model.addAttribute( "renters", memberService.getAllRenters());
        model.addAttribute( "facilities", facilityService.getActiveFacilities());

        return "facility-rental-form";
    }


    @PostMapping
    public String createRental(

            @RequestParam Long renterId,

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

            @RequestParam String purpose,

            RedirectAttributes redirectAttributes) {


        try {

            rentalService.createRental(
                    renterId,
                    facilityId,
                    startDateTime,
                    endDateTime,
                    purpose
            );

            redirectAttributes.addFlashAttribute( "successMessage", "Facility rental created successfully.");


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }


        return "redirect:/admin/rentals";
    }


    @PostMapping("/cancel/{id}")
    public String cancelRental(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {


        try {

            rentalService.cancelRental( id);
            redirectAttributes.addFlashAttribute( "successMessage", "Rental cancelled successfully.");

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute( "errorMessage", e.getMessage());
        }


        return "redirect:/admin/rentals";
    }
    
    @PostMapping("/approve/{id}")
    public String approveRequest(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {


        try {

            rentalService.approveRequest(id);


            redirectAttributes.addFlashAttribute( "successMessage", "Rental request approved.");


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }


        return "redirect:/admin/rentals";
    }
    
    @PostMapping("/reject/{id}")
        public String rejectRequest(
            @PathVariable Long id,
            @RequestParam(required = false)
            String comment,
            RedirectAttributes redirectAttributes) {


            try {

                rentalService.rejectRequest( id, comment );
                redirectAttributes.addFlashAttribute("successMessage", "Rental request rejected.");


            } catch (IllegalStateException e) {

                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            }


        return "redirect:/admin/rentals";
    }
}
