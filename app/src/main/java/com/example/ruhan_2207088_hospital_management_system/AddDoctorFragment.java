package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctorFragment extends Fragment {

    private EditText etName, etPhone, etSpec, etEmail, etSchedule;
    private DatabaseReference mDatabase;
    private boolean isEditMode = false;
    private String existingDocId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Inflate the layout
        View view = inflater.inflate(R.layout.activity_add_doctor, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("doctors");

        // 2. Initialize Views
        etName = view.findViewById(R.id.txtDoctorName);
        etPhone = view.findViewById(R.id.txtPhoneNumber);
        etSpec = view.findViewById(R.id.txtSpecialization);
        etEmail = view.findViewById(R.id.txtEmail);
        etSchedule = view.findViewById(R.id.txtSchedule);
        Button btnAdd = view.findViewById(R.id.btnAddDoctor);

        // 3. Handle Edit Mode via Arguments (Bundle)
        if (getArguments() != null && getArguments().containsKey("isEdit")) {
            isEditMode = true;
            existingDocId = getArguments().getString("doctorId");

            etName.setText(getArguments().getString("name"));
            etPhone.setText(getArguments().getString("phone"));
            etSpec.setText(getArguments().getString("spec"));
            etEmail.setText(getArguments().getString("email"));
            etSchedule.setText(getArguments().getString("schedule"));

            btnAdd.setText("Update Doctor Info");
        }


        btnAdd.setOnClickListener(v -> saveDoctor());



        return view;
    }

    private void saveDoctor() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String spec = etSpec.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String schedule = etSchedule.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || spec.isEmpty()) {
            Toast.makeText(getContext(), "Required fields missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String doctorId;
            if (isEditMode) {
                doctorId = existingDocId;
            } else {
                String cleanName = name.replaceAll("[.#$\\[\\] ]", "");
                String lastThree = phone.length() >= 3 ? phone.substring(phone.length() - 3) : phone;
                doctorId = cleanName + "_" + lastThree;
            }

            // Using "doctor" as the default password for new doctors
            Doctor doctorObj = new Doctor(doctorId, name, phone, spec, email, schedule, "doctor");

            mDatabase.child(doctorId).setValue(doctorObj)
                    .addOnSuccessListener(aVoid -> {
                        String msg = isEditMode ? "Doctor Updated!" : "Doctor Added!";
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                        if (isEditMode) {
                            getActivity().getOnBackPressedDispatcher().onBackPressed();
                        } else {
                            clearFields();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etSpec.setText("");
        etEmail.setText("");
        etSchedule.setText("");
        etName.requestFocus();
    }
}