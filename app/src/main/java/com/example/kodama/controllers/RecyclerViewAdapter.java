package com.example.kodama.controllers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kodama.R;
import com.example.kodama.models.PlantCard;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    private List<PlantCard> plantList;
    private ClickListener<PlantCard> clickListener;

    public RecyclerViewAdapter(List<PlantCard> plantList){
        this.plantList = plantList;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, final int position) {

        final PlantCard plant = plantList.get(position);

        holder.title.setText(plant.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(plant);
            }
        });


    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public void setOnItemClickListener(ClickListener<PlantCard> plantClickListener) {
        this.clickListener = plantClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
