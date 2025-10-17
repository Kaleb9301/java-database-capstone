
  // Import getAllAppointments to fetch appointments from the backend
  import { getAllAppointments } from '../services/appointmentService.js';
  // Import createPatientRow to generate a table row for each patient appointment
  import { createPatientRow } from '../components/patientRow.js';


  // Get the table body where patient rows will be added
  const tableBody = document.getElementById('patientTableBody');

  // Initialize selectedDate with today's date in 'YYYY-MM-DD' format
  let selectedDate = new Date().toISOString().split('T')[0];
  // Get the saved token from localStorage (used for authenticated API calls)
  const token = localStorage.getItem('token');
  // Initialize patientName to null (used for filtering by name)
  let patientName = "null";


  // Add an 'input' event listener to the search bar
  document.getElementById('searchBar').addEventListener('input', (event) => {
  // On each keystroke:
  //   - Trim and check the input value
    const input = event.target.value.trim();
  //   - If not empty, use it as the patientName for filtering
    if (input !== '') {
      patientName = input;
    }
  //   - Else, reset patientName to "null" (as expected by backend)
    else {
      patientName = "null";
    }
  //   - Reload the appointments list with the updated filter
    loadAppointments();
  });


  // Add a click listener to the "Today" button
  document.getElementById('todayButton').addEventListener('click', () => {
  // When clicked:
  //   - Set selectedDate to today's date
    selectedDate = new Date().toISOString().split('T')[0];
  //   - Update the date picker UI to match
    document.getElementById('datePicker').value = selectedDate;
  //   - Reload the appointments for today
    loadAppointments();

  // Add a change event listener to the date picker
  });

  document.getElementById('datePicker').addEventListener('change', (event) => {

  // When the date changes:
    selectedDate = event.target.value;
  //   - Update selectedDate with the new value
  //   - Reload the appointments for that specific date
    loadAppointments();
  });

  // Function: loadAppointments
  async function loadAppointments() {
  // Purpose: Fetch and display appointments based on selected date and optional patient name

  // Step 1: Call getAllAppointments with selectedDate, patientName, and token
  const appointments = await getAllAppointments(selectedDate, patientName, token);
  try {
    // Step 2: Clear the table body content before rendering new rows
    tableBody.innerHTML = '';
    if (appointments.length === 0) {
  // Step 3: If no appointments are returned:
      //   - Display a message row: "No Appointments found for today."
      const messageRow = createPatientRow({
        id: null,
        name: 'No Appointments found for today.',
        phone: '',
        email: ''
      });
      tableBody.appendChild(messageRow);
    } else {
      appointments.forEach(appointment => {
        //   - Loop through each appointment and construct a 'patient' object with id, name, phone, and email
        const patient = {
          id: appointment.id,
          name: appointment.patientName,
          phone: appointment.patientPhone,
          email: appointment.patientEmail
        };
        //   - Call createPatientRow to generate a table row for the appointment
        const row = createPatientRow(patient);
        //   - Append each row to the table body
        tableBody.appendChild(row);
      });
    }

  // Step 5: Catch and handle any errors during fetch:
  //   - Show a message row: "Error loading appointments. Try again later."
  } catch (error) {
    const messageRow = createPatientRow({
      id: null,
      name: 'Error loading appointments. Try again later.',
      phone: '',
      email: ''
    });
    tableBody.appendChild(messageRow);
  }
  }

  // When the page is fully loaded (DOMContentLoaded):
  //   - Call renderContent() (assumes it sets up the UI layout)
  document.addEventListener('DOMContentLoaded', () => {
    renderContent();
  //   - Call loadAppointments() to display today's appointments by default
    loadAppointments();
  });
