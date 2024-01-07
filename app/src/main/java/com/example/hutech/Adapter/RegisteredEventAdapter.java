package com.example.hutech.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutech.R;
import com.example.hutech.model.Events;
import com.example.hutech.model.RegisteredEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

public class RegisteredEventAdapter extends RecyclerView.Adapter<RegisteredEventAdapter.RegisteredEventViewHolder> {

    private Context context;
    private List<Events> eventsList;
    private FirebaseFirestore firestore;

    public RegisteredEventAdapter(Context context, List<Events> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RegisteredEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registeredevent_item, parent, false);
        return new RegisteredEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisteredEventViewHolder holder, int position) {
        Events event = eventsList.get(position);

        // Display the event details
        holder.nameTextView.setText(event.getName());
        holder.timeTextView.setText(event.getFormattedTime());
        holder.locationTextView.setText(event.getLocation());
        holder.txtKhoa.setText(event.getFaculty());
        Glide.with(context).load(event.getPoster()).into(holder.imageView);

        // Check user registration and handle UI updates
        checkUserRegistration(event.getId(), holder);

        // Set click listeners
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the QR code dialog for event ID
                checkUserRegistration(event.getId(), holder);
                showQrCodeDialog(event.getId());
            }
        });

        holder.btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the QR code dialog for check-in
                checkUserRegistration(event.getId(), holder);
                showQrCodeDialog("CheckIn:" + event.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class RegisteredEventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, timeTextView, locationTextView, txtKhoa;
        ImageView imageView;
        RelativeLayout relativeLayout;
        Button btnCheckIn;

        public RegisteredEventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            txtKhoa = itemView.findViewById(R.id.txtKhoa);
            imageView = itemView.findViewById(R.id.imageView);
            relativeLayout = itemView.findViewById(R.id.layoutItem);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
        }
    }

    private void showQrCodeDialog(String content) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_qr_code, null);
            ImageView imageViewQrCode = dialogView.findViewById(R.id.imageViewQrCode);
            imageViewQrCode.setImageBitmap(bitmap);

            builder.setView(dialogView);
            builder.setTitle("QR Code");
            builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void checkUserRegistration(String eventId, RegisteredEventViewHolder holder) {
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
                                if (registration != null && registration.getStatus() == 2) {
                                    // User is registered for this event
                                    updateUIAfterRegistration(holder, true);
                                } else {
                                    // User is not registered for this event
                                    updateUIAfterRegistration(holder, false);
                                }
                            }
                        } else {
                            // Handle error
                            updateUIAfterRegistration(holder, false);
                        }
                    });
        } else {
            // User is not logged in
            updateUIAfterRegistration(holder, false);
        }
    }

    private void updateUIAfterRegistration(RegisteredEventViewHolder holder, boolean isRegistered) {
        if (isRegistered) {
            holder.btnCheckIn.setText("Đã checkin");
            holder.btnCheckIn.setEnabled(false);
        } else {
            // Optionally, reset the button's text and enable it if needed
            holder.btnCheckIn.setText("Check In");
            holder.btnCheckIn.setEnabled(true);
        }
    }
}