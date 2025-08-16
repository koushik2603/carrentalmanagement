import apiClient from '../api/apiClient'; // Import the new client

// --- Vehicle Functions ---
export const getAllVehicles = async () => {
  const response = await apiClient.get('/api/vehicles/getAllVehicles');
  return response.data;
};

export const addCar = async (carData) => {
  const response = await apiClient.post('/api/vehicles/save', carData);
  return response.data;
};

export const updateCar = async (id, carData) => {
  const response = await apiClient.put(`/api/vehicles/updateVehicle/${id}`, carData);
  return response.data;
};

export const deleteCar = async (id) => {
  const response = await apiClient.delete(`/api/vehicles/deleteVehicle/${id}`);
  return response.data;
};

// --- Booking Functions ---
export const createBooking = async (bookingData) => {
  const response = await apiClient.post('/api/bookings', bookingData);
  return response.data;
};