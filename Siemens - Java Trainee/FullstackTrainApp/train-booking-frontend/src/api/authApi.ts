import axiosInstance from "./axiosInstance";
import type {
  AuthResponse,
  CurrentUserResponse,
  LoginRequest,
  RegisterRequest,
} from "../types/auth";

export const authApi = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await axiosInstance.post<AuthResponse>("/auth/login", data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await axiosInstance.post<AuthResponse>("/auth/register", data);
    return response.data;
  },

  me: async (): Promise<CurrentUserResponse> => {
    const response = await axiosInstance.get<CurrentUserResponse>("/auth/me");
    return response.data;
  },
};