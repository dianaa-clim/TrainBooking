import { Link } from "react-router-dom";
import { Search, Ticket, UserRound } from "lucide-react";
import { useAuth } from "../../auth/useAuth";

export default function CustomerDashboardPage() {
  const { user } = useAuth();

  return (
    <div>
      <div className="page-header">
        <h1>Customer dashboard</h1>
        <p>
          Welcome, {user?.firstName}. Search train journeys and manage your
          bookings.
        </p>
      </div>

      <div className="dashboard-grid">
        <Link to="/customer/search" className="card dashboard-card">
          <div className="dashboard-icon">
            <Search size={28} />
          </div>

          <h2>Search journeys</h2>
          <p>
            Find direct train journeys or routes with one change between two
            stations.
          </p>
        </Link>

        <Link to="/customer/bookings" className="card dashboard-card">
          <div className="dashboard-icon">
            <Ticket size={28} />
          </div>

          <h2>My bookings</h2>
          <p>
            View your confirmed bookings, booking codes, passengers and
            generated tickets.
          </p>
        </Link>

        <div className="card dashboard-card">
          <div className="dashboard-icon">
            <UserRound size={28} />
          </div>

          <h2>Account</h2>
          <p>
            Logged in as <strong>{user?.email}</strong> with role{" "}
            <strong>{user?.role}</strong>.
          </p>
        </div>
      </div>
    </div>
  );
}