package com.sarowal.networkingjson;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.MyViewHolder> {

    Context context;
    ArrayList<MobileModel> mobiles;

    public MobileAdapter(Context context, ArrayList<MobileModel> mobiles) {
        this.context = context;
        this.mobiles = mobiles;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public MobileAdapter.MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list, parent, false); //if true the app will crash
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MobileAdapter.MyViewHolder holder, int position) {
        holder.modelName.setText(mobiles.get(position).getModel());
        holder.price.setText("Price: $" + mobiles.get(position).getPrice());
        holder.imageView.setImageBitmap(mobiles.get(position).getImageBitmap());
        // Picasso Dependency: implementation 'com.squareup.picasso:picasso:2.5.2'
        //Picasso.with(context).load(mobiles.get(position).getImageBitmap()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mobiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView modelName;
        TextView price;
        ImageView imageView;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            modelName = itemView.findViewById(R.id.tvModel);
            price = itemView.findViewById(R.id.tvPrice);
            imageView = itemView.findViewById(R.id.ivImage);
        }
    }
}