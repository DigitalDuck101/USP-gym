package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.FacilityOpeningHour;
import com.fitnesusp.usp_gym.model.SportsFacility;
import com.fitnesusp.usp_gym.repository.FacilityOpeningHourRepository;
import com.fitnesusp.usp_gym.service.SportsFacilityService;

import java.time.DayOfWeek;
import java.time.LocalTime;

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
@RequestMapping(
        "/admin/facilities/{facilityId}/opening-hours"
)
public class FacilityOpeningHourController {


    private final FacilityOpeningHourRepository
            openingHourRepository;

    private final SportsFacilityService
            facilityService;


    public FacilityOpeningHourController(
            FacilityOpeningHourRepository openingHourRepository,
            SportsFacilityService facilityService) {

        this.openingHourRepository =
                openingHourRepository;

        this.facilityService =
                facilityService;
    }


    // =====================================================
    // VIEW OPENING HOURS
    // =====================================================

    @GetMapping
    public String showOpeningHours(
            @PathVariable Long facilityId,
            Model model) {

        SportsFacility facility =
                facilityService
                        .getFacilityById(facilityId);


        model.addAttribute(
                "facility",
                facility
        );


        model.addAttribute(
                "openingHours",
                openingHourRepository
                        .findByFacility_IdOrderByDayOfWeekAsc(
                                facilityId
                        )
        );


        return "facility-opening-hours";
    }


    // =====================================================
    // SHOW CREATE FORM
    // =====================================================

    @GetMapping("/new")
    public String showCreateForm(
            @PathVariable Long facilityId,
            Model model) {

        SportsFacility facility =
                facilityService
                        .getFacilityById(facilityId);


        model.addAttribute(
                "facility",
                facility
        );


        model.addAttribute(
                "days",
                DayOfWeek.values()
        );


        return "facility-opening-hour-form";
    }


    // =====================================================
    // CREATE OPENING HOURS
    // =====================================================

    @PostMapping
    public String createOpeningHour(
            @PathVariable Long facilityId,

            @RequestParam DayOfWeek dayOfWeek,

            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime openingTime,

            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime closingTime,

            RedirectAttributes redirectAttributes) {

        try {

            SportsFacility facility =
                    facilityService
                            .getFacilityById(facilityId);


            if (!closingTime.isAfter(openingTime)) {

                throw new IllegalStateException(
                        "Closing time must be after opening time."
                );
            }


            boolean alreadyExists =
                    openingHourRepository
                            .existsByFacility_IdAndDayOfWeek(
                                    facilityId,
                                    dayOfWeek
                            );


            if (alreadyExists) {

                throw new IllegalStateException(
                        "Opening hours already exist for "
                                + dayOfWeek
                                + ". Please edit the existing record."
                );
            }


            FacilityOpeningHour openingHour =
                    new FacilityOpeningHour();


            openingHour.setFacility(
                    facility
            );

            openingHour.setDayOfWeek(
                    dayOfWeek
            );

            openingHour.setOpeningTime(
                    openingTime
            );

            openingHour.setClosingTime(
                    closingTime
            );


            openingHourRepository.save(
                    openingHour
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Opening hours added successfully."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/admin/facilities/"
                + facilityId
                + "/opening-hours";
    }


    // =====================================================
    // SHOW EDIT FORM
    // =====================================================

    @GetMapping("/edit/{hourId}")
    public String showEditForm(
            @PathVariable Long facilityId,
            @PathVariable Long hourId,
            Model model) {

        SportsFacility facility =
                facilityService
                        .getFacilityById(facilityId);


        FacilityOpeningHour openingHour =
                openingHourRepository
                        .findById(hourId)
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "Opening-hours record not found."
                                )
                        );


        if (!openingHour
                .getFacility()
                .getId()
                .equals(facilityId)) {

            throw new IllegalStateException(
                    "Opening-hours record does not belong to this facility."
            );
        }


        model.addAttribute(
                "facility",
                facility
        );


        model.addAttribute(
                "openingHour",
                openingHour
        );


        model.addAttribute(
                "days",
                DayOfWeek.values()
        );


        return "facility-opening-hour-edit";
    }


    // =====================================================
    // UPDATE OPENING HOURS
    // =====================================================

    @PostMapping("/update/{hourId}")
    public String updateOpeningHour(
            @PathVariable Long facilityId,
            @PathVariable Long hourId,

            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime openingTime,

            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            LocalTime closingTime,

            RedirectAttributes redirectAttributes) {

        try {

            FacilityOpeningHour openingHour =
                    openingHourRepository
                            .findById(hourId)
                            .orElseThrow(
                                    () -> new IllegalStateException(
                                            "Opening-hours record not found."
                                    )
                            );


            if (!openingHour
                    .getFacility()
                    .getId()
                    .equals(facilityId)) {

                throw new IllegalStateException(
                        "Opening-hours record does not belong to this facility."
                );
            }


            if (!closingTime.isAfter(openingTime)) {

                throw new IllegalStateException(
                        "Closing time must be after opening time."
                );
            }


            openingHour.setOpeningTime(
                    openingTime
            );

            openingHour.setClosingTime(
                    closingTime
            );


            openingHourRepository.save(
                    openingHour
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Opening hours updated successfully."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/admin/facilities/"
                + facilityId
                + "/opening-hours";
    }


    // =====================================================
    // DELETE OPENING HOURS
    // =====================================================

    @PostMapping("/delete/{hourId}")
    public String deleteOpeningHour(
            @PathVariable Long facilityId,
            @PathVariable Long hourId,
            RedirectAttributes redirectAttributes) {

        try {

            FacilityOpeningHour openingHour =
                    openingHourRepository
                            .findById(hourId)
                            .orElseThrow(
                                    () -> new IllegalStateException(
                                            "Opening-hours record not found."
                                    )
                            );


            if (!openingHour
                    .getFacility()
                    .getId()
                    .equals(facilityId)) {

                throw new IllegalStateException(
                        "Opening-hours record does not belong to this facility."
                );
            }


            openingHourRepository.delete(
                    openingHour
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Opening hours removed successfully."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/admin/facilities/"
                + facilityId
                + "/opening-hours";
    }
}