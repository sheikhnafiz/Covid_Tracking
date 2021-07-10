package com.example.covidtracking.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.covidtracking.R;
import com.example.covidtracking.databinding.ActivityBlogDetailsBinding;
import com.squareup.picasso.Picasso;

public class BlogDetailsActivity extends AppCompatActivity {

    ActivityBlogDetailsBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title , image, details;
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_blog_details);

        title = getIntent().getStringExtra("title");
        image =  getIntent().getStringExtra("image");
        details = getIntent().getStringExtra("details");

        binding.blogDetailsDetails.setText(details);
        binding.blogDetailsTitle.setText(title);
        try {
            Picasso.get().load(image).placeholder(R.drawable.help_icon).error(R.drawable.help_icon).into(binding.blogDetailsImage);
        }catch (Exception e){

        }


    }
}