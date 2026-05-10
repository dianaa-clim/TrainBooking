import { useEffect, useState } from "react";
import { routeApi } from "../../api/routeApi";
import { trainApi } from "../../api/trainApi";
import { trainRunApi } from "../../api/trainRunApi";
import type { RouteResponse } from "../../types/route";
import type { TrainResponse } from "../../types/train";
import type { TrainRunRequest, TrainRunResponse } from "../../types/trainRun";
import axios from "axios";

export default function AdminTrainRunsPage() {
  const [trainRuns, setTrainRuns] = useState<TrainRunResponse[]>([]);
  const [trains, setTrains] = useState<TrainResponse[]>([]);
  const [routes, setRoutes] = useState<RouteResponse[]>([]);

  const [selectedTrainRun, setSelectedTrainRun] =
    useState<TrainRunResponse | null>(null);

  const [form, setForm] = useState<TrainRunRequest>({
    trainId: 0,
    routeId: 0,
    runDate: "2026-05-10",
  });

  const [editingId, setEditingId] = useState<number | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    try {
      const [trainRunsData, trainsData, routesData] = await Promise.all([
        trainRunApi.getAll(),
        trainApi.getAll(),
        routeApi.getAll(),
      ]);

      setTrainRuns(trainRunsData);
      setTrains(trainsData);
      setRoutes(routesData);

      if (selectedTrainRun) {
        const refreshed = trainRunsData.find(
          (run) => run.id === selectedTrainRun.id
        );
        setSelectedTrainRun(refreshed || null);
      }
    } catch {
      setError("Could not load train runs.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const resetForm = () => {
    setForm({
      trainId: 0,
      routeId: 0,
      runDate: "2026-05-10",
    });
    setEditingId(null);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");

    if (!form.trainId || !form.routeId) {
      setError("Select both train and route.");
      return;
    }

    try {
      if (editingId) {
        await trainRunApi.update(editingId, form);
      } else {
        await trainRunApi.create(form);
      }

      resetForm();
      await loadData();
 } catch (error) {
  if (axios.isAxiosError(error)) {
    setError(
      error.response?.data?.message ||
        "Could not save train run. Check if train and route are active."
    );
  } else {
    setError("Could not save train run.");
  }
}
  };

  const handleEdit = (trainRun: TrainRunResponse) => {
    setEditingId(trainRun.id);
    setForm({
      trainId: trainRun.trainId,
      routeId: trainRun.routeId,
      runDate: trainRun.runDate,
    });
  };

  const handleToggleActive = async (trainRun: TrainRunResponse) => {
    setError("");

    try {
      if (trainRun.active) {
        await trainRunApi.deactivate(trainRun.id);
      } else {
        await trainRunApi.activate(trainRun.id);
      }

      await loadData();
    } catch {
      setError("Could not update train run status.");
    }
  };

  const handleViewStops = async (id: number) => {
    setError("");

    try {
      const data = await trainRunApi.getById(id);
      setSelectedTrainRun(data);
    } catch {
      setError("Could not load train run details.");
    }
  };

  if (loading) {
    return <div className="card">Loading train runs...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>Train runs</h1>
        <p>Create concrete train trips for specific dates.</p>
      </div>

      <form className="card form-grid train-run-form" onSubmit={handleSubmit}>
        <div>
          <label>Train</label>
          <select
            value={form.trainId}
            onChange={(event) =>
              setForm({ ...form, trainId: Number(event.target.value) })
            }
            required
          >
            <option value={0}>Select train</option>
            {trains.map((train) => (
              <option key={train.id} value={train.id}>
                {train.code} - {train.name} ({train.capacity} seats)
              </option>
            ))}
          </select>
        </div>

        <div>
          <label>Route</label>
          <select
            value={form.routeId}
            onChange={(event) =>
              setForm({ ...form, routeId: Number(event.target.value) })
            }
            required
          >
            <option value={0}>Select route</option>
            {routes.map((route) => (
              <option key={route.id} value={route.id}>
                {route.code} - {route.name}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label>Run date</label>
          <input
            type="date"
            value={form.runDate}
            onChange={(event) =>
              setForm({ ...form, runDate: event.target.value })
            }
            required
          />
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            {editingId ? "Update train run" : "Create train run"}
          </button>

          {editingId && (
            <button className="btn-secondary" type="button" onClick={resetForm}>
              Cancel
            </button>
          )}
        </div>
      </form>

      {error && <p className="error mt">{error}</p>}

      <div className="card mt">
        <h2>All train runs</h2>

        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Train</th>
              <th>Route</th>
              <th>Date</th>
              <th>Status</th>
              <th>Active</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {trainRuns.map((trainRun) => (
              <tr key={trainRun.id}>
                <td>{trainRun.id}</td>
                <td>
                  {trainRun.trainCode} - {trainRun.trainName}
                </td>
                <td>
                  {trainRun.routeCode} - {trainRun.routeName}
                </td>
                <td>{trainRun.runDate}</td>
                <td>
                  <span
                    className={
                      trainRun.status === "DELAYED"
                        ? "badge-warning"
                        : "badge-ok"
                    }
                  >
                    {trainRun.status}
                  </span>
                </td>
                <td>
                  <span className={trainRun.active ? "badge-ok" : "badge-off"}>
                    {trainRun.active ? "Active" : "Inactive"}
                  </span>
                </td>
                <td className="table-actions">
                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleViewStops(trainRun.id)}
                  >
                    View stops
                  </button>

                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleEdit(trainRun)}
                  >
                    Edit
                  </button>

                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleToggleActive(trainRun)}
                  >
                    {trainRun.active ? "Deactivate" : "Activate"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {selectedTrainRun && (
        <div className="card mt">
          <h2>
            Stops for {selectedTrainRun.trainCode} on{" "}
            {selectedTrainRun.runDate}
          </h2>

          <table className="data-table">
            <thead>
              <tr>
                <th>Order</th>
                <th>Station</th>
                <th>Planned arrival</th>
                <th>Planned departure</th>
                <th>Actual arrival</th>
                <th>Actual departure</th>
              </tr>
            </thead>

            <tbody>
              {selectedTrainRun.stops.map((stop) => (
                <tr key={stop.id}>
                  <td>{stop.stopOrder}</td>
                  <td>
                    {stop.stationCode} - {stop.stationName}
                  </td>
                  <td>{formatDateTime(stop.plannedArrivalTime)}</td>
                  <td>{formatDateTime(stop.plannedDepartureTime)}</td>
                  <td>{formatDateTime(stop.actualArrivalTime)}</td>
                  <td>{formatDateTime(stop.actualDepartureTime)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

function formatDateTime(value: string | null) {
  if (!value) {
    return "-";
  }

  return new Date(value).toLocaleString();
}