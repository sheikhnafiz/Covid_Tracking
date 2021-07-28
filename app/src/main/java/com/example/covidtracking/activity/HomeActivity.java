package com.example.covidtracking.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.example.covidtracking.Config;
import com.example.covidtracking.MainActivity;
import com.example.covidtracking.R;
import com.example.covidtracking.adapter.BlogAdapter;
import com.example.covidtracking.databinding.ActivityHomeBinding;
import com.example.covidtracking.model.Blog;
import com.example.covidtracking.model.PatientHistory;
import com.example.covidtracking.model.UserProfileInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static com.example.covidtracking.Config.database;
import static com.example.covidtracking.Config.mAuth;
import static com.example.covidtracking.Config.myRef;
import static com.example.covidtracking.Config.userID;
import static com.example.covidtracking.Config.userProfileInfo;

public class HomeActivity extends FragmentActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;

    ActivityHomeBinding binding;

    BlogAdapter blogAdapter;
    BlogAdapter generalAdapter;
    BlogAdapter newsAdapter;


    //Drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.M)


//    LatLng TamWorth = new LatLng(23.778314, 90.401793);
//    LatLng NewCastle = new LatLng(24.106020, 89.947942);
//    LatLng Brisbane = new LatLng(23.553164, 89.883034);
//
//    // creating array list for adding all our locations.
//    private ArrayList<LatLng> locationArrayList;
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerId);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
       /* database = FirebaseDatabase.getInstance();
        myRef = database.getReference();*/
        initComponent();
        initListener();
        initRecyclerView();
        try {

            getProfileInfo();
        } catch (Exception e) {
        }
        initBlog();

        new MyTask().execute();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapH);
//      mapFragment.getMapAsync(this);

//        locationArrayList = new ArrayList<>();
//        locationArrayList.add(TamWorth);
//        locationArrayList.add(NewCastle);
//        locationArrayList.add(Brisbane);
    }

    private void initBlog() {

        Config.getBlog(new Config.AdapterCallBackListener() {
            @Override
            public void Generale(List<Blog> blogs) {
                generalAdapter = new BlogAdapter(HomeActivity.this, blogs);
                binding.generalRv.setAdapter(generalAdapter);
            }

            @Override
            public void news(List<Blog> blogs) {
                Log.d("recyclerValue", "newsAdapter : " + blogs.size());
                newsAdapter = new BlogAdapter(HomeActivity.this, blogs);
                binding.newsRv.setAdapter(newsAdapter);
            }

            @Override
            public void doctor(List<Blog> blogs) {
                Log.d("recyclerValue", "doctor home: " + blogs.size());
                blogAdapter = new BlogAdapter(HomeActivity.this, blogs);
                binding.doctorRv.setAdapter(blogAdapter);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        try {


        } catch (Exception e) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {

                getLocation();
            } catch (Exception e) {
            }
        }

    }

    private void getProfileInfo() {
        // Read from the database
        /*if (userID != null)*/
        myRef.child("User").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("recyclerValue", "newsAdapter onStart : profile ");

                userProfileInfo = dataSnapshot.getValue(UserProfileInfo.class);

                try {
                    if (userProfileInfo != null) {
                        binding.designNav.navHeaderName.setText(userProfileInfo.getName());
                        binding.designNav.patientCategory.setText(userProfileInfo.getCategory());
                        Picasso.get().load(userProfileInfo.getImage()).placeholder(R.drawable.profile).error(R.drawable.profile).into(binding.designNav.navHeaderProfileImage);
                    }//  binding.designNav.navHeaderName.setText(userProfileInfo.getName());
                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            getCoronaLive();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private void getCoronaLive() {

        Document doc = null, docBD;
        try {
            doc = Jsoup.connect("https://www.worldometers.info/coronavirus/").get();
            docBD = Jsoup.connect("https://www.worldometers.info/coronavirus/country/bangladesh/").get();
            Log.d("getCoronaLive", "getCoronaLive: " + doc);
            int checkingValue = 1;
            int checkingValueBD = 1;

            for (Element e : doc.select("div.maincounter-number")) {
                //     System.out.println(e.attr("abs:href"));
                Log.d("getCoronaLive", "getCoronaLive: " + e.text());
                switch (checkingValue) {
                    case 1:
                        binding.effectedWwHome.setText(e.text());
                        break;
                    case 2:
                        binding.deathsWwHome.setText(e.text());
                        break;
                    case 3:
                        binding.recoveredWwHome.setText(e.text());
                        break;
                }
                checkingValue++;


            }


            for (Element e : docBD.select("div.maincounter-number")) {
                //     System.out.println(e.attr("abs:href"));
                Log.d("getCoronaLive", "getCoronaLive: " + e.text());
                switch (checkingValueBD) {
                    case 1:
                        binding.effectedBd.setText(e.text());
                        break;
                    case 2:
                        binding.deathsBd.setText(e.text());
                        break;
                    case 3:
                        binding.recoveredBd.setText(e.text());
                        break;
                }
                checkingValueBD++;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Double lat = location.getLatitude();
                        Double longt = location.getLongitude();

                        PatientHistory patientHistory = new PatientHistory(userID, lat, longt);
                        if (userID != null) {
                            myRef.child("Location").child(userID).setValue(patientHistory);
                        }

                        // textView.setText(lat+" , " +longt);

                        // Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_LONG);
                    }
                }
            });

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    private void initRecyclerView() {
        binding.doctorRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.newsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.generalRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initComponent() {
        try {
            userID = mAuth.getCurrentUser().getEmail().split("@")[0];
            Log.d("login_data_from_table", "initComponent: " + userID);
        } catch (Exception e) {
        }

    }

    private void callMethod() {
        // Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:999"));

        if (ActivityCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    private void initListener() {


        binding.designNav.navMenuPatientCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
                dialog.setTitle("Change patient type");
                dialog.setMessage("Are you sure to change your patient type ??");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(userProfileInfo.getCategory().equals("Normal"))
                        {
                            myRef.child("User").child(userID).child("category").setValue("Patient");
                        }else {
                            myRef.child("User").child(userID).child("category").setValue("Normal");

                        }

                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

dialog.show();
            }
        });


        binding.designNav.navMenuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();

            }
        });

        binding.designNav.navMenuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        binding.designNav.navMenuPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(HomeActivity.this, PresccriptionActivity.class));
            }
        });

        binding.designNav.navHeaderProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });


        binding.drawerMenuId.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                //  drawerLayout.set
                //drawerLayout.openDrawer(Gravity.START);
                Log.d("testing", "onClick: ok");

                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }

            }
        });


        binding.mapH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


        binding.callHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callMethod();
            }
        });

    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }
}