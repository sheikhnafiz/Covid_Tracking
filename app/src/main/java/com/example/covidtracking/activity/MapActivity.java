package com.example.covidtracking.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracking.R;
import com.example.covidtracking.model.PatientHistory;
import com.example.covidtracking.model.UserProfileInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.covidtracking.Config.myRef;
import static com.example.covidtracking.Config.userID;
import static com.example.covidtracking.Config.userProfileInfo;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private Button button;
    private TextView textView;
    private FusedLocationProviderClient fusedLocationProviderClient ;
    PatientHistory patientHistory;
    List<PatientHistory> patientHistories;

    LatLng sydney = new LatLng(23, 90);
    LatLng TamWorth = new LatLng(23.083332, 90.916672);
    LatLng NewCastle = new LatLng(23.916668, 90.750000);
    LatLng Brisbane = new LatLng(23.470125, 90.021072);

    // creating array list for adding all our locations.
    private ArrayList<LatLng> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
        locationArrayList.add(sydney);
        locationArrayList.add(TamWorth);
        locationArrayList.add(NewCastle);
        locationArrayList.add(Brisbane);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        button = findViewById(R.id.location_button);
        textView = findViewById(R.id.location_text);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (getApplicationContext().checkSelfPermission(permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location !=null ){
                                Double lat = location.getLatitude();
                                Double longt = location.getLongitude();

                                textView.setText(lat+" , " +longt);
                                Toast.makeText(MapActivity.this,"Success",Toast.LENGTH_LONG);

                            }
                        }
                    });

                }else {
                    requestPermissions(new String[]{permission.ACCESS_FINE_LOCATION},1);
                }

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < locationArrayList.size(); i++) {
            googleMap.addMarker(new MarkerOptions().icon(BitmapFromVector(getApplicationContext(), R.drawable.person_map)).position(locationArrayList.get(i)).title("Nikunja2"));
            // googleMap.moveCamera(CameraUpdateFactory.newLatLng(Dhaka));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
            //googleMap
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Brisbane,7),2000,null);
        }
    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.

        vectorDrawable.setBounds(0, 0, 100, 100);

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
       // Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    @Override
    protected void onStart() {
        super.onStart();
        getPatientLocation();

    }

    private void getPatientLocation()
    {
            // Read from the database
        patientHistories = new ArrayList<>();
            myRef.child("Location").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        // TODO: handle the post

                        patientHistory = postSnapshot.getValue(PatientHistory.class);

                        patientHistories.add(patientHistory);

                        //String value = dataSnapshot.getValue(String.class);
                        Log.d("patientLocation", "Value is: " + patientHistory.getId());
                        Log.d("patientLocation", "Value is: " + patientHistory.getLatitute());
                        Log.d("patientLocation", "Value is: " + patientHistory.getLongatitute());

                    }
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    // Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

}