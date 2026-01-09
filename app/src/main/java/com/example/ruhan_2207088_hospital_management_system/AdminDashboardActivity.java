package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard); // shuru te eita must
        findViewById(R.id.btnViewPatients).setOnClickListener(v -> {

            Intent intent = new Intent(AdminDashboardActivity.this, ViewPatientsActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnAdminLogout).setOnClickListener(v -> finish());


    }
}
