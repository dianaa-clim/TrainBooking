export interface JourneyLegResponse {
  legOrder: number;

  trainRunId: number;
  trainCode: string;
  trainName: string;

  routeId: number;
  routeCode: string;
  routeName: string;

  originStopId: number;
  originStationCode: string;
  originStationName: string;

  destinationStopId: number;
  destinationStationCode: string;
  destinationStationName: string;

  departureTime: string;
  arrivalTime: string;
}

export interface JourneyOptionResponse {
  type: "DIRECT" | "ONE_CHANGE";
  departureTime: string;
  arrivalTime: string;
  transferStationCode: string | null;
  transferStationName: string | null;
  legs: JourneyLegResponse[];
}