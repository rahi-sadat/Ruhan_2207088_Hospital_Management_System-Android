package com.example.ruhan_2207088_hospital_management_system;

public class Doctor {
    public String doctorId;
    public String name;
    public String phoneNumber;
    public String specialization;
    public String email;
    public String schedule;


    public Doctor() {
    }


    public Doctor(String doctorId, String name, String phoneNumber, String specialization, String email, String schedule) {
        this.doctorId = doctorId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.specialization = specialization;
        this.email = email;
        this.schedule = schedule;
    }
}