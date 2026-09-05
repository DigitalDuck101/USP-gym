/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.Trainer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author valeriy
 */

public interface TrainerRepository
        extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByAppUserUsername(String username);
}
