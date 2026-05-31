import axios from "axios";
import type { LoginRequest, RegisterRequest, AuthResponse } from "@/types/auth";
import type { Task, TaskRequest } from "@/types/task";

const api = axios.create({
  baseURL: "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor to add JWT token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor to handle 401
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("username");
      localStorage.removeItem("email");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authApi = {
  login: (data: LoginRequest) =>
    api.post<AuthResponse>("/auth/login", data).then((res) => res.data),

  register: (data: RegisterRequest) =>
    api.post<AuthResponse>("/auth/register", data).then((res) => res.data),
};

// Tasks API
export const tasksApi = {
  list: (params?: { status?: string; priority?: string; search?: string }) =>
    api.get<Task[]>("/tasks", { params }).then((res) => res.data),

  getById: (id: number) =>
    api.get<Task>(`/tasks/${id}`).then((res) => res.data),

  create: (data: TaskRequest) =>
    api.post<Task>("/tasks", data).then((res) => res.data),

  update: (id: number, data: TaskRequest) =>
    api.put<Task>(`/tasks/${id}`, data).then((res) => res.data),

  delete: (id: number) =>
    api.delete(`/tasks/${id}`).then((res) => res.data),
};

export default api;
