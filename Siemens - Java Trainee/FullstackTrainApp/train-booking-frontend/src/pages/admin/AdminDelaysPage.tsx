import axios from "axios";
import { useEffect, useState } from "react";
import { delayApi } from "../../api/delayApi";
import { trainRunApi } from "../../api/trainRunApi";
import type { DelayRequest, DelayResponse } from "../../types/delay";
import type { TrainRunResponse } from "../../types/trainRun";

export default function AdminDelaysPage() {
  const [trainRuns, setTrainRuns] = useState<TrainRunResponse[]>([]);
  const [selectedTrainRunId, setSelectedTrainRunId] = useState<number>(0);
  const [delays, setDelays] = useState<DelayResponse[]>([]);

  const [form, setForm] = useState<DelayRequest>({
    delayMinutes: 1,
    reason: "",
  });

  const [loading, setLoading] = useState(true);
  const [loadingDelays, setLoadingDelays] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

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

  const loadDelays = async (trainRunId: number) => {
    if (!trainRunId) {
      setDelays([]);
      return;
    }

    setLoadingDelays(true);
    setError("");

    try {
      const data = await delayApi.getDelaysForTrainRun(trainRunId);
      setDelays(data);
    } catch {
      setError("Could not load delays for this train run.");
    } finally {
      setLoadingDelays(false);
    }
  };

  const handleTrainRunChange = async (trainRunId: number) => {
    setSelectedTrainRunId(trainRunId);
    await loadDelays(trainRunId);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");
    setSuccess("");

    if (!selectedTrainRunId) {
      setError("Select a train run first.");
      return;
    }

    try {
      await delayApi.registerDelay(selectedTrainRunId, form);

      setSuccess("Delay registered successfully. Affected customers were notified with simulated emails.");

      setForm({
        delayMinutes: 1,
        reason: "",
      });

      await loadTrainRuns();
      await loadDelays(selectedTrainRunId);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        setError(
          error.response?.data?.message ||
            "Could not register delay for this train run."
        );
      } else {
        setError("Could not register delay for this train run.");
      }
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
        <h1>Train delays</h1>
        <p>Register delays for train runs and notify affected customers.</p>
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

      <form className="card form-grid delay-form mt" onSubmit={handleSubmit}>
        <div>
          <label>Delay minutes</label>
          <input
            type="number"
            min={1}
            value={form.delayMinutes}
            onChange={(event) =>
              setForm({
                ...form,
                delayMinutes: Number(event.target.value),
              })
            }
            required
          />
        </div>

        <div>
          <label>Reason</label>
          <input
            value={form.reason}
            onChange={(event) =>
              setForm({
                ...form,
                reason: event.target.value,
              })
            }
            placeholder="Technical issue"
            required
          />
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            Register delay
          </button>
        </div>
      </form>

      {error && <p className="error mt">{error}</p>}

      {success && <p className="success mt">{success}</p>}

      <div className="card mt">
        <h2>Delay history</h2>

{!selectedTrainRunId ? (
  <p>Select a train run to view its delay history.</p>
) : loadingDelays ? (
  <p>Loading delays...</p>
) : delays.length === 0 ? (
  <p>No delays registered for this train run.</p>
) : (
  <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Train</th>
                <th>Delay</th>
                <th>Reason</th>
                <th>Created by</th>
                <th>Created at</th>
              </tr>
            </thead>

            <tbody>
              {delays.map((delay) => (
                <tr key={delay.id}>
                  <td>{delay.id}</td>
                  <td>{delay.trainCode}</td>
                  <td>{delay.delayMinutes} minutes</td>
                  <td>{delay.reason}</td>
                  <td>{delay.createdByEmail}</td>
                  <td>{new Date(delay.createdAt).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}