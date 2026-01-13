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

        // 1. Get Patient ID passed from Dashboard
        if (getArguments() != null) {
            patientId = getArguments().getString("patientId");
        }

        // 2. Init Views
        rvDoctors = view.findViewById(R.id.rvDoctors);
        lblSelectedDate = view.findViewById(R.id.lblSelectedDate);
        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnSubmitRequest = view.findViewById(R.id.btnSubmitRequest);

        // 3. Setup RecyclerView
        rvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DoctorSelectAdapter(doctorList, doc -> {
            selectedDocId = doc.doctorId;
            selectedDocName = doc.name;
        });
        rvDoctors.setAdapter(adapter);

        // 4. Date Picker Logic
        btnSelectDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(getContext(), (view1, year, month, day) -> {
                selectedDate = day + "/" + (month + 1) + "/" + year;
                lblSelectedDate.setText("Date: " + selectedDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 5. Submit Request
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
                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void handleBooking() {
        if (selectedDocId == null || selectedDate == null) {
            Toast.makeText(getContext(), "Select Doctor and Date first!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("appointments");
        String id = db.push().getKey();


        Appointment app = new Appointment(id, patientId, selectedDocId, selectedDocName, selectedDate, "Pending");

        db.child(id).setValue(app).addOnSuccessListener(unused -> {
            Toast.makeText(getContext(), "Request sent to Admin!", Toast.LENGTH_SHORT).show();
            // Reset UI
            lblSelectedDate.setText("No date selected");
            selectedDate = null;
        });
    }
}