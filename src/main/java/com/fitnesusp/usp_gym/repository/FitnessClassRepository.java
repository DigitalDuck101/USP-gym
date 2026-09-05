/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.FitnessClass;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
/**
 *
 * @author valeriy
 */

public interface FitnessClassRepository
        extends JpaRepository<FitnessClass, Long> {

    List<FitnessClass>
        findAllByOrderByDateAscStartTimeAsc();

    List<FitnessClass>
        findByTrainer_AppUser_UsernameOrderByDateAscStartTimeAsc(
                String username);
    List<FitnessClass>
        findByDateGreaterThanEqualOrderByDateAscStartTimeAsc(
        LocalDate date
    );    
}
