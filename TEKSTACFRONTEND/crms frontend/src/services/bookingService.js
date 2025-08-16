import apiClient from '../api/apiClient'; // Import the new client

export const getAllBookings = async () => {
  const response = await apiClient.get('/api/bookings/all');
  return response.data;
};

export const getBookingsByUser = async (email) => {
  const response = await apiClient.get(`/api/bookings/user/${email}`);
  return response.data;
};

export const cancelBooking = async (bookingId) => {
  await apiClient.delete(`/api/bookings/${bookingId}`);
};