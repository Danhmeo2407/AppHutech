package com.example.hutech.ui.account;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.hutech.Activites.RegisterActivity;
import com.example.hutech.Activites.ResetPasswordActivity;
import com.example.hutech.R;
import com.example.hutech.databinding.FragmentAccountBinding;
import com.example.hutech.ui.qrcode.QrcodeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private TextView txtNameUser, txtMSSV, txtSDT, txtGmail;
    private Button btnSua1, btnSua2, btnDoiPassword, btnLogOut, btnBack, btnLuu,btnDoiAvatar;
    private ImageView avatarImageView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;



    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        QrcodeViewModel dashboardViewModel =
                new ViewModelProvider(this).get(QrcodeViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtNameUser = root.findViewById(R.id.txtNameUser);
        txtGmail = root.findViewById(R.id.txtGmail);
        txtSDT = root.findViewById(R.id.txtSDT);
        txtMSSV = root.findViewById(R.id.txtMSSV);
        avatarImageView = root.findViewById(R.id.avatarUrl);

        btnLogOut = root.findViewById(R.id.btnLogOut);
        btnDoiPassword = root.findViewById(R.id.btnDoiPassword);

        btnDoiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the ResetPasswordActivity
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        btnDoiAvatar = root.findViewById(R.id.btnDoiAvatar);

        btnDoiAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        // Retrieve and display user data from Firestore
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("userName");
                            String emailId = documentSnapshot.getString("emailId");
                            String Mssv = documentSnapshot.getString("Mssv");
                            String Sdt = documentSnapshot.getString("Sdt");
                            String avatarUrl = documentSnapshot.getString("avatarUrl");

                            // ... (retrieve other fields)

                            updateUI(userName,emailId,Mssv,Sdt,avatarUrl);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failures
                    });
        }


        btnSua1 = root.findViewById(R.id.btnSua1);

        btnSua1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show an AlertDialog or custom dialog for editing phone number
                showEditPhoneNumberDialog();
            }
        });



        return root;
    }

    private void updateUI(String userName, String emailId,String Mssv,String Sdt,String avatarUrl) {
        txtNameUser.setText(userName);
        txtGmail.setText(emailId);
        txtMSSV.setText(Mssv);
        txtSDT.setText(Sdt);
        Glide.with(this).load(avatarUrl).into(avatarImageView);
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageAndUpdateDatabase(imageUri);
        }
    }

    private void uploadImageAndUpdateDatabase(Uri imageUri) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference avatarRef = storageReference.child("avatars/" + userId + ".jpg");
            avatarRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            updateAvatarInFirestore(uri.toString());
                        });
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }
    private void updateAvatarInFirestore(String imageUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users")
                    .document(userId)
                    .update("avatarUrl", imageUrl)
                    .addOnSuccessListener(aVoid -> {
                        updateUIWithAvatar(imageUrl);
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }

    private void updateUIWithAvatar(String avatarUrl) {
        Glide.with(this).load(avatarUrl).into(avatarImageView);
    }


    private void showEditPhoneNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Đổi số điện thoại");

        final EditText input = new EditText(requireContext());
        builder.setView(input);

        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPhoneNumber = input.getText().toString();
                txtSDT.setText(newPhoneNumber);
                updatePhoneNumberInFirestore(newPhoneNumber);
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void updatePhoneNumberInFirestore(String newPhoneNumber) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            db.collection("users")
                    .document(userId)
                    .update("Sdt", newPhoneNumber)
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
