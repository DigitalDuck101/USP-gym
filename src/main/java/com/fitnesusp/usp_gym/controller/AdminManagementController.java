/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.AppUser;
import com.fitnesusp.usp_gym.service.AdminService;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author svetik
 */

@Controller
@RequestMapping("/admin/admins")
public class AdminManagementController {


    private final AdminService adminService;


    public AdminManagementController( AdminService adminService) {

        this.adminService = adminService;
    }


    // ==========================================
    // LIST
    // ==========================================

    @GetMapping
    public String showAdmins( Model model, Principal principal) {


        model.addAttribute( "admins", adminService.getAllAdmins());
        model.addAttribute( "currentUsername", principal.getName());


        return "admins";
    }


    // ==========================================
    // CREATE FORM
    // ==========================================

    @GetMapping("/new")
    public String showCreateForm() {

        return "admin-form";
    }


    // ==========================================
    // CREATE
    // ==========================================

    @PostMapping
    public String createAdmin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(
                    defaultValue = "false"
            ) boolean enabled,
            RedirectAttributes redirectAttributes) {


        try {

            adminService.createAdmin(
                    username,
                    password,
                    enabled
            );


            redirectAttributes.addFlashAttribute(
                            "successMessage",
                            "Administrator created successfully."
                    );


            return "redirect:/admin/admins";


        } catch (IllegalStateException e) {


            redirectAttributes.addFlashAttribute( "errorMessage", e.getMessage());
            return "redirect:/admin/admins/new";
        }
    }


    // ==========================================
    // READ ONE
    // ==========================================

    @GetMapping("/{id}")
    public String viewAdmin(
            @PathVariable Long id,
            Model model,
            Principal principal) {


        model.addAttribute( "adminAccount", adminService.getAdminById(id));
        model.addAttribute( "currentUsername", principal.getName());


        return "admin-details";
    }


    // ==========================================
    // EDIT FORM
    // ==========================================

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {


        model.addAttribute( "adminAccount", adminService.getAdminById(id));


        return "admin-edit";
    }


    // ==========================================
    // UPDATE
    // ==========================================

    @PostMapping("/update/{id}")
    public String updateAdmin(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam(
                    required = false
            ) String password,
            @RequestParam(
                    defaultValue = "false"
            ) boolean enabled,
            Principal principal,
            RedirectAttributes redirectAttributes) {


        try {

            adminService.updateAdmin(
                    id,
                    username,
                    password,
                    enabled,
                    principal.getName()
            );


            redirectAttributes.addFlashAttribute( "successMessage", "Administrator updated successfully.");


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }


        return "redirect:/admin/admins";
    }


    // ==========================================
    // DELETE
    // ==========================================

    @PostMapping("/delete/{id}")
    public String deleteAdmin(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {


        try {

            adminService.deleteAdmin( id, principal.getName());

            redirectAttributes.addFlashAttribute("successMessage", "Administrator deleted successfully.");

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }


        return "redirect:/admin/admins";
    }

}
