package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class PatientLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login); // shuru te eita must

        findViewById(R.id.btnBackToMain).setOnClickListener(v->finish());

        findViewById(R.id.btnCreateAccount).setOnClickListener(v -> {
            Intent intent = new Intent(this, PatientRegistrationActivity.class);
            startActivity(intent);   // next page e jabo
        });
    }
}