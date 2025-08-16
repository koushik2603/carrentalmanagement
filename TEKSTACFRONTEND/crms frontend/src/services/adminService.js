import apiClient from '../api/apiClient'; // Import the new central client

/**
 * Fetches all regular users (role 'USER') from the backend.
 * This function is intended for admin use.
 * NOTE: The request now correctly goes through the API Gateway.
 */
export const getAllUsers = async () => {
    // The apiClient automatically adds the auth token
    // The path is relative and points to the endpoint exposed by the gateway
    const response = await apiClient.get('/auth/getregularusers');
    return response.data;
};

// You can add more admin-specific functions to this file in the future.