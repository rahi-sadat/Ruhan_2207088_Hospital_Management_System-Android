package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText txtAdminId, txtAdminPass;
    private Button btnAdminLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login); // shuru te eita must
        findViewById(R.id.btnBack).setOnClickListener(v->finish());


        txtAdminId = findViewById(R.id.txtAdminId);
        txtAdminPass = findViewById(R.id.txtAdminPass);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

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
    }


}
