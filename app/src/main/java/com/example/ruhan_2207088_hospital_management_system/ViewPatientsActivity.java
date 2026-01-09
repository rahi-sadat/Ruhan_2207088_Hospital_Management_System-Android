package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPatientsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private ArrayList<Patient> patientList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);

        recyclerView = findViewById(R.id.recyclerViewPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        patientList = new ArrayList<>();
        adapter = new PatientAdapter(patientList);
        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("patients");

        // database thake data fetch kortechi
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientList.clear(); // ager data clear kortechi
                for (DataSnapshot data : snapshot.getChildren()) {
                    Patient p = data.getValue(Patient.class);
                    patientList.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewPatientsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnBackToDashboard).setOnClickListener(v -> finish());
    }
}