/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.FacilityType;
import com.fitnesusp.usp_gym.model.SportsFacility;
import com.fitnesusp.usp_gym.service.SportsFacilityService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * @author svetik
 */

@Controller
@RequestMapping("/admin/facilities")
public class SportsFacilityController {

    private final SportsFacilityService facilityService;


    public SportsFacilityController( SportsFacilityService facilityService) {

        this.facilityService = facilityService;
    }


    @GetMapping
    public String showFacilities( Model model) {

        model.addAttribute( "facilities", facilityService.getAllFacilities());

        return "sports-facilities";
    }


    @GetMapping("/new")
    public String showNewFacilityForm( Model model) {

        model.addAttribute( "facility",  new SportsFacility());

        model.addAttribute( "facilityTypes", FacilityType.values());

        return "sports-facility-form";
    }


    @PostMapping
    public String createFacility(
            @ModelAttribute SportsFacility facility,
            RedirectAttributes redirectAttributes) {

        try {

            facility.setActive(true);
            facilityService.saveFacility(facility);
            redirectAttributes.addFlashAttribute( "successMessage", "Sports facility created successfully.");

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute( "errorMessage", e.getMessage());
        }

        return "redirect:/admin/facilities";
    }


    @GetMapping("/edit/{id}")
    public String showEditFacilityForm( @PathVariable Long id, Model model) {

        model.addAttribute( "facility", facilityService.getFacilityById(id));
        model.addAttribute( "facilityTypes", FacilityType.values());

        return "sports-facility-edit";
    }


    @PostMapping("/update/{id}")
    public String updateFacility(
            @PathVariable Long id,
            @ModelAttribute SportsFacility facility,
            RedirectAttributes redirectAttributes) {

        try {

            facilityService.updateFacility( id, facility );

            redirectAttributes.addFlashAttribute( "successMessage",  "Sports facility updated successfully." );

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute("errorMessage",  e.getMessage());
        }

        return "redirect:/admin/facilities";
    }


    @PostMapping("/toggle/{id}")
    public String toggleFacility( @PathVariable Long id) {

        facilityService.toggleActive(id);

        return "redirect:/admin/facilities";
    }
}
