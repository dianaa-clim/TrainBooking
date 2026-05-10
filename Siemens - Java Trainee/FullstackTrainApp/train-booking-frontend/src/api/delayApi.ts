import axiosInstance from "./axiosInstance";
import type { DelayRequest, DelayResponse } from "../types/delay";

export const delayApi = {
  registerDelay: async (
    trainRunId: number,
    data: DelayRequest
  ): Promise<DelayResponse> => {
    const response = await axiosInstance.post<DelayResponse>(
      `/admin/train-runs/${trainRunId}/delays`,
      data
    );

    return response.data;
  },

  getDelaysForTrainRun: async (
    trainRunId: number
  ): Promise<DelayResponse[]> => {
    const response = await axiosInstance.get<DelayResponse[]>(
      `/admin/train-runs/${trainRunId}/delays`
    );

    return response.data;
  },
};