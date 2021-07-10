package com.example.covidtracking;

import com.example.covidtracking.model.Blog;
import com.example.covidtracking.model.UserProfileInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Config
{


    public static FirebaseAuth mAuth;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    public static String userID;
    public static UserProfileInfo userProfileInfo;

    public static List<Blog> doctorBlog = new ArrayList<>();
    public static List<Blog> newsBlog = new ArrayList<>();
    public static List<Blog> generalBlog = new ArrayList<>();

    public static void getBlog(AdapterCallBackListener adapterCallBackListener) {
        // Read from the database

        myRef.child("Blog").child("News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                newsBlog.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Blog b = data.getValue(Blog.class);
                    newsBlog.add(b);
                }

                adapterCallBackListener.news(newsBlog);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("Blog").child("Doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                doctorBlog.clear();

                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Blog b = data.getValue(Blog.class);
                    doctorBlog.add(b);
                }
                adapterCallBackListener.doctor(doctorBlog);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef.child("Blog").child("General").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               generalBlog.clear();
                List<Blog> blogs = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Blog b = data.getValue(Blog.class);
                    generalBlog.add(b);
                }
                adapterCallBackListener.Generale(generalBlog);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    public interface AdapterCallBackListener {

        void Generale( List<Blog> blogs);
        void news( List<Blog> blogs);
        void doctor( List<Blog> blogs);

    }



}
