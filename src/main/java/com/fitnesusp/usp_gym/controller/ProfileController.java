/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;
import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.service.MemberService;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 *
 * @author svetik
 */
@Controller
public class ProfileController {

    private final MemberService memberService;

    public ProfileController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/profile")
    public String profile(
            Principal principal,
            Model model) {

        Member member = memberService.getMemberByUsername(principal.getName());
        model.addAttribute("member", member);

        return "member-profile";
    }
}
