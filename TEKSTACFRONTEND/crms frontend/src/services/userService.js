import apiClient from '../api/apiClient'; // Import the new client

/**
 * Fetches all regular users for the admin dashboard.
 */
export const getRegularUsers = async () => {
  // This now correctly uses the relative path, which will go through the gateway
  const response = await apiClient.get('/auth/getregularusers');
  return response.data;
};