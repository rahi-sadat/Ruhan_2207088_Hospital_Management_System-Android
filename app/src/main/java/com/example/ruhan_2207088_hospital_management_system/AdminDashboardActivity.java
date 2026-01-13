package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;

public class AdminDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.admin_drawer_layout);
        NavigationView navigationView = findViewById(R.id.admin_nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // FIX: The OnBackPressed logic must be INSIDE onCreate
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;
            String title = "Admin Dashboard";

            if (id == R.id.nav_admin_patients) {
                selectedFragment = new ViewPatientsFragment();
                title = "All Patients";
            } else if (id == R.id.nav_admin_add_doc) {
                selectedFragment = new AddDoctorFragment();
                title = "Add New Doctor";
            } else if (id == R.id.nav_admin_view_doc) {
                selectedFragment = new ViewDoctorsFragment();
                title = "All Doctors";
            } else if (id == R.id.nav_admin_pricing) { // New Pricing ID
                selectedFragment = new AdminPricingFragment();
                title = "Set Fees & Pricing";
            } else if (id == R.id.nav_admin_logout) {
                finish(); // Close dashboard and return to login
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_content_area, selectedFragment)
                        .addToBackStack(null)
                        .commit();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}