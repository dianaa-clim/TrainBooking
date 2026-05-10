import { Link, useLocation } from "react-router-dom";
import type { BookingResponse } from "../../types/booking";

interface LocationState {
  booking?: BookingResponse;
}

export default function BookingConfirmationPage() {
  const location = useLocation();
  const { booking } = (location.state || {}) as LocationState;

  if (!booking) {
    return (
      <div className="card">
        <h1>No booking found</h1>
        <p>You can view your bookings from the customer panel.</p>
        <Link to="/customer/bookings" className="btn-primary">
          My bookings
        </Link>
      </div>
    );
  }

  return (
    <div>
      <div className="page-header">
        <h1>Booking confirmed</h1>
        <p>Your tickets were generated successfully.</p>
      </div>

      <div className="card">
        <h2>Booking code: {booking.bookingCode}</h2>
        <p>Status: {booking.status}</p>
        <p>Customer: {booking.customerFullName}</p>
        <p>Email: {booking.customerEmail}</p>
      </div>

      <div className="tickets-grid">
        {booking.tickets.map((ticket) => (
          <div className="card ticket-card" key={ticket.id}>
            <h3>Ticket {ticket.ticketCode}</h3>
            <p>
              <strong>Passenger:</strong> {ticket.passengerName}
            </p>
            <p>
              <strong>Train:</strong> {ticket.trainCode} - {ticket.trainName}
            </p>
            <p>
              <strong>Route:</strong> {ticket.routeName}
            </p>
            <p>
              <strong>From:</strong> {ticket.originStationCode} -{" "}
              {ticket.originStationName}
            </p>
            <p>
              <strong>To:</strong> {ticket.destinationStationCode} -{" "}
              {ticket.destinationStationName}
            </p>
            <p>
              <strong>Departure:</strong>{" "}
              {new Date(ticket.departureTime).toLocaleString()}
            </p>
            <p>
              <strong>Arrival:</strong>{" "}
              {new Date(ticket.arrivalTime).toLocaleString()}
            </p>
          </div>
        ))}
      </div>

      <div className="actions">
        <Link to="/customer/search" className="btn-secondary">
          Search another journey
        </Link>

        <Link to="/customer/bookings" className="btn-primary">
          My bookings
        </Link>
      </div>
    </div>
  );
}