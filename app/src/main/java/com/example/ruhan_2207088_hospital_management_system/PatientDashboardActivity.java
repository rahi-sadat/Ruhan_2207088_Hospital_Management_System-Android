package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class PatientDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String loggedInId, loggedInName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_dashboard);

        // 1. Get Data from Intent
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

        // 3. Setup Header View
        View headerView = navigationView.getHeaderView(0);
        TextView lblPatientName = headerView.findViewById(R.id.lblPatientName);
        if (loggedInName != null) {
            lblPatientName.setText("Welcome, " + loggedInName);
        }

        // 4. Navigation Selection Logic
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;
            String title = "Patient Dashboard";

            if (id == R.id.nav_patient_profile) {
                selectedFragment = new PatientProfileFragment();
                title = "My Profile";
            } else if (id == R.id.nav_book_appointment) {
                selectedFragment = new BookAppointmentFragment();
                title = "Book Appointment";
            } else if (id == R.id.nav_medical_reports) {

                selectedFragment = new MedicalReportsFragment();
                title = "Medical Reports";

                // Pass BOTH IDs to be safe
                Bundle args = new Bundle();
                args.putString("patientId", loggedInId);

                // IMPORTANT: In a real app, you'd select an appointment first.
                // For now, we use patientId as the reference for the report.
                args.putString("appointmentId", loggedInId);
                selectedFragment.setArguments(args);

                }
            else if (id == R.id.nav_patient_billing) {
                // NEW: Handle Billing Section
                selectedFragment = new PatientBillingFragment();
                title = "My Bills";
            } else if (id == R.id.nav_patient_logout) {
                finish();
                return true;
            }

            if (selectedFragment != null) {
                // Pass Patient ID to the fragment
                Bundle args = new Bundle();
                args.putString("patientId", loggedInId);
                selectedFragment.setArguments(args);

                // Perform the Fragment Swap
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.patient_content_area, selectedFragment)
                        .addToBackStack(null) // Allows user to go back
                        .commit();

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Load Default Fragment
        if (savedInstanceState == null) {
            loadProfileFragment();
            navigationView.setCheckedItem(R.id.nav_patient_profile);
        }
    }

    private void loadProfileFragment() {
        PatientProfileFragment fragment = new PatientProfileFragment();
        Bundle args = new Bundle();
        args.putString("patientId", loggedInId);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.patient_content_area, fragment)
                .commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Profile");
        }
    }
}