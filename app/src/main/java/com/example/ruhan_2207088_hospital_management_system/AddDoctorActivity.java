package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctorActivity extends AppCompatActivity {

    private EditText etName, etPhone, etSpec, etEmail, etSchedule;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        mDatabase = FirebaseDatabase.getInstance().getReference("doctors");

        etName = findViewById(R.id.txtDoctorName);
        etPhone = findViewById(R.id.txtPhoneNumber);
        etSpec = findViewById(R.id.txtSpecialization);
        etEmail = findViewById(R.id.txtEmail);
        etSchedule = findViewById(R.id.txtSchedule);

        findViewById(R.id.btnAddDoctor).setOnClickListener(v -> saveDoctor());
        findViewById(R.id.btnBackToDashboard).setOnClickListener(v -> finish());
    }

    private void saveDoctor() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String spec = etSpec.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String schedule = etSchedule.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || spec.isEmpty()) {
            Toast.makeText(this, "Please fill in Name, Phone, and Specialization", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() < 3) {
            Toast.makeText(this, "Phone number must be at least 3 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            String cleanName = name.replaceAll("[.#$\\[\\] ]", "");
            String lastThree = phone.substring(phone.length() - 3);
            String doctorId = cleanName + "_" + lastThree;


            Doctor doctorObj = new Doctor(doctorId, name, phone, spec, email, schedule);


            mDatabase.child(doctorId).setValue(doctorObj)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddDoctorActivity.this, "Doctor Added!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddDoctorActivity.this, "Firebase Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etSpec.setText("");
        etEmail.setText("");
        etSchedule.setText("");


    }
    }

