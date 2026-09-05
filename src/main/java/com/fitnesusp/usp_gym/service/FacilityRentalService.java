/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.*;

import com.fitnesusp.usp_gym.repository.FacilityRentalRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author svetik
 */

@Service
public class FacilityRentalService {

    private final FacilityRentalRepository rentalRepository;
    private final SportsFacilityService facilityService;
    private final MemberService memberService;


    public FacilityRentalService(
            FacilityRentalRepository rentalRepository,
            SportsFacilityService facilityService,
            MemberService memberService) {

        this.rentalRepository = rentalRepository;
        this.facilityService = facilityService;
        this.memberService = memberService;
    }


    public List<FacilityRental> getAllRentals() { return rentalRepository.findAllByOrderByStartDateTimeAsc();}


    @Transactional
    public FacilityRental createRental(
            Long renterId,
            Long facilityId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            String purpose) {


        Member renter = memberService.getMemberById( renterId);


        // Only RENTER can rent facilities
        if (renter.getMemberType() != MemberType.RENTER) {

            throw new IllegalStateException( "Selected member is not a renter.");
        }


        SportsFacility facility = facilityService.getFacilityById( facilityId);


        if (!facility.isActive()) {

            throw new IllegalStateException( "This sports facility is currently inactive." );
        }


        if (startDateTime == null || endDateTime == null) {

            throw new IllegalStateException( "Start and end date/time are required.");
        }


        if (!endDateTime.isAfter(startDateTime)) {

            throw new IllegalStateException( "End time must be after start time.");
        }


        if (purpose == null || purpose.isBlank()) {

            throw new IllegalStateException( "Rental purpose is required.");
        }


        List<FacilityRental> conflicts = rentalRepository
                        .findConflictingRentals(
                                facilityId,
                                FacilityRentalStatus.CONFIRMED,
                                startDateTime,
                                endDateTime
                        );


        if (!conflicts.isEmpty()) {

            throw new IllegalStateException( "This sports facility is already rented during the selected time.");
        }


        FacilityRental rental = new FacilityRental();


        rental.setRenter( renter);
        rental.setFacility(facility);
        rental.setStartDateTime( startDateTime);
        rental.setEndDateTime( endDateTime);
        rental.setPurpose( purpose);
        rental.setStatus( FacilityRentalStatus.CONFIRMED);
        rental.setCreatedAt( LocalDateTime.now());


        return rentalRepository.save(rental);
    }
    
    private void validateRentalTimes(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime) {


        if (startDateTime == null || endDateTime == null) {

            throw new IllegalStateException( "Start and end date/time are required.");
        }


        if (!endDateTime.isAfter(startDateTime)) {

            throw new IllegalStateException( "End time must be after start time.");
        }
    }


    @Transactional
    public void cancelRental(Long id) {

        FacilityRental rental =
                rentalRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalStateException( "Rental not found."));


        if (rental.getStatus() == FacilityRentalStatus.CANCELLED) {

            throw new IllegalStateException( "Rental is already cancelled.");
        }

        rental.setStatus( FacilityRentalStatus.CANCELLED);


        rentalRepository.save(rental);
    }


    public List<FacilityRental>
            getRentalsForRenter(String username) {


        Member renter = memberService.getMemberByUsername( username);

        if (renter.getMemberType() != MemberType.RENTER) {

            throw new IllegalStateException("This account is not a renter.");
        }


        return rentalRepository.findByRenter_IdOrderByStartDateTimeAsc( renter.getId());
    }
            
    @Transactional
    public FacilityRental submitRentalRequest(
        String username,
        Long facilityId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String purpose) {


        Member renter =  memberService.getMemberByUsername(username);


        if (renter.getMemberType() != MemberType.RENTER) {

            throw new IllegalStateException( "Only renters can request sports facilities.");
        }


        SportsFacility facility = facilityService.getFacilityById(facilityId);


        if (!facility.isActive()) {

            throw new IllegalStateException( "This sports facility is currently unavailable.");
        }


        validateRentalTimes(  startDateTime, endDateTime);


        if (purpose == null || purpose.isBlank()) {

            throw new IllegalStateException( "Rental purpose is required.");
        }


    /*
     * We check existing CONFIRMED rentals now
     * to avoid obviously impossible requests.
     */
        List<FacilityRental> conflicts = rentalRepository
                    .findConflictingRentals(
                            facilityId,
                            FacilityRentalStatus.CONFIRMED,
                            startDateTime,
                            endDateTime
                    );


        if (!conflicts.isEmpty()) {

            throw new IllegalStateException("This facility is already booked during the selected time.");
        }


        FacilityRental request =  new FacilityRental();


        request.setRenter(renter);
        request.setFacility(facility);
        request.setStartDateTime(startDateTime);
        request.setEndDateTime(endDateTime);
        request.setPurpose(purpose);
        request.setStatus(FacilityRentalStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());


        return rentalRepository.save(request);
    }  
    
    @Transactional
    public void approveRequest(Long rentalId) {

        FacilityRental rental = rentalRepository
                    .findById(rentalId)
                    .orElseThrow(() -> new IllegalStateException("Rental request not found."));


        if (rental.getStatus() != FacilityRentalStatus.PENDING) {

            throw new IllegalStateException( "Only pending requests can be approved." );
        }


        if (!rental.getFacility().isActive()) {

            throw new IllegalStateException("The sports facility is currently inactive.");
        }


    /*
     * VERY IMPORTANT:
     * Check conflict again at approval time.
     */
        List<FacilityRental> conflicts = rentalRepository
                    .findConflictingRentals(
                            rental.getFacility().getId(),
                            FacilityRentalStatus.CONFIRMED,
                            rental.getStartDateTime(),
                            rental.getEndDateTime()
        );


        if (!conflicts.isEmpty()) {

            throw new IllegalStateException(
                "Cannot approve request. "
                + "The facility is already rented "
                + "during the selected time."
            );
        }


        rental.setStatus(FacilityRentalStatus.CONFIRMED);


        rentalRepository.save(rental);
    }
    
    @Transactional
    public void rejectRequest( Long rentalId, String comment) {


        FacilityRental rental = rentalRepository
                    .findById(rentalId)
                    .orElseThrow(() -> new IllegalStateException("Rental request not found."));


        if (rental.getStatus() != FacilityRentalStatus.PENDING) {

            throw new IllegalStateException( "Only pending requests can be rejected." );
        }


        rental.setStatus(FacilityRentalStatus.REJECTED);
        rental.setAdminComment(comment);


        rentalRepository.save(rental);
    }
    
    public List<FacilityRental> getPendingRequests() {

        return rentalRepository.findByStatusOrderByCreatedAtAsc( FacilityRentalStatus.PENDING);
    }
}