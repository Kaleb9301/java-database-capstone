
// Import the overlay function for booking appointments from loggedPatient.js
import { showBookingOverlay } from '../loggedPatient.js';

//   Import the deleteDoctor API function to remove doctors (admin role) from doctorServices.js
import { deleteDoctor } from '../services/doctorServices.js';

//   Import function to fetch patient details (used during booking) from patientServices.js
import { getPatientData } from '../services/patientServices.js';



//   Function to create and return a DOM element for a single doctor card
export function createDoctorCard(doctor) {
 
//     Create the main container for the doctor card
    const card = document.createElement("div");
    card.className = "doctor-card";

//     Retrieve the current user role from localStorage
    const role = localStorage.getItem("userRole");
//     Create a div to hold doctor information
    const doctorInfo = document.createElement("div");
//     Create and set the doctor’s name
    const name = document.createElement("h3");
    name.textContent = doctor.name;
//     Create and set the doctor's specialization
    const specialization = document.createElement("h3");
    specialization.textContent = doctor.specialization;

//     Create and set the doctor's email
    const email = document.createElement("h3");
    email.textContent = doctor.email;
//     Create and list available appointment times
    const availability = document.createElement("ul");
    (doctor.availability || []).forEach(time => {
        const listItem = document.createElement("li");
        listItem.textContent = time;
        availability.appendChild(listItem);
    });
//     Append all info elements to the doctor info container
    doctorInfo.appendChild(name);
    doctorInfo.appendChild(specialization);
    doctorInfo.appendChild(email);
    doctorInfo.appendChild(availability);

//     Create a container for card action buttons
    const actions = document.createElement("div");
    actions.className = "doctor-actions";

//     === ADMIN ROLE ACTIONS ===
    if (role === "admin") {
//       Create a delete button
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";


//       Add click handler for delete button
      removeBtn.addEventListener("click", async () => {
        // 1. Confirm deletion
        const confirmed = confirm("Are you sure you want to delete this doctor?");
        if (!confirmed) return;

        // 2. Get token from localStorage
        const token = localStorage.getItem("adminToken");

        // 3. Call API to delete
        const result = await deleteDoctor(doctor.id, token);
        if (result.success) {
          alert("Doctor deleted successfully.");
          card.remove(); // 4. On success: remove the card from the DOM
        } else {
          alert("Failed to delete doctor: " + result.message);
        }
       
      })

   
//     === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
    } else if (role === "patient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", () => {
      alert("Patient needs to login first.");
    });
  } 
  
//     === LOGGED-IN PATIENT ROLE ACTIONS === 
  else if (role === "loggedPatient") {
    const bookNow = document.createElement("button");
    bookNow.textContent = "Book Now";
    bookNow.addEventListener("click", async (e) => {
      const token = localStorage.getItem("token");
      const patientData = await getPatientData(token);
      showBookingOverlay(e, doctor, patientData);
    });
  }
    
   
//   Append doctor info and action buttons to the card
    card.appendChild(doctorInfo);
    card.appendChild(actions);
//   Return the complete doctor card element
    return card;
}
