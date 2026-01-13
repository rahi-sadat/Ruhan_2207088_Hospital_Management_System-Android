package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class PatientDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String loggedInId, loggedInName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);

        // 1. Get Data from Intent (Keys matched with PatientLoginActivity)
        loggedInId = getIntent().getStringExtra("p_id");
        loggedInName = getIntent().getStringExtra("p_name");

        // 2. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.patient_drawer_layout);
        NavigationView navigationView = findViewById(R.id.patient_nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        View headerView = navigationView.getHeaderView(0);
        TextView lblPatientName = headerView.findViewById(R.id.lblPatientName);
        if (loggedInName != null) {
            lblPatientName.setText("Welcome, " + loggedInName);
        }


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_patient_profile) {
                loadProfileFragment();
            } else if (id == R.id.nav_book_appointment) {

                BookAppointmentFragment bookFragment = new BookAppointmentFragment();


                Bundle args = new Bundle();
                args.putString("patientId", loggedInId);
                bookFragment.setArguments(args);


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.patient_content_area, bookFragment)
                        .commit();


                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle("Book Appointment");
                }

            } else if (id == R.id.nav_patient_logout) {
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        if (savedInstanceState == null) {
            loadProfileFragment();
            navigationView.setCheckedItem(R.id.nav_patient_profile);
        }
    }

    private void loadProfileFragment() {

        PatientProfileFragment fragment = new PatientProfileFragment();
        Bundle args = new Bundle();
        args.putString("patientId", loggedInId); // Fragment expects "patientId"
        fragment.setArguments(args);

        // Perform the swap
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.patient_content_area, fragment)
                .commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Profile");
        }
    }
}