/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.AppUser;
import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.model.Role;
import com.fitnesusp.usp_gym.model.StudentVerificationStatus;

import com.fitnesusp.usp_gym.repository.AppUserRepository;
import com.fitnesusp.usp_gym.repository.MemberRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author svetik
 */

@Service
public class RegistrationService {

    private final MemberRepository memberRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;


    public RegistrationService(
            MemberRepository memberRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        this.memberRepository = memberRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Member register(
            Member member,
            String username,
            String password) {


        if (username == null || username.isBlank()) {

            throw new IllegalStateException( "Username is required.");
        }

        if (password == null || password.isBlank()) {

            throw new IllegalStateException( "Password is required.");
        }

        if (member.getFirstName() == null || member.getFirstName().isBlank()) {

            throw new IllegalStateException( "First name is required.");
        }

        if (member.getLastName() == null || member.getLastName().isBlank()) {

            throw new IllegalStateException( "Last name is required.");
        }

        if (member.getEmail() == null || member.getEmail().isBlank()) {

            throw new IllegalStateException( "Email is required.");
        }

        if (!member.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {

            throw new IllegalStateException( "A valid email address is required.");
        }

        if (appUserRepository.existsByUsername(username)) {

            throw new IllegalStateException( "Username already exists.");
        }


        if (memberRepository.existsByEmail( member.getEmail())) {

            throw new IllegalStateException( "Email already exists.");
        }


        boolean accountEnabled;


        if (null == member.getMemberType()) {
            
            throw new IllegalStateException( "Member type is required.");
        } else switch (member.getMemberType()) {
            case STUDENT -> {
                if (member.getStudentId() == null || member.getStudentId().isBlank()) {
                    
                    throw new IllegalStateException( "Student ID is required for students.");
                }   if (memberRepository.existsByStudentId( member.getStudentId())) {
                    
                    throw new IllegalStateException( "Student ID already exists.");
                }   // Student cannot login yet
                member.setVerificationStatus( StudentVerificationStatus.PENDING );
                accountEnabled = false;
            }
            case RENTER -> {
                // Renter does not have Student ID
                member.setStudentId(null);
                member.setVerificationStatus(StudentVerificationStatus.NOT_REQUIRED);  
                accountEnabled = true;
            }
            default -> throw new IllegalStateException( "Member type is required.");
        }


        AppUser account = new AppUser(
                        username,
                        passwordEncoder.encode(password),
                        Role.MEMBER,
                        accountEnabled
                );
        member.setAppUser(account);


        return memberRepository.save(member);
    }
}
