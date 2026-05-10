import { useEffect, useState } from "react";
import { stationApi } from "../../api/stationApi";
import type { StationRequest, StationResponse } from "../../types/station";

export default function AdminStationsPage() {
  const [stations, setStations] = useState<StationResponse[]>([]);
  const [form, setForm] = useState<StationRequest>({
    code: "",
    name: "",
    city: "",
  });

  const [editingId, setEditingId] = useState<number | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const loadStations = async () => {
    try {
      const data = await stationApi.getAll();
      setStations(data);
    } catch {
      setError("Could not load stations.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStations();
  }, []);

  const resetForm = () => {
    setForm({
      code: "",
      name: "",
      city: "",
    });
    setEditingId(null);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");

    try {
      if (editingId) {
        await stationApi.update(editingId, form);
      } else {
        await stationApi.create(form);
      }

      resetForm();
      await loadStations();
    } catch {
      setError("Could not save station. The code may already exist.");
    }
  };

  const handleEdit = (station: StationResponse) => {
    setEditingId(station.id);
    setForm({
      code: station.code,
      name: station.name,
      city: station.city,
    });
  };

  const handleToggleActive = async (station: StationResponse) => {
    try {
      if (station.active) {
        await stationApi.deactivate(station.id);
      } else {
        await stationApi.activate(station.id);
      }

      await loadStations();
    } catch {
      setError("Could not update station status.");
    }
  };

  if (loading) {
    return <div className="card">Loading stations...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>Stations</h1>
        <p>Manage train stations used in routes.</p>
      </div>

      <form className="card form-grid admin-form" onSubmit={handleSubmit}>
        <div>
          <label>Code</label>
          <input
            value={form.code}
            onChange={(event) =>
              setForm({ ...form, code: event.target.value.toUpperCase() })
            }
            placeholder="CLJ"
            required
          />
        </div>

        <div>
          <label>Name</label>
          <input
            value={form.name}
            onChange={(event) => setForm({ ...form, name: event.target.value })}
            placeholder="Cluj-Napoca"
            required
          />
        </div>

        <div>
          <label>City</label>
          <input
            value={form.city}
            onChange={(event) => setForm({ ...form, city: event.target.value })}
            placeholder="Cluj-Napoca"
            required
          />
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            {editingId ? "Update station" : "Add station"}
          </button>

          {editingId && (
            <button
              className="btn-secondary"
              type="button"
              onClick={resetForm}
            >
              Cancel
            </button>
          )}
        </div>
      </form>

      {error && <p className="error mt">{error}</p>}

      <div className="card mt">
        <h2>All stations</h2>

        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Code</th>
              <th>Name</th>
              <th>City</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {stations.map((station) => (
              <tr key={station.id}>
                <td>{station.id}</td>
                <td>{station.code}</td>
                <td>{station.name}</td>
                <td>{station.city}</td>
                <td>
                  <span className={station.active ? "badge-ok" : "badge-off"}>
                    {station.active ? "Active" : "Inactive"}
                  </span>
                </td>
                <td className="table-actions">
                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleEdit(station)}
                  >
                    Edit
                  </button>

                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleToggleActive(station)}
                  >
                    {station.active ? "Deactivate" : "Activate"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}