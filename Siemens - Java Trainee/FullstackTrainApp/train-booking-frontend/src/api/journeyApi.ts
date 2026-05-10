import axiosInstance from "./axiosInstance";
import type { JourneyOptionResponse } from "../types/journey";

export const journeyApi = {
  searchJourneys: async (
    from: string,
    to: string,
    date: string
  ): Promise<JourneyOptionResponse[]> => {
    const response = await axiosInstance.get<JourneyOptionResponse[]>(
      "/customer/journeys",
      {
        params: {
          from,
          to,
          date,
        },
      }
    );

    return response.data;
  },
};