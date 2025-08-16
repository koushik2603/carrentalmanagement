// src/components/MyBookings.jsx
import React, { useState, useEffect } from 'react';
import { jwtDecode } from 'jwt-decode';
// Import the new cancelBooking function
import { getBookingsByUser, cancelBooking } from '../services/bookingService';
import { getAllVehicles } from '../services/carService';
import './MyBookings.css';

const MyBookings = ({ isDarkMode }) => {
    const [enrichedBookings, setEnrichedBookings] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState('');
    const [message, setMessage] = useState(''); // For success/error messages after an action

    // We'll wrap the fetch logic in a function to call it again after cancellation
    const fetchAndCombineData = async () => {
        setIsLoading(true);
        try {
            const token = localStorage.getItem('token');
            if (!token) throw new Error("Authentication token not found.");
            
            const decodedToken = jwtDecode(token);
            const userEmail = decodedToken.sub;
            if (!userEmail) throw new Error("Email not found in token.");

            const [bookingsData, vehiclesData] = await Promise.all([
                getBookingsByUser(userEmail),
                getAllVehicles()
            ]);

            const vehiclesMap = new Map(vehiclesData.map(v => [v.vehicleID, v]));

            const combinedData = bookingsData.map(booking => ({
                ...booking,
                vehicle: vehiclesMap.get(booking.vehicleId) || null
            }));

            setEnrichedBookings(combinedData);
        } catch (err) {
            setError(err.message || 'Failed to fetch your bookings.');
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchAndCombineData();
    }, []);

    // Handler for the cancel button
    const handleCancelBooking = async (bookingId) => {
        // Show a confirmation dialog
        const isConfirmed = window.confirm(
            "Are you sure you want to cancel this booking? This action cannot be undone."
        );

        if (isConfirmed) {
            try {
                await cancelBooking(bookingId);
                setMessage('Booking cancelled successfully!');
                // Update the UI by removing the cancelled booking from the state
                setEnrichedBookings(currentBookings =>
                    currentBookings.filter(b => b.bookingId !== bookingId)
                );
            } catch (err) {
                setError('Failed to cancel the booking. Please try again.');
            }
            // Clear message after 3 seconds
            setTimeout(() => setMessage(''), 3000);
        }
    };


    return (
        <div className={`my-bookings-page ${isDarkMode ? 'dark' : ''}`}>
            <header className="my-bookings-header">
                <h1>My Bookings</h1>
                <p>Here is a list of all your past and upcoming vehicle rentals.</p>
            </header>

            {isLoading && <p className="loading-message">Loading your bookings...</p>}
            {error && <p className="error-message">{error}</p>}
            {message && <p className="success-message">{message}</p>}

            <div className="bookings-list-container">
                {/* ... (rest of the component remains the same until the map function) ... */}

                {!isLoading && !error && enrichedBookings.length === 0 ? (
                    <p className="loading-message">You have no bookings.</p>
                ) : (
                    enrichedBookings.map((booking) => (
                        <div key={booking.bookingId} className="booking-card-rich">
                            <div className="booking-card-image-wrapper">
                                {/* ... image logic ... */}
                                {booking.vehicle ? (
                                    <img 
                                        src={booking.vehicle.pictureUrl} 
                                        alt={booking.vehicle.vehicleName} 
                                        className="booking-card-image" 
                                    />
                                ) : (
                                    <div className="image-placeholder">No Image</div>
                                )}
                            </div>
                            <div className="booking-card-details">
                                <h3 className="booking-vehicle-name">
                                    {booking.vehicle ? booking.vehicle.vehicleName : 'Vehicle not found'}
                                </h3>
                                <p><strong>Booking ID:</strong> {booking.bookingId}</p>
                                <p><strong>Rental Period:</strong> {new Date(booking.fromDate).toLocaleDateString()} to {new Date(booking.toDate).toLocaleDateString()}</p>
                                <p className="booking-total-fare">
                                    <strong>Total Fare:</strong> ₹{booking.totalFare.toLocaleString()}
                                </p>
                                {/* ADD THE CANCEL BUTTON HERE */}
                                <button
                                    onClick={() => handleCancelBooking(booking.bookingId)}
                                    className="cancel-booking-btn"
                                >
                                    Cancel Booking
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default MyBookings;