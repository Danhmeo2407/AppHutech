package com.example.hutech.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutech.Activites.DetailEventUpcomingActivity;
import com.example.hutech.R;
import com.example.hutech.model.Events;
import com.example.hutech.model.RegisteredEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUpComingAdapter extends RecyclerView.Adapter<ListUpComingAdapter.EventComingViewHolder> {
    private Context context;
    private List<Events> eventsList;

    private FirebaseFirestore firestore;

    public ListUpComingAdapter(Context context, List<Events> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public EventComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventComingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventComingViewHolder holder, int position) {
        Events event = eventsList.get(position);

        checkUserRegistration(event.getId(), holder);

        holder.nameTextView.setText(event.getName());
        holder.timeTextView.setText(event.getFormattedTime());
        holder.locationTextView.setText(event.getLocation());
        holder.txtKhoa.setText(event.getFaculty());

        Glide.with(context).load(event.getPoster()).into(holder.imageView);
        // Check user registration and handle UI updates
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailEventUpcomingActivity.class);
                intent.putExtra("id", event.getId());
                intent.putExtra("faculty", event.getFaculty());
                intent.putExtra("location", event.getLocation());
                intent.putExtra("name", event.getName());
                intent.putExtra("image", event.getPoster());
                intent.putExtra("startDay", event.getFormattedTime());
                intent.putExtra("description", event.getDescription());
                context.startActivity(intent);
                checkUserRegistration(event.getId(), holder);
            }
        });
        holder.btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Register for the event
                registerForEvent(event.getId(), holder);
                checkUserRegistration(event.getId(), holder);
//                Intent intent = new Intent(context, DetailEventUpcomingActivity.class);
//                intent.putExtra("id", event.getId());
//                intent.putExtra("faculty", event.getFaculty());
//                intent.putExtra("location", event.getLocation());
//                intent.putExtra("name", event.getName());
//                intent.putExtra("image", event.getPoster());
//                intent.putExtra("startDay", event.getFormattedTime());
//                intent.putExtra("description", event.getDescription());
//                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class EventComingViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, timeTextView, locationTextView, txtKhoa;
        ImageView imageView;
        Button btnDangKy;
        RelativeLayout relativeLayout;

        public EventComingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            txtKhoa = itemView.findViewById(R.id.txtKhoa);
            imageView = itemView.findViewById(R.id.imageView);
            btnDangKy = itemView.findViewById(R.id.btnDangKy);

            relativeLayout = itemView.findViewById(R.id.layoutItem);
        }
    }
    private void checkUserRegistration(String eventId, EventComingViewHolder holder) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            firestore.collection("registeredEvent")
                    .whereEqualTo("userId", currentUser.getUid())
                    .whereEqualTo("eventId", eventId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot document : task.getResult()) {
                                RegisteredEvent registration = document.toObject(RegisteredEvent.class);
                                if (registration != null && registration.getStatus() == 1 || registration.getStatus() == 2) {
                                    // User is registered for this event
                                    holder.btnDangKy.setText("Đã đăng ký");
                                    holder.btnDangKy.setEnabled(false);
                                }
                            }
                        }
                    });
        }
    }
    private void registerForEvent(String eventId, EventComingViewHolder holder) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
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

                        // Update the UI or perform additional actions as needed
                        updateUIAfterRegistration(holder);

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
    private void updateUIAfterRegistration(EventComingViewHolder holder) {
        // Implement UI updates or additional actions here
        // For example, disable the "Đăng ký" button or show a message
        // You can access UI elements using their IDs and update them accordingly
        // For example: holder.btnDangKy.setText("Đã đăng ký");
        // holder.btnDangKy.setEnabled(false);
    }
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
