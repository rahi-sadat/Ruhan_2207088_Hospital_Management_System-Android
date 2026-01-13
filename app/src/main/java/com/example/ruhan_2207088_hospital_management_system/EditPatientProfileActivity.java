
package com.example.ruhan_2207088_hospital_management_system;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditPatientProfileActivity extends AppCompatActivity {
    EditText etName, etPhone, etAge, etGender, etBlood, etHistory;
    String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_profile);

        patientId = getIntent().getStringExtra("pId");


        etName = findViewById(R.id.editName);
        etPhone = findViewById(R.id.editPhone);
        etAge = findViewById(R.id.editAge);
        etGender = findViewById(R.id.editGender);
        etBlood = findViewById(R.id.editBlood);
        etHistory = findViewById(R.id.editHistory);


        FirebaseDatabase.getInstance().getReference("patients").child(patientId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        etName.setText(snapshot.child("name").getValue(String.class));
                        etPhone.setText(snapshot.child("phoneNumber").getValue(String.class));
                        etAge.setText(snapshot.child("age").getValue(String.class));
                        etGender.setText(snapshot.child("gender").getValue(String.class));
                        etBlood.setText(snapshot.child("bloodGroup").getValue(String.class));
                        etHistory.setText(snapshot.child("medicalHistory").getValue(String.class));
                    }
                    @Override public void onCancelled(@NonNull DatabaseError error) {}
                });

        findViewById(R.id.btnSaveProfile).setOnClickListener(v -> saveToFirebase());
    }

    private void saveToFirebase() {
        java.util.HashMap<String, Object> updates = new java.util.HashMap<>();
        updates.put("name", etName.getText().toString());
        updates.put("phoneNumber", etPhone.getText().toString());
        updates.put("age", etAge.getText().toString());
        updates.put("gender", etGender.getText().toString());
        updates.put("bloodGroup", etBlood.getText().toString());
        updates.put("medicalHistory", etHistory.getText().toString());

        FirebaseDatabase.getInstance().getReference("patients").child(patientId)
                .updateChildren(updates).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}