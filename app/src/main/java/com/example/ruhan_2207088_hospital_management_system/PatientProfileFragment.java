package com.example.ruhan_2207088_hospital_management_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.*;

public class PatientProfileFragment extends Fragment {

    private TextView lblId, lblName, lblPhone, lblAgeGender, lblBlood, txtHistory;
    private ImageButton btnEdit;
    private DatabaseReference mDatabase;
    private String patientId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);


        lblId = view.findViewById(R.id.lblId);
        lblName = view.findViewById(R.id.lblName);
        lblPhone = view.findViewById(R.id.lblPhone);
        lblAgeGender = view.findViewById(R.id.lblAgeGender);
        lblBlood = view.findViewById(R.id.lblBlood);
        txtHistory = view.findViewById(R.id.txtHistory);


        btnEdit = view.findViewById(R.id.btnEditProfile);


        if (getArguments() != null) {
            patientId = getArguments().getString("patientId");
        }


        btnEdit.setOnClickListener(v -> {
            if (patientId != null) {
                Intent intent = new Intent(getActivity(), EditPatientProfileActivity.class);
                intent.putExtra("pId", patientId);
                startActivity(intent);
            }
        });

        // 6. Initial data load
        loadPatientData();

        return view;
    }

    private void loadPatientData() {
        if (patientId == null) return;

        mDatabase = FirebaseDatabase.getInstance().getReference("patients").child(patientId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Mapping Firebase keys to TextViews
                    lblId.setText(patientId);
                    lblName.setText(snapshot.child("name").getValue(String.class));
                    lblPhone.setText(snapshot.child("phoneNumber").getValue(String.class));

                    String age = snapshot.child("age").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    lblAgeGender.setText(age + " / " + gender);

                    lblBlood.setText(snapshot.child("bloodGroup").getValue(String.class));
                    txtHistory.setText(snapshot.child("medicalHistory").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }

    // This ensures that when the user finishes editing and comes back,
    // the profile screen updates with the new information immediately.
    @Override
    public void onResume() {
        super.onResume();
        loadPatientData();
    }
}