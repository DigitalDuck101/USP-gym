/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fitnesusp.usp_gym.service;

import com.fitnesusp.usp_gym.model.Booking;
import com.fitnesusp.usp_gym.model.BookingStatus;
import com.fitnesusp.usp_gym.model.FitnessClass;
import com.fitnesusp.usp_gym.model.Member;

import com.fitnesusp.usp_gym.repository.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author svetik
 */

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final MemberService memberService;
    private final FitnessClassService fitnessClassService;


    public BookingService(
            BookingRepository bookingRepository,
            MemberService memberService,
            FitnessClassService fitnessClassService) {

        this.bookingRepository = bookingRepository;
        this.memberService = memberService;
        this.fitnessClassService = fitnessClassService;
    }


    @Transactional
    /*public Booking bookClass(
        String username,
        Long fitnessClassId) {

        Member member =
            memberService.getMemberByUsername(
                    username
            );

        FitnessClass fitnessClass =
            fitnessClassService.getClassById(
                    fitnessClassId
            );


        Optional<Booking> existingBooking =
            bookingRepository
                    .findByMember_IdAndFitnessClass_Id(
                            member.getId(),
                            fitnessClassId
                    );


        if (existingBooking.isPresent()
            && existingBooking.get().getStatus()
            == BookingStatus.BOOKED) {

            throw new IllegalStateException(
                "You have already booked this class."
            );
        }


        long bookedPlaces =
            bookingRepository
                    .countByFitnessClass_IdAndStatus(
                            fitnessClassId,
                            BookingStatus.BOOKED
                    );


        if (bookedPlaces
            >= fitnessClass.getCapacity()) {

            throw new IllegalStateException(
                "This fitness class is full."
            );
        }


        if (existingBooking.isPresent()) {

            Booking booking = existingBooking.get();

            booking.setStatus(BookingStatus.BOOKED);

            booking.setBookingDate(LocalDateTime.now());

            return bookingRepository.save(booking);
        }


        Booking booking = new Booking();

        booking.setMember(member);

        booking.setFitnessClass(fitnessClass);

        booking.setBookingDate(LocalDateTime.now());

        booking.setStatus(BookingStatus.BOOKED);

        return bookingRepository.save(booking);
    }*/
    
    public Booking bookClass(
        String username,
        Long fitnessClassId) {

        // Find logged-in Member
        Member member = memberService.getMemberByUsername(username);


        // Find selected Fitness Class
        FitnessClass fitnessClass = fitnessClassService.getClassById(fitnessClassId);
        
        LocalDateTime classStart = LocalDateTime.of(fitnessClass.getDate(), fitnessClass.getStartTime());


        if (classStart.isBefore( LocalDateTime.now())) {
  
            throw new IllegalStateException("You cannot book a fitness class that has already started.");
        }


        // Check if this Member already has
        // a booking for this exact class
        Optional<Booking> existingBooking = bookingRepository
                    .findByMember_IdAndFitnessClass_Id(member.getId(), fitnessClassId);


        // Already actively booked
        if (existingBooking.isPresent()
            && existingBooking.get().getStatus()
            == BookingStatus.BOOKED) {

            throw new IllegalStateException("You have already booked this class.");
        }


        // TIME CONFLICT CHECK
        if (hasTimeConflict(
            member,
            fitnessClass)) {

            throw new IllegalStateException("Booking conflict! You already have another class at this time.");
        }


        // Count active bookings for capacity
        long bookedPlaces = bookingRepository
                    .countByFitnessClass_IdAndStatus(fitnessClassId, BookingStatus.BOOKED);


        if (bookedPlaces >= fitnessClass.getCapacity()) {

            throw new IllegalStateException("This fitness class is full.");
        }


        // If this class was previously cancelled,
        // reactivate existing booking
        if (existingBooking.isPresent()) {

            Booking booking = existingBooking.get();

            booking.setStatus(BookingStatus.BOOKED);

            booking.setBookingDate(LocalDateTime.now());

            return bookingRepository.save(booking);
        }


        // Otherwise create a new booking
        Booking booking = new Booking();

        booking.setMember(member);

        booking.setFitnessClass(fitnessClass);

        booking.setBookingDate(LocalDateTime.now());

        booking.setStatus(BookingStatus.BOOKED);


        return bookingRepository.save(booking);
    }

    public List<Booking>
            getBookingsForMember(String username) {

        Member member = memberService.getMemberByUsername(username);

        return bookingRepository
                .findByMember_IdOrderByFitnessClass_DateAscFitnessClass_StartTimeAsc(member.getId());
    }
            
    @Transactional
    public Booking cancelBooking( String username, Long bookingId) {

        Member member = memberService.getMemberByUsername(username);

        Booking booking = bookingRepository
                    .findByIdAndMember_Id( bookingId, member.getId())
                    .orElseThrow(() -> new IllegalStateException("Booking not found."));

        if (booking.getStatus() == BookingStatus.CANCELLED) {

           throw new IllegalStateException("This booking is already cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingRepository.save(booking);
    }  
    
    private boolean hasTimeConflict( Member member, FitnessClass newClass) {

        List<Booking> bookings = bookingRepository
                    .findByMember_IdAndStatusAndFitnessClass_Date(
                            member.getId(),
                            BookingStatus.BOOKED,
                            newClass.getDate()
                    );

        for (Booking booking : bookings) {

            FitnessClass existingClass = booking.getFitnessClass();

            boolean overlaps = existingClass.getStartTime().isBefore(newClass.getEndTime())
                             && existingClass.getEndTime().isAfter(newClass.getStartTime());

            if (overlaps) { return true; }
        }

        return false;
    }
}
