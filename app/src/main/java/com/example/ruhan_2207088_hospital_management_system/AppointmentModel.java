package com.example.ruhan_2207088_hospital_management_system;

public class AppointmentModel {
    // These match the keys in your Firebase 'appointments' node
    private String patientId;
    private String date;
    private String doctorId;
    private String status;
    private String appointmentId;

    // This field is not in the appointment node, but we use it for the UI
    private String patientName;

    // Required empty constructor for Firebase
    public AppointmentModel() {}

    // GETTERS
    public String getPatientId() { return patientId; }
    public String getDate() { return date; }
    public String getDoctorId() { return doctorId; }
    public String getStatus() { return status; }
    public String getAppointmentId() { return appointmentId; }
    public String getPatientName() { return patientName; }

    // SETTERS (Crucial for Firebase data mapping)
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setDate(String date) { this.date = date; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setStatus(String status) { this.status = status; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    // Setter for manual injection from the 'patients' node
    public void setPatientName(String patientName) { this.patientName = patientName; }
}