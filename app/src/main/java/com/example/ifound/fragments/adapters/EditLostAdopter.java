package com.example.ifound.fragments.adapters;


import static com.example.ifound.activities.dashboard.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifound.R;
import com.example.ifound.model.Founditem;
import com.example.ifound.model.Lostitem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class EditLostAdopter extends RecyclerView.Adapter <EditLostAdopter.ViewHolder>{
    private List<Lostitem> list;
    private Context mContext;

    private int positions;
    private FirebaseStorage mStorage;
    DatabaseReference mDatabaseRef;


    String selectedKey;

    // RecyclerView recyclerView;

    public EditLostAdopter(Context context, List<Lostitem> listdata) {
        mContext = context;
        list = listdata;
        this.notifyDataSetChanged();
    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View listItem= layoutInflater.inflate(R.layout.lost_itemview_edit, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Lostitem founditem = list.get(position);
        final String title = founditem.getTitle();
        final String discriotion = founditem.getDiscriotion();
        String update = founditem.getStatus();


        selectedKey = founditem.getKey();

        holder.tv_title.setText(title);
        holder.tv_discription.setText(discriotion);

        Picasso.get()
                .load(founditem.getImageurl())
                .fit()
                .centerInside()
                .into(holder.images);

        if (update.equals("NA")) {

        }else if (update.equals("Founded")){
            holder.rto.setVisibility(View.VISIBLE);
        }




    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final TextView tv_discription;
        ImageView images;
        ImageView next;
        Button rto;
        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_discription = itemView.findViewById(R.id.tv_description);
            images= itemView.findViewById(R.id.choose_imagee);
            next = itemView.findViewById(R.id.menu);
            rto = itemView.findViewById(R.id.founds);

            next.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(mContext, v, Gravity.END);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup1, popup.getMenu());
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.one) {

                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference classicalMechanicsRef = rootRef.child("Lost Items");
                                Query query = classicalMechanicsRef.orderByChild("key").equalTo(selectedKey);
                                ValueEventListener valueEventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                            ds.child("status").getRef().setValue("Founded");
                                            rto.setVisibility(View.VISIBLE);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                                    }
                                };
                                query.addListenerForSingleValueEvent(valueEventListener);


                            } else if (item.getItemId() == R.id.two) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("Alert!");
                                builder.setMessage("Deletion is permanent. Are you sure you want to delete?");

                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteProduct();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                AlertDialog ad = builder.create();
                                ad.show();
                            }
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }
            });//closing the setO




        }
    }


    private void deleteProduct(){
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Found Items");
        Lostitem selectedItem = list.get(positions);

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageurl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(mContext, "Post deleted", Toast.LENGTH_SHORT).show();

            }
        });
    }
}