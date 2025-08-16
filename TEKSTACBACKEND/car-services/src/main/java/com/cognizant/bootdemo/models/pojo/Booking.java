package com.cognizant.bootdemo.models.pojo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "tblBooking")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private Long bookingId;

    @Column(name = "UserEmail", nullable = false, length = 50)
    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "VehicleID", referencedColumnName = "vehicleID")
    private Vehicle vehicle;

    @Column(name = "FromDate", nullable = false)
    private LocalDate fromDate;

    @Column(name = "ToDate", nullable = false)
    private LocalDate toDate;

    @Column(name = "StartLocation", nullable = false, length = 100)
    private String startLocation;

    @Column(name = "EndLocation", nullable = false, length = 100)
    private String endLocation;

    @Column(name = "TotalFare", nullable = false)
    private Double totalFare;
}
