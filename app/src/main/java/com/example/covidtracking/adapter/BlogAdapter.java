package com.example.covidtracking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracking.R;
import com.example.covidtracking.activity.BlogDetailsActivity;
import com.example.covidtracking.model.Blog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {

    Context context;
    List<Blog> models;

    public BlogAdapter(Context context, List<Blog> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_layout,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        if(!models.isEmpty())
        {
            Blog data = models.get(position);
        holder.details.setText(data.getDetails());
        //holder.imageView.setImageResource(models.get(position).getImage());
            try {
                Picasso.get().load(data.getImage()).placeholder(R.drawable.help_icon).error(R.drawable.help_icon).into(holder.imageView);
            }catch (Exception e){

            }
        holder.title.setText(data.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                context.startActivity(new Intent(context, BlogDetailsActivity.class)
                .putExtra("title",data.getTitle())
                .putExtra("image",data.getImage())
                .putExtra("details",data.getDetails())
                );
    /*            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.model_details,null);
                builder.setView(view);


                ImageView imageView = view.findViewById(R.id.model_image_details);
                TextView titleTV = view.findViewById(R.id.model_title_details);
                TextView detailsTV  = view.findViewById(R.id.model_details_text);

                detailsTV.setText(models.get(position).getDetails());
              //  imageView.setImageResource(models.get(position).getImage());
                titleTV.setText(models.get(position).getTitle());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();*/

            }
        });

        }
    }

    @Override
    public int getItemCount() {

        if(models.size() <= 0)
        {
            return 5;
        }
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,details;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.model_image);
            title = itemView.findViewById(R.id.model_title);
            details = itemView.findViewById(R.id.model_details);

        }
    }
}
