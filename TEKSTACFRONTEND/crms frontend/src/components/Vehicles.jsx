import React, { useState, useEffect } from 'react';
import { getAllVehicles } from '../services/carService';
import GlareHover from './GlareHover';
import './Vehicles.css';

const Vehicles = ({ onSelectVehicle, isDarkMode }) => {
    const [vehicles, setVehicles] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchVehicles = async () => {
            try {
                const data = await getAllVehicles();
                setVehicles(data || []);
            } catch (err) {
                setError(err.message);
            } finally {
                setIsLoading(false);
            }
        };

        fetchVehicles();
    }, []);

    if (isLoading) {
        return <div className={`vehicles-page ${isDarkMode ? 'dark' : ''}`}><p>Loading vehicles...</p></div>;
    }

    if (error) {
        return <div className={`vehicles-page ${isDarkMode ? 'dark' : ''}`}><p>Error: {error}</p></div>;
    }

    return (
        <div className={`vehicles-page ${isDarkMode ? 'dark' : ''}`}>
            <header className="vehicles-header">
                <h1>Select Your Ride</h1>
                <p>Choose from our premium collection of vehicles.</p>
            </header>
            
            <div className="vehicles-grid">
                {vehicles.map(vehicle => (
                    <div key={vehicle.vehicleID} className="vehicle-card">
                        {/* THE FIX IS HERE: Increased transitionDuration for a smoother effect */}
                        <GlareHover glareColor="#ffffff" glareOpacity={0.1} glareSize={400} transitionDuration={1500}>
                            <img 
                                src={vehicle.pictureUrl} 
                                alt={vehicle.vehicleName} 
                                className="vehicle-image"
                            />
                            <div className="vehicle-info">
                                <h3 className="vehicle-name">{vehicle.vehicleName}</h3>
                                <div className="vehicle-details">
                                    <span>{vehicle.model}</span>
                                    <span className={vehicle.isAvailable === 'Yes' ? 'status-available' : 'status-unavailable'}>
                                        {vehicle.isAvailable === 'Yes' ? 'Available' : 'Rented'}
                                    </span>
                                </div>
                                <div className="vehicle-price-container">
                                    <span className="vehicle-price">
                                        ₹{vehicle.dailyRate ? vehicle.dailyRate.toLocaleString() : 'N/A'}
                                        <span className="price-unit"> / day</span>
                                    </span>
                                    <button 
                                        className="book-now-btn" 
                                        onClick={() => onSelectVehicle(vehicle)}
                                        disabled={vehicle.isAvailable !== 'Yes'}
                                    >
                                        Book Now
                                    </button>
                                </div>
                            </div>
                        </GlareHover>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Vehicles;