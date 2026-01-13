package com.example.ruhan_2207088_hospital_management_system;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillViewHolder> {

    private List<Appointment> list;

    public BillingAdapter(List<Appointment> list) { this.list = list; }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Appointment app = list.get(position);

        holder.tvBillDate.setText(app.getDate());
        holder.tvBillDoctor.setText("Ref: " + app.getDoctorName());

        // Display the bill amount
        double billAmount = app.getTotalBill();
        holder.tvBillAmount.setText(billAmount + " TK");

        // FIX 1: SERVICE LABELING LOGIC
        // If there is a doctor name, it's a Consultation.
        // Otherwise, check for specific fees or label as a Test.
        if (app.getDoctorName() != null && !app.getDoctorName().isEmpty()) {
            holder.tvBillService.setText("Consultation: " + app.getDoctorName());
        } else {
            holder.tvBillService.setText("Medical Service/Test");
        }

        // FIX 2: PAYMENT STATUS & BUTTON VISIBILITY
        boolean isPaid = "Paid".equalsIgnoreCase(app.getPaymentStatus());

        // If the bill is 0.0, we shouldn't show "Unpaid" or "Pay Now"
        if (billAmount <= 0) {
            holder.tvBillStatus.setText("N/A");
            holder.tvBillStatus.setTextColor(Color.GRAY);
            holder.btnPayNow.setVisibility(View.GONE);
            holder.btnDownloadPDF.setVisibility(View.GONE);
        } else {
            holder.tvBillStatus.setText(isPaid ? "Paid" : "Unpaid");
            holder.tvBillStatus.setTextColor(isPaid ? Color.parseColor("#27AE60") : Color.parseColor("#E74C3C"));

            holder.btnPayNow.setVisibility(isPaid ? View.GONE : View.VISIBLE);
            holder.btnDownloadPDF.setVisibility(isPaid ? View.VISIBLE : View.GONE);
        }

        holder.btnPayNow.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("appointments")
                    .child(app.getAppointmentId()).child("paymentStatus").setValue("Paid");
        });

        holder.btnDownloadPDF.setOnClickListener(v -> generatePDF(v.getContext(), app));
    }
    private void generatePDF(Context context, Appointment app) {
        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(300, 450, 1).create();
        PdfDocument.Page page = pdf.startPage(info);
        Canvas canvas = page.getCanvas();

        // --- PDF Content Drawing ---
        paint.setTextSize(16f); paint.setFakeBoldText(true);
        canvas.drawText("HOSPITAL BILL RECEIPT", 50, 50, paint);
        paint.setTextSize(12f); paint.setFakeBoldText(false);
        canvas.drawText("Date: " + app.getDate(), 20, 100, paint);
        canvas.drawText("Service: Consultation", 20, 120, paint);
        canvas.drawText("Patient ID: " + app.getPatientId(), 20, 140, paint);
        paint.setFakeBoldText(true);
        canvas.drawText("Total Amount: " + app.getTotalBill() + " TK", 20, 180, paint);
        canvas.drawText("Status: PAID", 20, 200, paint);
        pdf.finishPage(page);

        // --- FILE SAVING LOGIC ---
        // Save to the PUBLIC Downloads folder
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsFolder, "Receipt_" + app.getAppointmentId() + ".pdf");

        try {
            pdf.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "Saved to Downloads: " + file.getName(), Toast.LENGTH_LONG).show();

            // This line makes the file show up in the "Downloads" app immediately
            android.media.MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: Check Permissions", Toast.LENGTH_SHORT).show();
        }
        pdf.close();
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillDate, tvBillDoctor, tvBillService, tvBillAmount, tvBillStatus;
        Button btnPayNow;
        ImageButton btnDownloadPDF;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillDate = itemView.findViewById(R.id.tvBillDate);
            tvBillDoctor = itemView.findViewById(R.id.tvBillDoctor);
            tvBillService = itemView.findViewById(R.id.tvBillService);
            tvBillAmount = itemView.findViewById(R.id.tvBillAmount);
            tvBillStatus = itemView.findViewById(R.id.tvBillStatus);
            btnPayNow = itemView.findViewById(R.id.btnPayNow);
            btnDownloadPDF = itemView.findViewById(R.id.btnDownloadPDF);
        }
    }
}