package com.example.hutech.model;

import com.google.firebase.Timestamp;

public class Comment {
    public String id;
    private String userId;
    private String newsId;
    private String commentText;
    private Timestamp commentTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Comment(String userId, String commentText, String newsId, Timestamp commentTime) {
        this.userId = userId;
        this.commentText = commentText;
        this.newsId = newsId;
        this.commentTime = commentTime;
    }
    public Comment(String id, String userId, String commentText, String newsId, Timestamp commentTime) {
        this.id = id;
        this.userId = userId;
        this.commentText = commentText;
        this.newsId = newsId;
        this.commentTime = commentTime;
    }
}
