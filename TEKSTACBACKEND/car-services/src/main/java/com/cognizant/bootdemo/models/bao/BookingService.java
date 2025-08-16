package com.cognizant.bootdemo.models.bao;

import com.cognizant.bootdemo.models.dto.BookingRequest;
import com.cognizant.bootdemo.models.dto.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, String userEmail);
    BookingResponse getBookingById(Long id);
    List<BookingResponse> getBookingsByUser(String userEmail);
    void cancelBooking(Long id);
    List<BookingResponse> getAllBookings();

}