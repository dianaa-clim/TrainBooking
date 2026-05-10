import { useEffect, useState } from "react";
import { trainApi } from "../../api/trainApi";
import type { TrainRequest, TrainResponse } from "../../types/train";

export default function AdminTrainsPage() {
  const [trains, setTrains] = useState<TrainResponse[]>([]);
  const [form, setForm] = useState<TrainRequest>({
    code: "",
    name: "",
    capacity: 1,
  });

  const [editingId, setEditingId] = useState<number | null>(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const loadTrains = async () => {
    try {
      const data = await trainApi.getAll();
      setTrains(data);
    } catch {
      setError("Could not load trains.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTrains();
  }, []);

  const resetForm = () => {
    setForm({
      code: "",
      name: "",
      capacity: 1,
    });
    setEditingId(null);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setError("");

    try {
      if (editingId) {
        await trainApi.update(editingId, form);
      } else {
        await trainApi.create(form);
      }

      resetForm();
      await loadTrains();
    } catch {
      setError("Could not save train. The code may already exist.");
    }
  };

  const handleEdit = (train: TrainResponse) => {
    setEditingId(train.id);
    setForm({
      code: train.code,
      name: train.name,
      capacity: train.capacity,
    });
  };

  const handleToggleActive = async (train: TrainResponse) => {
    try {
      if (train.active) {
        await trainApi.deactivate(train.id);
      } else {
        await trainApi.activate(train.id);
      }

      await loadTrains();
    } catch {
      setError("Could not update train status.");
    }
  };

  if (loading) {
    return <div className="card">Loading trains...</div>;
  }

  return (
    <div>
      <div className="page-header">
        <h1>Trains</h1>
        <p>Manage trains and their passenger capacity.</p>
      </div>

      <form className="card form-grid admin-form" onSubmit={handleSubmit}>
        <div>
          <label>Code</label>
          <input
            value={form.code}
            onChange={(event) =>
              setForm({ ...form, code: event.target.value.toUpperCase() })
            }
            placeholder="IR1001"
            required
          />
        </div>

        <div>
          <label>Name</label>
          <input
            value={form.name}
            onChange={(event) => setForm({ ...form, name: event.target.value })}
            placeholder="InterRegio Cluj-București"
            required
          />
        </div>

        <div>
          <label>Capacity</label>
          <input
            type="number"
            min={1}
            value={form.capacity}
            onChange={(event) =>
              setForm({ ...form, capacity: Number(event.target.value) })
            }
            required
          />
        </div>

        <div className="form-actions">
          <button className="btn-primary" type="submit">
            {editingId ? "Update train" : "Add train"}
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
        <h2>All trains</h2>

        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Code</th>
              <th>Name</th>
              <th>Capacity</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>

          <tbody>
            {trains.map((train) => (
              <tr key={train.id}>
                <td>{train.id}</td>
                <td>{train.code}</td>
                <td>{train.name}</td>
                <td>{train.capacity}</td>
                <td>
                  <span className={train.active ? "badge-ok" : "badge-off"}>
                    {train.active ? "Active" : "Inactive"}
                  </span>
                </td>
                <td className="table-actions">
                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleEdit(train)}
                  >
                    Edit
                  </button>

                  <button
                    className="btn-secondary"
                    type="button"
                    onClick={() => handleToggleActive(train)}
                  >
                    {train.active ? "Deactivate" : "Activate"}
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