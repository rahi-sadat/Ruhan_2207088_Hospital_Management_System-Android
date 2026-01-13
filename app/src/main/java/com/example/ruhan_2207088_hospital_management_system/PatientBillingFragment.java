package com.example.ruhan_2207088_hospital_management_system;

import android.graphics.Color;
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

public class PatientBillingFragment extends Fragment {

    private RecyclerView rvBills;
    private BillingAdapter adapter;
    private ArrayList<Appointment> billList;
    private String patientId;
    private TextView tvTotalPendingAmount, tvEmptyState;
    private Button btnCheckout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_billing, container, false);

        if (getArguments() != null) {
            patientId = getArguments().getString("patientId");
        }

        // Initialize Views
        rvBills = view.findViewById(R.id.rvBills);
        tvTotalPendingAmount = view.findViewById(R.id.tvTotalPendingAmount);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        tvEmptyState = view.findViewById(R.id.tvEmptyState); // Make sure this ID exists in your XML

        // Setup RecyclerView
        rvBills.setLayoutManager(new LinearLayoutManager(getContext()));
        billList = new ArrayList<>();
        adapter = new BillingAdapter(billList);
        rvBills.setAdapter(adapter);

        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Checkout Successful! Wish you good health.", Toast.LENGTH_LONG).show();
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        fetchBills();
        return view;
    }

    private void fetchBills() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("appointments");
        // Query to filter by this specific patient
        Query query = db.orderByChild("patientId").equalTo(patientId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billList.clear();
                double totalDue = 0;
                boolean hasAtLeastOnePaidBill = false;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Appointment app = ds.getValue(Appointment.class);

                    if (app != null) {
                        double billAmount = app.getTotalBill();
                        String paymentStatus = app.getPaymentStatus();

                        // Logic: Only care about Approved appointments with an actual fee
                        if ("Approved".equalsIgnoreCase(app.getStatus()) && billAmount > 0) {
                            billList.add(app);

                            if ("Paid".equalsIgnoreCase(paymentStatus)) {
                                hasAtLeastOnePaidBill = true;
                            } else {
                                totalDue += billAmount;
                            }
                        }
                    }
                }

                // Handle Empty State Visibility
                if (billList.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    rvBills.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.GONE);
                    rvBills.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
                // Update UI based on the new logic
                updateUI(totalDue, hasAtLeastOnePaidBill);
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(double totalDue, boolean hasPaidSomething) {
        tvTotalPendingAmount.setText("Total Due: " + totalDue + " TK");

        // Logic: All dues must be 0 AND the user must have actually paid at least one bill to checkout
        if (totalDue == 0 && hasPaidSomething) {
            btnCheckout.setEnabled(true);
            btnCheckout.setBackgroundColor(Color.parseColor("#27AE60")); // Green
            btnCheckout.setText("Checkout Now");
        } else {
            btnCheckout.setEnabled(false);
            btnCheckout.setBackgroundColor(Color.parseColor("#95A5A6")); // Gray

            if (totalDue > 0) {
                btnCheckout.setText("Pay " + totalDue + " TK to Checkout");
            } else if (billList.isEmpty()) {
                btnCheckout.setText("Waiting for Approval");
            } else {
                // This covers the case where bills are present but nothing has been paid yet
                btnCheckout.setText("Pay Dues to Checkout");
            }
        }
    }
}