package com.example.ruhan_2207088_hospital_management_system;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddDoctorActivity.class);


            intent.putExtra("isEdit", true);
            intent.putExtra("doctorId", doc.doctorId);
            intent.putExtra("name", doc.name);
            intent.putExtra("phone", doc.phoneNumber);
            intent.putExtra("spec", doc.specialization);
            intent.putExtra("email", doc.email);
            intent.putExtra("schedule", doc.schedule);

            v.getContext().startActivity(intent);
        });



    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, spec;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvDocName);
            spec = itemView.findViewById(R.id.tvDocSpec);
            btnEdit = itemView.findViewById(R.id.btnEditDoctor);
            btnDelete = itemView.findViewById(R.id.btnDeleteDoctor);
        }
    }
}
