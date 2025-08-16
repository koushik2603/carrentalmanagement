package com.cognizant.bootdemo.models.dao.serviceImpl;

import com.cognizant.bootdemo.models.bao.BookingService;
import com.cognizant.bootdemo.models.dto.BookingRequest;
import com.cognizant.bootdemo.models.dto.BookingResponse;
import com.cognizant.bootdemo.models.pojo.Booking;
import com.cognizant.bootdemo.models.pojo.Vehicle;
import com.cognizant.bootdemo.models.repositories.BookingRepository;
import com.cognizant.bootdemo.models.repositories.VehicleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
	
	private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public BookingResponse createBooking(BookingRequest req, String userEmail) {
        // Load Vehicle
        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
            .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + req.getVehicleId()));

        if (req.getStartLocation().trim().equalsIgnoreCase(req.getEndLocation().trim())) {
            throw new IllegalArgumentException("Start and end locations must be different");
        }

        // Prevent overlapping bookings
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            req.getVehicleId(), req.getFromDate(), req.getToDate());
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Vehicle already booked in that period");
        }

        // Check user conflicts
        if (!bookingRepository
                .findUserConflicts(userEmail, req.getFromDate(), req.getToDate())
                .isEmpty()) {
            throw new IllegalStateException("You already have a booking in that period");
        }

        // Validate date range
        long days = ChronoUnit.DAYS.between(req.getFromDate(), req.getToDate());
        if (days <= 0) {
            throw new IllegalArgumentException("Invalid date range: Start Date and End Date must not be same");
        }

        // Calculate total fare
        double totalFare = vehicle.getDailyRate() * days;

        // Persist Booking
        Booking booking = new Booking();
        booking.setUserEmail(userEmail);
        booking.setVehicle(vehicle);
        booking.setFromDate(req.getFromDate());
        booking.setToDate(req.getToDate());
        booking.setStartLocation(req.getStartLocation());
        booking.setEndLocation(req.getEndLocation());
        booking.setTotalFare(totalFare);

        log.info("About to persist booking: vehicle={}, from={}, to={}, userEmail={}",
                booking.getVehicle() == null ? null : booking.getVehicle().getVehicleID(),
                booking.getFromDate(), booking.getToDate(),
                booking.getUserEmail());

        Booking saved = bookingRepository.save(booking);

        // 🔹 Update vehicle availability
        vehicle.setIsAvailable("No");
        vehicleRepository.save(vehicle);

        log.info("Saved booking id: {}", saved.getBookingId());
        log.info("Vehicle {} marked as unavailable", vehicle.getVehicleID());

        return toResponse(saved);
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking b = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
        return toResponse(b);
    }

    @Override
    public List<BookingResponse> getBookingsByUser(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail)
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
    }

/*
    @Override
    public void cancelBooking(Long id) {
        Booking b = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
        bookingRepository.delete(b);
    }
*/
    
    @Override
    public void cancelBooking(Long id) {
        Booking b = bookingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
        
        // Get the associated vehicle
        Vehicle vehicle = b.getVehicle();
        if (vehicle != null) {
            vehicle.setIsAvailable("Yes");
            vehicleRepository.save(vehicle); // Update availability
            log.info("Vehicle {} marked as available", vehicle.getVehicleID());
        }

        // Delete booking
        bookingRepository.delete(b);
        log.info("Booking {} cancelled", id);
    }
    
    private BookingResponse toResponse(Booking b) {
        BookingResponse r = new BookingResponse();
        r.setBookingId(b.getBookingId());
        r.setUserEmail(b.getUserEmail()); // Map userEmail to userId in response
        r.setVehicleId(b.getVehicle().getVehicleID());
        r.setFromDate(b.getFromDate());
        r.setToDate(b.getToDate());
        r.setStartLocation(b.getStartLocation());
        r.setEndLocation(b.getEndLocation());
        r.setTotalFare(b.getTotalFare());
        return r;
    }
    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    

    
    
    
}