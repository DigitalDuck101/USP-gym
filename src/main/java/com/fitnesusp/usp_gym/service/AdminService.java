/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.AppUser;
import com.fitnesusp.usp_gym.model.Role;
import com.fitnesusp.usp_gym.repository.AppUserRepository;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author svetik
 */

@Service
public class AdminService {


    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;


    public AdminService( AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;

        this.passwordEncoder = passwordEncoder;
    }


    // ==========================================
    // READ ALL
    // ==========================================

    public List<AppUser> getAllAdmins() {

        return appUserRepository.findByRoleOrderByUsernameAsc(Role.ADMIN);
    }


    // ==========================================
    // READ ONE
    // ==========================================

    public AppUser getAdminById( Long id) {

        AppUser admin = appUserRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalStateException( "Administrator not found."));


        if (admin.getRole() != Role.ADMIN) {

            throw new IllegalStateException("This user is not an administrator.");
        }


        return admin;
    }


    // ==========================================
    // CREATE
    // ==========================================

    @Transactional
    public AppUser createAdmin(
            String username,
            String password,
            boolean enabled) {


        username = validateUsername(username); 
        validatePassword(password);


        if (appUserRepository.existsByUsername(username)) {

            throw new IllegalStateException( "Username already exists.");
        }


        AppUser admin = new AppUser( 
                        username, 
                        passwordEncoder.encode( password),
                        Role.ADMIN,
                        enabled
                );


        return appUserRepository.save( admin);
    }


    // ==========================================
    // UPDATE
    // ==========================================

    @Transactional
    public AppUser updateAdmin(
            Long id,
            String username,
            String newPassword,
            boolean enabled,
            String currentUsername) {


        AppUser admin =  getAdminById(id);
        username = validateUsername(username);


        /*
         * Don't allow the currently logged-in
         * administrator to change their own
         * username while logged in.
         */

        if (admin.getUsername().equals(currentUsername) && !admin.getUsername().equals(username)) {

            throw new IllegalStateException( "You cannot change your own username while logged in.");
        }


        /*
         * Check username uniqueness.
         */

        appUserRepository
                .findByUsername(username)
                .ifPresent(existingUser -> {

                    if (!existingUser.getId().equals(admin.getId())) {

                        throw new IllegalStateException("Username already exists.");
                    }
                });


        /*
         * Don't allow admin to disable
         * their own account.
         */

        if (admin.getUsername().equals(currentUsername) && !enabled) {

            throw new IllegalStateException( "You cannot disable your own administrator account.");
        }


        admin.setUsername( username);
        admin.setEnabled( enabled);


        /*
         * Empty password means:
         * keep the old password.
         */

        if (newPassword != null && !newPassword.isBlank()) {

            validatePassword( newPassword);

            admin.setPasswordHash( passwordEncoder.encode( newPassword));
        }


        return appUserRepository.save( admin);
    }


    // ==========================================
    // DELETE
    // ==========================================

    @Transactional
    public void deleteAdmin( Long id, String currentUsername) {


        AppUser admin = getAdminById(id);


        /*
         * Admin cannot delete themselves.
         */

        if (admin.getUsername().equals(currentUsername)) {

            throw new IllegalStateException( "You cannot delete your own administrator account.");
        }


        /*
         * Always keep at least one ADMIN.
         */

        long adminCount = appUserRepository.countByRole( Role.ADMIN );


        if (adminCount <= 1) {

            throw new IllegalStateException( "The last administrator account cannot be deleted.");
        }


        appUserRepository.delete( admin);
    }


    // ==========================================
    // VALIDATION
    // ==========================================

    private String validateUsername( String username) {


        if (username == null || username.isBlank()) {

            throw new IllegalStateException( "Username is required.");
        }


        username =  username.trim();


        if (username.length() < 3) {

            throw new IllegalStateException( "Username must contain at least 3 characters." );
        }


        return username;
    }


    private void validatePassword( String password) {


        if (password == null || password.isBlank()) {

            throw new IllegalStateException( "Password is required." );
        }


        if (password.length() < 6) {

            throw new IllegalStateException( "Password must contain at least 6 characters." );
        }
    }

}
