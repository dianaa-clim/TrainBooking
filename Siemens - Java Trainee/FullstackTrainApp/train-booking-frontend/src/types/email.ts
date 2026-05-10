export interface EmailOutboxResponse {
  id: number;
  recipientEmail: string;
  subject: string;
  body: string;
  emailType: "BOOKING_CONFIRMATION" | "DELAY_NOTIFICATION";
  status: "PENDING" | "SIMULATED" | "SENT" | "FAILED";
  createdAt: string;
}