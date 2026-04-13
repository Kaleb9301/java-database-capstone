//
//   Import the openModal function to handle showing login popups/modals
import { openModal } from '../components/modals.js';
//   Import the base API URL from the config file
import { API_BASE_URL } from '../config/config.js';
//   Define constants for the admin and doctor login API endpoints using the base URL
const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login';
//   Use the window.onload event to ensure DOM elements are available after page load
window.onload = function() {
//   Inside this function:
//     - Select the "adminLogin" and "doctorLogin" buttons using getElementById
  const adminLoginBtn = document.getElementById('adminLogin');
  console.log("Admin Login Button:", adminLoginBtn);
  const doctorLoginBtn = document.getElementById('doctorLogin');
//     - If the admin login button exists:
  if (adminLoginBtn) {
    adminLoginBtn.addEventListener('click', () => {
      openModal('adminLogin');
    });
  }
//         - Add a click event listener that calls openModal('adminLogin') to show the admin login modal
//     - If the doctor login button exists:
//         - Add a click event listener that calls openModal('doctorLogin') to show the doctor login modal
  if (doctorLoginBtn) {
    doctorLoginBtn.addEventListener('click', () => {
      openModal('doctorLogin');
    });
  }
};

//   Define a function named adminLoginHandler on the global window object
window.adminLoginHandler = async function() {
//   This function will be triggered when the admin submits their login credentials

//   Step 1: Get the entered username and password from the input fields
  const username = document.getElementById('username').value;
  const password = document.getElementById('password').value;

//   Step 2: Create an admin object with these credentials
  const admin = {
    username: username,
    password: password
  };

//   Step 3: Use fetch() to send a POST request to the ADMIN_API endpoint
//     - Set method to POST
//     - Add headers with 'Content-Type: application/json'
//     - Convert the admin object to JSON and send in the body
console.log("Admin login attempt with username:", username);
  try {
    const response = await fetch(ADMIN_API + '/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(admin)
    });

//   Step 4: If the response is successful:
//     - Parse the JSON response to get the token
//     - Store the token in localStorage
//     - Call selectRole('admin') to proceed with admin-specific behavior
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      selectRole('admin');
    }

//   Step 5: If login fails or credentials are invalid:
//     - Show an alert with an error message
    else {
      alert('Invalid admin credentials. Please try again.');
    }

//   Step 6: Wrap everything in a try-catch to handle network or server errors
//     - Show a generic error message if something goes wrong
  } catch (error) {
    console.error('Error during admin login:', error);
    alert('An error occurred. Please try again later.');
  }
};
//   Define a function named doctorLoginHandler on the global window object
window.doctorLoginHandler = async function() {
//   This function will be triggered when a doctor submits their login credentials

//   Step 1: Get the entered email and password from the input fields
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

//   Step 2: Create a doctor object with these credentials
  const doctor = {
    email: email,
    password: password
  };

//   Step 3: Use fetch() to send a POST request to the DOCTOR_API endpoint
//     - Include headers and request body similar to admin login
  console.log("Doctor login attempt with email:", email);
  try {
    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(doctor)
    });

//   Step 4: If login is successful:
//     - Parse the JSON response to get the token
//     - Store the token in localStorage
//     - Call selectRole('doctor') to proceed with doctor-specific behavior
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('token', data.token);
      selectRole('doctor');
    }


//   Step 5: If login fails:
//     - Show an alert for invalid credentials
    else {
      console.log("Doctor login failed with status:", response.status);
      console.log("Response text:", await response.text());
      alert('Invalid doctor credentials. Please try again.');
    }

//   Step 6: Wrap in a try-catch block to handle errors gracefully
//     - Log the error to the console
//     - Show a generic error message
  } catch (error) {
    console.error('Error during doctor login:', error);
    alert('An error occurred. Please try again later.');
  }
};
