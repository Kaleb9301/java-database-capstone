
import { showBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientDetails } from "../services/patientServices.js";

/**
 * Creates a doctor card element with information and actions based on user role.
 * @param {Object} doctor - The doctor object containing details like name, specialization, email, and available times.
 * @returns {HTMLElement} - The complete doctor card element.
 * @throws {Error} - If the doctor object is not provided.
 * @example
 * const doctorCard = createDoctorCard({
 *  name: "Dr. John Doe",
 *  specialization: "Cardiology",
 *  email: "
  *  availableTimes: ["9:00 AM", "10:00 AM", "11:00 AM"]
  * });
  * document.body.appendChild(doctorCard);
  * */
export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.className = "doctor-card";

  const role = localStorage.getItem("useRole");

  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;

  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialization}`;

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;

  const availableTimes = document.createElement("p");
  availableTimes.textContent = `Available Times: ${doctor.availableTimes.join(", ")}`;

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availableTimes);

  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");

  if (role === "admin") {
    const removeButton = document.createElement("button");
    removeButton.textContent = "Delete";

    
    removeButton.addEventListener("click", async () => {
      if (!confirm("Are you sure you want to delete this doctor?")) {
        return;
      }
      const token = localStorage.getItem("token");
      if (!token) {
        alert("You must be logged in as an admin to delete a doctor.");
        return;
      }
      try {
        const result = await deleteDoctor(doctor.id, token);
        if (result.success) {
          alert("Doctor deleted successfully.");
          card.remove();
        } else {
          alert(`Error deleting doctor: ${result.message}`);
        }
      } catch (error) {
        console.error("Error deleting doctor:", error);
        alert("An error occurred while deleting the doctor.");
      }

    });
    actionsDiv.appendChild(removeButton);
  } else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });
  }
  else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      const patientData = await getPatientDetails(token);
      showBookingOverlay(doctor, patientData);
    });
  }
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);
  return card
}








/*
Import the overlay function for booking appointments from loggedPatient.js

  Import the deleteDoctor API function to remove doctors (admin role) from docotrServices.js

  Import function to fetch patient details (used during booking) from patientServices.js

  Function to create and return a DOM element for a single doctor card
    Create the main container for the doctor card
    Retrieve the current user role from localStorage
    Create a div to hold doctor information
    Create and set the doctor’s name
    Create and set the doctor's specialization
    Create and set the doctor's email
    Create and list available appointment times
    Append all info elements to the doctor info container
    Create a container for card action buttons
    === ADMIN ROLE ACTIONS ===
      Create a delete button
      Add click handler for delete button
     Get the admin token from localStorage
        Call API to delete the doctor
        Show result and remove card if successful
      Add delete button to actions container
   
    === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
      Create a book now button
      Alert patient to log in before booking
      Add button to actions container
  
    === LOGGED-IN PATIENT ROLE ACTIONS === 
      Create a book now button
      Handle booking logic for logged-in patient   
        Redirect if token not available
        Fetch patient data with token
        Show booking overlay UI with doctor and patient info
      Add button to actions container
   
  Append doctor info and action buttons to the car
  Return the complete doctor card element
*/
