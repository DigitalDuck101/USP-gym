/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.service.MemberService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author svetik
 */

@Controller
@RequestMapping("/admin/student-approvals")
public class StudentApprovalController {

    private final MemberService memberService;


    public StudentApprovalController( MemberService memberService) {

        this.memberService = memberService;
    }


    @GetMapping
    public String pendingStudents( Model model) {

        model.addAttribute("students", memberService.getPendingStudents());

        return "student-approvals";
    }


    @PostMapping("/approve/{id}")
    public String approve(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        memberService.approveStudent(id);

        redirectAttributes.addFlashAttribute( "successMessage", "Student approved." );

        return "redirect:/admin/student-approvals";
    }


    @PostMapping("/reject/{id}")
    public String reject(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        memberService.rejectStudent(id);

        redirectAttributes.addFlashAttribute("successMessage", "Student rejected.");

        return "redirect:/admin/student-approvals";
    }
}
