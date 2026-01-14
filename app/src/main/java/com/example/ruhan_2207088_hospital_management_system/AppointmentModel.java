package com.example.ruhan_2207088_hospital_management_system;

public class AppointmentModel {

    private String patientId;
    private String date;
    private String doctorId;
    private String status;
    private String appointmentId;


    private String patientName;


    public AppointmentModel() {}


    public String getPatientId() { return patientId; }
    public String getDate() { return date; }
    public String getDoctorId() { return doctorId; }
    public String getStatus() { return status; }
    public String getAppointmentId() { return appointmentId; }
    public String getPatientName() { return patientName; }


    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setDate(String date) { this.date = date; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setStatus(String status) { this.status = status; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }


    public void setPatientName(String patientName) { this.patientName = patientName; }
}