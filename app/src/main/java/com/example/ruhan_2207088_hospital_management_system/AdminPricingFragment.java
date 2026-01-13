package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class AdminPricingFragment extends Fragment {

    private EditText etName, etPrice;
    private RecyclerView rvPricing;
    private DatabaseReference dbRef;
    private List<Pricing> pricingList = new ArrayList<>();
    private PricingAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_pricing, container, false);

        etName = view.findViewById(R.id.etServiceName);
        etPrice = view.findViewById(R.id.etServicePrice);
        rvPricing = view.findViewById(R.id.rvPricing);

        dbRef = FirebaseDatabase.getInstance().getReference("hospital_pricing");

        view.findViewById(R.id.btnSavePrice).setOnClickListener(v -> savePricing());

        setupRecyclerView();
        loadPricingData();

        return view;
    }

    private void savePricing() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // IMPORTANT FIX:
        // To ensure the "Approve" button finds the price,
        // we should keep the ID exactly as the name for standard services.
        String id = name;

        // Check if your Pricing model constructor takes (String, String, String)
        // If price in your model is Double, keep it as: new Pricing(id, name, Double.parseDouble(priceStr))
        Pricing p = new Pricing(id, name, priceStr);

        dbRef.child(id).setValue(p).addOnSuccessListener(unused -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Price Updated Successfully!", Toast.LENGTH_SHORT).show();
            }
            etName.setText("");
            etPrice.setText("");
        }).addOnFailureListener(e -> {
            if (getContext() != null) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPricingData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pricingList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Pricing p = ds.getValue(Pricing.class);
                    if (p != null) {
                        // Ensure the serviceId is set from the Firebase key
                        p.serviceId = ds.getKey();
                        pricingList.add(p);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView() {
        rvPricing.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PricingAdapter(pricingList);
        rvPricing.setAdapter(adapter);
    }
}