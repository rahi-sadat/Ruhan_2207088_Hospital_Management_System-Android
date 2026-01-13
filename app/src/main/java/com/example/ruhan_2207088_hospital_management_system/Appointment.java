package com.example.ruhan_2207088_hospital_management_system;
public class Appointment {
    private String appointmentId;
    private String date;
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String status;
    private double totalBill;
    private String paymentStatus;
    public Appointment() {} // Required


    public String getAppointmentId() { return appointmentId; }
    public String getDate() { return date; }
    public String getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getPatientId() { return patientId; }
    public String getStatus() { return status; }


    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public void setDate(String date) { this.date = date; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalBill() { return totalBill; }
    public void setTotalBill(double totalBill) { this.totalBill = totalBill; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

}