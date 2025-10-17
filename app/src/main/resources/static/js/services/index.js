
  // Import the openModal function to handle showing login popups/modals
  import { openModal } from './modal.js';
  // Import the base API URL from the config file
  import { BASE_API_URL } from '../config.js';
  // Define constants for the admin and doctor login API endpoints using the base URL
  const ADMIN_API = `${BASE_API_URL}/admin/login`;
  const DOCTOR_API = `${BASE_API_URL}/doctor/login`;

    window.onload = function () {
      const adminBtn = document.getElementById('adminLogin');
      if (adminBtn) {
      adminBtn.addEventListener('click', () => {
        openModal('adminLogin');
      });
        }     
      const doctorBtn = document.getElementById('doctorLogin');
      if (doctorBtn) {
      doctorBtn.addEventListener('click', () => {
        openModal('doctorLogin');
      });
    }
    }



  // Define a function named adminLoginHandler on the global window object
  window.adminLoginHandler = async function () {
  // This function will be triggered when the admin submits their login credentials
  

  // Step 1: Get the entered username and password from the input fields
  const username = document.getElementById('adminUsername').value;
  const password = document.getElementById('adminPassword').value;

  // Step 2: Create an admin object with these credentials
  const admin = {
    username: username,
    password: password
  };

  // Step 3: Use fetch() to send a POST request to the ADMIN_API endpoint
  //   - Set method to POST
  //   - Add headers with 'Content-Type: application/json'
  //   - Convert the admin object to JSON and send in the body
  try {
    const response = await fetch(ADMIN_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(admin)
    });

  // Step 4: If the response is successful:
  //   - Parse the JSON response to get the token
  //   - Store the token in localStorage
  //   - Call selectRole('admin') to proceed with admin-specific behavior
    if (response.ok) {
      const data = await response.json();
      const token = data.token;
      localStorage.setItem('adminToken', token);
      selectRole('admin');
    }
    

  // Step 5: If login fails or credentials are invalid:
  //   - Show an alert with an error message
    else {
      alert('Invalid admin credentials. Please try again.');
    }

  // Step 6: Wrap everything in a try-catch to handle network or server errors
  //   - Show a generic error message if something goes wrong
  } catch (error) {
    console.error('Error during admin login:', error);
    alert('An error occurred. Please try again later.');
  }
  };

  // Define a function named doctorLoginHandler on the global window object
  // This function will be triggered when a doctor submits their login credentials
  window.doctorLoginHandler = async function () {

  // Step 1: Get the entered email and password from the input fields
  // Step 2: Create a doctor object with these credentials
  const email = document.getElementById('doctorEmail').value;
  const password = document.getElementById('doctorPassword').value;

  // Step 3: Use fetch() to send a POST request to the DOCTOR_API endpoint
  //   - Include headers and request body similar to admin login
  try {
    const response = await fetch(DOCTOR_API, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email, password })
    });

  // Step 4: If login is successful:
  //   - Parse the JSON response to get the token
  //   - Store the token in localStorage
  //   - Call selectRole('doctor') to proceed with doctor-specific behavior
    if (response.ok) {
      const data = await response.json();
      const token = data.token;
      localStorage.setItem('doctorToken', token);
      selectRole('doctor');
    }

  // Step 5: If login fails:
  //   - Show an alert for invalid credentials
    else {
      alert('Invalid doctor credentials. Please try again.');
    }
    
  // Step 6: Wrap in a try-catch block to handle errors gracefully
  //   - Log the error to the console
  //   - Show a generic error message
  } catch (error) {
    console.error('Error during doctor login:', error);
    alert('An error occurred. Please try again later.');
  }
  };
