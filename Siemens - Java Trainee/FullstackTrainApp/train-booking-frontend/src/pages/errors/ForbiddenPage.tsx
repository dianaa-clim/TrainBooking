import { Link } from "react-router-dom";

export default function ForbiddenPage() {
  return (
    <div className="card">
      <h1>403 - Forbidden</h1>
      <p>You do not have permission to access this page.</p>
      <Link to="/" className="btn-primary">
        Go home
      </Link>
    </div>
  );
}