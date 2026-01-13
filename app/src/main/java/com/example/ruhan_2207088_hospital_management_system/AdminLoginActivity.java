package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText txtAdminId, txtAdminPass;
    private Button btnAdminLogin, btnBack; // Added btnBack

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login); // Ensure this matches your styled XML file name

        txtAdminId = findViewById(R.id.txtAdminId);
        txtAdminPass = findViewById(R.id.txtAdminPass);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        btnBack = findViewById(R.id.btnBack); // Initialize the back button

        // Login Logic
        btnAdminLogin.setOnClickListener(v -> {
            String id = txtAdminId.getText().toString().trim();
            String pass = txtAdminPass.getText().toString().trim();

            if (id.equals("admin") && pass.equals("admin")) {
                Toast.makeText(this, "Admin Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        // FIXED: Back Button Logic
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}