import { useEffect, useState } from "react";
import { bookingApi } from "../../api/bookingApi";
import { trainRunApi } from "../../api/trainRunApi";
import type { BookingResponse } from "../../types/booking";
import type { TrainRunResponse } from "../../types/trainRun";

export default function AdminBookingsPage() {
  const [trainRuns, setTrainRuns] = useState<TrainRunResponse[]>([]);
  const [selectedTrainRunId, setSelectedTrainRunId] = useState<number>(0);
  const [bookings, setBookings] = useState<BookingResponse[]>([]);

  const [loading, setLoading] = useState(true);
  const [loadingBookings, setLoadingBookings] = useState(false);
  const [error, setError] = useState("");

  const loadTrainRuns = async () => {
    try {
      const data = await trainRunApi.getAll();
      setTrainRuns(data);
    } catch {
      setError("Could not load train runs.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTrainRuns();
  }, []);

  const handleTrainRunChange = async (trainRunId: number) => {
    setSelectedTrainRunId(trainRunId);
    setBookings([]);
    setError("");

    if (!trainRunId) {
      return;
    }

    setLoadingBookings(true);

    try {
      const data = await bookingApi.getBookingsForTrainRun(trainRunId);
      setBookings(data);
    } catch {
      setError("Could not load bookings for this train run.");
    } finally {
      setLoadingBookings(false);
    }
  };

  if (loading) {
    return <div className="card">Loading train runs...</div>;
  }

  const selectedTrainRun = trainRuns.find(
    (trainRun) => trainRun.id === selectedTrainRunId
  );

  return (
    <div>
      <div className="page-header">
        <h1>Bookings by train run</h1>
        <p>View all confirmed bookings for a selected train run.</p>
      </div>

      <div className="card">
        <label>Select train run</label>

        <select
          className="full-select"
          value={selectedTrainRunId}
          onChange={(event) =>
            handleTrainRunChange(Number(event.target.value))
          }
        >
          <option value={0}>Select train run</option>

          {trainRuns.map((trainRun) => (
            <option key={trainRun.id} value={trainRun.id}>
              #{trainRun.id} - {trainRun.trainCode} - {trainRun.routeName} -{" "}
              {trainRun.runDate} - {trainRun.status}
            </option>
          ))}
        </select>
      </div>

      {selectedTrainRun && (
        <div className="card mt">
          <h2>Selected train run</h2>

          <div className="details-grid">
            <p>
              <strong>Train:</strong> {selectedTrainRun.trainCode} -{" "}
              {selectedTrainRun.trainName}
            </p>

            <p>
              <strong>Route:</strong> {selectedTrainRun.routeCode} -{" "}
              {selectedTrainRun.routeName}
            </p>

            <p>
              <strong>Date:</strong> {selectedTrainRun.runDate}
            </p>

            <p>
              <strong>Status:</strong>{" "}
              <span
                className={
                  selectedTrainRun.status === "DELAYED"
                    ? "badge-warning"
                    : "badge-ok"
                }
              >
                {selectedTrainRun.status}
              </span>
            </p>
          </div>
        </div>
      )}

      {error && <p className="error mt">{error}</p>}

      <div className="card mt">
        <h2>Bookings</h2>

        {!selectedTrainRunId ? (
          <p>Select a train run to view its bookings.</p>
        ) : loadingBookings ? (
          <p>Loading bookings...</p>
        ) : bookings.length === 0 ? (
          <p>No bookings found for this train run.</p>
        ) : (
          <div className="results-list">
            {bookings.map((booking) => (
              <div className="booking-admin-card" key={booking.id}>
                <div className="booking-admin-header">
                  <div>
                    <h3>{booking.bookingCode}</h3>
                    <p>
                      <strong>Customer:</strong> {booking.customerFullName}
                    </p>
                    <p>
                      <strong>Email:</strong> {booking.customerEmail}
                    </p>
                    <p>
                      <strong>Status:</strong> {booking.status}
                    </p>
                    <p>
                      <strong>Created:</strong>{" "}
                      {new Date(booking.createdAt).toLocaleString()}
                    </p>
                  </div>

                  <span className="badge-ok">{booking.tickets.length} tickets</span>
                </div>

                <div className="tickets-grid">
                  {booking.tickets.map((ticket) => (
                    <div className="ticket-mini" key={ticket.id}>
                      <strong>{ticket.ticketCode}</strong>
                      <p>{ticket.passengerName}</p>
                      <p>
                        {ticket.originStationCode} →{" "}
                        {ticket.destinationStationCode}
                      </p>
                      <p>
                        {new Date(ticket.departureTime).toLocaleString()} →{" "}
                        {new Date(ticket.arrivalTime).toLocaleString()}
                      </p>
                      <p>{ticket.trainCode}</p>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}