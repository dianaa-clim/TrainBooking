import { useState } from "react";
import type { FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { bookingApi } from "../../api/bookingApi";
import type { BookingRequest } from "../../types/booking";
import type { JourneyOptionResponse } from "../../types/journey";

interface LocationState {
  journey?: JourneyOptionResponse;
}

export default function BookingPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const { journey } = (location.state || {}) as LocationState;

  const [passengers, setPassengers] = useState<string[]>([""]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  if (!journey) {
    return (
      <div className="card">
        <h1>No journey selected</h1>
        <p>Please search for a journey first.</p>
        <Link to="/customer/search" className="btn-primary">
          Search journeys
        </Link>
      </div>
    );
  }

  const addPassenger = () => {
    setPassengers((current) => [...current, ""]);
  };

  const removePassenger = (index: number) => {
    setPassengers((current) => current.filter((_, i) => i !== index));
  };

  const updatePassenger = (index: number, value: string) => {
    setPassengers((current) =>
      current.map((passenger, i) => (i === index ? value : passenger))
    );
  };

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setError("");
    setLoading(true);

    const validPassengers = passengers
      .map((passenger) => passenger.trim())
      .filter(Boolean);

    if (validPassengers.length === 0) {
      setError("At least one passenger is required.");
      setLoading(false);
      return;
    }

    const request: BookingRequest = {
      passengers: validPassengers.map((fullName) => ({
        fullName,
      })),
      legs: journey.legs.map((leg) => ({
        trainRunId: leg.trainRunId,
        originStopId: leg.originStopId,
        destinationStopId: leg.destinationStopId,
      })),
    };

    try {
      const booking = await bookingApi.createBooking(request);

      navigate("/customer/booking-confirmation", {
        state: {
          booking,
        },
      });
    } catch {
      setError("Booking failed. There may not be enough available seats.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Book journey</h1>
        <p>Complete passenger details and confirm your booking.</p>
      </div>

      <div className="card">
        <h2>Selected journey</h2>

        {journey.legs.map((leg) => (
          <div className="leg-item" key={leg.legOrder}>
            <strong>Leg {leg.legOrder}</strong>
            <p>
              Train {leg.trainCode} - {leg.trainName}
            </p>
            <p>
              {leg.originStationCode} → {leg.destinationStationCode}
            </p>
            <p>
              {formatDateTime(leg.departureTime)} →{" "}
              {formatDateTime(leg.arrivalTime)}
            </p>
          </div>
        ))}
      </div>

      <form className="card mt" onSubmit={handleSubmit}>
        <h2>Passengers</h2>

        {error && <p className="error">{error}</p>}

        {passengers.map((passenger, index) => (
          <div className="passenger-row" key={index}>
            <input
              value={passenger}
              onChange={(event) => updatePassenger(index, event.target.value)}
              placeholder={`Passenger ${index + 1} full name`}
              required
            />

            {passengers.length > 1 && (
              <button
                type="button"
                className="btn-secondary"
                onClick={() => removePassenger(index)}
              >
                Remove
              </button>
            )}
          </div>
        ))}

        <div className="actions">
          <button type="button" className="btn-secondary" onClick={addPassenger}>
            Add passenger
          </button>

          <button type="submit" className="btn-primary">
            {loading ? "Booking..." : "Confirm booking"}
          </button>
        </div>
      </form>
    </div>
  );
}

function formatDateTime(value: string) {
  return new Date(value).toLocaleString();
}