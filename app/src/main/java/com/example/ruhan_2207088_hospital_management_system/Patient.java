package com.example.ruhan_2207088_hospital_management_system;

public class Patient {

    public String patientId;
    public String password;
    public String name;
    public String phoneNumber;
    public String age;
    public String gender;
    public String bloodGroup;
    public String medicalHistory;
    public String reportPath;

      // firebase er jonno
    public Patient() {}
    // constructor
    public Patient(String patientId, String password, String name, String phoneNumber,
                   String age, String gender, String bloodGroup, String medicalHistory) {
        this.patientId = patientId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.medicalHistory = medicalHistory;
        this.reportPath = "";
    }
}
