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

        Intent intent = getIntent();
        if (intent != null) {
            nameTextView.setText(intent.getStringExtra("name"));
            txtKhoa.setText(intent.getStringExtra("faculty"));
            locationTextView.setText(intent.getStringExtra("location"));
            timeTextView.setText("Start Day: " + intent.getStringExtra("startDay"));
            String imageUrl = intent.getStringExtra("image");
            Glide.with(this).load(imageUrl).into(imageView);
        }
    }

    private void registerForEvent() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String eventName = getIntent().getStringExtra("name");
            String eventLocation = getIntent().getStringExtra("location");
            String eventFaculty = getIntent().getStringExtra("faculty");
            String eventStartDay = getIntent().getStringExtra("startDay");
            String eventImage = getIntent().getStringExtra("image");

            Map<String, Object> registrationDetails = new HashMap<>();
            registrationDetails.put("userId", currentUser.getUid());
            registrationDetails.put("name", eventName);
            registrationDetails.put("location", eventLocation);
            registrationDetails.put("faculty", eventFaculty);
            registrationDetails.put("startDay", eventStartDay);
            registrationDetails.put("image", eventImage);

            firestore.collection("registeredEvent")
                    .add(registrationDetails)
                    .addOnSuccessListener(documentReference -> {
                        // Handle success, for example, show a success message
                        showToast("Registered for the event successfully");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure, for example, show an error message
                        showToast("Failed to register for the event: " + e.getMessage());
                    });
        } else {
            // Handle the case where the user is not logged in
            showToast("Please log in before registering for the event");
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
