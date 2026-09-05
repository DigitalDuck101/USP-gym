/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.SportsFacility;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author valeriy
 */

public interface SportsFacilityRepository
        extends JpaRepository<SportsFacility, Long> {


    List<SportsFacility>
        findAllByOrderByNameAsc();


    List<SportsFacility>
        findByActiveTrueOrderByNameAsc();
}
