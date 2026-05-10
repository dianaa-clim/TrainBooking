export interface DelayRequest {
  delayMinutes: number;
  reason: string;
}

export interface DelayResponse {
  id: number;
  trainRunId: number;
  trainCode: string;
  delayMinutes: number;
  reason: string;
  createdByEmail: string;
  createdAt: string;
}