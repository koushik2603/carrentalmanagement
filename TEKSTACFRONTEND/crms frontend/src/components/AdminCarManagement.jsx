// src/components/AdminCarManagement.jsx
import React, { useState, useEffect } from 'react';
import { getAllVehicles, addCar, updateCar, deleteCar } from '../services/carService';
import './AdminCarManagement.css';

const AdminCarManagement = ({ isDarkMode }) => {
    const [cars, setCars] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [currentCar, setCurrentCar] = useState(null);

    const fetchCars = async () => {
        setIsLoading(true);
        setError('');
        try {
            const data = await getAllVehicles();
            setCars(data || []);
        } catch (err) {
            setError('Failed to fetch cars. The server might be down.');
            console.error(err);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchCars();
    }, []);

    const handleOpenModal = (car = null) => {
        setCurrentCar(car);
        setIsModalOpen(true);
        setError('');
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setCurrentCar(null);
    };

    const handleDelete = async (carId) => {
        if (window.confirm('Are you sure you want to delete this car?')) {
            try {
                await deleteCar(carId);
                fetchCars();
            } catch (err) {
                setError('Failed to delete car.');
                console.error(err);
            }
        }
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const carData = Object.fromEntries(formData.entries());
        
        // Ensure numeric fields are correctly typed
        carData.dailyRate = parseInt(carData.dailyRate, 10);
        if (carData.vehicleID) {
            carData.vehicleID = parseInt(carData.vehicleID, 10);
        }

        try {
            if (currentCar) {
                // For updates, use the ID of the car being edited
                await updateCar(currentCar.vehicleID, carData);
            } else {
                // For new cars, the backend will assign an ID
                await addCar(carData);
            }
            fetchCars();
            handleCloseModal();
        } catch (err) {
            const errorMessage = err.response?.data?.message || err.response?.data || 'An unexpected error occurred.';
            setError(`Failed to save car: ${errorMessage}`);
            console.error(err);
        }
    };
    
    return (
        <div className={`admin-car-management ${isDarkMode ? 'dark' : ''}`}>
            <div className="header-with-button">
                <h2>Manage Cars</h2>
                <button onClick={() => handleOpenModal()} className="add-car-btn">
                    + Add New Car
                </button>
            </div>
            
            {error && !isModalOpen && <p className="error-message">{error}</p>}

            <div className="car-table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Brand & Model</th>
                            <th>Price/Day</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {isLoading ? (
                            <tr><td colSpan="5">Loading cars...</td></tr>
                        ) : cars.length > 0 ? (
                            cars.map((car) => (
                                <tr key={car.vehicleID}>
                                    <td><img src={car.pictureUrl} alt={`${car.brand} ${car.model}`} className="car-thumbnail" /></td>
                                    <td>{car.vehicleName}</td>
                                    <td>{`${car.brand} ${car.model}`}</td>
                                    <td>₹{car.dailyRate}</td>
                                    <td>
                                        <button className="action-btn edit" onClick={() => handleOpenModal(car)}>Edit</button>
                                        <button className="action-btn delete" onClick={() => handleDelete(car.vehicleID)}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr><td colSpan="5" style={{ textAlign: 'center' }}>No Vehicle Exist</td></tr>
                        )}
                    </tbody>
                </table>
            </div>

            {isModalOpen && (
                <div className="modal-overlay">
                    <div className={`modal-content ${isDarkMode ? 'dark' : ''}`}>
                        <h3>{currentCar ? 'Edit Car' : 'Add New Car'}</h3>
                        <form onSubmit={handleFormSubmit}>
                            {error && <p className="error-message">{error}</p>}
                            
                            {/* THE FIX IS HERE: Add a hidden input for the vehicleID during an update */}
                            {currentCar && (
                                <input type="hidden" name="vehicleID" value={currentCar.vehicleID} />
                            )}
                            
                            {/* When adding a NEW car, the ID field is visible and editable */}
                             {!currentCar && (
                                <div className="form-group">
                                    <label htmlFor="vehicleID">Vehicle ID</label>
                                    <input type="number" name="vehicleID" required />
                                </div>
                            )}

                            <div className="form-group">
                                <label htmlFor="vehicleName">Vehicle Name</label>
                                <input type="text" name="vehicleName" defaultValue={currentCar?.vehicleName || ''} required />
                            </div>
                            <div className="form-group">
                                <label htmlFor="brand">Brand</label>
                                <input type="text" name="brand" defaultValue={currentCar?.brand || ''} required />
                            </div>
                            <div className="form-group">
                                <label htmlFor="model">Model</label>
                                <input type="text" name="model" defaultValue={currentCar?.model || ''} required />
                            </div>
                            <div className="form-group">
                                <label htmlFor="dailyRate">Price per Day (₹)</label>
                                <input type="number" name="dailyRate" defaultValue={currentCar?.dailyRate || ''} required />
                            </div>
                            <div className="form-group">
                                <label htmlFor="pictureUrl">Image URL</label>
                                <input type="text" name="pictureUrl" defaultValue={currentCar?.pictureUrl || ''} required />
                            </div>
                             <div className="form-group">
                                <label htmlFor="regNumber">Registration Number</label>
                                <input type="text" name="regNumber" defaultValue={currentCar?.regNumber || ''} required />
                            </div>
                             <div className="form-group">
                                <label htmlFor="isAvailable">Availability (Yes/No)</label>
                                <input type="text" name="isAvailable" defaultValue={currentCar?.isAvailable || 'Yes'} required />
                            </div>
                            <div className="modal-actions">
                                <button type="button" onClick={handleCloseModal} className="btn-cancel">Cancel</button>
                                <button type="submit" className="btn-save">Save</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AdminCarManagement;