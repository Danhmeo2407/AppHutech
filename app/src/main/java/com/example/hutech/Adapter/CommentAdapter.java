package com.example.hutech.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutech.R;
import com.example.hutech.model.Comment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentsList;

    public CommentAdapter(List<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentsList.get(position);

        holder.commentText.setText(comment.getCommentText());

        // Format the Timestamp to a String before setting it in the TextView
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy h:mm a", Locale.US);
        String formattedTime = dateFormat.format(comment.getCommentTime().toDate());
        holder.commentTime.setText(formattedTime);
        fetchUserInformation(comment.getUserId(), holder);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatarUser;
        private TextView commentText, commentTime, txtNameUser;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarUser = itemView.findViewById(R.id.avatarUser);
            commentText = itemView.findViewById(R.id.commentText);
            commentTime = itemView.findViewById(R.id.commentTime);
            txtNameUser = itemView.findViewById(R.id.txtNameUser);
        }
    }
    private void fetchUserInformation(String userId, CommentViewHolder holder) {
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User found, update the UI with user information
                        String userName = documentSnapshot.getString("displayName");
                        String userAvatarUrl = documentSnapshot.getString("image");

                        // Update UI
                        holder.txtNameUser.setText(userName);

                        // Load user avatar using Glide
                        Glide.with(holder.itemView.getContext())
                                .load(userAvatarUrl)
                                .circleCrop()
                                .into(holder.avatarUser);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}
