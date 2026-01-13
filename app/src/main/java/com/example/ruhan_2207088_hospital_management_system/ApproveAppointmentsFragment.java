package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class ApproveAppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentApprovalAdapter adapter;
    private ArrayList<Appointment> appointmentList;
    private DatabaseReference dbRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approve_appointments, container, false);

        recyclerView = view.findViewById(R.id.rvPendingAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        appointmentList = new ArrayList<>();
        // Pass the list to the adapter
        adapter = new AppointmentApprovalAdapter(appointmentList);
        recyclerView.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("appointments");

        // Query matches your Firebase screenshot casing "Pending"
        Query pendingQuery = dbRef.orderByChild("status").equalTo("Pending");

        pendingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Appointment app = ds.getValue(Appointment.class);
                    if (app != null) {
                        // Use the correct setter from your Appointment model
                        app.setAppointmentId(ds.getKey());
                        appointmentList.add(app);
                    }
                }
                adapter.notifyDataSetChanged();

                if (appointmentList.isEmpty() && getContext() != null) {
                    Toast.makeText(getContext(), "No pending appointments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}