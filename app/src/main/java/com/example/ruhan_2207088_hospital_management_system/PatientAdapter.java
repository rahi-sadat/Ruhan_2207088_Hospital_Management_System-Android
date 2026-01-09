package com.example.ruhan_2207088_hospital_management_system;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private ArrayList<Patient> list;

    public PatientAdapter(ArrayList<Patient> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patient p = list.get(position);
        holder.name.setText(p.name);
        holder.details.setText("ID: " + p.patientId + " | Age: " + p.age + " | Blood: " + p.bloodGroup);
        holder.phone.setText("Phone: " + p.phoneNumber);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, details, phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvRowName);
            details = itemView.findViewById(R.id.tvRowDetails);
            phone = itemView.findViewById(R.id.tvRowPhone);
        }
    }
}