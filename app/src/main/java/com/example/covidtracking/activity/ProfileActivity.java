package com.example.covidtracking.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.covidtracking.R;
import com.example.covidtracking.databinding.ActivityProfileBinding;

import static com.example.covidtracking.Config.mAuth;
import static com.example.covidtracking.Config.userID;
import static com.example.covidtracking.Config.userProfileInfo;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        initComponent();
    }

    private void initComponent()
    {
        try {


            if (userProfileInfo != null) {
                binding.tvName.setText(userProfileInfo.getName());
                binding.tvPhoneNumber.setText(userProfileInfo.getName());
                binding.tvGender.setText(userProfileInfo.getGender());
                binding.tvPatient.setText(userProfileInfo.getCategory());
                binding.tvEmail.setText(mAuth.getCurrentUser().getEmail());
            }
        }catch (Exception e){}
        }
}