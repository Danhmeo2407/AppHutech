package com.example.hutech.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hutech.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailEventUpcomingActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event_upcoming);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firestore = FirebaseFirestore.getInstance();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnDangKy = findViewById(R.id.btnDangKy);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForEvent();
            }
        });

        TextView nameTextView = findViewById(R.id.detailNameTextView);
        ImageView imageView = findViewById(R.id.detailImageView);
        TextView timeTextView = findViewById(R.id.detailTimeTextView);
        TextView txtKhoa = findViewById(R.id.detailFacultyTextView);
        TextView locationTextView = findViewById(R.id.detailLocationTextView);
        TextView descriptionTextView = findViewById(R.id.detailDescriptionTextView);

        Intent intent = getIntent();
        if (intent != null) {
            nameTextView.setText(intent.getStringExtra("name"));
            txtKhoa.setText(intent.getStringExtra("faculty"));
            locationTextView.setText(intent.getStringExtra("location"));
            timeTextView.setText(intent.getStringExtra("startDay"));
            String imageUrl = intent.getStringExtra("image");
            Glide.with(this).load(imageUrl).into(imageView);
            descriptionTextView.setText(intent.getStringExtra("description"));
        }
    }

    private void registerForEvent() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String eventId = getIntent().getStringExtra("id");

            // Check if the user is already registered for the event
            firestore.collection("registeredEvent")
                    .whereEqualTo("userId", currentUser.getUid())
                    .whereEqualTo("eventId", eventId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            // User is already registered for this event

                            showToast("You have already registered for this event");
                        } else {
                            // User is not registered, proceed with registration
                            Map<String, Object> registrationDetails = new HashMap<>();
                            registrationDetails.put("userId", currentUser.getUid());
                            registrationDetails.put("eventId", eventId);
                            registrationDetails.put("timestamp", FieldValue.serverTimestamp());
                            registrationDetails.put("status", 1);

                            firestore.collection("registeredEvent")
                                    .add(registrationDetails)
                                    .addOnSuccessListener(documentReference -> {
                                        // Handle success, for example, show a success message
                                        showToast("Registered for the event successfully");

                                        // Update UI to reflect the registration status
                                        updateRegistrationStatus(true);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure, for example, show an error message
                                        showToast("Failed to register for the event: " + e.getMessage());
                                    });
                        }
                    });
        } else {
            // Handle the case where the user is not logged in
            showToast("Please log in before registering for the event");
        }
    }

    private void updateRegistrationStatus(boolean isRegistered) {
        Button btnDangKy = findViewById(R.id.btnDangKy);
        if (isRegistered) {
            // Set button text and disable it
            btnDangKy.setText("Đã đăng ký");
            btnDangKy.setEnabled(false);
        } else {
            // Handle the case where the registration status is not 1
            // You can add specific handling based on your requirements
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
