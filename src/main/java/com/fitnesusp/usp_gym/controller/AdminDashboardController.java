package com.fitnesusp.usp_gym.controller;

import com.fitnesusp.usp_gym.model.MemberType;
import com.fitnesusp.usp_gym.model.StudentVerificationStatus;
import com.fitnesusp.usp_gym.model.FacilityRentalStatus;
import com.fitnesusp.usp_gym.repository.FacilityRentalRepository;
import com.fitnesusp.usp_gym.repository.FitnessClassRepository;
import com.fitnesusp.usp_gym.repository.MemberRepository;
import com.fitnesusp.usp_gym.repository.SportsFacilityRepository;
import com.fitnesusp.usp_gym.repository.TrainerRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final SportsFacilityRepository sportsFacilityRepository;
    private final FitnessClassRepository fitnessClassRepository;
    private final FacilityRentalRepository facilityRentalRepository;


    public AdminDashboardController(
            MemberRepository memberRepository,
            TrainerRepository trainerRepository,
            SportsFacilityRepository sportsFacilityRepository,
            FitnessClassRepository fitnessClassRepository,
            FacilityRentalRepository facilityRentalRepository) {

        this.memberRepository = memberRepository;
        this.trainerRepository = trainerRepository;
        this.sportsFacilityRepository = sportsFacilityRepository;
        this.fitnessClassRepository = fitnessClassRepository;
        this.facilityRentalRepository = facilityRentalRepository;
    }


    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        // Total records
        long totalMembers =
                memberRepository.count();

        long totalTrainers =
                trainerRepository.count();

        long totalFacilities =
                sportsFacilityRepository.count();

        long totalFitnessClasses =
                fitnessClassRepository.count();


        // Send values to dashboard
        model.addAttribute(
                "totalMembers",
                totalMembers
        );

        model.addAttribute(
                "totalTrainers",
                totalTrainers
        );

        model.addAttribute(
                "totalFacilities",
                totalFacilities
        );

        model.addAttribute(
                "totalFitnessClasses",
                totalFitnessClasses
        );


        // Temporary values
        int pendingStudents =
                memberRepository
                        .findByMemberTypeAndVerificationStatus(
                                MemberType.STUDENT,
                                StudentVerificationStatus.PENDING
                        )
                        .size();

        int pendingRentals =
                facilityRentalRepository
                        .findByStatusOrderByCreatedAtAsc(
                                FacilityRentalStatus.PENDING
                        )
                        .size();

        model.addAttribute(
                "pendingStudents",
                pendingStudents
        );

        model.addAttribute(
                "pendingRentals",
                pendingRentals
        );

        return "admin-dashboard";
    }
}