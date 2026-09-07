/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.service.FacilityRentalService;
import com.fitnesusp.usp_gym.service.SportsFacilityService;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author svetik
 */

@Controller
public class RenterRentalController {

    private final FacilityRentalService rentalService;
    private final SportsFacilityService facilityService;

    public RenterRentalController(
        FacilityRentalService rentalService,
        SportsFacilityService facilityService) {

        this.rentalService = rentalService;
        this.facilityService = facilityService;
    }
    
    
    


    @GetMapping("/renter/rentals")
    public String myRentals( Principal principal, Model model) {


        model.addAttribute( "rentals", rentalService.getRentalsForRenter( principal.getName()));


        return "renter-rentals";
    }
    
    //private final FacilityRentalService rentalService;
    //private final SportsFacilityService facilityService;   
    
@GetMapping("/renter/rental-request/new")
public String showRentalRequestForm( Model model) {

        model.addAttribute( "facilities", facilityService.getActiveFacilities());

        return "renter-rental-request";
    }   

    @PostMapping("/renter/rental-request")
    public String submitRentalRequest(

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

        Principal principal,

        RedirectAttributes redirectAttributes) {


    try {

        rentalService.submitRentalRequest(
                principal.getName(),
                facilityId,
                startDateTime,
                endDateTime,
                purpose
        );


        redirectAttributes.addFlashAttribute( "successMessage", "Rental request sent to administrator." );


    } catch (IllegalStateException e) {

        redirectAttributes.addFlashAttribute( "errorMessage",  e.getMessage());
    }


    return "redirect:/renter/rentals";
    }
    
    @PostMapping("/renter/rentals/cancel/{id}")
    public String cancelRental(
        @PathVariable Long id,
        Principal principal,
        RedirectAttributes redirectAttributes) {

        try {

            rentalService.cancelRentalByRenter( principal.getName(), id);
            redirectAttributes.addFlashAttribute( "successMessage", "Rental cancelled successfully.");


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute( "errorMessage", e.getMessage());
        }


       return "redirect:/renter/rentals";
    }
    
}
