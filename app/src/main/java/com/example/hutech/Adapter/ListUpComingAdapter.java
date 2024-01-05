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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hutech.Activites.DetailEventUpcomingActivity;
import com.example.hutech.Activites.DetailNewsActivity;
import com.example.hutech.R;
import com.example.hutech.model.AttendedEvent;
import com.example.hutech.model.EventListComing;
import com.example.hutech.model.Newpaper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ListUpComingAdapter extends RecyclerView.Adapter<ListUpComingAdapter.EventComingViewHolder> {

    private Context context;
    private List<EventListComing> eventListComings;

    private FirebaseFirestore firestore;

    public ListUpComingAdapter(Context context, List<EventListComing> eventListComingList) {
        this.context = context;
        this.eventListComings = eventListComingList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public EventComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ListUpComingAdapter.EventComingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventComingViewHolder holder, int position) {
        EventListComing eventListComing = eventListComings.get(position);

        holder.nameTextView.setText(eventListComing.getName());
        holder.timeTextView.setText(eventListComing.getStartDay());
        holder.locationTextView.setText(eventListComing.getLocation());
        holder.txtKhoa.setText(eventListComing.getFaculty());

        Glide.with(context).load(eventListComing.getImage()).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailEventUpcomingActivity.class);
                intent.putExtra("id", eventListComing.getId());
                intent.putExtra("faculty", eventListComing.getFaculty());
                intent.putExtra("location", eventListComing.getLocation());
                intent.putExtra("name", eventListComing.getName());
                intent.putExtra("image", eventListComing.getImage());
                intent.putExtra("startDay", eventListComing.getStartDay());
                context.startActivity(intent);
            }
        });
        holder.btnXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the DetailNewsActivity with the details of the selected news article
                Intent intent = new Intent(context, DetailEventUpcomingActivity.class);
                intent.putExtra("id", eventListComing.getId());
                intent.putExtra("faculty", eventListComing.getFaculty());
                intent.putExtra("location", eventListComing.getLocation());
                intent.putExtra("name", eventListComing.getName());
                intent.putExtra("image", eventListComing.getImage());
                intent.putExtra("startDay", eventListComing.getStartDay());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventListComings.size();
    }

    public class EventComingViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, timeTextView,locationTextView,txtKhoa;
        ImageView imageView;
        Button btnXemChiTiet;
        RelativeLayout relativeLayout;

        public EventComingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            txtKhoa = itemView.findViewById(R.id.txtKhoa);
            imageView = itemView.findViewById(R.id.imageView);
            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTiet);

            relativeLayout = itemView.findViewById(R.id.layoutItem);

        }
    }
}
