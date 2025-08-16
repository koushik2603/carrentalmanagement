// src/components/Confirmation.jsx
import React from 'react';
import './Confirmation.css';

const Confirmation = ({ bookingDetails, onReturnHome, isDarkMode }) => {
    // Safety check: If for some reason bookingDetails are missing, show a generic message.
    if (!bookingDetails) {
        return (
            <div className={`confirmation-page ${isDarkMode ? 'dark' : ''}`}>
                <div className="confirmation-card">
                    <h1>Booking Confirmed!</h1>
                    <p className="subtitle">Your booking details have been processed.</p>
                    <button className="home-button" onClick={onReturnHome}>
                        Book Another Ride
                    </button>
                </div>
            </div>
        );
    }

    // **THE FIX IS HERE:**
    // Destructure the correct property names from the backend response.
    const { vehicle, fromDate, toDate, totalFare, bookingId } = bookingDetails;

    // Helper function to format dates for a more readable display
    const formatDate = (dateString) => {
        if (!dateString) return 'N/A';
        // Use timeZone: 'UTC' to prevent off-by-one day errors
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric', month: 'long', day: 'numeric', timeZone: 'UTC'
        });
    }

    return (
        <div className={`confirmation-page ${isDarkMode ? 'dark' : ''}`}>
            <div className="confirmation-card">
                <div className="success-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" d="M9 12.75L11.25 15 15 9.75M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                </div>
                <h1>Booking Successful!</h1>
                <p className="subtitle">Your adventure is just around the corner. Here are your booking details.</p>
                
                <div className="booking-summary">
                    <div className="summary-item">
                        <span>Booking ID</span>
                        <strong>{bookingId || 'N/A'}</strong>
                    </div>
                    <div className="summary-item">
                        <span>Vehicle</span>
                        {/* Safely access vehicleName from the nested vehicle object */}
                        <strong>{vehicle?.vehicleName || 'N/A'}</strong>
                    </div>
                    <div className="summary-item">
                        <span>Rental Period</span>
                        {/* Use the corrected date properties */}
                        <strong>{formatDate(fromDate)} to {formatDate(toDate)}</strong>
                    </div>
                    <div className="summary-item total">
                        <span>Total Price</span>
                        {/* Use the corrected totalFare property */}
                        <strong>₹{totalFare ? totalFare.toLocaleString() : 'N/A'}</strong>
                    </div>
                </div>

                <button className="home-button" onClick={onReturnHome}>
                    Book Another Ride
                </button>
            </div>
        </div>
    );
};

export default Confirmation;
