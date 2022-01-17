package com.example.ifound.fragments.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifound.R;
import com.example.ifound.model.Founditem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class FounditemrecviewAdopter extends RecyclerView.Adapter <FounditemrecviewAdopter.ViewHolder>{
    private List<Founditem> list;
    private Context mContext;
    StorageReference storageReference;

    // RecyclerView recyclerView;

    public FounditemrecviewAdopter(Context context, List<Founditem> listdata) {
        mContext = context;
        list = listdata;
        notifyDataSetChanged();
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.found_itemview, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filteredads(ArrayList<Founditem> adlist){
        list=adlist;
        notifyDataSetChanged();
    }

    public void locfilter(ArrayList<Founditem> loclist){
        list=loclist;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Founditem founditem = list.get(position);
        final String title = founditem.getTitle();
        final String discriotion = founditem.getDiscriotion();
        final String status = founditem.getcity();
        final String location = founditem.getLocation();
        final String time = founditem.getTime();
        final String update = founditem.getStatus();
        final  String id = founditem.getUid();


        holder.tv_title.setText(title);
        holder.tv_discription.setText(discriotion);
        holder.tv_status.setText(status);
        holder.tv_location.setText(" " + location);
        holder.tv_time.setText(" " + time);

        if(update.equals("Returned to Owner")){

            holder.btn.setText("Returned to Owner");
            holder.btn.setEnabled(false);

        }else if (update.equals("NA")){

            holder.btn.setText("Send message");
            holder.btn.setEnabled(true);
        }

        Picasso.get()
                .load(founditem.getImageurl())
                .fit()
                .centerInside()
                .into(holder.image);

        DatabaseReference ref4= FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String namee = dataSnapshot.child("Name").getValue(String.class);
                holder.name.setText(namee);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("Users/" + id + "/profilepicture.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.images);
            }
        });

    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final TextView tv_discription;
        private final TextView tv_status;
        private final TextView tv_location;
        private final TextView tv_time;
        private final ImageView image;
        Button btn;
        private TextView name;
        CircularImageView images;
        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.myname);
            images = itemView.findViewById(R.id.pp);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_discription = itemView.findViewById(R.id.tv_description);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_time = itemView.findViewById(R.id.tv_time);
            image= itemView.findViewById(R.id.choose_image);
            btn= itemView.findViewById(R.id.found);



        }
    }
}