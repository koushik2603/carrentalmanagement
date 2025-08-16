// src/components/BookingManagement.jsx
import React, { useState, useEffect } from 'react';
import { getAllBookings } from '../services/bookingService';
import { getAllVehicles } from '../services/carService'; // 1. Import vehicle service
import './BookingManagement.css';

const BookingManagement = ({ isDarkMode }) => {
    // This state will hold the final, combined data
    const [bookings, setBookings] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchAllData = async () => {
            setIsLoading(true);
            setError('');
            try {
                // 2. Fetch all bookings and all vehicles concurrently
                const [bookingsData, vehiclesData] = await Promise.all([
                    getAllBookings(),
                    getAllVehicles()
                ]);

                // 3. Create a Map for quick vehicle lookups by ID
                const vehiclesMap = new Map(vehiclesData.map(v => [v.vehicleID, v]));

                // 4. Combine booking data with the vehicle name
                const enrichedBookings = bookingsData.map(booking => ({
                    ...booking,
                    vehicleName: vehiclesMap.get(booking.vehicleId)?.vehicleName || 'Unknown Vehicle'
                }));

                setBookings(enrichedBookings);

            } catch (err) {
                setError('Failed to fetch data. Please ensure you are logged in as an admin.');
                console.error(err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchAllData();
    }, []);

    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        return new Date(dateString).toLocaleDateString('en-IN', { timeZone: 'UTC' });
    }

    return (
        <div className={`booking-management ${isDarkMode ? 'dark' : ''}`}>
            <h2>All Customer Bookings</h2>
            
            {isLoading && <p>Loading bookings...</p>}
            {error && <p className="error-message">{error}</p>}

            <div className="booking-table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>User Email</th>
                            <th>Vehicle Name</th> {/* <-- 5. Updated table header */}
                            <th>From Date</th>
                            <th>To Date</th>
                            <th>Total Fare</th>
                        </tr>
                    </thead>
                    <tbody>
                        {bookings.length > 0 ? (
                            bookings.map((booking) => (
                                <tr key={booking.bookingId}>
                                    <td>{booking.bookingId}</td>
                                    <td>{booking.userEmail}</td>
                                    <td>{booking.vehicleName}</td> {/* <-- 6. Display vehicle name */}
                                    <td>{formatDate(booking.fromDate)}</td>
                                    <td>{formatDate(booking.toDate)}</td>
                                    <td>₹{typeof booking.totalFare === 'number' ? booking.totalFare.toLocaleString() : '0.00'}</td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="6">No bookings found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default BookingManagement;