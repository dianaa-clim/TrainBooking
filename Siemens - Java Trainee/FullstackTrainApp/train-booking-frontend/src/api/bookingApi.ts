import axiosInstance from "./axiosInstance";
import type { BookingRequest, BookingResponse } from "../types/booking";

export const bookingApi = {
  createBooking: async (data: BookingRequest): Promise<BookingResponse> => {
    const response = await axiosInstance.post<BookingResponse>(
      "/customer/bookings",
      data
    );

    return response.data;
  },

  getMyBookings: async (): Promise<BookingResponse[]> => {
    const response = await axiosInstance.get<BookingResponse[]>(
      "/customer/bookings"
    );

    return response.data;
  },

  getBookingByCode: async (bookingCode: string): Promise<BookingResponse> => {
    const response = await axiosInstance.get<BookingResponse>(
      `/customer/bookings/${bookingCode}`
    );

    return response.data;
  },

  getBookingsForTrainRun: async (
    trainRunId: number
  ): Promise<BookingResponse[]> => {
    const response = await axiosInstance.get<BookingResponse[]>(
      `/admin/train-runs/${trainRunId}/bookings`
    );

    return response.data;
  },
};