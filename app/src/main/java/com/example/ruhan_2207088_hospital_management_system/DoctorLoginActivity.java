package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class DoctorLoginActivity extends AppCompatActivity {

    private EditText etLoginId, etPassword;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_login);

        mDatabase = FirebaseDatabase.getInstance().getReference("doctors");


        etLoginId = findViewById(R.id.txtDoctorId);
        etPassword = findViewById(R.id.txtDoctorPass);

        findViewById(R.id.btnDoctorLogin).setOnClickListener(v -> loginDoctor());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginDoctor() {
        String loginId = etLoginId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (loginId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter ID and Password", Toast.LENGTH_SHORT).show();
            return;
        }


        mDatabase.child(loginId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String dbPassword = snapshot.child("password").getValue(String.class);

                    if (dbPassword != null && password.equals(dbPassword)) {
                        Toast.makeText(DoctorLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DoctorLoginActivity.this, DoctorDashboard.class);


                        intent.putExtra("doctorId", loginId);


                        intent.putExtra("docName", snapshot.child("name").getValue(String.class));

                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(DoctorLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DoctorLoginActivity.this, "Doctor ID not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoctorLoginActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}