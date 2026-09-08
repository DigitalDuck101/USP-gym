package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.service.RegistrationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


@Controller
public class RegistrationController {

    private final RegistrationService registrationService;


    public RegistrationController(
            RegistrationService registrationService) {

        this.registrationService = registrationService;
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute(
                "member",
                new Member()
        );

        return "register";
    }


    @PostMapping("/register")
    public String register(
            @ModelAttribute Member member,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {


        try {


            /*
             * ==========================================
             * PASSWORD CONFIRMATION
             * ==========================================
             */

            if (!password.equals(confirmPassword)) {

                throw new IllegalStateException(
                        "Passwords do not match."
                );
            }


            /*
             * ==========================================
             * STUDENT EMAIL
             * ==========================================
             */

            if (member.getMemberType() == MemberType.STUDENT) {

                if (member.getStudentId() == null
                        || member.getStudentId().isBlank()) {

                    throw new IllegalStateException(
                            "Student ID is required."
                    );
                }


                String studentId =
                        member.getStudentId()
                                .trim()
                                .toUpperCase();


                /*
                 * Automatically create USP student email
                 */
                String studentEmail =
                        studentId
                                + "@student.usp.ac.fj";


                member.setStudentId(studentId);
                member.setEmail(studentEmail);
            }


            /*
             * ==========================================
             * REGISTER ACCOUNT
             * ==========================================
             */

            Member savedMember =
                    registrationService.register(
                            member,
                            "",
                            password
                    );


            /*
             * Student still requires approval for bookings,
             * but can login immediately.
             */
            if (savedMember.getMemberType()
                    == MemberType.STUDENT) {

                return "redirect:/login?studentRegistered";
            }


            return "redirect:/login?registered";


        } catch (IllegalStateException e) {

            model.addAttribute(
                    "errorMessage",
                    e.getMessage()
            );

            return "register";
        }
    }
}