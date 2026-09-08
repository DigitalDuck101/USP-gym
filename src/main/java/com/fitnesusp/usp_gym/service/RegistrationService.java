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


        if (member.getMemberType() == null) {

            throw new IllegalStateException(
                    "Account type is required."
            );
        }


        if (password == null || password.length() < 8) {

            throw new IllegalStateException(
                    "Password must contain at least 8 characters."
            );
        }


        String loginUsername;


        /*
         * ==========================================
         * USP STUDENT
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
             * USP Student ID format
             * Example: S11231214
             */
            if (!studentId.matches("S\\d{8}")) {

                throw new IllegalStateException(
                        "Enter a valid USP Student ID, for example S11231214."
                );
            }


            if (memberRepository.existsByStudentId(studentId)) {

                throw new IllegalStateException(
                        "This Student ID is already registered."
                );
            }


            member.setStudentId(studentId);


            /*
             * Student ID becomes login username.
             */
            loginUsername = studentId;


            /*
             * Student registration still requires
             * administrator verification.
             *
             * The student CAN login, but booking
             * remains blocked until APPROVED.
             */
            member.setVerificationStatus(
                    StudentVerificationStatus.PENDING
            );


            /*
             * ==========================================
             * COMMUNITY MEMBER
             * ==========================================
             */
        } else if (member.getMemberType() == MemberType.RENTER) {


            member.setStudentId(null);

            member.setVerificationStatus(
                    StudentVerificationStatus.NOT_REQUIRED
            );


            if (member.getEmail() == null
                    || member.getEmail().isBlank()) {

                throw new IllegalStateException(
                        "Email address is required."
                );
            }


            /*
             * Community member uses email to login.
             */
            loginUsername =
                    member.getEmail()
                            .trim()
                            .toLowerCase();


        } else {

            throw new IllegalStateException(
                    "Invalid account type."
            );
        }


        /*
         * ==========================================
         * EMAIL VALIDATION
         * ==========================================
         */

        if (member.getEmail() == null
                || member.getEmail().isBlank()) {

            throw new IllegalStateException(
                    "Email address is required."
            );
        }


        String email =
                member.getEmail()
                        .trim()
                        .toLowerCase();

        member.setEmail(email);


        if (memberRepository.existsByEmail(email)) {

            throw new IllegalStateException(
                    "This email address is already registered."
            );
        }


        /*
         * ==========================================
         * LOGIN USERNAME VALIDATION
         * ==========================================
         */

        if (appUserRepository.existsByUsername(loginUsername)) {

            throw new IllegalStateException(
                    "An account with this login already exists."
            );
        }


        /*
         * ==========================================
         * CREATE LOGIN ACCOUNT
         * ==========================================
         */

        AppUser account = new AppUser(
                loginUsername,
                passwordEncoder.encode(password),
                Role.MEMBER,
                true
        );


        member.setAppUser(account);


        return memberRepository.save(member);
    }
}