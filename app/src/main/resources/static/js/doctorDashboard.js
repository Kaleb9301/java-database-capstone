// doctorDashboard.js

import { getAllAppointments } from "../services/appointmentService.js";
import { createPatientRow } from "./createPatientRow.js";

// DOM Elements
const tableBody = document.getElementById("patientTableBody");
const searchBar = document.getElementById("searchBar");
const todayButton = document.getElementById("todayButton");
const datePicker = document.getElementById("datePicker");

let selectedDate = new Date().toISOString().split("T")[0]; // format: YYYY-MM-DD
let patientName = null;
const token = localStorage.getItem("token");

// Handle patient name search input
if (searchBar) {
  searchBar.addEventListener("input", () => {
    const value = searchBar.value.trim();
    patientName = value !== "" ? value : null;
    loadAppointments();
  });
}

// Handle "Today" button click
if (todayButton) {
  todayButton.addEventListener("click", () => {
    selectedDate = new Date().toISOString().split("T")[0];
    if (datePicker) datePicker.value = selectedDate;
    loadAppointments();
  });
}

// Handle date picker change
if (datePicker) {
  datePicker.addEventListener("change", () => {
    selectedDate = datePicker.value;
    loadAppointments();
  });
}

// Load and render appointments
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(selectedDate, patientName, token);
    tableBody.innerHTML = ""; // clear previous results

    if (!appointments || appointments.length === 0) {
      tableBody.innerHTML = `
        <tr><td colspan="5" style="text-align: center;">No Appointments found for today.</td></tr>
      `;
      return;
    }

    appointments.forEach((appointment) => {
      const patient = {
        id: appointment.patientId,
        name: appointment.patientName,
        phone: appointment.phone,
        email: appointment.email,
        prescription: appointment.prescription
      };
      const row = createPatientRow(patient);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error loading appointments:", error);
    tableBody.innerHTML = `
      <tr><td colspan="5" style="text-align: center; color: red;">Error loading appointments. Try again later.</td></tr>
    `;
  }
}

// Initialize on page load
document.addEventListener("DOMContentLoaded", () => {
  if (typeof renderContent === "function") {
    renderContent();
  }
  loadAppointments();
});
