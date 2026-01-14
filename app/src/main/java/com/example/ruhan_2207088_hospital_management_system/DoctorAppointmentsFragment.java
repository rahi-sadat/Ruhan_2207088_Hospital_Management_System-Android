package com.example.ruhan_2207088_hospital_management_system;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorAppointmentsFragment extends Fragment {

    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<AppointmentModel> appointmentList;
    private String doctorId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_appointments, container, false);

        if (getArguments() != null) {
            doctorId = getArguments().getString("doctorId");
        }

        rvAppointments = view.findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentList = new ArrayList<>();

        adapter = new AppointmentAdapter(appointmentList, new AppointmentAdapter.OnAppointmentClickListener() {
            @Override
            public void onTreatClick(AppointmentModel appointment) {
                showTreatmentDialog(appointment);
            }

            @Override
            public void onHistoryClick(String patientId) {
                showMedicalHistoryPopup(patientId);
            }

            @Override
            public void onReportClick(String patientId) {
                viewDigitalReport(patientId);
            }
        });

        rvAppointments.setAdapter(adapter);
        fetchAppointments();

        return view;
    }

    private void fetchAppointments() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("appointments");
        dbRef.orderByChild("doctorId").equalTo(doctorId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        appointmentList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            AppointmentModel model = data.getValue(AppointmentModel.class);
                            if (model != null) {
                                model.setAppointmentId(data.getKey());


                                if ("Approved".equalsIgnoreCase(model.getStatus())) {
                                    fetchPatientNameAndAddToList(model);
                                }
                            }
                        }

                        if (!snapshot.exists()) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void fetchPatientNameAndAddToList(AppointmentModel model) {
        FirebaseDatabase.getInstance().getReference("patients")
                .child(model.getPatientId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue(String.class);
                        model.setPatientName(name != null ? name : "Unknown Patient");
                        appointmentList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void showMedicalHistoryPopup(String patientId) {
        FirebaseDatabase.getInstance().getReference("patients").child(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String history = snapshot.child("medicalHistory").getValue(String.class);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Medical History");
                        builder.setMessage(history != null ? history : "No history recorded.");
                        builder.setPositiveButton("Close", null);
                        builder.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void viewDigitalReport(String patientId) {
        FirebaseDatabase.getInstance().getReference("patients").child(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String url = snapshot.child("reportUrl").getValue(String.class);
                        if (url != null && !url.isEmpty()) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(i);
                        } else {
                            Toast.makeText(getContext(), "No report found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void showTreatmentDialog(AppointmentModel appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Treating: " + appointment.getPatientName());

        ScrollView scrollView = new ScrollView(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);
        scrollView.addView(layout);

        FirebaseDatabase.getInstance().getReference("hospital_pricing")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot service : snapshot.getChildren()) {
                            if (service.getKey().equals("doctor's_visit_fee")) continue;
                            String sName = service.child("serviceName").getValue(String.class);
                            Object priceObj = service.child("price").getValue();
                            if (sName != null && priceObj != null) {
                                CheckBox cb = new CheckBox(getContext());
                                cb.setText(sName + " - ৳" + priceObj.toString());
                                cb.setTag(priceObj.toString());
                                cb.setContentDescription(sName);
                                layout.addView(cb);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        builder.setView(scrollView);
        builder.setPositiveButton("Finish Visit", (dialog, which) -> {
            processSelection(layout, appointment.getPatientId(), appointment.getAppointmentId());
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void processSelection(LinearLayout layout, String patientId, String appointmentId) {
        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("billings").child(patientId);

        double newTestsTotal = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof CheckBox && ((CheckBox) v).isChecked()) {
                String serviceName = ((CheckBox) v).getContentDescription().toString();

                double price = Double.parseDouble(v.getTag().toString());
                newTestsTotal += price;

                Map<String, Object> billItem = new HashMap<>();
                billItem.put("serviceName", serviceName);
                billItem.put("amount", price);
                billItem.put("timestamp", ServerValue.TIMESTAMP);


                billRef.push().setValue(billItem);
            }
        }


        calculateAndSyncTotalBill(patientId, appointmentId, newTestsTotal);
    }

    private void calculateAndSyncTotalBill(String patientId, String appointmentId, final double newTestsTotal) {
        DatabaseReference appRef = FirebaseDatabase.getInstance().getReference("appointments").child(appointmentId);
        DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference("patients").child(patientId);

        appRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Double existingBaseFee = snapshot.child("totalBill").getValue(Double.class);
                if (existingBaseFee == null) existingBaseFee = 0.0;


                double finalAppointmentTotal = existingBaseFee + newTestsTotal;


                appRef.child("totalBill").setValue(finalAppointmentTotal);
                appRef.child("status").setValue("Visited");


                patientRef.child("totalDue").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        Double currentTotalDue = currentData.getValue(Double.class);
                        if (currentTotalDue == null) currentTotalDue = 0.0;


                        currentData.setValue(currentTotalDue + newTestsTotal);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                        if (committed && getContext() != null) {
                            Toast.makeText(getContext(), "Visit Finished. Total: ৳" + finalAppointmentTotal, Toast.LENGTH_SHORT).show();
                        } else if (error != null) {
                            Log.e("FirebaseError", error.getMessage());
                        }
                    }
                });
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}