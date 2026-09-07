package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.model.MembershipStatus;
import com.fitnesusp.usp_gym.service.MemberService;

import java.time.LocalDate;

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
@RequestMapping("/admin/members")
public class AdminMembershipController {


    private final MemberService memberService;


    public AdminMembershipController(
            MemberService memberService) {

        this.memberService = memberService;
    }



    // =====================================================
    // SHOW MEMBERSHIP EDIT FORM
    // =====================================================

    @GetMapping("/{id}/membership")
    public String showMembershipForm(
            @PathVariable Long id,
            Model model) {


        Member member =
                memberService.getMemberById(id);


        model.addAttribute(
                "member",
                member
        );


        model.addAttribute(
                "membershipStatuses",
                MembershipStatus.values()
        );


        return "member-membership-edit";
    }



    // =====================================================
    // UPDATE MEMBERSHIP
    // =====================================================

    @PostMapping("/{id}/membership")
    public String updateMembership(
            @PathVariable Long id,

            @RequestParam
            MembershipStatus membershipStatus,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate membershipExpiryDate,

            RedirectAttributes redirectAttributes) {


        try {

            Member member =
                    memberService.getMemberById(id);


            // =====================================================
            // VALIDATE ACTIVE MEMBERSHIP
            // =====================================================

            if (membershipStatus
                    == MembershipStatus.ACTIVE
                    &&
                    membershipExpiryDate != null
                    &&
                    membershipExpiryDate.isBefore(
                            LocalDate.now()
                    )) {

                throw new IllegalStateException(
                        "An active membership cannot have an expiry date in the past."
                );
            }



            // =====================================================
            // NOT REQUIRED DOES NOT NEED EXPIRY DATE
            // =====================================================

            if (membershipStatus
                    == MembershipStatus.NOT_REQUIRED) {

                membershipExpiryDate = null;
            }



            // =====================================================
            // SAVE MEMBERSHIP DETAILS
            // =====================================================

            member.setMembershipStatus(
                    membershipStatus
            );


            member.setMembershipExpiryDate(
                    membershipExpiryDate
            );


            memberService.saveMember(
                    member
            );


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Membership information updated successfully."
            );


        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );
        }


        return "redirect:/members";
    }
}