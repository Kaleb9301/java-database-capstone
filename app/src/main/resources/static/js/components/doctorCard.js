// doctorCard.js

// Import necessary functions
import { showBookingOverlay } from "./loggedPatient.js";
import { deleteDoctor } from "./services/doctorServices.js";
import { getPatientDetails } from "./services/patientServices.js";

// Function to create and return a doctor card DOM element
export function createDoctorCard(doctor) {
  // Create the main doctor card container
  const card = document.createElement("div");
  card.className = "doctor-card";

  // Get the current user role
  const role = localStorage.getItem("userRole");

  // Create doctor info section
  const infoDiv = document.createElement("div");
  infoDiv.className = "doctor-info";

  const nameEl = document.createElement("h3");
  nameEl.textContent = `Dr. ${doctor.name}`;

  const specializationEl = document.createElement("p");
  specializationEl.textContent = `Specialization: ${doctor.specialization}`;

  const emailEl = document.createElement("p");
  emailEl.textContent = `Email: ${doctor.email}`;

  const timesEl = document.createElement("ul");
  timesEl.textContent = "Available Times:";
  doctor.availableTimes?.forEach((time) => {
    const li = document.createElement("li");
    li.textContent = time;
    timesEl.appendChild(li);
  });

  // Append doctor info
  infoDiv.appendChild(nameEl);
  infoDiv.appendChild(specializationEl);
  infoDiv.appendChild(emailEl);
  infoDiv.appendChild(timesEl);

  // Create actions container
  const actionsDiv = document.createElement("div");
  actionsDiv.className = "doctor-actions";

  // === ADMIN ROLE ===
  if (role === "admin") {
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete Doctor";
    deleteBtn.className = "adminBtn";

    deleteBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        return;
      }

      const confirmed = confirm(`Are you sure you want to delete Dr. ${doctor.name}?`);
      if (confirmed) {
        try {
          await deleteDoctor(doctor.id, token);
          alert("Doctor deleted successfully.");
          card.remove();
        } catch (err) {
          alert("Failed to delete doctor.");
          console.error(err);
        }
      }
    });

    actionsDiv.appendChild(deleteBtn);
  }

  // === NOT LOGGED-IN PATIENT ===
  else if (role === "patient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "adminBtn";

    bookBtn.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });

    actionsDiv.appendChild(bookBtn);
  }

  // === LOGGED-IN PATIENT ===
  else if (role === "loggedPatient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "adminBtn";

    bookBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        return;
      }

      try {
        const patient = await getPatientDetails(token);
        showBookingOverlay(doctor, patient);
      } catch (err) {
        console.error("Booking failed:", err);
        alert("Could not fetch patient info. Please try again.");
      }
    });

    actionsDiv.appendChild(bookBtn);
  }

  // Append info and actions to card
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  return card;
}
