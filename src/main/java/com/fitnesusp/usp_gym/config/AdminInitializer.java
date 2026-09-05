/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.config;

import com.fitnesusp.usp_gym.model.AppUser;
import com.fitnesusp.usp_gym.model.Role;
import com.fitnesusp.usp_gym.repository.AppUserRepository;

import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;
/**
 *
 * @author svetik
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) {

        if (!appUserRepository.existsByUsername("admin")) {

            AppUser admin = new AppUser(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    Role.ADMIN,
                    true
            );

            appUserRepository.save(admin);

            System.out.println(
                    "Initial ADMIN account created!"
            );
        }
    }
}
