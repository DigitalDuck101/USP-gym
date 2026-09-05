/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.FitnessClass;
import com.fitnesusp.usp_gym.model.Trainer;
import com.fitnesusp.usp_gym.repository.FitnessClassRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
/**
 *
 * @author svetik
 */

@Service
public class FitnessClassService {

    private final FitnessClassRepository fitnessClassRepository;
    private final TrainerService trainerService;

    public FitnessClassService( FitnessClassRepository fitnessClassRepository, TrainerService trainerService) {

        this.fitnessClassRepository = fitnessClassRepository;
        this.trainerService = trainerService;
    }

    public List<FitnessClass> getAllClasses() {
        return fitnessClassRepository.findAllByOrderByDateAscStartTimeAsc();
    }

    public FitnessClass getClassById(Long id) {

        return fitnessClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fitness class not found"));
    }

    public FitnessClass createClass( FitnessClass fitnessClass, Long trainerId) {

        validateClass(fitnessClass);

        Trainer trainer = trainerService.getTrainerById(trainerId);
        fitnessClass.setTrainer(trainer);

        return fitnessClassRepository.save(fitnessClass);
    }

    public FitnessClass updateClass(
            Long id,
            FitnessClass formClass,
            Long trainerId) {

        validateClass(formClass);

        FitnessClass fitnessClass =  getClassById(id);

        Trainer trainer = trainerService.getTrainerById(trainerId);

        fitnessClass.setName(formClass.getName());
        fitnessClass.setDate(formClass.getDate());
        fitnessClass.setStartTime(formClass.getStartTime());
        fitnessClass.setEndTime(formClass.getEndTime());
        fitnessClass.setCapacity(formClass.getCapacity());
        fitnessClass.setTrainer(trainer);

        return fitnessClassRepository.save(fitnessClass);
    }

    public void deleteClass(Long id) {
        fitnessClassRepository.deleteById(id);
    }

    public List<FitnessClass>
            getClassesForTrainer( String username) {

        return fitnessClassRepository.findByTrainer_AppUser_UsernameOrderByDateAscStartTimeAsc( username);
    }

    private void validateClass( FitnessClass fitnessClass) {

        if (fitnessClass.getStartTime() == null || fitnessClass.getEndTime() == null) {

            throw new IllegalArgumentException( "Start and end time are required"); }

        if (!fitnessClass.getEndTime().isAfter(fitnessClass.getStartTime())) {

            throw new IllegalArgumentException( "End time must be after start time");
        }

        if (fitnessClass.getCapacity() == null || fitnessClass.getCapacity() <= 0) {

            throw new IllegalArgumentException( "Capacity must be greater than zero");
        }
    }
    
    public List<FitnessClass> getUpcomingClasses() {

        return fitnessClassRepository.findByDateGreaterThanEqualOrderByDateAscStartTimeAsc(LocalDate.now());
    }
}
