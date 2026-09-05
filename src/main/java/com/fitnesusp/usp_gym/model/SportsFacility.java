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
@Table(name = "sports_facilities")
public class SportsFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    private String location;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean active = true;


    public SportsFacility() {
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}