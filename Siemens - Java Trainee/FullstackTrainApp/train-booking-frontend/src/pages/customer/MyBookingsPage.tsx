import { useEffect, useState } from "react";
import { bookingApi } from "../../api/bookingApi";
import type { BookingResponse } from "../../types/booking";

export default function MyBookingsPage() {
  const [bookings, setBookings] = useState<BookingResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadBookings = async () => {
      try {
        const data = await bookingApi.getMyBookings();
        setBookings(data);
      } catch {
        setError("Could not load bookings.");
      } finally {
        setLoading(false);
      }
    };

    loadBookings();
  }, []);

  if (loading) {
    return <div className="card">Loading bookings...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>My bookings</h1>
        <p>View your bookings and generated tickets.</p>
      </div>

      {error && <p className="error">{error}</p>}

      {bookings.length === 0 && (
        <div className="card">
          <p>You do not have any bookings yet.</p>
        </div>
      )}

      <div className="results-list">
        {bookings.map((booking) => (
          <div className="card" key={booking.id}>
            <h2>{booking.bookingCode}</h2>
            <p>Status: {booking.status}</p>
            <p>Created at: {new Date(booking.createdAt).toLocaleString()}</p>

            <div className="tickets-grid">
              {booking.tickets.map((ticket) => (
                <div className="ticket-mini" key={ticket.id}>
                  <strong>{ticket.ticketCode}</strong>
                  <p>{ticket.passengerName}</p>
                  <p>
                    {ticket.originStationCode} →{" "}
                    {ticket.destinationStationCode}
                  </p>
                  <p>{ticket.trainCode}</p>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}