/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.fitnesusp.usp_gym.config;

/**
 *
 * @author svetik

import com.fitnesusp.usp_gym.model.Member;
import com.fitnesusp.usp_gym.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DataInitializer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {

        if (memberRepository.count() == 0) {

            Member member = new Member(
                    "S12345678",
                    "Javdish",
                    "Prasad",
                    "javdish@usp.jam.fj",
                    "9999999"
            );

            memberRepository.save(member);

            System.out.println("Test member added to PostgreSQL!");
        }
    }

   /* @Override
    public void run(String... args) throws Exception {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("DATA INITIALIZER IS WORKING");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
   
}
 */
