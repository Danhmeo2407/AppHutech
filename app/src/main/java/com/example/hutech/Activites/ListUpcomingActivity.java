package com.example.hutech.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hutech.Adapter.ListUpComingAdapter;
import com.example.hutech.R;
import com.example.hutech.model.EventListComing;
import com.example.hutech.model.Events;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListUpcomingActivity extends AppCompatActivity {

    private Button btnBack, btnDangKy;
    private RecyclerView recyclerView;
    private ListUpComingAdapter listUpComingAdapter;
    private List<Events> events;

    // Firestore instance
    private FirebaseFirestore firestore;
    private CollectionReference eventListComingCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_upcoming);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerViewEventListComing);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        events = new ArrayList<>();

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Specify the collection reference for EventListComing
        eventListComingCollection = firestore.collection("events");

        // Fetch events from Firestore
        fetchEventsListFromFirestore();

        listUpComingAdapter = new ListUpComingAdapter(this, events);
        recyclerView.setAdapter(listUpComingAdapter);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void fetchEventsListFromFirestore() {
        eventListComingCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                events.clear(); // Clear existing data before adding new data

                for (DocumentSnapshot document : task.getResult()) {
                    Events event = document.toObject(Events.class);

                    // Check if the event's start time is after the current date
                    if (event != null && event.getBeginTime() != null && event.getBeginTime().toDate().after(new Date())) {
                        events.add(event);
                    }
                }

                listUpComingAdapter.notifyDataSetChanged();
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
