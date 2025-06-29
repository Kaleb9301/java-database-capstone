// adminDashboard.js

import { getDoctors, filterDoctors, saveDoctor } from "../services/doctorServices.js";
import { openModal, closeModal } from "./modal.js"; // Assumes you have modal.js to handle opening/closing
import { createDoctorCard } from "./doctorCard.js";

// Add click listener to "Add Doctor" button
document.addEventListener("DOMContentLoaded", () => {
  const addDocBtn = document.getElementById("addDocBtn");
  if (addDocBtn) {
    addDocBtn.addEventListener("click", () => openModal("addDoctor"));
  }

  // Load doctor cards on page load
  loadDoctorCards();

  // Attach filter listeners
  const searchBar = document.getElementById("searchBar");
  const timeFilter = document.getElementById("timeFilter");
  const specialtyFilter = document.getElementById("specialtyFilter");

  if (searchBar) searchBar.addEventListener("input", filterDoctorsOnChange);
  if (timeFilter) timeFilter.addEventListener("change", filterDoctorsOnChange);
  if (specialtyFilter) specialtyFilter.addEventListener("change", filterDoctorsOnChange);
});

// Load all doctors and render
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Failed to load doctors:", error);
  }
}

// Filter and display doctors
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar")?.value || "";
  const time = document.getElementById("timeFilter")?.value || "";
  const specialty = document.getElementById("specialtyFilter")?.value || "";

  try {
    const doctors = await filterDoctors(name.trim(), time.trim(), specialty.trim());
    if (doctors.length > 0) {
      renderDoctorCards(doctors);
    } else {
      document.getElementById("content").innerHTML = "<p>No doctors found with the given filters.</p>";
    }
  } catch (error) {
    alert("Error filtering doctors. Try again.");
    console.error("Filter error:", error);
  }
}

// Render doctor cards from a list of doctor objects
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";
  doctors.forEach((doc) => {
    const card = createDoctorCard(doc);
    contentDiv.appendChild(card);
  });
}

// Handle admin submission of new doctor
window.adminAddDoctor = async function () {
  const name = document.getElementById("docName").value;
  const email = document.getElementById("docEmail").value;
  const phone = document.getElementById("docPhone").value;
  const password = document.getElementById("docPassword").value;
  const specialty = document.getElementById("docSpecialty").value;
  const availableTime = document.getElementById("docAvailableTime").value;

  const token = localStorage.getItem("token");
  if (!token) {
    alert("You must be logged in as admin to add a doctor.");
    return;
  }

  const doctor = { name, email, phone, password, specialty, availableTime };

  try {
    const result = await saveDoctor(doctor, token);
    if (result.success) {
      alert("Doctor added successfully!");
      closeModal("addDoctor");
      loadDoctorCards();
    } else {
      alert("Failed to add doctor: " + result.message);
    }
  } catch (error) {
    console.error("Error adding doctor:", error);
    alert("An error occurred while adding the doctor.");
  }
};
