import { useEffect, useState } from "react";
import { routeApi } from "../../api/routeApi";
import { stationApi } from "../../api/stationApi";
import type {
  RouteRequest,
  RouteResponse,
  RouteStopRequest,
  RouteStopResponse,
} from "../../types/route";
import type { StationResponse } from "../../types/station";

export default function AdminRoutesPage() {
  const [routes, setRoutes] = useState<RouteResponse[]>([]);
  const [stations, setStations] = useState<StationResponse[]>([]);

  const [selectedRoute, setSelectedRoute] = useState<RouteResponse | null>(null);

  const [routeForm, setRouteForm] = useState<RouteRequest>({
    code: "",
    name: "",
  });

  const [stopForm, setStopForm] = useState<RouteStopRequest>({
    stationId: 0,
    stopOrder: 1,
    arrivalTime: null,
    departureTime: null,
  });

  const [editingRouteId, setEditingRouteId] = useState<number | null>(null);
  const [editingStopId, setEditingStopId] = useState<number | null>(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadData = async () => {
    try {
      const [routesData, stationsData] = await Promise.all([
        routeApi.getAll(),
        stationApi.getAll(),
      ]);

      setRoutes(routesData);
      setStations(stationsData);

      if (selectedRoute) {
        const refreshedRoute = routesData.find(
          (route) => route.id === selectedRoute.id
        );
        setSelectedRoute(refreshedRoute || null);
      }
    } catch {
      setError("Could not load routes or stations.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const resetRouteForm = () => {
    setRouteForm({
      code: "",
      name: "",
    });
    setEditingRouteId(null);
  };

  const resetStopForm = () => {
    setStopForm({
      stationId: 0,
      stopOrder: selectedRoute ? selectedRoute.stops.length + 1 : 1,
      arrivalTime: null,
      departureTime: null,
    });
    setEditingStopId(null);
  };

  const handleRouteSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");

    try {
      if (editingRouteId) {
        await routeApi.update(editingRouteId, routeForm);
      } else {
        await routeApi.create(routeForm);
      }

      resetRouteForm();
      await loadData();
    } catch {
      setError("Could not save route. The code may already exist.");
    }
  };

  const handleEditRoute = (route: RouteResponse) => {
    setEditingRouteId(route.id);
    setRouteForm({
      code: route.code,
      name: route.name,
    });
  };

  const handleToggleRouteActive = async (route: RouteResponse) => {
    setError("");

    try {
      if (route.active) {
        await routeApi.deactivate(route.id);
      } else {
        await routeApi.activate(route.id);
      }

      await loadData();
    } catch {
      setError("Could not update route status.");
    }
  };

  const handleSelectRoute = async (routeId: number) => {
    setError("");

    try {
      const route = await routeApi.getById(routeId);
      setSelectedRoute(route);

      setStopForm({
        stationId: 0,
        stopOrder: route.stops.length + 1,
        arrivalTime: null,
        departureTime: null,
      });

      setEditingStopId(null);
    } catch {
      setError("Could not load selected route.");
    }
  };

  const handleStopSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");

    if (!selectedRoute) {
      setError("Select a route first.");
      return;
    }

    if (!stopForm.stationId) {
      setError("Select a station.");
      return;
    }

    try {
      if (editingStopId) {
        const updatedRoute = await routeApi.updateStop(
          selectedRoute.id,
          editingStopId,
          stopForm
        );
        setSelectedRoute(updatedRoute);
      } else {
        const updatedRoute = await routeApi.addStop(selectedRoute.id, stopForm);
        setSelectedRoute(updatedRoute);
      }

      resetStopForm();
      await loadData();
    } catch {
      setError(
        "Could not save route stop. Station or stop order may already exist."
      );
    }
  };

  const handleEditStop = (stop: RouteStopResponse) => {
    setEditingStopId(stop.id);
    setStopForm({
      stationId: stop.stationId,
      stopOrder: stop.stopOrder,
      arrivalTime: stop.arrivalTime,
      departureTime: stop.departureTime,
    });
  };

  const handleRemoveStop = async (stopId: number) => {
    if (!selectedRoute) {
      return;
    }

    setError("");

    try {
      const updatedRoute = await routeApi.removeStop(selectedRoute.id, stopId);
      setSelectedRoute(updatedRoute);
      await loadData();
    } catch {
      setError("Could not remove route stop.");
    }
  };

  if (loading) {
    return <div className="card">Loading routes...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>Routes</h1>
        <p>Manage train routes and the ordered stations inside each route.</p>
      </div>

      <form className="card form-grid admin-form" onSubmit={handleRouteSubmit}>
        <div>
          <label>Route code</label>
          <input
            value={routeForm.code}
            onChange={(event) =>
              setRouteForm({
                ...routeForm,
                code: event.target.value.toUpperCase(),
              })
            }
            placeholder="R-CLJ-BUC"
            required
          />
        </div>

        <div>
          <label>Route name</label>
          <input
            value={routeForm.name}
            onChange={(event) =>
              setRouteForm({
                ...routeForm,
                name: event.target.value,
              })
            }
            placeholder="Cluj-Napoca - București"
            required
          />
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            {editingRouteId ? "Update route" : "Add route"}
          </button>

          {editingRouteId && (
            <button
              className="btn-secondary"
              type="button"
              onClick={resetRouteForm}
            >
              Cancel
            </button>
          )}
        </div>
      </form>

      {error && <p className="error mt">{error}</p>}

      <div className="card mt">
        <h2>All routes</h2>

        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Code</th>
              <th>Name</th>
              <th>Stops</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {routes.map((route) => (
              <tr key={route.id}>
                <td>{route.id}</td>
                <td>{route.code}</td>
                <td>{route.name}</td>
                <td>{route.stops.length}</td>
                <td>
                  <span className={route.active ? "badge-ok" : "badge-off"}>
                    {route.active ? "Active" : "Inactive"}
                  </span>
                </td>
                <td className="table-actions">
                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleSelectRoute(route.id)}
                  >
                    Manage stops
                  </button>

                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleEditRoute(route)}
                  >
                    Edit
                  </button>

                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleToggleRouteActive(route)}
                  >
                    {route.active ? "Deactivate" : "Activate"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {selectedRoute && (
        <div className="card mt">
          <h2>
            Stops for {selectedRoute.code} - {selectedRoute.name}
          </h2>

          <form className="form-grid route-stop-form" onSubmit={handleStopSubmit}>
            <div>
              <label>Station</label>
              <select
                value={stopForm.stationId}
                onChange={(event) =>
                  setStopForm({
                    ...stopForm,
                    stationId: Number(event.target.value),
                  })
                }
                required
              >
                <option value={0}>Select station</option>
                {stations.map((station) => (
                  <option key={station.id} value={station.id}>
                    {station.code} - {station.name}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label>Stop order</label>
              <input
                type="number"
                min={1}
                value={stopForm.stopOrder}
                onChange={(event) =>
                  setStopForm({
                    ...stopForm,
                    stopOrder: Number(event.target.value),
                  })
                }
                required
              />
            </div>

            <div>
              <label>Arrival time</label>
              <input
                type="time"
                value={stopForm.arrivalTime ?? ""}
                onChange={(event) =>
                  setStopForm({
                    ...stopForm,
                    arrivalTime: event.target.value || null,
                  })
                }
              />
            </div>

            <div>
              <label>Departure time</label>
              <input
                type="time"
                value={stopForm.departureTime ?? ""}
                onChange={(event) =>
                  setStopForm({
                    ...stopForm,
                    departureTime: event.target.value || null,
                  })
                }
              />
            </div>

            <div className="form-actions">
              <button className="btn-primary" type="submit">
                {editingStopId ? "Update stop" : "Add stop"}
              </button>

              {editingStopId && (
                <button
                  className="btn-secondary"
                  type="button"
                  onClick={resetStopForm}
                >
                  Cancel
                </button>
              )}
            </div>
          </form>

          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Order</th>
                <th>Station</th>
                <th>Arrival</th>
                <th>Departure</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              {selectedRoute.stops.map((stop) => (
                <tr key={stop.id}>
                  <td>{stop.id}</td>
                  <td>{stop.stopOrder}</td>
                  <td>
                    {stop.stationCode} - {stop.stationName}
                  </td>
                  <td>{stop.arrivalTime ?? "-"}</td>
                  <td>{stop.departureTime ?? "-"}</td>
                  <td className="table-actions">
                    <button
                      className="btn-secondary"
                      type="button"
                      onClick={() => handleEditStop(stop)}
                    >
                      Edit
                    </button>

                    <button
                      className="btn-secondary"
                      type="button"
                      onClick={() => handleRemoveStop(stop.id)}
                    >
                      Remove
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}