import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/authService";

function Login() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    usernameOrEmail: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await login(formData);
         console.log("FULL LOGIN RESPONSE:", response);
console.log("ROLE:", response.role);
console.log("ROLES:", response.roles);
      localStorage.setItem("token", response.token);

      // Save user information
      localStorage.setItem("user", JSON.stringify(response));

      // Redirect based on role
      const userRole =
  response.role ||
  response.roleName ||
  response.roles?.[0] ||
  response.authorities?.[0]?.authority;

console.log("DETECTED ROLE:", userRole);

if (userRole === "ADMIN" || userRole === "ROLE_ADMIN") {
  navigate("/admin/dashboard");
} else if (userRole === "OFFICER" || userRole === "ROLE_OFFICER") {
  navigate("/officer/dashboard");
} else if (userRole === "CITIZEN" || userRole === "ROLE_CITIZEN") {
  navigate("/citizen/dashboard");
} else {
  console.error("Unknown user role:", response);
  setError("Unable to determine user role.");
}

    } catch (error) {
      console.error(error);

      setError(
        error.response?.data?.message ||
        "Invalid username/email or password"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>

        <h1 style={styles.title}>Crime Management System</h1>

        <p style={styles.subtitle}>
          Sign in to your account
        </p>

        <form onSubmit={handleSubmit}>

          <input
            type="text"
            name="usernameOrEmail"
            placeholder="Username or Email"
            value={formData.usernameOrEmail}
            onChange={handleChange}
            required
            style={styles.input}
          />

          <input
            type="password"
            name="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
            style={styles.input}
          />

          {error && (
            <p style={styles.error}>
              {error}
            </p>
          )}

          <button
            type="submit"
            disabled={loading}
            style={styles.button}
          >
            {loading ? "Signing in..." : "Login"}
          </button>

        </form>

      </div>
    </div>
  );
}

const styles = {
  container: {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "#f4f6f8",
  },

  card: {
    width: "400px",
    padding: "40px",
    borderRadius: "12px",
    background: "white",
    boxShadow: "0 8px 30px rgba(0,0,0,0.12)",
  },

  title: {
    textAlign: "center",
    marginBottom: "10px",
  },

  subtitle: {
    textAlign: "center",
    color: "#666",
    marginBottom: "30px",
  },

  input: {
    width: "100%",
    padding: "12px",
    marginBottom: "15px",
    boxSizing: "border-box",
    border: "1px solid #ccc",
    borderRadius: "6px",
    fontSize: "15px",
  },

  button: {
    width: "100%",
    padding: "12px",
    border: "none",
    borderRadius: "6px",
    background: "#1976d2",
    color: "white",
    fontSize: "16px",
    cursor: "pointer",
  },

  error: {
    color: "#d32f2f",
    fontSize: "14px",
    marginBottom: "15px",
  },
};

export default Login;