/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.model;

import jakarta.persistence.*;
/**
 *
 * @author svetik
 */

@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "staff_id", unique = true, nullable = false)
    private String staffId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String specialization;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "app_user_id",
            unique = true
    )
    private AppUser appUser;

    public Trainer() {
    }

    public Trainer(String staffId,
                   String firstName,
                   String lastName,
                   String email,
                   String phone,
                   String specialization) {

        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
