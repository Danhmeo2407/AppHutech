package com.example.hutech.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hutech.Adapter.ListUpComingAdapter;
import com.example.hutech.Adapter.RegisteredEventAdapter;
import com.example.hutech.R;
import com.example.hutech.model.EventListComing;
import com.example.hutech.model.RegisteredEvent;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RegisteredEventActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView recyclerView;
    private RegisteredEventAdapter registeredEventAdapter;
    private List<RegisteredEvent> registeredEventList;

    private FirebaseFirestore firestore;
    private CollectionReference registeredEventCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        recyclerView = findViewById(R.id.recyclerViewRegisteredEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        registeredEventList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        registeredEventCollection = firestore.collection("registeredEvent");

        fetchRegisteredEventFromFirestore();

        registeredEventAdapter = new RegisteredEventAdapter(this, registeredEventList);
        recyclerView.setAdapter(registeredEventAdapter);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void fetchRegisteredEventFromFirestore() {
        registeredEventCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                registeredEventList.clear(); // Clear existing data before adding new data

                for (DocumentSnapshot document : task.getResult()) {
                    RegisteredEvent registeredEvent = document.toObject(RegisteredEvent.class);
                    registeredEventList.add(registeredEvent);
                }

                registeredEventAdapter.notifyDataSetChanged();
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