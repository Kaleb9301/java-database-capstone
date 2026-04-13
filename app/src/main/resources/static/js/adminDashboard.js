// Import necessary functions from the service layer
import { getDoctors, deleteDoctor, saveDoctor, filterDoctors } from './services/doctorServices.js';

// Import the modal utility function
import { openModal } from './components/modals.js';

import { createDoctorCard } from './components/doctorCard.js';



  // This script handles the admin dashboard functionality for managing doctors:
  // - Loads all doctor cards
  // - Filters doctors by name, time, or specialty
  // - Adds a new doctor via modal form


  // Attach a click listener to the "Add Doctor" button
  // When clicked, it opens a modal form using openModal('addDoctor')
  const addDocBtn = document.getElementById('addDocBtn');
  if (addDocBtn) {
    addDocBtn.addEventListener('click', () => {
      openModal('addDoctor');
    });
  }


  // When the DOM is fully loaded:
  //   - Call loadDoctorCards() to fetch and display all doctors
  document.addEventListener('DOMContentLoaded', () => {
    loadDoctorCards();
  });


  // Function: loadDoctorCards
  // Purpose: Fetch all doctors and display them as cards
  async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = ''; // Clear existing content
    doctors.forEach(doctor => {
      const doctorCard = createDoctorCard(doctor);
      contentDiv.appendChild(doctorCard);
    }
    );
  } catch (error) {
    console.error('Error loading doctor cards:', error);
  }
}

  //   Call getDoctors() from the service layer
  //   Clear the current content area
  //   For each doctor returned:
  //   - Create a doctor card using createDoctorCard()
  //   - Append it to the content div

  //   Handle any fetch errors by logging them


  // Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  // On any input change, call filterDoctorsOnChange()
  const searchBar = document.getElementById('searchBar');
  const timeFilter = document.getElementById('timeFilter');
  const specialtyFilter = document.getElementById('specialtyFilter');

  if (searchBar) {
    searchBar.addEventListener('input', filterDoctorsOnChange);
  }
  if (timeFilter) {
    timeFilter.addEventListener('change', filterDoctorsOnChange);
  }
  if (specialtyFilter) {
    specialtyFilter.addEventListener('change', filterDoctorsOnChange);
  }

  
  // Function: filterDoctorsOnChange
  async function filterDoctorsOnChange() {
  // Purpose: Filter doctors based on name, available time, and specialty
    try {
    const nameInput = document.getElementById('searchBar').value.trim();
    const timeInput = document.getElementById('timeFilter').value;
    const specialtyInput = document.getElementById('specialtyFilter').value;

    const name = nameInput === '' ? null : nameInput;
    const time = timeInput === '' ? null : timeInput;
    const specialty = specialtyInput === '' ? null : specialtyInput;

    const result = await filterDoctors(name, time, specialty);
    const doctors = result.doctors;
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = ''; // Clear existing content

    if (doctors.length > 0) {

      doctors.forEach(doctor => {
        const doctorCard = createDoctorCard(doctor);
        contentDiv.appendChild(doctorCard);
      }); 
    } else {
      contentDiv.innerHTML = '<p>No doctors found with the given filters.</p>';
    }
  //   If doctors are found:
  //   - Render them using createDoctorCard()
  //   If no doctors match the filter:
  //   - Show a message: "No doctors found with the given filters."


  //   Catch and display any errors with an alert
  } catch (error) {
    alert('Error filtering doctors: ' + error.message);
  }
  }

  // Function: renderDoctorCards
  function renderDoctorCards(doctors) {
    
  // Purpose: A helper function to render a list of doctors passed to it
    try {

  //   Clear the content area
    const contentDiv = document.getElementById('content');
    contentDiv.innerHTML = '';
  //   Loop through the doctors and append each card to the content area
    doctors.forEach(doctor => {
      const doctorCard = createDoctorCard(doctor);
      contentDiv.appendChild(doctorCard);
    });
    } catch (error) {
      console.error('Error rendering doctor cards:', error);
    }
  }


  // Function: adminAddDoctor
  function adminAddDoctor() {
  // Purpose: Collect form data and add a new doctor to the system
    try {

  //   Collect input values from the modal form
  //   - Includes name, email, phone, password, specialty, and available times
    const name = document.getElementById('doctorName').value.trim();
    const email = document.getElementById('doctorEmail').value.trim();
    const phone = document.getElementById('doctorPhone').value.trim();
    const password = document.getElementById('doctorPassword').value;
    const specialty = document.getElementById('doctorSpecialty').value.trim();
    const availableTimes = Array.from(document.getElementById('doctorAvailableTimes').selectedOptions).map(option => option.value);


  //   Retrieve the authentication token from localStorage
    const token = localStorage.getItem('authToken');
  //   - If no token is found, show an alert and stop execution
    if (!token) {
      alert('Authentication token not found. Please log in again.');
      return;
    }

  //   Build a doctor object with the form values
    const doctor = {
      name,
      email,
      phone,
      password,
      specialty,
      availableTimes
    };

  //   Call saveDoctor(doctor, token) from the service
    saveDoctor(doctor, token)

  //   If save is successful:
    if (response.success) {
      alert('Doctor added successfully!');
      closeModal('addDoctor');
      loadDoctorCards();
    } else {
      alert('Error adding doctor: ' + response.message);
    }
    } catch (error) {
      alert('Error adding doctor: ' + error.message);
    }
  }
  
