import { Link } from "react-router-dom";

export default function HomePage() {
  return (
    <section className="hero">
      <h1>Train Ticket Booking App</h1>
      <p>
        Search journeys, book train tickets, and manage train routes from one
        application.
      </p>

      <div className="actions">
        <Link to="/login" className="btn-primary">
          Login
        </Link>
        <Link to="/register" className="btn-secondary">
          Register
        </Link>
      </div>
    </section>
  );
}