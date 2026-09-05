/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fitnesusp.usp_gym.repository;

import com.fitnesusp.usp_gym.model.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author valeriy
 */
public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
