package com.example.hutech.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hutech.R;
import com.example.hutech.model.ReportData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReportActivity extends AppCompatActivity {

    private Button btnBack;
    private Button btnSend;
    private EditText edtMSSV;
    private EditText edtHoTen;
    private EditText edtReportContent;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firestore = FirebaseFirestore.getInstance();

        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        edtMSSV = findViewById(R.id.edtMSSV);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtReportContent = findViewById(R.id.edtReportContent);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mssv = edtMSSV.getText().toString().trim();
                String fullName = edtHoTen.getText().toString().trim();
                String reportContent = edtReportContent.getText().toString().trim();

                if (!mssv.isEmpty() && !fullName.isEmpty() && !reportContent.isEmpty()) {
                    // Get the current user's UID
                    String userUid = getCurrentUserUid();

                    if (userUid != null) {
                        ReportData reportData = new ReportData(mssv, fullName, reportContent, userUid);

                        firestore.collection("reports")
                                .add(reportData)
                                .addOnSuccessListener(documentReference -> {
                                    showToast("Gửi thành công");
                                    navigateToHomeActivity();
                                })
                                .addOnFailureListener(e -> {
                                    showToast("Gửi thất bại: " + e.getMessage());
                                });
                    } else {
                        showToast("Không thể xác định người dùng hiện tại");
                    }
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private String getCurrentUserUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }
}
