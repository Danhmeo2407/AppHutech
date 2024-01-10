package com.example.hutech.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hutech.R;
import com.example.hutech.model.ReportData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HistoryReportActivity extends AppCompatActivity {

    private Button btnBack;
    private TextView txtReportList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firestore = FirebaseFirestore.getInstance();

        btnBack = findViewById(R.id.btnBack);
        txtReportList = findViewById(R.id.txtReportList);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Đọc và hiển thị danh sách báo cáo từ Firestore
        readReportListFromFirestore();
    }

    private void readReportListFromFirestore() {
        // Get the current user's UID
        String currentUserUid = getCurrentUserUid();

        if (currentUserUid != null) {
            CollectionReference reportsRef = firestore.collection("reports");

            reportsRef.whereEqualTo("userUid", currentUserUid)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            StringBuilder reportList = new StringBuilder();

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                ReportData reportData = documentSnapshot.toObject(ReportData.class);

                                if (reportData != null) {
                                    reportList.append("Nội dung khiếu nại: ").append(reportData.getReportContent()).append("\n\n");
                                    if(reportData.getFeedback() != null){
                                    reportList.append("Trang thái: ").append(reportData.getFeedback()).append("\n\n");
                                    }else {
                                        reportList.append("Trạng thái: Chưa nhận được câu trả lời!\n\n\n\n\n");
                                    }

                                }
                            }

                            txtReportList.setText(reportList.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    private String getCurrentUserUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }
}
