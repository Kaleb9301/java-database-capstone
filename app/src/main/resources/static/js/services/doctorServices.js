// doctorServices.js

import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = `${API_BASE_URL}/doctor`;

/**
 * Fetch all doctors from the server.
 * @returns {Promise<Array>} Array of doctors or an empty array if error.
 */
export async function getDoctors() {
  try {
    const response = await fetch(`${DOCTOR_API}/all`);
    const data = await response.json();
    return data.doctors || [];
  } catch (error) {
    console.error("Failed to fetch doctors:", error);
    return [];
  }
}

/**
 * Delete a doctor by ID.
 * @param {string} doctorId - The doctor's ID.
 * @param {string} token - Admin's JWT token for authentication.
 * @returns {Promise<{ success: boolean, message: string }>}
 */
export async function deleteDoctor(doctorId, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/delete/${doctorId}/${token}`, {
      method: "DELETE",
    });
    const result = await response.json();
    return { success: response.ok, message: result.message };
  } catch (error) {
    console.error("Failed to delete doctor:", error);
    return { success: false, message: "Delete failed. Try again later." };
  }
}

/**
 * Save a new doctor.
 * @param {Object} doctor - The doctor object to save.
 * @param {string} token - Admin's JWT token for authentication.
 * @returns {Promise<{ success: boolean, message: string }>}
 */
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/save/${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(doctor),
    });
    const result = await response.json();
    return { success: response.ok, message: result.message };
  } catch (error) {
    console.error("Failed to save doctor:", error);
    return { success: false, message: "Failed to save doctor." };
  }
}

/**
 * Filter doctors based on name, time (AM/PM), and specialty.
 * @param {string} name 
 * @param {string} time 
 * @param {string} specialty 
 * @returns {Promise<Array>} Filtered doctor list or empty array.
 */
export async function filterDoctors(name, time, specialty) {
  try {
    const response = await fetch(`${DOCTOR_API}/filter/${name}/${time}/${specialty}`);
    if (response.ok) {
      const data = await response.json();
      return data.doctors || [];
    } else {
      console.error("Filter request failed:", response.statusText);
      return [];
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    return [];
  }
}
