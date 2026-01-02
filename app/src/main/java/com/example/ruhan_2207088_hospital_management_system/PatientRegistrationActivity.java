package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast; // Added for alerts
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientRegistrationActivity extends AppCompatActivity {
    private EditText etPass, etName, etPhone, etAge, etHistory;
    private Spinner spGender, spBloodGroup;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_registration_form);

       // database initialization
        mDatabase = FirebaseDatabase.getInstance().getReference("patients");

        // linked
        etPass = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etPatientName);
        etPhone = findViewById(R.id.etPhoneNumber);
        etAge = findViewById(R.id.etAge);
        etHistory = findViewById(R.id.etMedicalHistory);
        spGender = findViewById(R.id.spGender);
        spBloodGroup = findViewById(R.id.spBloodGroup);

        setupSpinners();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            registerPatientInFirebase();
        });
    }

    private void registerPatientInFirebase() {
      // input thake data nilam
        String name = etName.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String history = etHistory.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();
        String blood = spBloodGroup.getSelectedItem().toString();

          // alert
        if (name.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter Name and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        String lastFourDigits = phone.substring(phone.length() - 3);
        String customId = name + "_" + lastFourDigits;


            // patient object
            Patient patient = new Patient(customId, pass, name, phone, age, gender, blood, history);


            mDatabase.child(customId).setValue(patient)
                    .addOnSuccessListener(aVoid -> {

                        Toast.makeText(PatientRegistrationActivity.this,
                                "Registration Successful! ID: " + customId, Toast.LENGTH_LONG).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PatientRegistrationActivity.this,
                                "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

    }

    private void setupSpinners() {
        // Gender Spinner
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);
        spGender.setAdapter(genderAdapter);

        // Blood Group Spinner
        String[] bloodGroups = {"Select Blood Group", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        spBloodGroup.setAdapter(bloodAdapter);
    }
}