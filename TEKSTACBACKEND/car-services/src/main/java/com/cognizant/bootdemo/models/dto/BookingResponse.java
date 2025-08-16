package com.cognizant.bootdemo.models.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingResponse {
    private Long bookingId;
    private String userEmail;
    private Integer vehicleId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String startLocation;
    private String endLocation;
    private Double totalFare;
}
