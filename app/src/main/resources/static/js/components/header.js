// header.js

// 1. Define the renderHeader function
function renderHeader() {
  // 2. Select the Header Div
  const headerDiv = document.getElementById("header");

  // 3. Check if the current page is the root page
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return;
  }

  // 4. Retrieve user's role and token
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // 5. Initialize Header Content
  let headerContent = `<header class="header">
    <div class="logo-section">
      <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
      <span class="logo-title">Hospital CMS</span>
    </div>
    <nav>`;

  // 6. Handle session expiry or invalid login
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  }

  // 7. Add Role-Specific Header Content
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" onclick="logoutPatient()">Logout</a>`;
  }

  // 9. Close the Header Section
  headerContent += `</nav></header>`;

  // 10. Render the Header Content
  headerDiv.innerHTML = headerContent;

  // 11. Attach Event Listeners to Header Buttons
  attachHeaderButtonListeners();
}

// 13. Helper Function: Attach Header Button Event Listeners
function attachHeaderButtonListeners() {
  const loginBtn = document.getElementById("patientLogin");
  const signupBtn = document.getElementById("patientSignup");

  if (loginBtn) {
    loginBtn.addEventListener("click", () => openModal("patientLogin"));
  }

  if (signupBtn) {
    signupBtn.addEventListener("click", () => openModal("patientSignup"));
  }
}

// 14. Helper Function: Logout for Admin/Doctor
function logout() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}

// 15. Helper Function: Logout for Logged-In Patient
function logoutPatient() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/pages/patientDashboard.html";
}

// 16. Render the Header on Page Load
document.addEventListener("DOMContentLoaded", renderHeader);
