import { Link, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/useAuth";

export default function AdminLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div>
      <nav className="navbar">
        <Link to="/admin" className="brand">
          Admin Panel
        </Link>

        <div className="nav-links">
          <Link to="/admin/stations">Stations</Link>
          <Link to="/admin/trains">Trains</Link>
          <Link to="/admin/routes">Routes</Link>
          <Link to="/admin/train-runs">Train runs</Link>
          <Link to="/admin/delays">Delays</Link>
          <Link to="/admin/emails">Emails</Link>
          <Link to="/admin/bookings">Bookings</Link>
          <span>{user?.email}</span>
          <button onClick={handleLogout} className="btn-secondary">
            Logout
          </button>
        </div>
      </nav>

      <main className="container">
        <Outlet />
      </main>
    </div>
  );
}