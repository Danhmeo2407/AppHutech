package com.example.hutech.model;

import com.google.gson.annotations.SerializedName;

public class AttendedEvent {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private String time;
    @SerializedName("image")
    private String image;
    public AttendedEvent() {

    }

    public AttendedEvent(String id, String title, String description, String time, String image) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
