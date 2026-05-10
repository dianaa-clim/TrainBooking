import { Link } from "react-router-dom";
import {
  MapPin,
  Train,
  Route,
  CalendarDays,
  ClockAlert,
  Mail,
  ClipboardList,
} from "lucide-react";
import { useAuth } from "../../auth/useAuth";

export default function AdminDashboardPage() {
  const { user } = useAuth();

  return (
    <div>
      <div className="page-header">
        <h1>Admin dashboard</h1>
        <p>
          Welcome, {user?.firstName}. Manage train booking data, delays and
          customer bookings.
        </p>
      </div>

      <div className="dashboard-grid">
        <Link to="/admin/stations" className="card dashboard-card">
          <div className="dashboard-icon">
            <MapPin size={28} />
          </div>

          <h2>Stations</h2>
          <p>Add, edit, activate and deactivate train stations.</p>
        </Link>

        <Link to="/admin/trains" className="card dashboard-card">
          <div className="dashboard-icon">
            <Train size={28} />
          </div>

          <h2>Trains</h2>
          <p>Manage trains and their passenger capacity.</p>
        </Link>

        <Link to="/admin/routes" className="card dashboard-card">
          <div className="dashboard-icon">
            <Route size={28} />
          </div>

          <h2>Routes</h2>
          <p>Create routes and manage ordered route stops.</p>
        </Link>

        <Link to="/admin/train-runs" className="card dashboard-card">
          <div className="dashboard-icon">
            <CalendarDays size={28} />
          </div>

          <h2>Train runs</h2>
          <p>Create concrete train trips for specific dates.</p>
        </Link>

        <Link to="/admin/delays" className="card dashboard-card">
          <div className="dashboard-icon">
            <ClockAlert size={28} />
          </div>

          <h2>Delays</h2>
          <p>Register delays and generate simulated notifications.</p>
        </Link>

        <Link to="/admin/emails" className="card dashboard-card">
          <div className="dashboard-icon">
            <Mail size={28} />
          </div>

          <h2>Email outbox</h2>
          <p>View simulated booking and delay notification emails.</p>
        </Link>

        <Link to="/admin/bookings" className="card dashboard-card">
          <div className="dashboard-icon">
            <ClipboardList size={28} />
          </div>

          <h2>Bookings</h2>
          <p>View customer bookings for a selected train run.</p>
        </Link>
      </div>
    </div>
  );
}