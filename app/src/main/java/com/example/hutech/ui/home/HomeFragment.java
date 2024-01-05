package com.example.hutech.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hutech.Activites.HistoryReportActivity;
import com.example.hutech.Activites.ListUpcomingActivity;
import com.example.hutech.Activites.RegisteredEventActivity;
import com.example.hutech.Activites.ReportActivity;
import com.example.hutech.R;
import com.example.hutech.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private Button btnSuKienSapDienRa;
    private Button btnKheuNai;
    private Button btnLichKhieuNai;
    private Button btnSuKienDaThamGia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        btnSuKienSapDienRa = root.findViewById(R.id.btnSuKienSapDienRa);
        btnLichKhieuNai = root.findViewById(R.id.btnLichKhieuNai);
        btnKheuNai = root.findViewById(R.id.btnKheuNai);
        btnSuKienDaThamGia = root.findViewById(R.id.btnSuKienDaThamGia);


        btnSuKienSapDienRa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ListUpcomingActivity.class);
                startActivity(intent);
            }
        });

        btnLichKhieuNai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryReportActivity.class);
                startActivity(intent);
            }
        });

        btnKheuNai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });

        btnSuKienDaThamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisteredEventActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}