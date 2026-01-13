package com.example.ruhan_2207088_hospital_management_system;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookAppointmentFragment extends Fragment {

    private RecyclerView rvDoctors;
    private TextView lblSelectedDate;
    private Button btnSelectDate, btnSubmitRequest;

    private String patientId, selectedDocId, selectedDocName, selectedDate;
    private List<Doctor> doctorList = new ArrayList<>();
    private DoctorSelectAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_appointment, container, false);

        if (getArguments() != null) {
            patientId = getArguments().getString("patientId");
        }

        rvDoctors = view.findViewById(R.id.rvDoctors);
        lblSelectedDate = view.findViewById(R.id.lblSelectedDate);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnSubmitRequest = view.findViewById(R.id.btnSubmitRequest);

        rvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DoctorSelectAdapter(doctorList, doc -> {
            selectedDocId = doc.doctorId;
            selectedDocName = doc.name;
            Toast.makeText(getContext(), "Selected: " + selectedDocName, Toast.LENGTH_SHORT).show();
        });
        rvDoctors.setAdapter(adapter);

        btnSelectDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(requireContext(), (view1, year, month, day) -> {
                // Formatting date to match your Firebase screenshot (D/M/YYYY)
                selectedDate = day + "/" + (month + 1) + "/" + year;
                lblSelectedDate.setText("Date: " + selectedDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePicker.show();
        });

        btnSubmitRequest.setOnClickListener(v -> handleBooking());

        fetchDoctors();

        return view;
    }

    private void fetchDoctors() {
        FirebaseDatabase.getInstance().getReference("doctors")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        doctorList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Doctor d = ds.getValue(Doctor.class);
                            if (d != null) doctorList.add(d);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) {
                        if(getContext() != null) Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleBooking() {
        if (selectedDocId == null || selectedDate == null) {
            Toast.makeText(getContext(), "Select Doctor and Date first!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("appointments");
        String appId = db.push().getKey();

        if (appId != null) {
            // FIX: Using the setters that match your new Appointment Model
            Appointment app = new Appointment();
            app.setAppointmentId(appId);     // Matches Firebase "appointmentId"
            app.setPatientId(patientId);       // Matches Firebase "patientId"
            app.setDoctorId(selectedDocId);    // Matches Firebase "doctorId"
            app.setDoctorName(selectedDocName);// Matches Firebase "doctorName"
            app.setDate(selectedDate);         // Matches Firebase "date"
            app.setStatus("Pending");          // Matches Firebase "Pending" (Case Sensitive)

            db.child(appId).setValue(app).addOnSuccessListener(unused -> {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Request sent to Admin!", Toast.LENGTH_LONG).show();
                }
                lblSelectedDate.setText("No date selected");
                selectedDate = null;
            }).addOnFailureListener(e -> {
                if (getContext() != null) Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}