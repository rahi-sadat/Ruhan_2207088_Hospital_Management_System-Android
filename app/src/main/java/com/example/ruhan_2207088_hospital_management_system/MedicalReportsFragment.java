package com.example.ruhan_2207088_hospital_management_system;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MedicalReportsFragment extends Fragment {

    private Uri imageUri;
    private ImageView imgPreview;
    private Button btnSelect, btnUpload;
    private ProgressBar progressBar;
    private String patientId; // Changed from appointmentId to match your Dashboard

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    imageUri = uri; // This saves the selection
                    imgPreview.setImageURI(uri);
                    imgPreview.setPadding(0, 0, 0, 0);
                    imgPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    Log.d("PHOTO_PICKER", "Photo Selected: " + uri.toString());
                } else {
                    Log.d("PHOTO_PICKER", "No photo selected");
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medical_reports, container, false);

        // FIX: Your PatientDashboardActivity sends "patientId"
        if (getArguments() != null) {
            patientId = getArguments().getString("patientId");
            Log.d("DEBUG_ID", "Received Patient ID: " + patientId);
        }

        imgPreview = view.findViewById(R.id.imgReportPreview);
        btnSelect = view.findViewById(R.id.btnSelectReport);
        btnUpload = view.findViewById(R.id.btnUploadReport);
        progressBar = view.findViewById(R.id.uploadProgressBar);

        btnSelect.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        btnUpload.setOnClickListener(v -> uploadFile());

        return view;
    }
    private void uploadFile() {
        if (imageUri == null || patientId == null) {
            Toast.makeText(getContext(), "Selection missing", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnUpload.setEnabled(false);

        try {
            // 1. Convert URI to Byte Array
            InputStream iStream = getContext().getContentResolver().openInputStream(imageUri);
            byte[] inputData = getBytes(iStream);

            // 2. Prepare the RequestBody
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), inputData);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", "report.jpg", requestFile);

            // 3. Initialize Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.imgbb.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ImgBBApi api = retrofit.create(ImgBBApi.class);

            // 4. Make the Call (Replace YOUR_API_KEY with your real key)
            api.uploadImage("369b5649ee2737791b6971416d1a8373", body).enqueue(new Callback<ImgBBResponse>() {
                @Override
                public void onResponse(Call<ImgBBResponse> call, Response<ImgBBResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String finalUrl = response.body().data.url;


                        saveToFirebase(finalUrl);
                    } else {
                        handleFailure("Upload failed: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ImgBBResponse> call, Throwable t) {
                    handleFailure("Network Error: " + t.getMessage());
                }
            });

        } catch (IOException e) {
            handleFailure("File Error: " + e.getMessage());
        }
    }


    private void saveToFirebase(String url) {
        FirebaseDatabase.getInstance().getReference("patients")
                .child(patientId)
                .child("reportUrl").setValue(url)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpload.setEnabled(true);
                    Toast.makeText(getContext(), "Success! Report uploaded.", Toast.LENGTH_SHORT).show();
                });
    }

    // Helper to handle bytes
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    private void handleFailure(String message) {
        // Run on UI Thread to update views
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                btnUpload.setEnabled(true);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                Log.e("UPLOAD_ERROR", message);
            });
        }
    }

}