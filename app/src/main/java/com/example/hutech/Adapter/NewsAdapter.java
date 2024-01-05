package com.example.hutech.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutech.Activites.DetailNewsActivity;
import com.example.hutech.R;
import com.example.hutech.model.Newpaper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewViewHolder> {
    private Context context;
    private List<Newpaper> newpapers;
    private FirebaseFirestore firestore;

    public NewsAdapter(Context context, List<Newpaper> newpaperList) {
        this.context = context;
        this.newpapers = newpaperList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public NewsAdapter.NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_item, parent, false);
        return new NewsAdapter.NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewViewHolder holder, int position) {
        Newpaper newpaper = newpapers.get(position);

        holder.newtitleTextView.setText(newpaper.getTitle());
//        holder.newsDescriptionTextView.setText(newpaper.getDescription());
        holder.newtimeTextView.setText(newpaper.getTime());

        // Use Glide to load the image from the URL
        Glide.with(context).load(newpaper.getImage()).into(holder.newimageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the DetailNewsActivity with the details of the selected news article
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra("id", newpaper.getId());
                intent.putExtra("description", newpaper.getDescription());
                intent.putExtra("title", newpaper.getTitle());
                intent.putExtra("image", newpaper.getImage());
                intent.putExtra("time", newpaper.getTime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newpapers.size();
    }

    public class NewViewHolder extends RecyclerView.ViewHolder {

        private TextView newtitleTextView, newtimeTextView,newsDescriptionTextView;
        private ImageView newimageView;
        private RelativeLayout relativeLayout;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);

            newtitleTextView = itemView.findViewById(R.id.newsTitleTextView);
            newsDescriptionTextView = itemView.findViewById(R.id.newsDescriptionTextView);
            newtimeTextView = itemView.findViewById(R.id.newsTimeTextView);
            newimageView = itemView.findViewById(R.id.newsImageView);

            relativeLayout = itemView.findViewById(R.id.layoutNews);
        }
    }
}
