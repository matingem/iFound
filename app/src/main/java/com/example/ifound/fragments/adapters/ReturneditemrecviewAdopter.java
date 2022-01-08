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
import com.example.ifound.model.Returnediitem;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ReturneditemrecviewAdopter extends RecyclerView.Adapter <ReturneditemrecviewAdopter.ViewHolder>{
    private List<Returnediitem> list;
    private Context mContext;

    // RecyclerView recyclerView;

    public ReturneditemrecviewAdopter(Context context, List<Returnediitem> listdata) {
        mContext = context;
        list = listdata;
        notifyDataSetChanged();
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.itemview_returned, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Returnediitem founditem = list.get(position);


        Picasso.get()
                .load(founditem.getImageurl())
                .fit()
                .centerInside()
                .into(holder.image);

    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        Button btn;
        public ViewHolder(View itemView) {
            super(itemView);

            image= itemView.findViewById(R.id.choose_image);
            btn= itemView.findViewById(R.id.found);



        }
    }
}