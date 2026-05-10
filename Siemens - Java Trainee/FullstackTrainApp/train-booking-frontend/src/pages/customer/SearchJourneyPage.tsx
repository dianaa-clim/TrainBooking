import { useState } from "react";
import type { FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { journeyApi } from "../../api/journeyApi";
import type { JourneyOptionResponse } from "../../types/journey";

export default function SearchJourneyPage() {
  const navigate = useNavigate();

  const [from, setFrom] = useState("CLJ");
  const [to, setTo] = useState("BUC");
  const [date, setDate] = useState("2026-05-10");

  const [journeys, setJourneys] = useState<JourneyOptionResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSearch = async (event: FormEvent) => {
    event.preventDefault();

    setLoading(true);
    setError("");
    setJourneys([]);

    try {
      const data = await journeyApi.searchJourneys(from, to, date);
      setJourneys(data);
    } catch {
      setError("No journey found for the selected stations and date.");
    } finally {
      setLoading(false);
    }
  };

  const handleBook = (journey: JourneyOptionResponse) => {
    navigate("/customer/book", {
      state: {
        journey,
      },
    });
  };

  return (
    <div>
      <div className="page-header">
        <h1>Search journeys</h1>
        <p>Find direct journeys or journeys with one train change.</p>
      </div>

      <form className="card form-grid" onSubmit={handleSearch}>
        <div>
          <label>From station code</label>
          <input
            value={from}
            onChange={(event) => setFrom(event.target.value.toUpperCase())}
            placeholder="CLJ"
            required
          />
        </div>

        <div>
          <label>To station code</label>
          <input
            value={to}
            onChange={(event) => setTo(event.target.value.toUpperCase())}
            placeholder="BUC"
            required
          />
        </div>

        <div>
          <label>Date</label>
          <input
            type="date"
            value={date}
            onChange={(event) => setDate(event.target.value)}
            required
          />
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            {loading ? "Searching..." : "Search"}
          </button>
        </div>
      </form>

      {error && <p className="error mt">{error}</p>}

      <div className="results-list">
        {journeys.map((journey, index) => (
          <div className="card journey-card" key={`${journey.type}-${index}`}>
            <div className="journey-card-header">
              <div>
                <h2>
                  {journey.type === "DIRECT"
                    ? "Direct journey"
                    : "Journey with one change"}
                </h2>

                <p>
                  {formatDateTime(journey.departureTime)} →{" "}
                  {formatDateTime(journey.arrivalTime)}
                </p>

                {journey.transferStationCode && (
                  <p>
                    Change at: {journey.transferStationCode} -{" "}
                    {journey.transferStationName}
                  </p>
                )}
              </div>

              <button
                className="btn-primary"
                type="button"
                onClick={() => handleBook(journey)}
              >
                Book this journey
              </button>
            </div>

            <div className="legs-list">
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
          </div>
        ))}
      </div>
    </div>
  );
}

function formatDateTime(value: string) {
  return new Date(value).toLocaleString();
}