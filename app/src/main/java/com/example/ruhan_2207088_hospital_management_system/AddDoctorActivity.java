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
    private boolean isEditMode = false;
    private String existingDocId = "";

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


        Button btnAdd = findViewById(R.id.btnAddDoctor);

        if (getIntent().hasExtra("isEdit")) {
            isEditMode = true;
            existingDocId = getIntent().getStringExtra("doctorId");

            etName.setText(getIntent().getStringExtra("name"));
            etPhone.setText(getIntent().getStringExtra("phone"));
            etSpec.setText(getIntent().getStringExtra("spec"));
            etEmail.setText(getIntent().getStringExtra("email"));
            etSchedule.setText(getIntent().getStringExtra("schedule"));


            btnAdd.setText("Update Doctor Info");
        }

        btnAdd.setOnClickListener(v -> saveDoctor());
        findViewById(R.id.btnBackToDashboard).setOnClickListener(v -> finish());
    }

    private void saveDoctor() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String spec = etSpec.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String schedule = etSchedule.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || spec.isEmpty()) {
            Toast.makeText(this, "Required fields missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String doctorId;


            if (isEditMode) {
                doctorId = existingDocId;
            } else {
                String cleanName = name.replaceAll("[.#$\\[\\] ]", "");
                String lastThree = phone.length() >= 3 ? phone.substring(phone.length() - 3) : phone;
                doctorId = cleanName + "_" + lastThree;
            }

            Doctor doctorObj = new Doctor(doctorId, name, phone, spec, email, schedule,"doctor");

            mDatabase.child(doctorId).setValue(doctorObj)
                    .addOnSuccessListener(aVoid -> {
                        String msg = isEditMode ? "Doctor Updated!" : "Doctor Added!";
                        Toast.makeText(AddDoctorActivity.this, msg, Toast.LENGTH_SHORT).show();

                        if (isEditMode) {
                            finish();
                        } else {
                            clearFields();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        etName.requestFocus();
    }
}