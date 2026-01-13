package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
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

        double price = Double.parseDouble(priceStr);
        String id = name.toLowerCase().replace(" ", "_"); // Create a unique ID from name

        Pricing p = new Pricing(id, name, price);
        dbRef.child(id).setValue(p).addOnSuccessListener(unused -> {
            Toast.makeText(getContext(), "Price Updated!", Toast.LENGTH_SHORT).show();
            etName.setText("");
            etPrice.setText("");
        });
    }

    private void loadPricingData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pricingList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Pricing p = ds.getValue(Pricing.class);
                    if (p != null) pricingList.add(p);
                }
                adapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void setupRecyclerView() {
        rvPricing.setLayoutManager(new LinearLayoutManager(getContext()));
        // Note: You can reuse a simple text-based adapter or create a PricingAdapter
        adapter = new PricingAdapter(pricingList);
        rvPricing.setAdapter(adapter);
    }
}