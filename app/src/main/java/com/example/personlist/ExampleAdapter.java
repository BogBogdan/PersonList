package com.example.personlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ultramegasoft.radarchart.RadarHolder;
import com.ultramegasoft.radarchart.RadarView;

import java.util.ArrayList;
import java.util.Collections;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {


    private ArrayList<ExampleItem> mExampleList;
    private OnInemClickLisener mlisener;


    public interface OnInemClickLisener
    {
        void onItemClick(int position);
    }


    public void setOnItemClickLisener(OnInemClickLisener lisener)
    {
        mlisener=lisener;
    }
    public static class ExampleViewHolder extends  RecyclerView.ViewHolder{

        public RadarView radarView;
        public TextView textView1;
        public TextView textview2;

        public ExampleViewHolder(@NonNull View itemView, OnInemClickLisener lisener) {
            super(itemView);
            radarView=itemView.findViewById(R.id.imagev);
            textView1=itemView.findViewById(R.id.textview1);
            textview2=itemView.findViewById(R.id.textview2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(lisener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            lisener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public ExampleAdapter(ArrayList<ExampleItem> exampleItems, Context context)
    {

        mExampleList=exampleItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item,parent,false);
        ExampleViewHolder evh=new ExampleViewHolder(v,mlisener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        ExampleItem currentItem=mExampleList.get(position);

        ArrayList<RadarHolder> abilitys=currentItem.getAbilitys();
        int max=10;
        for(RadarHolder a:abilitys)
        {
            if(max<a.value)
                max=a.value;
        }
        holder.radarView.setMaxValue(max);
        holder.radarView.setData(abilitys);
        holder.textView1.setText(currentItem.getMtext1());
        holder.textview2.setText(currentItem.getMtext2());


    }

    private final Context context;


    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}
