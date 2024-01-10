package com.example.hutech.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutech.Adapter.CommentAdapter;
import com.example.hutech.R;
import com.example.hutech.model.Comment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailNewsActivity extends AppCompatActivity {

    private EditText contentComment;
    private ImageButton sendButton;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentsAdapter;
    private List<Comment> commentsList;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setupUI();
        setupRecyclerView();
        String newsId = getIntent().getStringExtra("id");
        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Load existing comments from Firebase
        loadCommentsFromFirebase(newsId);
    }

    private void setupUI() {
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

        contentComment = findViewById(R.id.contentComment);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendButtonClick();
            }
        });
    }

    private void setupRecyclerView() {
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsList = new ArrayList<>();
        commentsAdapter = new CommentAdapter(commentsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(layoutManager);
        commentsRecyclerView.setAdapter(commentsAdapter);
    }


    private void handleSendButtonClick() {
        String commentText = contentComment.getText().toString();
        if (!commentText.isEmpty()) {
            // Add the comment to Firebase
            addCommentToFirebase(commentText);
        }
    }

    private void addCommentToFirebase(String commentText) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String newsId = getIntent().getStringExtra("id");

            Comment comment = new Comment(userId, commentText, newsId, Timestamp.now());

            firestore.collection("comments")
                    .add(comment)
                    .addOnSuccessListener(documentReference -> {
                        // Clear the comment input field
                        contentComment.setText("");

                        // Reload comments from Firebase
                        loadCommentsFromFirebase(newsId);
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }


    private void loadCommentsFromFirebase(String newsId) {
        firestore.collection("comments")
                .whereEqualTo("newsId", newsId)  // Filter comments by newsId
                .orderBy("commentTime")  // Order comments by time
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        commentsList.clear(); // Clear existing data before adding new data

                        for (QueryDocumentSnapshot document : value) {
                            Comment comment = document.toObject(Comment.class);
                            commentsList.add(comment);
                        }

                        commentsAdapter.notifyDataSetChanged();
                    } else {
                        // Handle error
                        if (error != null) {
                            error.printStackTrace(); // Log the error for debugging
                        }
                    }
                });
    }
}
