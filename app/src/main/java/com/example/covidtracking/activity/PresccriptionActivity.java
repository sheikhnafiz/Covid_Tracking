package com.example.covidtracking.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.covidtracking.R;
import com.example.covidtracking.adapter.PrescriptionAdapter;
import com.example.covidtracking.databinding.ActivityPresccriptionBinding;
import com.example.covidtracking.databinding.ActivityProfileBinding;
import com.example.covidtracking.model.Blog;
import com.example.covidtracking.model.Prescription;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.covidtracking.Config.BitMapToString;
import static com.example.covidtracking.Config.database;
import static com.example.covidtracking.Config.doctorBlog;
import static com.example.covidtracking.Config.myRef;
import static com.example.covidtracking.Config.userID;
import static com.example.covidtracking.Config.userProfileInfo;

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

        if (ContextCompat.checkSelfPermission(PresccriptionActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PresccriptionActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        binding.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

    }

    Bitmap captureImage = null;

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        captureImage = (Bitmap) data.getExtras().get("data");
        binding.addPrescriptionImageId.setImageBitmap(captureImage);
    }

    private void initComponent() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        binding.showPrescriptionRV.setLayoutManager(new GridLayoutManager(this, 2));
       // binding.showPrescriptionRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getPrescription();
       // getPrescription();
      /*  adapter = new PrescriptionAdapter(PresccriptionActivity.this,   userProfileInfo.getPrescription());
        binding.showPrescriptionRV.setAdapter(adapter);*/

    }

    private void initFunction() {
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
            public void onClick(View v) {


                viraication();
            }
        });
    }

    private void getPrescription() {

        myRef.child("User").child(userID).child("Prescription").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Prescription b = data.getValue(Prescription.class);
                    list.add(b);
                }
                adapter = new PrescriptionAdapter(PresccriptionActivity.this,  list);
                binding.showPrescriptionRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    String DoctorName, Date, Detils;

    private void viraication() {

        DoctorName = binding.addPrescriptionDoctorNameId.getText().toString().trim();
        Date = binding.addPrescriptionDateId.getText().toString().trim();
        Detils = binding.addPrescriptionDetailsId.getText().toString().trim();
        if (captureImage == null) {
            Toast.makeText(this, "Select the image !!", Toast.LENGTH_SHORT).show();
            return;
        } else if (DoctorName.isEmpty()) {

            binding.addPrescriptionDoctorNameId.setError("Please Enter Doctor Mame");
            return;
        } else if (Date.isEmpty()) {
            binding.addPrescriptionDateId.setError("Please Enter Date");
            return;
        } else if (Detils.isEmpty()) {
            binding.addPrescriptionDetailsId.setError("Please Enter details");
            return;
        } else {
            Prescription prescription = new Prescription(BitMapToString(captureImage), DoctorName, Detils, Date);

            myRef.child("User").child(userID).child("Prescription").child(myRef.push().getKey()).setValue(prescription);
            Toast.makeText(this, "Saved !!", Toast.LENGTH_SHORT).show();
            binding.showPrescriptionRV.setVisibility(View.VISIBLE);
            binding.addPrescriptionLL.setVisibility(View.GONE);
           /* binding.showPrescriptionRV.setVisibility(View.VISIBLE);
            binding.addPrescriptionLL.setVisibility(View.GONE);*/
        }

    }


}