import { useEffect, useState } from "react";
import { getAdminDashboard } from "../../services/adminService";

function AdminDashboard() {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const data = await getAdminDashboard();

        console.log("ADMIN DASHBOARD:", data);

        setDashboard(data);
      } catch (error) {
        console.error("Dashboard error:", error);

        setError(
          error.response?.data?.message ||
          "Unable to load admin dashboard"
        );
      } finally {
        setLoading(false);
      }
    };

    loadDashboard();
  }, []);

  if (loading) {
    return (
      <div style={styles.loading}>
        Loading dashboard...
      </div>
    );
  }

  if (error) {
    return (
      <div style={styles.error}>
        {error}
      </div>
    );
  }

  const cards = [
    {
      title: "Total Complaints",
      value: dashboard.totalComplaints,
    },
    {
      title: "Pending",
      value: dashboard.pendingComplaints,
    },
    {
      title: "Under Investigation",
      value: dashboard.underInvestigation,
    },
    {
      title: "Resolved",
      value: dashboard.resolvedComplaints,
    },
    {
      title: "Closed",
      value: dashboard.closedComplaints,
    },
    {
      title: "Total Citizens",
      value: dashboard.totalCitizens,
    },
    {
      title: "Total Officers",
      value: dashboard.totalOfficers,
    },
  ];

  return (
    <div style={styles.page}>

      {/* Header */}
      <div style={styles.header}>
        <div>
          <h1 style={styles.heading}>
            Admin Dashboard
          </h1>

          <p style={styles.subtitle}>
            Crime Management System
          </p>
        </div>

        <button
          style={styles.logoutButton}
          onClick={() => {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "/";
          }}
        >
          Logout
        </button>
      </div>

      {/* Statistics */}
      <div style={styles.grid}>

        {cards.map((card) => (
          <div
            key={card.title}
            style={styles.card}
          >
            <h3 style={styles.cardTitle}>
              {card.title}
            </h3>

            <p style={styles.cardValue}>
              {card.value}
            </p>
          </div>
        ))}

      </div>

      {/* Summary */}
      <div style={styles.summary}>

        <h2>Complaint Summary</h2>

        <p>
          Total complaints currently registered:
          <strong> {dashboard.totalComplaints}</strong>
        </p>

        <p>
          Pending complaints:
          <strong> {dashboard.pendingComplaints}</strong>
        </p>

        <p>
          Complaints under investigation:
          <strong> {dashboard.underInvestigation}</strong>
        </p>

        <p>
          Resolved complaints:
          <strong> {dashboard.resolvedComplaints}</strong>
        </p>

        <p>
          Closed complaints:
          <strong> {dashboard.closedComplaints}</strong>
        </p>

      </div>

    </div>
  );
}

const styles = {

  page: {
    minHeight: "100vh",
    background: "#f4f6f8",
    padding: "30px",
    boxSizing: "border-box",
  },

  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "30px",
  },

  heading: {
    margin: 0,
    fontSize: "32px",
  },

  subtitle: {
    marginTop: "5px",
    color: "#666",
  },

  logoutButton: {
    padding: "10px 20px",
    border: "none",
    borderRadius: "6px",
    background: "#d32f2f",
    color: "white",
    cursor: "pointer",
    fontSize: "14px",
  },

  grid: {
    display: "grid",
    gridTemplateColumns:
      "repeat(auto-fit, minmax(200px, 1fr))",
    gap: "20px",
  },

  card: {
    background: "white",
    padding: "25px",
    borderRadius: "12px",
    boxShadow: "0 4px 15px rgba(0,0,0,0.08)",
  },

  cardTitle: {
    margin: 0,
    color: "#666",
    fontSize: "16px",
  },

  cardValue: {
    marginTop: "15px",
    marginBottom: 0,
    fontSize: "32px",
    fontWeight: "bold",
  },

  summary: {
    marginTop: "30px",
    background: "white",
    padding: "25px",
    borderRadius: "12px",
    boxShadow: "0 4px 15px rgba(0,0,0,0.08)",
  },

  loading: {
    padding: "40px",
    textAlign: "center",
    fontSize: "20px",
  },

  error: {
    padding: "40px",
    color: "#d32f2f",
    textAlign: "center",
    fontSize: "18px",
  },
};

export default AdminDashboard;