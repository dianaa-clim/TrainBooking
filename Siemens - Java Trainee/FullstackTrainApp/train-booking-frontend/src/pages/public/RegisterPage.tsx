import { useState } from "react";
import type { FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/useAuth";

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("Ana");
  const [lastName, setLastName] = useState("Popescu");
  const [email, setEmail] = useState("ana2@test.com");
  const [password, setPassword] = useState("password123");
  const [error, setError] = useState("");

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setError("");

    try {
      await register({
        firstName,
        lastName,
        email,
        password,
      });

      navigate("/customer");
    } catch {
      setError("Could not create account. The email may already be used.");
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Register</h1>

        {error && <p className="error">{error}</p>}

        <label>First name</label>
        <input
          value={firstName}
          onChange={(event) => setFirstName(event.target.value)}
          required
        />

        <label>Last name</label>
        <input
          value={lastName}
          onChange={(event) => setLastName(event.target.value)}
          required
        />

        <label>Email</label>
        <input
          type="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          required
        />

        <label>Password</label>
        <input
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          required
        />

        <button className="btn-primary" type="submit">
          Register
        </button>
      </form>
    </div>
  );
}