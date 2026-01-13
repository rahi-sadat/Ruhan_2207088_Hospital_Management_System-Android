package com.example.ruhan_2207088_hospital_management_system;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PricingAdapter extends RecyclerView.Adapter<PricingAdapter.PricingViewHolder> {

    private List<Pricing> pricingList;

    public PricingAdapter(List<Pricing> pricingList) {
        this.pricingList = pricingList;
    }

    @NonNull
    @Override
    public PricingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pricing, parent, false);
        return new PricingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PricingViewHolder holder, int position) {
        Pricing pricing = pricingList.get(position);
        holder.tvName.setText(pricing.serviceName);
        holder.tvAmount.setText(pricing.price + " TK");

        holder.btnDelete.setOnClickListener(v -> {
            if (pricing.serviceId != null) {
                // Delete from Firebase
                FirebaseDatabase.getInstance().getReference("hospital_pricing")
                        .child(pricing.serviceId)
                        .removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(v.getContext(), pricing.serviceName + " Removed", Toast.LENGTH_SHORT).show();
                            // Note: The UI will auto-refresh if you have a ValueEventListener
                            // in your Fragment. If not, you'd call notifyItemRemoved(position) here.
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return pricingList.size();
    }

    public static class PricingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAmount;
        ImageButton btnDelete;

        public PricingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPricingName);
            tvAmount = itemView.findViewById(R.id.tvPricingAmount);
            btnDelete = itemView.findViewById(R.id.btnDeletePrice);
        }
    }
}