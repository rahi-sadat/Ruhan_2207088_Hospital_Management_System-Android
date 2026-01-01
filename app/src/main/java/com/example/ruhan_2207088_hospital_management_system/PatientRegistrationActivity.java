package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class PatientRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This links the Java class to your patient_registration_form.xml layout
        setContentView(R.layout.patient_registration_form);

       //spinners set kori
        setupSpinners();


        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
           // piche jao
            finish();
        });

        // 3. Handle the Register Button
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
           // firebase er logic jabe ekhane
        });
    }

    private void setupSpinners() {
        // Setup Gender Spinner
        Spinner spGender = findViewById(R.id.spGender);
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, genders);
        spGender.setAdapter(genderAdapter);

        // Setup Blood Group Spinner
        Spinner spBloodGroup = findViewById(R.id.spBloodGroup);
        String[] bloodGroups = {"Select Blood Group", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        spBloodGroup.setAdapter(bloodAdapter);
    }
}