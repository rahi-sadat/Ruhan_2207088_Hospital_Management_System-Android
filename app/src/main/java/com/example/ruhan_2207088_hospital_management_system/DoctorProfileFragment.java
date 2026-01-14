package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfileFragment extends Fragment {

    private TextInputEditText displayDocName, displayDocSpecialty;
    private TextView txtHeaderName, txtHeaderSpecialty;
    private String doctorId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);


        if (getArguments() != null) {
            doctorId = getArguments().getString("doctorId");
        }


        txtHeaderName = view.findViewById(R.id.txtDoctorName);
        txtHeaderSpecialty = view.findViewById(R.id.txtSpecialty);
        displayDocName = view.findViewById(R.id.editDocName);
        displayDocSpecialty = view.findViewById(R.id.editDocSpecialty);


        loadDoctorData();

        return view;
    }

    private void loadDoctorData() {
        if (doctorId == null) return;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("doctors").child(doctorId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && isAdded()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String specialty = snapshot.child("specialization").getValue(String.class);


                    txtHeaderName.setText(name);
                    txtHeaderSpecialty.setText(specialty);


                    displayDocName.setText(name);
                    displayDocSpecialty.setText(specialty);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}