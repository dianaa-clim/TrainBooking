export interface TrainRunStopResponse {
  id: number;
  stationId: number;
  stationCode: string;
  stationName: string;
  city: string;
  stopOrder: number;
  plannedArrivalTime: string | null;
  plannedDepartureTime: string | null;
  actualArrivalTime: string | null;
  actualDepartureTime: string | null;
}

export interface TrainRunResponse {
  id: number;

  trainId: number;
  trainCode: string;
  trainName: string;
  trainCapacity: number;

  routeId: number;
  routeCode: string;
  routeName: string;

  runDate: string;
  status: "SCHEDULED" | "DELAYED" | "CANCELLED";
  active: boolean;

  stops: TrainRunStopResponse[];
}

export interface TrainRunRequest {
  trainId: number;
  routeId: number;
  runDate: string;
}