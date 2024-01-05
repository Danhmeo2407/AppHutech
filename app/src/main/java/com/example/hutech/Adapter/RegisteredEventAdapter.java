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
import com.example.hutech.Activites.DetailRegisteredEventActivity;
import com.example.hutech.R;
import com.example.hutech.model.EventListComing;
import com.example.hutech.model.RegisteredEvent;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RegisteredEventAdapter extends RecyclerView.Adapter<RegisteredEventAdapter.RegisteredEventViewHolder> {

    private Context context;
    private List<RegisteredEvent> registeredEvents;

    private FirebaseFirestore firestore;

    public RegisteredEventAdapter(Context context, List<RegisteredEvent> registeredEventList) {
        this.context = context;
        this.registeredEvents = registeredEventList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RegisteredEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.registeredevent_item, parent, false);
        return new RegisteredEventAdapter.RegisteredEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisteredEventViewHolder holder, int position) {
        RegisteredEvent registeredEvent = registeredEvents.get(position);

        holder.nameTextView.setText(registeredEvent.getName());
        holder.timeTextView.setText(registeredEvent.getStartDay());
        holder.locationTextView.setText(registeredEvent.getLocation());
        holder.txtKhoa.setText(registeredEvent.getFaculty());

        Glide.with(context).load(registeredEvent.getImage()).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailRegisteredEventActivity.class);
                intent.putExtra("id", registeredEvent.getId());
                intent.putExtra("faculty", registeredEvent.getFaculty());
                intent.putExtra("location", registeredEvent.getLocation());
                intent.putExtra("name", registeredEvent.getName());
                intent.putExtra("image", registeredEvent.getImage());
                intent.putExtra("startDay", registeredEvent.getStartDay());
                context.startActivity(intent);
            }
        });
        holder.btnXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the DetailNewsActivity with the details of the selected news article
                Intent intent = new Intent(context, DetailRegisteredEventActivity.class);
                intent.putExtra("id", registeredEvent.getId());
                intent.putExtra("faculty", registeredEvent.getFaculty());
                intent.putExtra("location", registeredEvent.getLocation());
                intent.putExtra("name", registeredEvent.getName());
                intent.putExtra("image", registeredEvent.getImage());
                intent.putExtra("startDay", registeredEvent.getStartDay());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return registeredEvents.size();
    }

    public class RegisteredEventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, timeTextView,locationTextView,txtKhoa;
        ImageView imageView;
        Button btnXemChiTiet;
        RelativeLayout relativeLayout;

        public RegisteredEventViewHolder(@NonNull View itemView) {
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
