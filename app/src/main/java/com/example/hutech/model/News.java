package com.example.hutech.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class News{
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private Timestamp time;
    @SerializedName("image")
    private String image;

    public News() {
        // Default constructor is required by Firestore
    }

    public News(String id, String title, String description, Timestamp time, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFormattedTime() {
        if (time != null) {
            Date date = time.toDate();
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy h:mm a", Locale.US);
            return outputFormat.format(date);
        }
        return null;
    }



}
