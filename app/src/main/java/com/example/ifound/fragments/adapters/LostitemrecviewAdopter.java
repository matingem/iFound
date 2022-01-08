package com.example.ifound.fragments.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ifound.R;
import com.example.ifound.model.Founditem;
import com.example.ifound.model.Lostitem;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class LostitemrecviewAdopter extends RecyclerView.Adapter <LostitemrecviewAdopter.ViewHolder>{
    private List<Lostitem> list;
    private Context mContext;

    // RecyclerView recyclerView;

    public LostitemrecviewAdopter(Context context,List<Lostitem> listdata) {
        mContext = context;
        list = listdata;
        this.notifyDataSetChanged();
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.lost_itemview, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Lostitem lostitem = list.get(position);
        final String title = lostitem.getTitle();
        final String discriotion = lostitem.getDiscriotion();
        final String status = lostitem.getcity();
        final String location = lostitem.getreward();
        final String time = lostitem.getTime();
        final  String update = lostitem.getStatus();

        holder.tv_title.setText(title);
        holder.tv_discription.setText(discriotion);
        holder.tv_status.setText(" " + status);
        holder.tv_location.setText(" Reward" + " " + location );
        holder.tv_time.setText(" " + time);

        Picasso.get()
                .load(lostitem.getImageurl())
                .fit()
                .centerInside()
                .into(holder.image);

        if(update.equals("Founded")){

            holder.lost.setText("Founded");
            holder.lost.setEnabled(false);

        }else if (update.equals("NA")){

            holder.lost.setText("Send message");
            holder.lost.setEnabled(true);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filteredads(ArrayList<Lostitem> adlist){
        list=adlist;
        notifyDataSetChanged();
    }
    public void locfilter(ArrayList<Lostitem> loclist){
        list=loclist;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final TextView tv_discription;
        private final TextView tv_status;
        private final TextView tv_location;
        private final TextView tv_time;
        ImageView image;
        Button lost;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            lost = itemView.findViewById(R.id.lost);
            tv_discription = itemView.findViewById(R.id.tv_description);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_time = itemView.findViewById(R.id.tv_time);
            image= itemView.findViewById(R.id.choose_image);

        }
    }
}