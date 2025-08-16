package com.cognizant.bootdemo.controller;

import com.cognizant.bootdemo.models.bao.BookingService;
import com.cognizant.bootdemo.models.dao.services.VehicleService;
import com.cognizant.bootdemo.models.dto.BookingRequest;
import com.cognizant.bootdemo.models.dto.BookingResponse;
import com.cognizant.bootdemo.models.pojo.Vehicle;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class VBController {
	private static final Logger log = LoggerFactory.getLogger(VBController.class);

	@Autowired
	public VehicleService VehicleServiceImpl;

	@Autowired
	private BookingService bookingService;

	@Autowired
	private com.cognizant.bootdemo.config.JwtUtil jwtUtil;

	@PostMapping
	public ResponseEntity<BookingResponse> create(
			@RequestHeader(value = "Authorization", required = false) String authHeader,
			@RequestBody BookingRequest req) {

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
		}

		String token = authHeader.substring(7); // strip "Bearer "

		if (!jwtUtil.validateToken(token)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
		}

		String email = jwtUtil.extractEmail(token);
		// ensure the booking uses the email from token (ignore client-provided email)
		log.info("Booking token email extracted: {}", email);
		req.setUserEmail(email);

		BookingResponse resp = bookingService.createBooking(req, email); // ensure service signature supports email
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(bookingService.getBookingById(id));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<BookingResponse>> getByUser(@PathVariable String userId) {
		return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancel(@PathVariable Long id) {
		bookingService.cancelBooking(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<BookingResponse>> getAllBookings() {
	    List<BookingResponse> bookings = bookingService.getAllBookings();
	    return ResponseEntity.ok(bookings);
	}

	
	
	
	
	
	
	
	
	
}
