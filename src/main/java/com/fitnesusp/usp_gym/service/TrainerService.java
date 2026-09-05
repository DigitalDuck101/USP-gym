/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.AppUser;
import com.fitnesusp.usp_gym.model.Role;
import com.fitnesusp.usp_gym.model.Trainer;

import com.fitnesusp.usp_gym.repository.AppUserRepository;
import com.fitnesusp.usp_gym.repository.TrainerRepository;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/**
 *
 * @author svetik
 */

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public TrainerService(
            TrainerRepository trainerRepository,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {

        this.trainerRepository = trainerRepository;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
    }

    public Trainer getTrainerByUsername(String username) {

        return trainerRepository
                .findByAppUserUsername(username)
                .orElseThrow(() -> new RuntimeException( "Trainer account not found"));
    }

    public Trainer createTrainer(
            Trainer trainer,
            String username,
            String password) {

        if (appUserRepository.existsByUsername(username)) {
            throw new RuntimeException( "Username already exists: " + username);
        }

        AppUser account = new AppUser(
                username,
                passwordEncoder.encode(password),
                Role.TRAINER,
                true
        );

        trainer.setAppUser(account);

        return trainerRepository.save(trainer);
    }

    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }
}
