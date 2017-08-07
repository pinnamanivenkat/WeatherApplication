package com.venkat.mroads.weatherapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<CityInfo> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView CityName,Lattitude,Longitude;
        ImageView mCityImage;
        private CardView cardView;
        private Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.CityName = (TextView) itemView.findViewById(R.id.CityName);
            this.Lattitude = (TextView) itemView.findViewById(R.id.Lattitude);
            this.Longitude = (TextView) itemView.findViewById(R.id.Longitude);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    public CustomAdapter(ArrayList<CityInfo> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView CityName = holder.CityName;
        TextView Latitude = holder.Lattitude;
        TextView Longitude = holder.Longitude;
        CardView mCityCard = holder.cardView;
        final Context context = holder.context;

        CityName.setText(dataSet.get(listPosition).getCityName());
        Latitude.setText("Latitude : "+Double.toString(dataSet.get(listPosition).getLatitude()));
        Longitude.setText("Longitude : "+Double.toString(dataSet.get(listPosition).getLongitude()));

        mCityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Latitude",Double.toString(dataSet.get(listPosition).getLatitude()));
                bundle.putString("Longitude",Double.toString(dataSet.get(listPosition).getLongitude()));
                bundle.putString("ImagePath",Integer.toString(dataSet.get(listPosition).getImagePath()));
                Intent intent = new Intent(context,DisplayWeather.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}