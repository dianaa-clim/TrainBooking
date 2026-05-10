import { Link, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/useAuth";

export default function CustomerLayout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div>
      <nav className="navbar">
        <Link to="/customer" className="brand">
          Customer Panel
        </Link>

        <div className="nav-links">
          <Link to="/customer/search">Search journeys</Link>
          <Link to="/customer/bookings">My bookings</Link>
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