package com.example.ruhan_2207088_hospital_management_system;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class PatientDashboardActivity extends AppCompatActivity {

    private TextView lblPatientName;
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);

        lblPatientName = findViewById(R.id.lblPatientName);


        String name = getIntent().getStringExtra("p_name");
        patientId = getIntent().getStringExtra("p_id");

        if (name != null) {
            lblPatientName.setText("Welcome, " + name);
        }

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            finish();
        });
    }
}
