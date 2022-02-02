package com.example.ifound.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifound.ChatActivity;
import com.example.ifound.R;
import com.example.ifound.Users;
import com.example.ifound.model.User;

import java.util.List;


public class User_adapter extends RecyclerView.Adapter<User_adapter.MyHolder> {
    Context context;
    List<Users> userlist;

    public User_adapter(Context context, List<Users> userlist) {
        this.context = context;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_row,parent,false);  //missing

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Users model = userlist.get(position);
        holder.emmail.setText(model.getEmail());
        holder.naame.setText(model.getFullName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChatActivity.class)
                .putExtra("uid",model.getId())
                .putExtra("name",model.getFullName()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView naame,emmail;
      //  CircleImageView
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            naame=itemView.findViewById(R.id.text_name);
            emmail=itemView.findViewById(R.id.text_email1);
        }
    }
}
