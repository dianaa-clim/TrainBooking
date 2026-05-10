export interface StationResponse {
  id: number;
  code: string;
  name: string;
  city: string;
  active: boolean;
}

export interface StationRequest {
  code: string;
  name: string;
  city: string;
}