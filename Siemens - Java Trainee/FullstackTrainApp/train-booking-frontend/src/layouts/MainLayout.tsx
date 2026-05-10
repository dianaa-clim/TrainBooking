import { Link, Outlet } from "react-router-dom";

export default function MainLayout() {
  return (
    <div>
      <nav className="navbar">
        <Link to="/" className="brand">
          Train Booking
        </Link>

        <div className="nav-links">
          <Link to="/login">Login</Link>
          <Link to="/register">Register</Link>
        </div>
      </nav>

      <main className="container">
        <Outlet />
      </main>
    </div>
  );
}