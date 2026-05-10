import axiosInstance from "./axiosInstance";
import type { StationRequest, StationResponse } from "../types/station";

export const stationApi = {
  getAll: async (): Promise<StationResponse[]> => {
    const response = await axiosInstance.get<StationResponse[]>("/admin/stations");
    return response.data;
  },

  create: async (data: StationRequest): Promise<StationResponse> => {
    const response = await axiosInstance.post<StationResponse>("/admin/stations", data);
    return response.data;
  },

  update: async (id: number, data: StationRequest): Promise<StationResponse> => {
    const response = await axiosInstance.put<StationResponse>(
      `/admin/stations/${id}`,
      data
    );
    return response.data;
  },

  deactivate: async (id: number): Promise<StationResponse> => {
    const response = await axiosInstance.patch<StationResponse>(
      `/admin/stations/${id}/deactivate`
    );
    return response.data;
  },

  activate: async (id: number): Promise<StationResponse> => {
    const response = await axiosInstance.patch<StationResponse>(
      `/admin/stations/${id}/activate`
    );
    return response.data;
  },
};