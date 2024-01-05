package com.example.hutech.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hutech.R;

public class DetailRegisteredEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_registered_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView nameTextView = findViewById(R.id.detailNameTextView);
        ImageView imageView = findViewById(R.id.detailImageView);
        TextView timeTextView = findViewById(R.id.detailTimeTextView);
        TextView txtKhoa = findViewById(R.id.detailFacultyTextView);
        TextView locationTextView = findViewById(R.id.detailLocationTextView);

        Intent intent = getIntent();
        if (intent != null) {
            nameTextView.setText(intent.getStringExtra("name")); // Update with the correct key
            txtKhoa.setText(intent.getStringExtra("faculty")); // Update with the correct key
            locationTextView.setText(intent.getStringExtra("location")); // Update with the correct key
            timeTextView.setText("Start Day: " + intent.getStringExtra("startDay"));
            String imageUrl = intent.getStringExtra("image");
            Glide.with(this).load(imageUrl).into(imageView);
        }
    }
}