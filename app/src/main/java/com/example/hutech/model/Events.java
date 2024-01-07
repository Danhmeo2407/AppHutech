package com.example.hutech.model;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Events {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("location")
    private String location;
    @SerializedName("poster")
    private String poster;
    @SerializedName("faculty")
    private String faculty;
    @SerializedName("beginTime")
    private Timestamp beginTime;
    @SerializedName("quantity")
    private Long quantity;

    public Events(String id, String name, String description, String location, String poster, String faculty, Timestamp beginTime, Long quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.poster = poster;
        this.faculty = faculty;
        this.beginTime = beginTime;
        this.quantity = quantity;
    }

    public Events() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getFormattedTime() {
        if (beginTime != null) {
            Date date = beginTime.toDate();
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy h:mm a", Locale.US);
            return outputFormat.format(date);
        }
        return null;
    }
}
