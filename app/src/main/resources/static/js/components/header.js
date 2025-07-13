
// app/src/main/resources/static/js/components/header.js
function renderHeader() {
  const headerDiv = document.getElementById("header");

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

  // Retrieve the User's Role and Token from LocalStorage
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // Initialize Header Content
  let headerContent = `<header class="header">
    <div class="logo-section">
      <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
      <span class="logo-title">Hospital CMS</span>
    </div>
    <nav>`;

  // Handle Session Expiry or Invalid Login
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/"; // or a specific login page
    return;
  }

  // Add Role-Specific Header Content
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" onclick="logout()">Logout</a>`;
  }
  else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Logout</a>`;
  }
  else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  }
  else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" onclick="logoutPatient()">Logout</a>`;
  }

  // Close the Header Section
  headerContent += `
    </nav>  
  </header>`;
  // Render the Header Content
  headerDiv.innerHTML = headerContent;

  // Attach Event Listeners to Header Buttons
  attachHeaderButtonListeners();

  // If the user is a patient, attach login and signup listeners
  if (role === "patient") {
    document.getElementById("patientLogin").addEventListener("click", () => {
      openModal("patientLogin");
    });
    document.getElementById("patientSignup").addEventListener("click", () => {
      openModal("patientSignup");
    });
  }

  // If the user is a logged patient, attach listeners for home and appointments
  if (role === "loggedPatient") { 
    document.getElementById("home").addEventListener("click", () => {
      window.location.href = "/pages/loggedPatientDashboard.html";
    });
    document.getElementById("patientAppointments").addEventListener("click", () => {
      window.location.href = "/pages/patientAppointments.html";
    });
  }

  // Attach listeners for admin and doctor roles
  if (role === "admin" || role === "doctor") {
    document.getElementById("addDocBtn").addEventListener("click", () => {
      openModal("addDoctor");
    });
  } else if (role === "doctor") {
    document.querySelector(".adminBtn").addEventListener("click", () => {
      selectRole("doctor");
    });
  }
  // Attach logout listeners
  document.querySelectorAll("a[href='#']").forEach(link => {
    link.addEventListener("click", (event) => {
      event.preventDefault();
      if (role === "loggedPatient") {
        logoutPatient();
      } else {
        logout();
      }
    });
  });

}


function attachHeaderButtonListeners() {
  // Attach event listeners to login buttons for "Doctor" and "Admin" role
  document.getElementById("doctorLoginBtn").addEventListener("click", () => {
    openModal("doctorLogin");
  });

  document.getElementById("adminLoginBtn").addEventListener("click", () => {
    openModal("adminLogin"); 

  });

  document.getElementById("logoutBtn").addEventListener("click", () => {
    logout();
  }
  );
}

function logout() {
  // Remove user session data
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  // Redirect to the root page
  window.location.href = "/";
}

function logoutPatient() {
  // Remove the patient's session token
  localStorage.removeItem("patientToken");
  // Redirect to the patient dashboard
  window.location.href = "/pages/patientDashboard.html";
}

// Function to open a modal based on the role

function openModal(role) {
  const modal = document.getElementById(`${role}Modal`);
  if (modal) {
    modal.style.display = "block"; // Show the modal
  } else {
    console.error(`Modal for ${role} not found.`);
  }


}

// Function to select the role and redirect accordingly
function selectRole(role) {
  localStorage.setItem("userRole", role);
  if (role === "doctor") {
    window.location.href = "/pages/doctorDashboard.html";
  } else if (role === "admin") {
    window.location.href = "/pages/adminDashboard.html";
  } else {
    console.error(`Role ${role} is not recognized.`);
  }
}

function closeModal(role) {
  const modal = document.getElementById(`${role}Modal`);
  if (modal) {
    modal.style.display = "none"; // Hide the modal
  } else {
    console.error(`Modal for ${role} not found.`);
  }
}



// Render the Header when the page loads
document.addEventListener("DOMContentLoaded", renderHeader);


