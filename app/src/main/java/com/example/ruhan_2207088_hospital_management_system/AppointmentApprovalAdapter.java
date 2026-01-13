package com.example.ruhan_2207088_hospital_management_system;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppointmentApprovalAdapter extends RecyclerView.Adapter<AppointmentApprovalAdapter.ViewHolder> {
    private ArrayList<Appointment> list;

    public AppointmentApprovalAdapter(ArrayList<Appointment> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_approval, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment app = list.get(position);
        holder.tvPatient.setText("Patient ID: " + app.getPatientId());
        holder.tvDoctor.setText("Doctor: " + app.getDoctorName());
        holder.tvDate.setText("Scheduled: " + app.getDate());

        holder.btnApprove.setOnClickListener(v -> {
            DatabaseReference pricingRef = FirebaseDatabase.getInstance().getReference("hospital_pricing");

            // FIX: Match the lowercase name with underscores as seen in your Firebase screenshot
            pricingRef.child("doctor's_visit_fee").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    double fee = 0.0;

                    if (snapshot.exists()) {
                        // Pull the 'price' field from the 'doctor's_visit_fee' node
                        Object priceObj = snapshot.child("price").getValue();
                        if (priceObj != null) {
                            try {
                                // If it's stored as a Number or String, this converts it safely
                                fee = Double.parseDouble(priceObj.toString());
                            } catch (NumberFormatException e) {
                                fee = 0.0;
                            }
                        }
                    }

                    // Update the appointment in the database
                    DatabaseReference appRef = FirebaseDatabase.getInstance().getReference("appointments")
                            .child(app.getAppointmentId());

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("status", "Approved");
                    updates.put("totalBill", fee);        // Now this will be 1000.0 instead of 0.0
                    updates.put("paymentStatus", "Unpaid");

                    double finalFee = fee;
                    appRef.updateChildren(updates).addOnSuccessListener(aVoid -> {
                        Toast.makeText(v.getContext(), "Approved! Fee: " + finalFee + " TK", Toast.LENGTH_SHORT).show();
                        // Optionally remove item from list or refresh
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(v.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatient, tvDoctor, tvDate;
        Button btnApprove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatient = itemView.findViewById(R.id.tvPatientInfo);
            tvDoctor = itemView.findViewById(R.id.tvDoctorInfo);
            tvDate = itemView.findViewById(R.id.tvAppDate);
            btnApprove = itemView.findViewById(R.id.btnApprove);
        }
    }
}