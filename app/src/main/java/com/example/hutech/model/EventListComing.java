package com.example.hutech.model;

import com.google.gson.annotations.SerializedName;

public class EventListComing {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("post")
    private String post;
    @SerializedName("content")
    private String content;
    @SerializedName("location")
    private String location;
    @SerializedName("startDay")
    private String startDay;
    @SerializedName("timeStart")
    private String timeStart;
    @SerializedName("timeEnd")
    private String timeEnd;
    @SerializedName("image")
    private String image;
    @SerializedName("faculty")
    private String faculty;

    public EventListComing() {
        // Empty constructor required for Firestore
    }

    public EventListComing(String id, String name, String post, String content, String location, String startDay, String timeStart, String timeEnd, String image, String faculty) {
        this.id = id;
        this.name = name;
        this.post = post;
        this.content = content;
        this.location = location;
        this.startDay = startDay;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.image = image;
        this.faculty = faculty;
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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
