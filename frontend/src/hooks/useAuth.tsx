import { createContext, useContext, useState, useCallback, type ReactNode } from "react";
import { useNavigate } from "react-router-dom";
import { authApi } from "@/services/api";
import type { LoginRequest, RegisterRequest } from "@/types/auth";

interface AuthContextType {
  token: string | null;
  username: string | null;
  email: string | null;
  isAuthenticated: boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem("token"));
  const [username, setUsername] = useState<string | null>(() => localStorage.getItem("username"));
  const [email, setEmail] = useState<string | null>(() => localStorage.getItem("email"));
  const navigate = useNavigate();

  const isAuthenticated = !!token;

  const login = useCallback(async (data: LoginRequest) => {
    const response = await authApi.login(data);
    localStorage.setItem("token", response.token);
    localStorage.setItem("username", response.username);
    localStorage.setItem("email", response.email);
    setToken(response.token);
    setUsername(response.username);
    setEmail(response.email);
    navigate("/");
  }, [navigate]);

  const register = useCallback(async (data: RegisterRequest) => {
    const response = await authApi.register(data);
    localStorage.setItem("token", response.token);
    localStorage.setItem("username", response.username);
    localStorage.setItem("email", response.email);
    setToken(response.token);
    setUsername(response.username);
    setEmail(response.email);
    navigate("/");
  }, [navigate]);

  const logout = useCallback(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("email");
    setToken(null);
    setUsername(null);
    setEmail(null);
    navigate("/login");
  }, [navigate]);

  return (
    <AuthContext.Provider value={{ token, username, email, isAuthenticated, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
