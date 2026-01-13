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

        rvBills = view.findViewById(R.id.rvBills);
        tvTotalPendingAmount = view.findViewById(R.id.tvTotalPendingAmount);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        rvBills.setLayoutManager(new LinearLayoutManager(getContext()));
        billList = new ArrayList<>();
        adapter = new BillingAdapter(billList);
        rvBills.setAdapter(adapter);

        btnCheckout.setOnClickListener(v -> {
            if (getActivity() != null) {
                Toast.makeText(getContext(), "Checkout Successful! Records cleared.", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        fetchBillsAndTotalDue();
        return view;
    }

    private void fetchBillsAndTotalDue() {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments");
        // Change this to look at the global patient total if needed,
        // but the list calculation is more accurate for the current UI.

        appointmentsRef.orderByChild("patientId").equalTo(patientId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        billList.clear();
                        double calculatedTotal = 0; // Local variable to sum bills

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Appointment app = ds.getValue(Appointment.class);
                            if (app != null) {
                                // REMOVE the ds.getRef().removeValue() logic.
                                // We want to keep paid bills in the list so we can download them.
                                billList.add(app);

                                // Only add to the "Pending Total" if it is NOT paid
                                if (!"Paid".equalsIgnoreCase(app.getPaymentStatus())) {
                                    calculatedTotal += app.getTotalBill();
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();
                        tvEmptyState.setVisibility(billList.isEmpty() ? View.VISIBLE : View.GONE);
                        rvBills.setVisibility(billList.isEmpty() ? View.GONE : View.VISIBLE);

                        // 3. Update the UI with the ACTUAL sum (100 + 1000 = 1100)
                        updateUI(calculatedTotal);

                        // Optional: Sync this correct total back to the Patient node so it's correct everywhere
                        FirebaseDatabase.getInstance().getReference("patients")
                                .child(patientId).child("totalDue").setValue(calculatedTotal);
                    }

                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
    private void updateUI(double totalDue) {
        tvTotalPendingAmount.setText("Total Pending: ৳" + totalDue);

        if (totalDue <= 0 && !billList.isEmpty()) {
            btnCheckout.setEnabled(true);
            btnCheckout.setBackgroundColor(Color.parseColor("#27AE60"));
            btnCheckout.setText("Checkout Now");
        } else if (totalDue > 0) {
            btnCheckout.setEnabled(false);
            btnCheckout.setBackgroundColor(Color.parseColor("#E74C3C"));
            btnCheckout.setText("Pay ৳" + totalDue + " at Reception");
        } else {
            btnCheckout.setEnabled(false);
            btnCheckout.setBackgroundColor(Color.parseColor("#95A5A6"));
            btnCheckout.setText("No Pending Bills");
        }
    }
}