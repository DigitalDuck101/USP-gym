/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.FitnessClass;
import com.fitnesusp.usp_gym.service.FitnessClassService;
import com.fitnesusp.usp_gym.service.TrainerService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author svetik
 */

@Controller
@RequestMapping("/fitness-classes")
public class FitnessClassController {

    private final FitnessClassService fitnessClassService;
    private final TrainerService trainerService;

    public FitnessClassController(
            FitnessClassService fitnessClassService,
            TrainerService trainerService) {

        this.fitnessClassService = fitnessClassService;
        this.trainerService = trainerService;
    }

    @GetMapping
    public String showClasses(Model model) {

        model.addAttribute( "fitnessClasses", fitnessClassService.getAllClasses());

        return "fitness-classes";
    }

    @GetMapping("/new")
    public String showNewClassForm(Model model) {

        model.addAttribute( "fitnessClass", new FitnessClass());
        model.addAttribute( "trainers", trainerService.getAllTrainers());

        return "fitness-class-form";
    }

    @PostMapping
    public String createClass(
            @ModelAttribute FitnessClass fitnessClass,
            @RequestParam Long trainerId) {

        fitnessClassService.createClass( fitnessClass, trainerId);

        return "redirect:/fitness-classes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute( "fitnessClass", fitnessClassService.getClassById(id));
        model.addAttribute( "trainers", trainerService.getAllTrainers());

        return "fitness-class-edit";
    }

    @PostMapping("/update/{id}")
    public String updateClass(
            @PathVariable Long id,
            @ModelAttribute FitnessClass fitnessClass,
            @RequestParam Long trainerId) {

        fitnessClassService.updateClass( id, fitnessClass, trainerId);

        return "redirect:/fitness-classes";
    }

    @PostMapping("/delete/{id}")
    public String deleteClass(
            @PathVariable Long id) {

        fitnessClassService.deleteClass(id);

        return "redirect:/fitness-classes";
    }
}