import axiosInstance from "./axiosInstance";
import type { TrainRequest, TrainResponse } from "../types/train";

export const trainApi = {
  getAll: async (): Promise<TrainResponse[]> => {
    const response = await axiosInstance.get<TrainResponse[]>("/admin/trains");
    return response.data;
  },

  create: async (data: TrainRequest): Promise<TrainResponse> => {
    const response = await axiosInstance.post<TrainResponse>("/admin/trains", data);
    return response.data;
  },

  update: async (id: number, data: TrainRequest): Promise<TrainResponse> => {
    const response = await axiosInstance.put<TrainResponse>(
      `/admin/trains/${id}`,
      data
    );
    return response.data;
  },

  deactivate: async (id: number): Promise<TrainResponse> => {
    const response = await axiosInstance.patch<TrainResponse>(
      `/admin/trains/${id}/deactivate`
    );
    return response.data;
  },

  activate: async (id: number): Promise<TrainResponse> => {
    const response = await axiosInstance.patch<TrainResponse>(
      `/admin/trains/${id}/activate`
    );
    return response.data;
  },
};