package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat; // Added for better drawer control
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String loggedInDoctorId;
    private TextView lblName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        // 1. Get Data from Intent (Using your working method)
        loggedInDoctorId = getIntent().getStringExtra("doctorId");
        String nameFromLogin = getIntent().getStringExtra("docName");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // 2. Setup Header - Immediate update to prevent empty view
        View headerView = navigationView.getHeaderView(0);
        lblName = headerView.findViewById(R.id.lblDoctorName);

        if (nameFromLogin != null && lblName != null) {
            lblName.setText("Welcome, " + nameFromLogin);
        }

        // 3. Optional: Real-time update in background
        fetchDoctorInfo();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 4. Fragment Loading - Added check to prevent crash if XML ID is wrong
        if (savedInstanceState == null && findViewById(R.id.fragment_container) != null) {
            loadFragment(new DoctorProfileFragment(), "Profile");
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;
            String title = "Dashboard";

            if (id == R.id.nav_profile) {
                selectedFragment = new DoctorProfileFragment();
                title = "My Profile";
            } else if (id == R.id.nav_appointments) {
                selectedFragment = new DoctorAppointmentsFragment();
                title = "Patient Appointments";
            } else if (id == R.id.nav_logout) {
                handleLogout();
                return true;
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, title);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void fetchDoctorInfo() {
        if (loggedInDoctorId == null || lblName == null) return;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("doctors").child(loggedInDoctorId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isFinishing() && snapshot.exists() && lblName != null) {
                    String name = snapshot.child("name").getValue(String.class);
                    lblName.setText("Welcome, " + name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadFragment(Fragment fragment, String title) {
        if (findViewById(R.id.fragment_container) == null) return;

        Bundle args = new Bundle();
        args.putString("doctorId", loggedInDoctorId);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss(); // More stable for quick transitions

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void handleLogout() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}