/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Trainer;
import com.fitnesusp.usp_gym.service.FitnessClassService;
import com.fitnesusp.usp_gym.service.TrainerService;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author svetik
 */

@Controller
public class TrainerProfileController {

    private final TrainerService trainerService;
    private final FitnessClassService fitnessClassService;

    public TrainerProfileController(
            TrainerService trainerService,
            FitnessClassService fitnessClassService) {

        this.trainerService = trainerService;
        this.fitnessClassService = fitnessClassService;
    }

    @GetMapping("/trainer/profile")
    public String trainerProfile( Principal principal, Model model) {

        Trainer trainer =  trainerService.getTrainerByUsername( principal.getName());

        model.addAttribute( "trainer", trainer);

        return "trainer-profile";
    }


    @GetMapping("/trainer/classes")
    public String trainerClasses( Principal principal, Model model) {

        model.addAttribute( "fitnessClasses",
                fitnessClassService.getClassesForTrainer( principal.getName()));

        return "trainer-classes";
    }
}