package com.example.hutech.model;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.SerializedName;

public class RegisteredEvent extends Events{
    @SerializedName("id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("eventId")
    private String eventId;
    @SerializedName("timestamp")
    private Timestamp timestamp;
    @SerializedName("status")
    private int status;
    public RegisteredEvent(){

    }

    public RegisteredEvent(String id, String userId, String eventId, Timestamp timestamp, int status) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.status = status;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
