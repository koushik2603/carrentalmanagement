package com.cognizant.bootdemo.models.repositories;

import com.cognizant.bootdemo.models.pojo.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	// simple finder (no change required, but kept explicit)
	List<Booking> findByUserEmail(String userEmail);

	// check vehicle conflicts (explicit @Param avoids reliance on parameter names)
	@Query("SELECT b FROM Booking b WHERE b.vehicle.vehicleID = :vehicleId "
			+ "AND (b.fromDate <= :toDate AND b.toDate >= :fromDate)")
	List<Booking> findConflictingBookings(@Param("vehicleId") Integer vehicleId, @Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);

	// user conflicts — Booking has userEmail field, so query on b.userEmail
	@Query("""
			SELECT b FROM Booking b
			 WHERE b.userEmail = :userEmail
			   AND (b.fromDate <= :toDate AND b.toDate >= :fromDate)
			""")
	List<Booking> findUserConflicts(@Param("userEmail") String userEmail, @Param("fromDate") LocalDate fromDate,
			@Param("toDate") LocalDate toDate);
}
