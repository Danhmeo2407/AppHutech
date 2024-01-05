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
import com.example.hutech.model.AttendedEvent;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AttendedEventAdapter extends RecyclerView.Adapter<AttendedEventAdapter.AttendedEventViewHolder> {
    private Context context;
    private List<AttendedEvent> attendedEvents;
    private FirebaseFirestore firestore;

    public AttendedEventAdapter(Context context, List<AttendedEvent> attendedEventList) {
        this.context = context;
        this.attendedEvents = attendedEventList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public AttendedEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendedevent_item, parent, false);
        return new AttendedEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendedEventViewHolder holder, int position) {
        AttendedEvent attendedEvent = attendedEvents.get(position);

        holder.attendedEventTitleTextView.setText(attendedEvent.getTitle());
        holder.attendedEventTimeTextView.setText(attendedEvent.getTime());
        holder.attendedEventDescriptionTextView.setText(attendedEvent.getDescription());

        // Use Glide to load the image from the URL
        Glide.with(context).load(attendedEvent.getImage()).into(holder.attendedEventImageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the DetailNewsActivity with the details of the selected attended event
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra("id", attendedEvent.getId());
                intent.putExtra("description", attendedEvent.getDescription());
                intent.putExtra("title", attendedEvent.getTitle());
                intent.putExtra("image", attendedEvent.getImage());
                intent.putExtra("time", attendedEvent.getTime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendedEvents.size();
    }

    public class AttendedEventViewHolder extends RecyclerView.ViewHolder {

        private TextView attendedEventTitleTextView, attendedEventTimeTextView, attendedEventDescriptionTextView;
        private ImageView attendedEventImageView;
        private RelativeLayout relativeLayout;

        public AttendedEventViewHolder(@NonNull View itemView) {
            super(itemView);

            attendedEventTitleTextView = itemView.findViewById(R.id.attendedEventTitleTextView);
            attendedEventTimeTextView = itemView.findViewById(R.id.attendedEventTimeTextView);
            attendedEventDescriptionTextView = itemView.findViewById(R.id.attendedEventDescriptionTextView);
            attendedEventImageView = itemView.findViewById(R.id.attendedEventImageView);

            relativeLayout = itemView.findViewById(R.id.layoutAttendedEvent);
        }
    }
}
