package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientLoginActivity extends AppCompatActivity {


    private EditText etLoginId, etLoginPass;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login);


        mDatabase = FirebaseDatabase.getInstance().getReference("patients");


        etLoginId = findViewById(R.id.txtPatientId);
        etLoginPass = findViewById(R.id.txtPatientPass);

        findViewById(R.id.btnBackToMain).setOnClickListener(v -> finish());

        findViewById(R.id.btnCreateAccount).setOnClickListener(v -> {
            Intent intent = new Intent(this, PatientRegistrationActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.btnPatientLogin).setOnClickListener(v -> {
            loginPatient();
        });
    }

    private void loginPatient() {
        String id = etLoginId.getText().toString().trim();
        String pass = etLoginPass.getText().toString().trim();

        if (id.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            return;
        }

       // database e search
        mDatabase.child(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {

                    Patient patient = task.getResult().getValue(Patient.class);

                    // password match ditechi
                    if (patient != null && patient.password.equals(pass)) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        // pore jao
                        Intent intent = new Intent(this, PatientDashboardActivity.class);
                        intent.putExtra("p_id", patient.patientId);
                        intent.putExtra("p_name", patient.name);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "ID not found! Please register first.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Database Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}