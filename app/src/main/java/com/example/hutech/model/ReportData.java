package com.example.hutech.model;

import com.google.gson.annotations.SerializedName;

public class ReportData {
    @SerializedName("mssv")
    private String mssv;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("reportContent")
    private String reportContent;
    @SerializedName("userUid")
    private String userUid;  // New field for user UID

    // Constructors, getters, and setters...

    public ReportData() {
        // Empty constructor needed for Firestore
    }

    public ReportData(String mssv, String fullName, String reportContent, String userUid) {
        this.mssv = mssv;
        this.fullName = fullName;
        this.reportContent = reportContent;
        this.userUid = userUid;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
