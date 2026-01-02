package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;



    public class MainActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            findViewById(R.id.btnPatient).setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PatientLoginActivity.class);
                startActivity(intent);
            });

            findViewById(R.id.btnDoctor).setOnClickListener(v->{
              Intent intent = new Intent( this , DoctorLoginActivity.class);
              startActivity(intent);


            });


        }
    }
