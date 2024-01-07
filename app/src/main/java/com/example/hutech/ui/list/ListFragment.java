package com.example.hutech.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hutech.Adapter.AttendedEventAdapter;
import com.example.hutech.Adapter.RegisteredEventAdapter;
import com.example.hutech.R;
import com.example.hutech.databinding.FragmentListBinding;
import com.example.hutech.model.AttendedEvent;
import com.example.hutech.model.Events;
import com.example.hutech.model.RegisteredEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private RecyclerView recyclerView;
    private RegisteredEventAdapter registeredEventAdapter;
    private List<Events> events;

    // Firestore instance
    private FirebaseFirestore firestore;
    private CollectionReference registeredEventCollection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerViewEventJoined);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        events = new ArrayList<>();
        registeredEventAdapter = new RegisteredEventAdapter(getContext(), events);

        recyclerView.setAdapter(registeredEventAdapter);

        firestore = FirebaseFirestore.getInstance();
        registeredEventCollection = firestore.collection("registeredEvent");

        fetchRegisteredEventsFromFirestore();

        return root;
    }

    private void fetchRegisteredEventsFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            firestore.collection("registeredEvent")
                    .whereEqualTo("userId", currentUser.getUid())
                    .whereEqualTo("status", 2)  // Adjust status as needed
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<String> eventIds = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                RegisteredEvent registration = document.toObject(RegisteredEvent.class);
                                if (registration != null && registration.getStatus() == 2) {
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
