package com.example.ruhan_2207088_hospital_management_system;

public class Appointment {
    public String appointmentId, patientId, doctorId, doctorName, date, status;

    public Appointment() {}

    public Appointment(String appointmentId, String patientId, String doctorId, String doctorName, String date, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.status = status; // "Pending", "Approved", or "Rejected"
    }
}
