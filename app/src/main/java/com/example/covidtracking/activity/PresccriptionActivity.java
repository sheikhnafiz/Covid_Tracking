package com.example.covidtracking.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.covidtracking.R;
import com.example.covidtracking.adapter.PrescriptionAdapter;
import com.example.covidtracking.databinding.ActivityPresccriptionBinding;
import com.example.covidtracking.databinding.ActivityProfileBinding;
import com.example.covidtracking.model.Prescription;

import java.util.ArrayList;
import java.util.List;

public class PresccriptionActivity extends AppCompatActivity {

    ActivityPresccriptionBinding binding;
    PrescriptionAdapter adapter;
    List<Prescription> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_presccription);
        initComponent();
        initFunction();
    }

    private void initComponent()
    {
        binding.showPrescriptionRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.add(new Prescription("","AJM Hafiz","This is my operation doctor","12-2-2020"));
        list.add(new Prescription("","AGM Khair","This is my operation doctor","12-2-2021"));
        list.add(new Prescription("","AGM Nahid","This is my operation doctor","12-2-2022"));
        adapter = new PrescriptionAdapter(this,list);
        binding.showPrescriptionRV.setAdapter(adapter);
    }

    private void initFunction()
    {
        binding.addPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addPrescriptionLL.setVisibility(View.VISIBLE);
                binding.showPrescriptionRV.setVisibility(View.GONE);
            }
        });

        binding.showPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addPrescriptionLL.setVisibility(View.GONE);
                binding.showPrescriptionRV.setVisibility(View.VISIBLE);
            }
        });

        binding.savePrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                binding.showPrescriptionRV.setVisibility(View.VISIBLE);
                binding.addPrescriptionLL.setVisibility(View.GONE);
            }
        });
    }
}