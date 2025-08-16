import axios from 'axios';

// 1. Create a new Axios instance with a relative baseURL
const apiClient = axios.create({
  baseURL: '/', // This makes all requests relative to the domain
});

// 2. Use an interceptor to automatically add the token to every request
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default apiClient;