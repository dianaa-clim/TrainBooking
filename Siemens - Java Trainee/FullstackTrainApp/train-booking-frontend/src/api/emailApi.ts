import axiosInstance from "./axiosInstance";
import type { EmailOutboxResponse } from "../types/email";

export const emailApi = {
  getAll: async (): Promise<EmailOutboxResponse[]> => {
    const response = await axiosInstance.get<EmailOutboxResponse[]>(
      "/admin/emails"
    );

    return response.data;
  },
};