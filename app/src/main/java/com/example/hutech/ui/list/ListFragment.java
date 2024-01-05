//package com.example.hutech.ui.list;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.hutech.Adapter.AttendedEventAdapter;
//import com.example.hutech.R;
//import com.example.hutech.databinding.FragmentListBinding;
//import com.example.hutech.model.AttendedEvent;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ListFragment extends Fragment {
//
//    private FragmentListBinding binding;
//    private RecyclerView recyclerView;
//    private AttendedEventAdapter attendedEventAdapter;
//    private List<AttendedEvent> attendedEventList;
//    private FirebaseFirestore firestore;
//    private CollectionReference attendedEventCollection;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentListBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        recyclerView = root.findViewById(R.id.recyclerViewEventJoined);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        attendedEventList = new ArrayList<>();
//        attendedEventAdapter = new AttendedEventAdapter(getContext(), attendedEventList);
//
//        recyclerView.setAdapter(attendedEventAdapter);
//
//        firestore = FirebaseFirestore.getInstance();
//        attendedEventCollection = firestore.collection("eventjoined");
//
//        fetchAttendedEventFromFirestore();
//
//        return root;
//    }
//
//    private void fetchAttendedEventFromFirestore() {
//        attendedEventCollection.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                attendedEventList.clear(); // Clear existing data before adding new data
//
//                for (DocumentSnapshot document : task.getResult()) {
//                    AttendedEvent attendedEvent = document.toObject(AttendedEvent.class);
//                    attendedEventList.add(attendedEvent);
//                }
//
//                attendedEventAdapter.notifyDataSetChanged();
//            } else {
//                // Handle error
//                Exception exception = task.getException();
//                if (exception != null) {
//                    exception.printStackTrace(); // Log the exception for debugging
//                }
//            }
//        });
//    }
//}

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
import com.example.hutech.R;
import com.example.hutech.databinding.FragmentListBinding;
import com.example.hutech.model.AttendedEvent;
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
    private AttendedEventAdapter attendedEventAdapter;
    private List<AttendedEvent> attendedEventList;
    private FirebaseFirestore firestore;
    private CollectionReference attendedEventCollection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerViewEventJoined);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        attendedEventList = new ArrayList<>();
        attendedEventAdapter = new AttendedEventAdapter(getContext(), attendedEventList);

        recyclerView.setAdapter(attendedEventAdapter);

        firestore = FirebaseFirestore.getInstance();
        attendedEventCollection = firestore.collection("eventjoined");

        fetchAttendedEventFromFirestore();

        return root;
    }

    private void fetchAttendedEventFromFirestore() {
        // Get the current user's UID
        String currentUserUid = getCurrentUserUid();

        if (currentUserUid != null) {
            attendedEventCollection.whereEqualTo("userUid", currentUserUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            attendedEventList.clear(); // Clear existing data before adding new data

                            for (DocumentSnapshot document : task.getResult()) {
                                AttendedEvent attendedEvent = document.toObject(AttendedEvent.class);
                                attendedEventList.add(attendedEvent);
                            }

                            attendedEventAdapter.notifyDataSetChanged();
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

    // Helper method to get the current user's UID
    private String getCurrentUserUid() {
        // Assuming you have Firebase Authentication set up
        // Retrieve the current user UID from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }
}

