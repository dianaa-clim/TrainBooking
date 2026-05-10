export interface PassengerRequest {
  fullName: string;
}

export interface BookingLegRequest {
  trainRunId: number;
  originStopId: number;
  destinationStopId: number;
}

export interface BookingRequest {
  passengers: PassengerRequest[];
  legs: BookingLegRequest[];
}

export interface TicketResponse {
  id: number;
  ticketCode: string;
  passengerName: string;

  trainCode: string;
  trainName: string;

  routeName: string;

  originStationCode: string;
  originStationName: string;

  destinationStationCode: string;
  destinationStationName: string;

  departureTime: string;
  arrivalTime: string;
}

export interface BookingResponse {
  id: number;
  bookingCode: string;
  status: string;
  createdAt: string;

  customerId: number;
  customerEmail: string;
  customerFullName: string;

  tickets: TicketResponse[];
}