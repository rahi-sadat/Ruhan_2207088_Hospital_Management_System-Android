package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_patient) {
                startActivity(new Intent(this, PatientLoginActivity.class));
            } else if (id == R.id.nav_doctor) {
                startActivity(new Intent(this, DoctorLoginActivity.class));
            } else if (id == R.id.nav_admin) {
                startActivity(new Intent(this, AdminLoginActivity.class));
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }
}