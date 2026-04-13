
  // Import the base API URL from the config fil
  import { API_BASE_URL } from '../config/config.js';

  // Define a constant DOCTOR_API to hold the full endpoint for doctor-related actions
  const DOCTOR_API = `${API_BASE_URL}/doctor`;
  


  // Function: getDoctors
  async function getDoctors() {
  // Purpose: Fetch the list of all doctors from the API
    try {
    //  Use fetch() to send a GET request to the DOCTOR_API endpoint
    const response = await fetch(DOCTOR_API);

    //  Convert the response to JSON
    const data = await response.json();

    //  Return the 'doctors' array from the response
    return data.doctors || [];
  } catch (error) {
    console.error('Error fetching doctors:', error);
    return [];
  }
  }
  //  If there's an error (e.g., network issue), log it and return an empty array

  // Export the getDoctors function for use in other modules
  export { getDoctors };


  // Function: deleteDoctor
  async function deleteDoctor(doctorId, token) {
  // Purpose: Delete a specific doctor using their ID and an authentication token
    try {
    //  Use fetch() with the DELETE method
    //   - The URL includes the doctor ID and token as path parameters
    const response = await fetch(`${DOCTOR_API}/${doctorId}?token=${token}`, {
      method: 'DELETE'
    });

    //  Convert the response to JSON
    const data = await response.json();
  //  Return an object with:
  //   - success: true if deletion was successful
  //   - message: message from the server
    return {
      success: response.ok,
      message: data.message
    };
  
  //  If an error occurs, log it and return a default failure response
  } catch (error) {
    console.error('Error deleting doctor:', error);
    return {  success: false, message: 'An error occurred while deleting the doctor.' };    
  }

  }

  // Export the deleteDoctor function for use in other modules
  export { deleteDoctor };

  // Function: saveDoctor
  async function saveDoctor(doctor, token) {
  // Purpose: Save (create) a new doctor using a POST request
    try {
  //  Use fetch() with the POST method
  //   - URL includes the token in the path
  //   - Set headers to specify JSON content type
  //   - Convert the doctor object to JSON in the request body
    const response = await fetch(`${DOCTOR_API}?token=${token}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(doctor)
    });

  //  Parse the JSON response and return:
  //   - success: whether the request succeeded
  //   - message: from the server
    const data = await response.json();
    return {
      success: response.ok,
      message: data.message
    };
  } catch (error) {
    console.error('Error saving doctor:', error);
    return { success: false, message: 'An error occurred while saving the doctor.' };
  }
  }
  //  Catch and log errors
  //   - Return a failure response if an error occurs

  // Export the saveDoctor function for use in other modules
  export { saveDoctor };

  // Function: filterDoctors
  async function filterDoctors(name, time, specialty) {
  // Purpose: Fetch doctors based on filtering criteria (name, time, and specialty)
    try {

  //  Use fetch() with the GET method
  //   - Include the name, time, and specialty as URL path parameters
  //  Check if the response is OK
  //   - If yes, parse and return the doctor data
  //   - If no, log the error and return an object with an empty 'doctors' array
    const response = await fetch(`${DOCTOR_API}/filter/${encodeURIComponent(name)}/${encodeURIComponent(time)}/${encodeURIComponent(specialty)}`);
    console.log("Filter Doctors Response Status:", response.status);
    console.log(`${DOCTOR_API}/filter/${encodeURIComponent(name)}/${encodeURIComponent(time)}/${encodeURIComponent(specialty)}`);
    console.log("Filter Doctors Response Status:", response.status);
    if (response.ok) {
      const data = await response.json();
      console.log("Filtered Doctors Data:", data);
      return data;
    } else {
      console.error('Error filtering doctors:', response.statusText);
      return { doctors: [] };
    }

  //  Catch any other errors, alert the user, and return a default empty result
  } catch (error) {
    console.error('Error filtering doctors:', error);
    return { doctors: [] };
  }
}

  // Export the filterDoctors function for use in other modules
  export { filterDoctors };
