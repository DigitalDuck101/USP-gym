/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.controller;

/**
 *
 * @author svetik
 */


import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Show all members
    @GetMapping
    public String showMembers(Model model) {

        model.addAttribute( "members", memberService.getAllMembers());

        return "members";
    }

    // Show Add Member form
    @GetMapping("/new")
    public String showAddMemberForm(Model model) {

        model.addAttribute( "member",  new Member());

        return "member-form";
    }

    // Save new member
    @PostMapping
    /*public String saveMember(
            @ModelAttribute("member") Member member) {

        memberService.saveMember(member);

        return "redirect:/members";
    }*/
    
    public String saveMember(
        @ModelAttribute("member") Member member,
        @RequestParam String username,
        @RequestParam String password) {

        memberService.createMember(
            member,
            username,
            password
        );

        return "redirect:/members";
    }
    
    // Delete member
    @PostMapping("/delete/{id}")
    public String deleteMember(
            @PathVariable Long id) {

        memberService.deleteMember(id);

        return "redirect:/members";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditMemberForm(
        @PathVariable Long id, Model model) {

        Member member = memberService.getMemberById(id);

        model.addAttribute("member", member);

        return "member-edit";
    }
    
    @PostMapping("/update/{id}")
    public String updateMember(
        @PathVariable Long id,
        @ModelAttribute("member") Member formMember) {

        Member member = memberService.getMemberById(id);

        member.setStudentId(formMember.getStudentId());
        member.setFirstName(formMember.getFirstName());
        member.setLastName(formMember.getLastName());
        member.setEmail(formMember.getEmail());
        member.setPhone(formMember.getPhone());

        memberService.saveMember(member);

        return "redirect:/members";
    }
}
