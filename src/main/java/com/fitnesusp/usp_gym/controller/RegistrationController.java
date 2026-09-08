/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.service.RegistrationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


/**
 *
 * @author svetik
 */

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;


    public RegistrationController( RegistrationService registrationService) {

        this.registrationService = registrationService;
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute( "member",  new Member());

        return "register";
    }


    @PostMapping("/register")
    public String register(
            @ModelAttribute Member member,
            @RequestParam String username,
            @RequestParam String password,
            Model model) {


        try {

            Member savedMember = registrationService.register(
                            member,
                            username,
                            password
                    );


            if (savedMember.getMemberType() == MemberType.STUDENT) {

                return "redirect:/login?pending";
            }


            return "redirect:/login?registered";


        } catch (IllegalStateException e) {

            model.addAttribute("errorMessage", e.getMessage());

            return "register";
        }
    }
}
