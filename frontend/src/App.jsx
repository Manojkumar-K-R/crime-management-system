import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./pages/Login";
import AdminDashboard from "./pages/admin/AdminDashboard";
import AdminLayout from "./pages/admin/AdminLayout";
import AdminComplaints from "./pages/admin/AdminComplaints";

function App() {

  console.log("CURRENT APP.JSX IS RUNNING");
  return (
    <BrowserRouter>

      <Routes>

        {/* Login */}
        <Route path="/" element={<Login />} />

        {/* Admin */}
        <Route path="/admin" element={<AdminLayout />}>

          <Route
            path="dashboard"
            element={<AdminDashboard />}
          />

          <Route
  path="complaints"
  element={<AdminComplaints />}
/>

        </Route>

      </Routes>

    </BrowserRouter>
  );
}

export default App;