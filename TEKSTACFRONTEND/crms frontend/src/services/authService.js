import apiClient from '../api/apiClient'; // Import the new client

export const registerUser = async (userData) => {
  // Use apiClient and a relative path
  const response = await apiClient.post('/auth/save', userData);
  return response.data;
};

export const loginUser = async (credentials) => {
  const response = await apiClient.post('/auth/login', credentials);
  return response.data;
};

export const getUserByEmail = async (email) => {
  // The token is added automatically by the apiClient
  const response = await apiClient.get(`/auth/getuserbyemail/${email}`);
  return response.data;
};