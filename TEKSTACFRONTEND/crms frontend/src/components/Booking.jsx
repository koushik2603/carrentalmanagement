// src/components/Booking.jsx

import React, { useState } from 'react';
import { createBooking } from '../services/carService';
import './Booking.css';

const Booking = ({ vehicle, onConfirmBooking, onGoBack, isDarkMode }) => {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [startLocation, setStartLocation] = useState('Vijayawada');
    const [endLocation, setEndLocation] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    if (!vehicle) {
        return <div className={`booking-page ${isDarkMode ? 'dark' : ''}`}><p>Loading booking details...</p></div>;
    }

    const calculateTotalPrice = () => {
        if (startDate && endDate && vehicle.dailyRate) {
            const start = new Date(startDate);
            const end = new Date(endDate);
            if (end >= start) {
                // Add 1 to include both start and end dates in the rental period
                const diffTime = Math.abs(end - start);
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1; 
                return diffDays * vehicle.dailyRate;
            }
        }
        return 0;
    };

    const totalPrice = calculateTotalPrice();

    const handleBookingSubmit = async (e) => {
        e.preventDefault();
        if (!startDate || !endDate) {
            setError('Please select both a start and end date.');
            return;
        }
        if (new Date(endDate) < new Date(startDate)) {
            setError('End date cannot be before the start date.');
            return;
        }
        
        setError('');
        setIsLoading(true);

        try {
            const bookingData = {
                vehicleId: vehicle.vehicleID,
                fromDate: startDate,
                toDate: endDate,
                totalFare: totalPrice,
                startLocation: startLocation,
                endLocation: endLocation
            };

            const confirmedBooking = await createBooking(bookingData);

            onConfirmBooking({
                ...confirmedBooking,
                vehicle: vehicle,
            });

        } catch (err) {
            // **THE FIX IS HERE**
            // Check if the error response from the server has data and use it.
            // Otherwise, fall back to the generic message.
            if (err.response && err.response.data) {
                // The 'data' can be a string or an object like { message: '...' }
                const serverError = typeof err.response.data === 'string' 
                    ? err.response.data 
                    : err.response.data.message;
                setError(serverError || 'Booking failed. Please try again.');
            } else {
                setError(err.message);
            }
        } finally {
            setIsLoading(false);
        }
    };
  
    const getTodayString = () => {
        const today = new Date();
        const offset = today.getTimezoneOffset();
        const localDate = new Date(today.getTime() - (offset*60*1000));
        return localDate.toISOString().split('T')[0];
    }

    return (
        <div className={`booking-page ${isDarkMode ? 'dark' : ''}`}>
            <div className="booking-container">
                <div className="booking-vehicle-summary">
                    <img src={vehicle.pictureUrl} alt={vehicle.vehicleName} />
                    <h2>{vehicle.vehicleName}</h2>
                    <p>{vehicle.model}</p>
                    <div className="summary-price">
                        ₹{vehicle.dailyRate ? vehicle.dailyRate.toLocaleString() : 'N/A'} / day
                    </div>
                    <button className="back-button" onClick={onGoBack}>
                        &larr; Choose a different car
                    </button>
                </div>

                <div className="booking-form-section">
                    <h2>Finalize Your Booking</h2>
                    <form onSubmit={handleBookingSubmit}>
                        <div className="form-row">
                            <div className="form-group">
                                <label htmlFor="start-location">From</label>
                                <input type="text" id="start-location" value={startLocation} onChange={(e) => setStartLocation(e.target.value)} required />
                            </div>
                            <div className="form-group">
                                <label htmlFor="end-location">To</label>
                                <input type="text" id="end-location" placeholder="e.g., Machilipatnam" value={endLocation} onChange={(e) => setEndLocation(e.target.value)} required />
                            </div>
                        </div>
                        <div className="form-row">
                            <div className="form-group">
                                <label htmlFor="start-date">Start Date</label>
                                <input type="date" id="start-date" value={startDate} min={getTodayString()} onChange={(e) => setStartDate(e.target.value)} required />
                            </div>
                            <div className="form-group">
                                <label htmlFor="end-date">End Date</label>
                                <input type="date" id="end-date" value={endDate} min={startDate || getTodayString()} onChange={(e) => setEndDate(e.target.value)} required />
                            </div>
                        </div>
                        
                        {error && <p className="error-message">{error}</p>}

                        {totalPrice > 0 && (
                            <div className="total-price-section">
                                <h3>Total Price</h3>
                                <p>₹{totalPrice.toLocaleString()}</p>
                            </div>
                        )}
                        
                        <button type="submit" className="confirm-booking-btn" disabled={isLoading}>
                            {isLoading ? 'Confirming...' : 'Confirm & Book'}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Booking;