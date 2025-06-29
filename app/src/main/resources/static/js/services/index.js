// login.js

// Import dependencies
import { openModal } from "./components/modal.js";
import { API_BASE_URL } from "./config/config.js"

// Define API endpoints
const ADMIN_API = `${API_BASE_URL}/admin/login`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;

// Wait for DOM to load
window.onload = () => {
  const adminLoginBtn = document.getElementById("adminLogin");
  const doctorLoginBtn = document.getElementById("doctorLogin");

  if (adminLoginBtn) {
    adminLoginBtn.addEventListener("click", () => openModal("adminLogin"));
  }

  if (doctorLoginBtn) {
    doctorLoginBtn.addEventListener("click", () => openModal("doctorLogin"));
  }
};

// Handle admin login
window.adminLoginHandler = async () => {
  try {
    const username = document.getElementById("adminUsername").value;
    const password = document.getElementById("adminPassword").value;

    const admin = { username, password };

    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(admin),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("userRole", "admin");
      selectRole("admin");
    } else {
      alert("Invalid admin credentials.");
    }
  } catch (error) {
    console.error("Admin login error:", error);
    alert("Something went wrong. Please try again later.");
  }
};

// Handle doctor login
window.doctorLoginHandler = async () => {
  try {
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;

    const doctor = { email, password };

    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(doctor),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("userRole", "doctor");
      selectRole("doctor");
    } else {
      alert("Invalid doctor credentials.");
    }
  } catch (error) {
    console.error("Doctor login error:", error);
    alert("Something went wrong. Please try again later.");
  }
};
