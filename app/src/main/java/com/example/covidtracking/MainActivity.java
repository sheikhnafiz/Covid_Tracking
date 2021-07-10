package com.example.covidtracking;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.covidtracking.activity.HomeActivity;
import com.example.covidtracking.activity.SignUpActivity;
import com.example.covidtracking.adapter.BlogAdapter;
import com.example.covidtracking.databinding.ActivityMainBinding;
import com.example.covidtracking.model.Blog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import Login.Login;

import static com.example.covidtracking.Config.database;
import static com.example.covidtracking.Config.mAuth;
import static com.example.covidtracking.Config.myRef;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback
{
    ViewFlipper viewFlipper;
    ActivityMainBinding binding;
    BlogAdapter blogAdapter;
    BlogAdapter generalAdapter;
    BlogAdapter newsAdapter;
    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
        FirebaseUser currentUser = mAuth.getCurrentUser();
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
            initBlog();

        if (currentUser != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
    LatLng sydney = new LatLng(-31, 150);
    LatLng TamWorth = new LatLng(-31.083332, 150.916672);
    LatLng NewCastle = new LatLng(-31.916668, 150.750000);
    LatLng Brisbane = new LatLng(-31.470125, 151.021072);
    // creating array list for adding all our locations.
    private ArrayList<LatLng> locationArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        initListener();
        initRecyclerView();
        initComponent();
        new MyTask().execute();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapM);
        mapFragment.getMapAsync(this);

        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
        locationArrayList.add(sydney);
        locationArrayList.add(TamWorth);
        locationArrayList.add(NewCastle);
        locationArrayList.add(Brisbane);
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
            Log.d("getCoronaLive" ,"getCoronaLive: "+doc);
            int checkingValue = 1;
            int checkingValueBD = 1;

            for (Element e: doc.select("div.maincounter-number")) {
                //     System.out.println(e.attr("abs:href"));
                Log.d("getCoronaLive" ,"getCoronaLive: "+e.text());
                switch (checkingValue)
                {
                    case 1 :
                        binding.effectedWw.setText(e.text());
                        break;
                    case 2 :
                        binding.deathsWw.setText(e.text());
                        break;
                        case 3 :
                            binding.recoveredWw.setText(e.text());
                        break;
                }
              checkingValue ++;


            }


            for (Element e: docBD.select("div.maincounter-number")) {
                //     System.out.println(e.attr("abs:href"));
                Log.d("getCoronaLive" ,"getCoronaLive: "+e.text());
                switch (checkingValueBD)
                {
                    case 1 :
                        binding.effectedBd.setText(e.text());
                        break;
                    case 2 :
                        binding.deathsBd.setText(e.text());
                        break;
                    case 3 :
                        binding.recoveredBd.setText(e.text());
                        break;
                }
                checkingValueBD ++;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void initBlog() {
        Config.getBlog(new Config.AdapterCallBackListener() {
            @Override
            public void Generale(List<Blog> blogs) {
                Log.d("recyclerValue", "generalAdapter main: "+blogs.size());

                generalAdapter = new BlogAdapter(MainActivity.this,blogs);
                binding.generalRv.setAdapter(generalAdapter);
            }

            @Override
            public void news(List<Blog> blogs) {
                Log.d("recyclerValue", "newsAdapter main: "+blogs.size());
                newsAdapter = new BlogAdapter(MainActivity.this, blogs);
                binding.newsRv.setAdapter(newsAdapter);
            }

            @Override
            public void doctor(List<Blog> blogs) {
                Log.d("recyclerValue", "doctor main: "+blogs.size());

                blogAdapter = new BlogAdapter(MainActivity.this, blogs);
                binding.doctorRv.setAdapter(blogAdapter);
            }
        });
    }
    private void initRecyclerView() {

        binding.doctorRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.newsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.generalRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


    }
    private void initComponent() {
        // phoneRecycler = findViewById(R.id.my_recycler);
        //phoneRecycler();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapM);
        mapFragment.getMapAsync(this);

        int images[] = {R.drawable.track_icon, R.drawable.news_icon, R.drawable.doctor_tips_icon, R.drawable.help_icon};

        viewFlipper = findViewById(R.id.view_flipper);
        for (int image : images) {
            // Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show();
            flipperImages(image);
        }
    }
    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setInAnimation(this, android.R.anim.slide_out_right);
    }
    private void initListener() {
        binding.callId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMethod();
            }
        });
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        });
    }
    private void callMethod() {
        // Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:999"));

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
    public void onMapReady(GoogleMap googleMap) {

        // LatLng Dhaka = new LatLng(23.834570, 90.415481);

        for (int i = 0; i < locationArrayList.size(); i++) {
            googleMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(""));
            // googleMap.moveCamera(CameraUpdateFactory.newLatLng(Dhaka));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,7),5000,null);
           // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Brisbane,10),2000,null);
           // int zoom = mMap.getCa
        }
    }
    // Permission the storage in the project
    /* Start the Permission */
    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}