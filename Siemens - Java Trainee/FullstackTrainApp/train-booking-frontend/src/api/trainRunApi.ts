import axiosInstance from "./axiosInstance";
import type { TrainRunRequest, TrainRunResponse } from "../types/trainRun";

export const trainRunApi = {
  getAll: async (): Promise<TrainRunResponse[]> => {
    const response = await axiosInstance.get<TrainRunResponse[]>(
      "/admin/train-runs"
    );
    return response.data;
  },

  getById: async (id: number): Promise<TrainRunResponse> => {
    const response = await axiosInstance.get<TrainRunResponse>(
      `/admin/train-runs/${id}`
    );
    return response.data;
  },

  create: async (data: TrainRunRequest): Promise<TrainRunResponse> => {
    const response = await axiosInstance.post<TrainRunResponse>(
      "/admin/train-runs",
      data
    );
    return response.data;
  },

  update: async (
    id: number,
    data: TrainRunRequest
  ): Promise<TrainRunResponse> => {
    const response = await axiosInstance.put<TrainRunResponse>(
      `/admin/train-runs/${id}`,
      data
    );
    return response.data;
  },

  deactivate: async (id: number): Promise<TrainRunResponse> => {
    const response = await axiosInstance.patch<TrainRunResponse>(
      `/admin/train-runs/${id}/deactivate`
    );
    return response.data;
  },

  activate: async (id: number): Promise<TrainRunResponse> => {
    const response = await axiosInstance.patch<TrainRunResponse>(
      `/admin/train-runs/${id}/activate`
    );
    return response.data;
  },
};