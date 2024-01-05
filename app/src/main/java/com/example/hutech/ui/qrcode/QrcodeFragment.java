package com.example.hutech.ui.qrcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hutech.R;
import com.example.hutech.databinding.FragmentQrcodeBinding;
import com.example.hutech.model.RegisteredEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class QrcodeFragment extends Fragment {

    private FragmentQrcodeBinding binding;

    private ImageView qrCodeImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QrcodeViewModel dashboardViewModel =
                new ViewModelProvider(this).get(QrcodeViewModel.class);

        binding = FragmentQrcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


        Button btnScanQRCode = root.findViewById(R.id.btnQuetQR);


        btnScanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the QR code scanner
                startQRCodeScanner();
            }
        });

        qrCodeImageView = root.findViewById(R.id.qrCodeImageView);


        Button btnGenerateQRCode = root.findViewById(R.id.btnGenerateQRCode);

        btnGenerateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem người dùng đã đăng nhập hay chưa
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    String userUid = currentUser.getUid();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("users").document(userUid);

                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String fetchedUserName = documentSnapshot.getString("userName");
                                String fetchedMssv = documentSnapshot.getString("Mssv");

                                generateQRCode(userUid, fetchedUserName, fetchedMssv);
                            } else {
                                // Xử lý trường hợp khi tài khoản người dùng không tồn tại trong Firestore
                                Toast.makeText(view.getContext(), "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Xử lý trường hợp khi có lỗi khi truy cập Firestore
                            Toast.makeText(view.getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Xử lý trường hợp khi người dùng chưa đăng nhập
                    Toast.makeText(view.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return root;


    }

    private void startQRCodeScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("QR code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            // Extract information from the scanned result
            String[] qrCodeData = result.getContents().split("\\|");

            if (qrCodeData.length == 3) {
                String userUid = qrCodeData[0];
                String userName = qrCodeData[1];
                String mssv = qrCodeData[2];

                // Save the information to eventjoined or perform other actions
                saveToEventJoined(userUid, userName, mssv);
            } else {
                // Handle invalid QR code data format
                Toast.makeText(requireContext(), "Invalid QR Code", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void saveToEventJoined(String userUid, String userName, String mssv) {
        // Create an instance of RegisteredEvent with the provided data
        RegisteredEvent registeredEvent = new RegisteredEvent(null, null, null, null, null, null, null, null, null, userUid);

        // Save the data to the "eventjoined" collection in Firestore
        FirebaseFirestore.getInstance().collection("eventjoined").document(userUid)
                .set(registeredEvent)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Toast.makeText(requireContext(), "Successfully saved to eventjoined", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Failed to save to eventjoined", Toast.LENGTH_SHORT).show();
                });
    }


    private void generateQRCode(String userUid, String fetchedUserName, String fetchedMssv) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userUid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String userName = document.getString("userName");
                    String mssv = document.getString("Mssv");

                    String data = userUid + "|" + userName + "|" + mssv;

                    try {
                        // Generate QR Code
                        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                        // Display QR Code
                        showQRCode(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Document does not exist
                    // Handle the case when the document does not exist
                }
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    exception.printStackTrace();
                }
            }
        });
    }


    private void showQRCode(Bitmap bitmap) {
        qrCodeImageView.setImageBitmap(bitmap);
    }


}