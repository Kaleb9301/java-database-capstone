// render.js

function selectRole(role) {
  setRole(role);
  const token = localStorage.getItem('token');
  if (role === "admin") {
    console.log("Selecting admin role");
    console.log("Token during admin role selection:", token);
    if (token) {
      console.log("Admin token exists");
      window.location.href = `/adminDashboard/${token}`;
      console.log("Admin token found, redirecting to admin dashboard.");
    }
  } if (role === "patient") {
    window.location.href = "/pages/patientDashboard.html";
  } else if (role === "doctor") {
    if (token) {
      window.location.href = `/doctorDashboard/${token}`;
    } else if (role === "loggedPatient") {
      window.location.href = "loggedPatientDashboard.html";
    }
  }
}


function renderContent() {
  const role = getRole();
  if (!role) {
    window.location.href = "/"; // if no role, send to role selection page
    return;
  }
}
