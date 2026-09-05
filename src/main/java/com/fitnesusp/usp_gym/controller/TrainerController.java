/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Trainer;
import com.fitnesusp.usp_gym.service.TrainerService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author valeriy
 */

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController( TrainerService trainerService) {

        this.trainerService = trainerService;
    }

    @GetMapping
    public String showTrainers(Model model) {

        model.addAttribute( "trainers", trainerService.getAllTrainers());

        return "trainers";
    }

    @GetMapping("/new")
    public String showTrainerForm(Model model) {

        model.addAttribute( "trainer", new Trainer());

        return "trainer-form";
    }

    @PostMapping
    public String createTrainer(
            @ModelAttribute Trainer trainer,
            @RequestParam String username,
            @RequestParam String password) {

        trainerService.createTrainer(
                trainer,
                username,
                password);

        return "redirect:/trainers";
    }

    @GetMapping("/edit/{id}")
    public String editTrainer(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("trainer", trainerService.getTrainerById(id));

        return "trainer-edit";
    }

    @PostMapping("/update/{id}")
    public String updateTrainer(
            @PathVariable Long id,
            @ModelAttribute Trainer formTrainer) {

        Trainer trainer =  trainerService.getTrainerById(id);

        trainer.setStaffId(formTrainer.getStaffId());
        trainer.setFirstName(formTrainer.getFirstName());
        trainer.setLastName(formTrainer.getLastName());
        trainer.setEmail(formTrainer.getEmail());
        trainer.setPhone(formTrainer.getPhone());
        trainer.setSpecialization(formTrainer.getSpecialization());

        trainerService.saveTrainer(trainer);

        return "redirect:/trainers";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrainer(
            @PathVariable Long id) {

        trainerService.deleteTrainer(id);

        return "redirect:/trainers";
    }
}
