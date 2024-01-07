package com.example.hutech.ui.news;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hutech.Adapter.NewsAdapter;
import com.example.hutech.R;
import com.example.hutech.databinding.FragmentNewsBinding;
import com.example.hutech.model.News;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewFragment extends Fragment {

    private FragmentNewsBinding binding;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    private FirebaseFirestore firestore;
    private CollectionReference newsCollection;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerViewNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(getContext(), newsList);

        recyclerView.setAdapter(newsAdapter);

        firestore = FirebaseFirestore.getInstance();
        newsCollection = firestore.collection("news");

        fetchNewsFromFirestore();

        return root;
    }

    private void fetchNewsFromFirestore() {
        newsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                newsList.clear(); // Clear existing data before adding new data

                for (DocumentSnapshot document : task.getResult()) {
                    News news = document.toObject(News.class);
                    newsList.add(news);
                }

                newsAdapter.notifyDataSetChanged();
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
