import { useEffect, useState } from "react";
import { searchComplaints } from "../../services/complaintService";

function AdminComplaints() {
  const [complaints, setComplaints] = useState([]);
  const [status, setStatus] = useState("");
  const [keyword, setKeyword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const loadComplaints = async () => {
    try {
      setLoading(true);
      setError("");

      const params = {};

      if (status) {
        params.status = status;
      }

      if (keyword.trim()) {
        params.keyword = keyword.trim();
      }

      const data = await searchComplaints(params);

      console.log("COMPLAINTS:", data);

      setComplaints(data);
    } catch (error) {
      console.error("Complaint error:", error);

      setError(
        error.response?.data?.message ||
        "Unable to load complaints"
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadComplaints();
  }, []);

  return (
    <div style={styles.page}>

      <h1>Complaints</h1>

      {/* Filters */}
      <div style={styles.filters}>

        <select
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          style={styles.input}
        >
          <option value="">All Statuses</option>
          <option value="PENDING">Pending</option>
          <option value="UNDER_INVESTIGATION">
            Under Investigation
          </option>
          <option value="RESOLVED">Resolved</option>
          <option value="CLOSED">Closed</option>
        </select>

        <input
          type="text"
          placeholder="Search keyword..."
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          style={styles.input}
        />

        <button
          onClick={loadComplaints}
          style={styles.button}
        >
          Search
        </button>

      </div>

      {/* Error */}
      {error && (
        <p style={styles.error}>
          {error}
        </p>
      )}

      {/* Loading */}
      {loading ? (
        <p>Loading complaints...</p>
      ) : (
        <div style={styles.tableContainer}>

          <table style={styles.table}>

            <thead>
              <tr>
                <th style={styles.th}>Complaint No.</th>
                <th style={styles.th}>Title</th>
                <th style={styles.th}>Crime Type</th>
                <th style={styles.th}>Location</th>
                <th style={styles.th}>Incident Date</th>
                <th style={styles.th}>Status</th>
                <th style={styles.th}>Officer</th>
              </tr>
            </thead>

            <tbody>

              {complaints.length === 0 ? (
                <tr>
                  <td
                    colSpan="7"
                    style={styles.empty}
                  >
                    No complaints found.
                  </td>
                </tr>
              ) : (
                complaints.map((complaint) => (
                  <tr key={complaint.complaintNumber}>

                    <td style={styles.td}>
                      {complaint.complaintNumber}
                    </td>

                    <td style={styles.td}>
                      {complaint.title}
                    </td>

                    <td style={styles.td}>
                      {complaint.crimeType}
                    </td>

                    <td style={styles.td}>
                      {complaint.incidentLocation}
                    </td>

                    <td style={styles.td}>
                      {complaint.incidentDate}
                    </td>

                    <td style={styles.td}>
                      <span style={styles.status}>
                        {complaint.status}
                      </span>
                    </td>

                    <td style={styles.td}>
                      {complaint.officerName || "Not Assigned"}
                    </td>

                  </tr>
                ))
              )}

            </tbody>

          </table>

        </div>
      )}

    </div>
  );
}

const styles = {
  page: {
    width: "100%",
  },

  filters: {
    display: "flex",
    gap: "10px",
    margin: "20px 0",
  },

  input: {
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "6px",
    fontSize: "14px",
  },

  button: {
    padding: "10px 20px",
    border: "none",
    borderRadius: "6px",
    background: "#1976d2",
    color: "white",
    cursor: "pointer",
  },

  tableContainer: {
    background: "white",
    borderRadius: "10px",
    overflowX: "auto",
    boxShadow: "0 3px 12px rgba(0,0,0,0.08)",
  },

  table: {
    width: "100%",
    borderCollapse: "collapse",
  },

  th: {
    padding: "14px",
    textAlign: "left",
    background: "#f1f3f5",
    borderBottom: "1px solid #ddd",
  },

  td: {
    padding: "14px",
    borderBottom: "1px solid #eee",
  },

  status: {
    padding: "5px 10px",
    borderRadius: "12px",
    fontSize: "12px",
    fontWeight: "bold",
  },

  empty: {
    padding: "30px",
    textAlign: "center",
  },

  error: {
    color: "#d32f2f",
  },
};

export default AdminComplaints;