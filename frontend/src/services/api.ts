import axios from 'axios';
import { AuthResponse, LoginRequest, SignupRequest, Report, CreateReportRequest } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
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

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: async (loginData: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/signin', loginData);
    return response.data;
  },

  register: async (signupData: SignupRequest): Promise<{ message: string }> => {
    const response = await api.post('/auth/signup', signupData);
    return response.data;
  },
};

export const reportService = {
  createReport: async (reportData: CreateReportRequest): Promise<Report> => {
    const response = await api.post('/reports', reportData);
    return response.data;
  },

  getAllReports: async (params?: {
    page?: number;
    size?: number;
    sortBy?: string;
    sortDir?: string;
    type?: string;
    status?: string;
  }): Promise<{ content: Report[]; totalElements: number; totalPages: number }> => {
    const response = await api.get('/reports', { params });
    return response.data;
  },

  getReportById: async (id: string): Promise<Report> => {
    const response = await api.get(`/reports/${id}`);
    return response.data;
  },

  getMyReports: async (): Promise<Report[]> => {
    const response = await api.get('/reports/my-reports');
    return response.data;
  },

  getNearbyReports: async (params: {
    latitude: number;
    longitude: number;
    radiusInMeters?: number;
    type?: string;
  }): Promise<Report[]> => {
    const response = await api.get('/reports/nearby', { params });
    return response.data;
  },

  updateReportStatus: async (id: string, status: string): Promise<{ message: string }> => {
    const response = await api.put(`/reports/${id}/status`, null, {
      params: { status }
    });
    return response.data;
  },
};

export default api;