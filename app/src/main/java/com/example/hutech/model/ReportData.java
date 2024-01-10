package com.example.hutech.model;

import com.google.gson.annotations.SerializedName;

public class ReportData {
    @SerializedName("reportContent")
    private String reportContent;
    @SerializedName("userUid")
    private String userUid;
    @SerializedName("feedback")
    private String feedback;

    // Constructors, getters, and setters...

    public ReportData() {
        // Empty constructor needed for Firestore
    }

    public ReportData(String reportContent, String userUid, String feedback) {
        this.reportContent = reportContent;
        this.userUid = userUid;
        this.feedback = feedback;
    }

    public ReportData(String reportContent, String userUid) {
        this.reportContent = reportContent;
        this.userUid = userUid;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
