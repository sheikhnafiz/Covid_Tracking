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

import com.example.covidtracking.Config;
import com.example.covidtracking.R;
import com.example.covidtracking.model.Model;
import com.example.covidtracking.model.Prescription;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.MyViewHolder> {

    Context context;
    List<Prescription> models;

    public PrescriptionAdapter(Context context, List<Prescription> models)
    {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.model_prescription,null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        holder.date.setText(models.get(position).getDate());
       // Picasso.get().load(models.get(position).getImage()).error(R.drawable.prescription).placeholder(R.drawable.prescription).into(holder.imageView);
       holder.imageView.setImageBitmap(Config.StringToBitMap(models.get(position).getImage()));
        holder.title.setText(models.get(position).getDoctorName());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.model_details,null);
                builder.setView(view);


                ImageView imageView = view.findViewById(R.id.model_image_details);
                TextView titleTV = view.findViewById(R.id.model_title_details);
                TextView detailsTV  = view.findViewById(R.id.model_details_text);

                detailsTV.setText(models.get(position).getDetails());
                imageView.setImageResource(models.get(position).getImage());
                titleTV.setText(models.get(position).getTitle());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();*/

            }
        });
    }

    @Override
    public int getItemCount() {

        if(models == null)
        {
            return 0;
        }
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.model_prescription_image);
            title = itemView.findViewById(R.id.model_prescription_doctor_name);
            date = itemView.findViewById(R.id.model_prescription_date);

        }
    }
}
