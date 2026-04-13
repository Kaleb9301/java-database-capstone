function renderHeader() {
    const headerDiv = document.getElementById("header");

    if (window.location.pathname.endsWith("/")) {
         localStorage.removeItem("userRole");
         localStorage.removeItem("token");
         headerDiv.innerHTML = `
           <header class="header">
             <div class="logo-section">
               <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
               <span class="logo-title">Hospital CMS 123</span>
             </div>
           </header>`;
          //  console.log("At role selection page, no header buttons rendered.");
         return;
       }

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");
    // console.log("Rendering header for role:", role);
    let headerContent = `<header class="header">
      <div class="logo-section">
        <img src="/assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
        <span class="logo-title">Hospital CMS</span>
      </div>
      <nav>`;
       
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
      localStorage.removeItem("userRole");
      alert("Session expired or invalid login. Please log in again. 123");
      window.location.href = "/";   // or a specific login page
      return;
    } else if (role === "admin") {
      headerContent += `
        <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
        <a href="#" onclick="logout()">Logout</a>`;
    } else if (role === "doctor") {
      headerContent += `
        <button class="adminBtn"  onclick="selectRole('doctor')">Home</button>
        <a href="#" onclick="logout()">Logout</a>`;
    } else if (role === "patient") {
      headerContent += `
        <button id="patientLogin" class="adminBtn">Login</button>
        <button id="patientSignup" class="adminBtn">Sign Up</button>`;
        console.log("No role found in header.js 123");
    } else if (role === "loggedPatient") {
      headerContent += `
        <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
        <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
        <a href="#" onclick="logoutPatient()">Logout</a>`;
    }

    else {
      headerContent += `
        <button id="doctorLogin" class="adminBtn">Doctor Login</button>
        <button id="doctorSignup" class="adminBtn">Doctor Sign Up</button>
      `;
      console.log("No role found in header.js");
    }

    // console.log("No role found in header.js");

    headerContent += `
      </nav>
    </header>`;

    headerDiv.innerHTML = headerContent;
    attachHeaderButtonListeners();
  }

  function attachHeaderButtonListeners() {
    const doctorLoginBtn = document.getElementById("doctorLogin");
    const adminLoginBtn = document.getElementById("adminLogin");

    if (doctorLoginBtn) {
      doctorLoginBtn.addEventListener("click", () => {
        openModal('doctorLogin');
      });
    }

    if (adminLoginBtn) {
      adminLoginBtn.addEventListener("click", () => {
        openModal('adminLogin');
      });
    }
  }

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    window.location.href = "/";
  }
  function logoutPatient() {
    localStorage.removeItem("token");
    window.location.href = "/pages/loggedPatientDashboard.html";
  }

  renderHeader();




