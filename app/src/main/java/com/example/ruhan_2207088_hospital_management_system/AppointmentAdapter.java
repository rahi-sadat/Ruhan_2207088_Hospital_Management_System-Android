package com.example.ruhan_2207088_hospital_management_system;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<AppointmentModel> list;
    private OnAppointmentClickListener listener;


    public interface OnAppointmentClickListener {
        void onTreatClick(AppointmentModel appointment);
        void onHistoryClick(String patientId);
        void onReportClick(String patientId);
    }

    public AppointmentAdapter(List<AppointmentModel> list, OnAppointmentClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentModel m = list.get(position);


        String displayName = m.getPatientName();
        holder.name.setText(displayName != null && !displayName.isEmpty() ? displayName : "Unknown Patient");

        holder.date.setText(m.getDate());


        holder.btnHistory.setOnClickListener(v -> listener.onHistoryClick(m.getPatientId()));


        holder.btnReport.setOnClickListener(v -> listener.onReportClick(m.getPatientId()));


        holder.btnTreat.setOnClickListener(v -> listener.onTreatClick(m));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;
        Button btnTreat, btnHistory, btnReport;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.patientName);
            date = itemView.findViewById(R.id.appointmentDate);
            btnTreat = itemView.findViewById(R.id.btnTreat);
            btnHistory = itemView.findViewById(R.id.btnHistory);
            btnReport = itemView.findViewById(R.id.btnReport);
        }
    }
}