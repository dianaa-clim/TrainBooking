import axiosInstance from "./axiosInstance";
import type {
  RouteRequest,
  RouteResponse,
  RouteStopRequest,
} from "../types/route";

export const routeApi = {
  getAll: async (): Promise<RouteResponse[]> => {
    const response = await axiosInstance.get<RouteResponse[]>("/admin/routes");
    return response.data;
  },

  getById: async (id: number): Promise<RouteResponse> => {
    const response = await axiosInstance.get<RouteResponse>(`/admin/routes/${id}`);
    return response.data;
  },

  create: async (data: RouteRequest): Promise<RouteResponse> => {
    const response = await axiosInstance.post<RouteResponse>("/admin/routes", data);
    return response.data;
  },

  update: async (id: number, data: RouteRequest): Promise<RouteResponse> => {
    const response = await axiosInstance.put<RouteResponse>(
      `/admin/routes/${id}`,
      data
    );
    return response.data;
  },

  deactivate: async (id: number): Promise<RouteResponse> => {
    const response = await axiosInstance.patch<RouteResponse>(
      `/admin/routes/${id}/deactivate`
    );
    return response.data;
  },

  activate: async (id: number): Promise<RouteResponse> => {
    const response = await axiosInstance.patch<RouteResponse>(
      `/admin/routes/${id}/activate`
    );
    return response.data;
  },

  addStop: async (
    routeId: number,
    data: RouteStopRequest
  ): Promise<RouteResponse> => {
    const response = await axiosInstance.post<RouteResponse>(
      `/admin/routes/${routeId}/stops`,
      data
    );
    return response.data;
  },

  updateStop: async (
    routeId: number,
    stopId: number,
    data: RouteStopRequest
  ): Promise<RouteResponse> => {
    const response = await axiosInstance.put<RouteResponse>(
      `/admin/routes/${routeId}/stops/${stopId}`,
      data
    );
    return response.data;
  },

  removeStop: async (
    routeId: number,
    stopId: number
  ): Promise<RouteResponse> => {
    const response = await axiosInstance.delete<RouteResponse>(
      `/admin/routes/${routeId}/stops/${stopId}`
    );
    return response.data;
  },
};