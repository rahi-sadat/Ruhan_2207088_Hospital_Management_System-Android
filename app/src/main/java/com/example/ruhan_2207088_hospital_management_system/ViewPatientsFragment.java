package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPatientsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private ArrayList<Patient> patientList;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_view_patients, container, false);


        recyclerView = view.findViewById(R.id.recyclerViewPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        patientList = new ArrayList<>();
        adapter = new PatientAdapter(patientList);
        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference("patients");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Patient p = data.getValue(Patient.class);
                    if (p != null) {
                        patientList.add(p);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




        return view;
    }
}