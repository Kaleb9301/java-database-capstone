# Admin User Stories

---

## **User Story 1**

**Title:**  
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**  
1. Admin can access the login form.  
2. Admin can log in using a valid username and password.  
3. Admin is redirected to the admin dashboard upon successful login.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Invalid credentials should return a clear error message.  
- Authentication should use secure password hashing.

---

## **User Story 2**

**Title:**  
_As an admin, I want to log out of the portal, so that I can protect system access._

**Acceptance Criteria:**  
1. Admin can click a logout button from any page.  
2. Admin session is terminated after logout.  
3. Admin is redirected to the login page after logout.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Automatic logout should occur after a session timeout.  
- Logout should clear all sensitive session data.

---

## **User Story 3**

**Title:**  
_As an admin, I want to add doctors to the portal, so that they can be registered and available for appointments._

**Acceptance Criteria:**  
1. Admin can open and view a form to register a doctor.  
2. Admin can submit the form with details like name, specialization, and contact.  
3. The new doctor is added to the system and visible in the list.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Form should validate required fields before submission.  
- Duplicate doctor entries should be prevented.

---

## **User Story 4**

**Title:**  
_As an admin, I want to delete a doctor's profile from the portal, so that I can remove outdated or incorrect information._

**Acceptance Criteria:**  
1. Admin can view the list of doctors with a delete option.  
2. Admin is prompted to confirm the deletion.  
3. The selected doctor is removed from the system after confirmation.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Deletion

---
## **User Story 5**

**Title:**  
_As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track platform usage statistics._

**Acceptance Criteria:**  
1. A stored procedure exists in the MySQL database to count appointments per month.  
2. Admin can access and execute the stored procedure from the MySQL CLI.  
3. The procedure returns monthly appointment counts accurately.

**Priority:** Medium  
**Story Points:** 5  
**Notes:**  
- Result should include year and month columns.  
- The procedure should handle months with zero appointments gracefully.  
- Output should be clear and exportable if needed.


# Patient User Stories

---

## **User Story 1**

**Title:**  
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**  
1. Patients can access a public page listing available doctors.  
2. Doctor list displays key information (name, specialization, availability).  
3. No login or registration is required to view this list.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Ensure sensitive contact or scheduling info is hidden until login.  
- Enable filtering by specialization or availability.

---

## **User Story 2**

**Title:**  
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**  
1. Patient can access a registration form.  
2. Form includes fields for email, password, and basic profile info.  
3. Registration creates a new user account and stores credentials securely.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Validate email format and password strength.  
- Send confirmation email upon successful signup.

---

## **User Story 3**

**Title:**  
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**  
1. Login form is accessible to registered users.  
2. Patients can log in using email and password.  
3. Logged-in users are redirected to their dashboard.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Implement session management and authentication guard for protected routes.  
- Show login errors clearly when credentials are invalid.

---

## **User Story 4**

**Title:**  
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**  
1. Patients can see and use a logout button.  
2. Logging out clears the session and redirects to the homepage or login screen.  
3. After logout, protected pages are no longer accessible.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Implement session expiration for added security.  

---

## **User Story 5**

**Title:**  
_As a patient, I want to book an hour-long appointment with a doctor after logging in, so that I can consult with them._

**Acceptance Criteria:**  
1. Patient must be logged in to book an appointment.  
2. Patient selects a doctor and available time slot.  
3. System saves a one-hour appointment and confirms the booking.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Prevent double-booking of time slots.  
- Send confirmation email or notification.

---

## **User Story 6**

**Title:**  
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**  
1. Logged-in patients can view a list of their future appointments.  
2. Each appointment shows the doctor, date, time, and status.  
3. List is sorted by date and filters past appointments.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Add calendar or reminder integration if possible.  
- Allow cancellation or rescheduling (future enhancement).

# Define Doctor User Stories

---

## **User Story 1**

**Title:**  
_As a doctor, I want to log into the portal to manage my appointments._

**Acceptance Criteria:**  
1. Doctor can access the login form.  
2. Doctor can log in using valid credentials.  
3. Doctor is redirected to the dashboard upon successful login.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Ensure credentials are securely authenticated.  
- Doctor role must be verified upon login.

---

## **User Story 2**

**Title:**  
_As a doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**  
1. Doctor can click a logout button from any page.  
2. Session is destroyed upon logout.  
3. Doctor is redirected to the login or home page after logout.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Session timeout should also trigger automatic logout.  
- All session data must be securely cleared.

---

## **User Story 3**

**Title:**  
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**  
1. Logged-in doctor can access an appointment calendar view.  
2. Calendar displays scheduled appointments by date and time.  
3. Appointment entries include patient name and appointment status.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Allow calendar to be viewed by day, week, or month.  
- Consider integrating with Google Calendar or exporting data.

---

## **User Story 4**

**Title:**  
_As a doctor, I want to mark my unavailability, so that patients only see the available slots._

**Acceptance Criteria:**  
1. Doctor can open a form or calendar to select unavailable dates/times.  
2. Marked unavailable slots are removed from the patient booking view.  
3. Unavailability is saved and reflected in real-time.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Prevent patients from booking during unavailable periods.  
- Allow doctors to edit or remove unavailable slots.

---

## **User Story 5**

**Title:**  
_As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**  
1. Doctor can access and edit profile information.  
2. Fields include name, specialization, phone, and bio.  
3. Changes are saved and updated across the platform.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Profile updates should be validated before saving.  
- Consider admin approval for major changes.

---

## **User Story 6**

**Title:**  
_As a doctor, I want to view the patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**  
1. Doctor can view a list of upcoming appointments.  
2. Each appointment includes patient details (name, contact, reason for visit).  
3. Data is accessible securely and only by the assigned doctor.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Ensure HIPAA compliance or relevant data privacy protection.  
- Patient details must be read-only for doctors.




# User Story Template

**Title:**
_As a [user role], I want [feature/goal], so that [reason]._

**Acceptance Criteria:**
1. [Criteria 1]
2. [Criteria 2]
3. [Criteria 3]

**Priority:** [High/Medium/Low]
**Story Points:** [Estimated Effort in Points]
**Notes:**
- [Additional information or edge cases]

