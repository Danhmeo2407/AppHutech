package com.example.hutech.model;

import com.google.gson.annotations.SerializedName;

public class RegisteredEvent {
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
    @SerializedName("image")
    private String image;
    @SerializedName("faculty")
    private String faculty;
    @SerializedName("qr")
    private String qr;
    @SerializedName("userUid")
    private String userUid;

    public RegisteredEvent(){

    }

    public RegisteredEvent(String id, String name, String post, String content, String location, String startDay, String image, String faculty, String qr, String userUid) {
        this.id = id;
        this.name = name;
        this.post = post;
        this.content = content;
        this.location = location;
        this.startDay = startDay;
        this.image = image;
        this.faculty = faculty;
        this.qr = qr;
        this.userUid = userUid;
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

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
