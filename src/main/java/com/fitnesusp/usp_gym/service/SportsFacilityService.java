/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.SportsFacility;
import com.fitnesusp.usp_gym.repository.SportsFacilityRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author svetik
 */

@Service
public class SportsFacilityService {

    private final SportsFacilityRepository facilityRepository;


    public SportsFacilityService( SportsFacilityRepository facilityRepository) {

        this.facilityRepository = facilityRepository;
    }


    public List<SportsFacility> getAllFacilities() {

        return facilityRepository.findAllByOrderByNameAsc();
    }


    public List<SportsFacility> getActiveFacilities() {

        return facilityRepository.findByActiveTrueOrderByNameAsc();
    }


    public SportsFacility getFacilityById(Long id) {

        return facilityRepository
                .findById(id)
                .orElseThrow(() -> new IllegalStateException( "Sports facility not found."));
    }


    public SportsFacility saveFacility( SportsFacility facility) {

        validateFacility(facility);

        return facilityRepository.save(facility);
    }


    @Transactional
    public void updateFacility( Long id, SportsFacility formFacility) {

        SportsFacility facility = getFacilityById(id);
        validateFacility(formFacility);
        facility.setName( formFacility.getName());
        facility.setType( formFacility.getType());
        facility.setLocation( formFacility.getLocation());
        facility.setDescription( formFacility.getDescription());


        facilityRepository.save(facility);
    }


    @Transactional
    public void toggleActive(Long id) {

        SportsFacility facility = getFacilityById(id);

        facility.setActive(!facility.isActive());

        facilityRepository.save(facility);
    }


    private void validateFacility( SportsFacility facility) {

        if (facility.getName() == null || facility.getName().isBlank()) {

            throw new IllegalStateException( "Facility name is required.");
        }


        if (facility.getType() == null) {

            throw new IllegalStateException( "Facility type is required.");
        }
    }
}