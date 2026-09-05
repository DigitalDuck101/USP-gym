/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 *
 * @author svetik
 */

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication) {

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                        );

        if (isAdmin) {
            return "redirect:/members";
        }


        boolean isMember =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_MEMBER")
                        );

        if (isMember) {
            return "redirect:/profile";
        }


        boolean isTrainer =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_TRAINER")
                        );

        if (isTrainer) {
            return "redirect:/trainer/profile";
        }


        return "redirect:/login";
    }
}