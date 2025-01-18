// src/axiosConfig.js
import axios from "axios";

// Create an instance of axios
const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // This ensures cookies are sent with requests
});

// You can add interceptors here if needed (e.g., for adding authentication headers)

// Export the instance
export default axiosInstance;
