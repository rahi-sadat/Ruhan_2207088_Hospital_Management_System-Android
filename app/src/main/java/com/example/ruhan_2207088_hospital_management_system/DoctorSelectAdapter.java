package com.example.ruhan_2207088_hospital_management_system;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorSelectAdapter extends RecyclerView.Adapter<DoctorSelectAdapter.ViewHolder> {

    private List<Doctor> doctorList;
    private OnDoctorClickListener listener;
    private int selectedPosition = -1;

    // Interface to handle doctor selection in the Fragment
    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorSelectAdapter(List<Doctor> doctorList, OnDoctorClickListener listener) {
        this.doctorList = doctorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doc = doctorList.get(position);


        holder.tvDocName.setText(doc.name);
        holder.tvDocSpec.setText(doc.specialization);
        holder.tvDocPhone.setText("📞 " + doc.phoneNumber);
        holder.tvDocSchedule.setText("⏰ " + (doc.schedule != null ? doc.schedule : "TBD"));


        holder.btnEdit.setVisibility(View.GONE);
        holder.btnDelete.setVisibility(View.GONE);


        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E3F2FD")); // Light Blue
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }


        holder.itemView.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                selectedPosition = currentPos;
                notifyDataSetChanged();
                listener.onDoctorClick(doc);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList != null ? doctorList.size() : 0;
    }

    // SINGLE ViewHolder class containing all necessary views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocName, tvDocSpec, tvDocPhone, tvDocSchedule;
        ImageButton btnEdit, btnDelete;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocName = itemView.findViewById(R.id.tvDocName);
            tvDocSpec = itemView.findViewById(R.id.tvDocSpec);
            tvDocPhone = itemView.findViewById(R.id.tvDocPhone);
            tvDocSchedule = itemView.findViewById(R.id.tvDocSchedule);
            btnEdit = itemView.findViewById(R.id.btnEditDoctor);
            btnDelete = itemView.findViewById(R.id.btnDeleteDoctor);
            cardView = (CardView) itemView;
        }
    }
}