export interface RouteStopResponse {
  id: number;
  stationId: number;
  stationCode: string;
  stationName: string;
  city: string;
  stopOrder: number;
  arrivalTime: string | null;
  departureTime: string | null;
  active: boolean;
}

export interface RouteResponse {
  id: number;
  code: string;
  name: string;
  active: boolean;
  stops: RouteStopResponse[];
}

export interface RouteRequest {
  code: string;
  name: string;
}

export interface RouteStopRequest {
  stationId: number;
  stopOrder: number;
  arrivalTime: string | null;
  departureTime: string | null;
}