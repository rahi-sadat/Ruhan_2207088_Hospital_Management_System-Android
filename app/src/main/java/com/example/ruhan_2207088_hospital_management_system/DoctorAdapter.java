package com.example.ruhan_2207088_hospital_management_system;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private final ArrayList<Doctor> list;

    public DoctorAdapter(ArrayList<Doctor> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doc = list.get(position);

        holder.name.setText(doc.name);
        holder.spec.setText(doc.specialization);
        holder.phone.setText("📞 " + doc.phoneNumber);
        holder.schedule.setText("⏰ " + (doc.schedule != null ? doc.schedule : "Not set"));

        // DELETE LOGIC
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Doctor")
                    .setMessage("Are you sure you want to remove " + doc.name + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("doctors")
                                .child(doc.doctorId).removeValue()
                                .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // EDIT LOGIC (Fragment Transaction)
        holder.btnEdit.setOnClickListener(v -> {
            AddDoctorFragment fragment = new AddDoctorFragment();
            Bundle args = new Bundle();
            args.putBoolean("isEdit", true);
            args.putString("doctorId", doc.doctorId);
            args.putString("name", doc.name);
            args.putString("phone", doc.phoneNumber);
            args.putString("spec", doc.specialization);
            args.putString("email", doc.email);
            args.putString("schedule", doc.schedule);
            fragment.setArguments(args);

            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_content_area, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, spec, phone, schedule;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvDocName);
            spec = itemView.findViewById(R.id.tvDocSpec);
            phone = itemView.findViewById(R.id.tvDocPhone);
            schedule = itemView.findViewById(R.id.tvDocSchedule);
            btnEdit = itemView.findViewById(R.id.btnEditDoctor);
            btnDelete = itemView.findViewById(R.id.btnDeleteDoctor);
        }
    }
}