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

public class DetailNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

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

        TextView titleTextView = findViewById(R.id.detailNewTitleTextView);
        ImageView imageView = findViewById(R.id.detailNewsImageView);
        TextView timeTextView = findViewById(R.id.detailNewsTimeTextView);
        TextView descriptionTextView = findViewById(R.id.detailNewsDescriptionTextView);

        Intent intent = getIntent();
        if (intent != null) {
            titleTextView.setText(intent.getStringExtra("title"));
            descriptionTextView.setText(intent.getStringExtra("description"));
            timeTextView.setText("Time: " + intent.getStringExtra("time"));
            String imageUrl = intent.getStringExtra("image");
            Glide.with(this).load(imageUrl).into(imageView);
        }
    }
}
