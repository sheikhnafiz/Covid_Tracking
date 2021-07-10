package com.example.covidtracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracking.MainActivity;
import com.example.covidtracking.R;
import com.example.covidtracking.model.Model;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    List<Model> models;

    public NewsAdapter(Context context, List<Model> models) {
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
        holder.details.setText(models.get(position).getDetails());
        holder.imageView.setImageResource(models.get(position).getImage());
        holder.title.setText(models.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.model_details,null);
                builder.setView(view);


                ImageView imageView = view.findViewById(R.id.model_image_details);
                TextView titleTV = view.findViewById(R.id.model_title_details);
                TextView detailsTV  = view.findViewById(R.id.model_details_text);

                detailsTV.setText(models.get(position).getDetails());
                imageView.setImageResource(models.get(position).getImage());
                titleTV.setText(models.get(position).getTitle());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
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
