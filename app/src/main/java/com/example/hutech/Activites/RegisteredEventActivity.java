package com.example.hutech.Activites;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hutech.Adapter.ListUpComingAdapter;
import com.example.hutech.Adapter.RegisteredEventAdapter;
import com.example.hutech.R;
import com.example.hutech.model.Events;
import com.example.hutech.model.RegisteredEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RegisteredEventActivity extends AppCompatActivity {

    private Button btnBack, btnCheckIn;
    private RecyclerView recyclerView;
    private RegisteredEventAdapter registeredEventAdapter;
    private List<Events> events;

    // Firestore instance
    private FirebaseFirestore firestore;
    private CollectionReference eventRegisteredCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerViewRegisteredEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        events = new ArrayList<>();

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Specify the collection reference for EventListComing
        eventRegisteredCollection = firestore.collection("events");

        // Fetch events from Firestore
        fetchEventsListFromFirestore();

        registeredEventAdapter = new RegisteredEventAdapter(this, events);
        recyclerView.setAdapter(registeredEventAdapter);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void fetchEventsListFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            firestore.collection("registeredEvent")
                    .whereEqualTo("userId", currentUser.getUid())
                    .whereIn("status", Arrays.asList(1, 2))  // Use 'whereIn' for multiple status values
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<String> eventIds = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                RegisteredEvent registration = document.toObject(RegisteredEvent.class);
                                if (registration != null && (registration.getStatus() == 1 || registration.getStatus() == 2)) {
                                    eventIds.add(registration.getEventId());
                                }
                            }
                            // Now fetch the details of the events
                            fetchEventDetails(eventIds);
                        } else {
                            // Handle error
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace(); // Log the exception for debugging
                            }
                        }
                    });
        }
    }

    private void fetchEventDetails(List<String> eventIds) {
        events.clear(); // Clear existing data before adding new data

        for (String eventId : eventIds) {
            firestore.collection("events")
                    .document(eventId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Events event = document.toObject(Events.class);
                                events.add(event);
                                registeredEventAdapter.notifyDataSetChanged();

                            } else {
                                // Handle the case where the event document doesn't exist
                            }
                        } else {
                            // Handle error
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace(); // Log the exception for debugging
                            }
                        }
                    });
        }
    }

}
