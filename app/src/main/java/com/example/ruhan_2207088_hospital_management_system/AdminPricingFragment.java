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


        String id = name.toLowerCase().replace(" ", "_").replaceAll("[^a-z0-9_]", "");

        try {

            long numericPrice = Long.parseLong(priceStr);


            Pricing p = new Pricing(id, name, numericPrice);

            dbRef.child(id).setValue(p).addOnSuccessListener(unused -> {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Price Added Successfully!", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etPrice.setText("");
                }
            }).addOnFailureListener(e -> {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid numeric price", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPricingData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) return;

                pricingList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        try {

                            Pricing p = ds.getValue(Pricing.class);
                            if (p != null) {
                                p.serviceId = ds.getKey();
                                pricingList.add(p);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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