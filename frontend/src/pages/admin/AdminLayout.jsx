import { Link, Outlet, useNavigate } from "react-router-dom";

function AdminLayout() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/");
  };

  return (
    <div style={styles.container}>

      {/* Sidebar */}
      <aside style={styles.sidebar}>

        <h2 style={styles.logo}>
          Crime Management
        </h2>

        <nav>

          <Link to="/admin/dashboard" style={styles.link}>
            Dashboard
          </Link>

          <Link to="/admin/complaints" style={styles.link}>
            Complaints
          </Link>

          <Link to="/admin/officers" style={styles.link}>
            Officers
          </Link>

          <Link to="/admin/citizens" style={styles.link}>
            Citizens
          </Link>

        </nav>

        <button
          onClick={logout}
          style={styles.logout}
        >
          Logout
        </button>

      </aside>

      {/* Main content */}
      <main style={styles.content}>
        <Outlet />
      </main>

    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    minHeight: "100vh",
    background: "#f4f6f8",
  },

  sidebar: {
    width: "240px",
    background: "#17202a",
    color: "white",
    padding: "25px 15px",
    boxSizing: "border-box",
    display: "flex",
    flexDirection: "column",
  },

  logo: {
    fontSize: "20px",
    marginBottom: "30px",
    paddingLeft: "10px",
  },

  link: {
    display: "block",
    padding: "14px 12px",
    marginBottom: "5px",
    color: "white",
    textDecoration: "none",
    borderRadius: "6px",
  },

  logout: {
    marginTop: "auto",
    padding: "12px",
    border: "none",
    borderRadius: "6px",
    background: "#d32f2f",
    color: "white",
    cursor: "pointer",
  },

  content: {
    flex: 1,
    padding: "30px",
  },
};

export default AdminLayout;