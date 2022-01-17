package com.example.ifound;

import static com.example.ifound.activities.dashboard.TAG;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ifound.fragments.adapters.FounditemrecviewAdopter;
import com.example.ifound.model.Founditem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyService extends Service
{
    //Camera variables
//a surface holder
    private SurfaceHolder sHolder;


    //a variable to control the camera
    //the camera parameters
    private Policy.Parameters parameters;
    /** Called when the activity is first created. */
    private StorageReference mStorageRef;
    File spyfile;

    private List<Founditem> list;

    FirebaseDatabase database;

    private FounditemrecviewAdopter mAdapter;

    public static DatabaseReference RequestRef,SpyStatus;
    String devicemodel,query;
    private Query productQuery;
    private DatabaseReference mDatabaseReference;

    @Override
    public void onCreate()
    {
        super.onCreate();
       // android.os.Debug.waitForDebugger();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        query =  intent.getStringExtra("query");



        devicemodel = android.os.Build.MODEL;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        RequestRef = database.getReference("Found Items");
        SpyStatus = database.getReference("Lost Items");
        ListenerForRequestDone();

        list = new ArrayList<>();

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("RESTART_SERVICE");
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return null ;
    }

    public void ListenerForRequestDone() {
        RequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                FirebaseDatabase receiverFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference receiverDatabaseReference = receiverFirebaseDatabase.getReference("Found Items");
                receiverDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                        list.clear();
                        if (snapshot.exists()){
                            for (DataSnapshot data : snapshot.getChildren()){
                                //String key = Objects.requireNonNull(data.child("key").getValue()).toString();
                                String key = Objects.requireNonNull(data.child("key").getValue()).toString();
                                String title = Objects.requireNonNull(data.child("title").getValue()).toString();
                                String discription = Objects.requireNonNull(data.child("discriotion").getValue()).toString();
                                String status = Objects.requireNonNull(data.child("city").getValue()).toString();
                                String location = Objects.requireNonNull(data.child("location").getValue()).toString();
                                String time = Objects.requireNonNull(data.child("time").getValue()).toString();
                                String imageurl = Objects.requireNonNull(data.child("imageurl").getValue()).toString();
                                String update = Objects.requireNonNull(data.child("status").getValue()).toString();
                                String email = Objects.requireNonNull(data.child("email").getValue()).toString();
                                String id = Objects.requireNonNull(data.child("id").getValue()).toString();

                                Founditem founditem = new Founditem(title, discription , email, location, time, status,imageurl,update,key,id);
                                list.add(founditem);

                            }



                            //   mAdapter = new FounditemrecviewAdopter(getApplication(),list);;
                            //   mAdapter.notifyDataSetChanged();



                        }else {
                            Log.d(TAG, "onDataChange: no exist");
                        }
                        Log.d(TAG, "onDataChange:snapshot:"+snapshot.toString());

                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {
                        Log.d(TAG, "onDataChange: error:"+error.getMessage());


                    }
                });

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");

                String userId = FirebaseAuth.getInstance().getUid();

                rootRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        String tokenId = dataSnapshot.child("TokenId").getValue(String.class);
                        String title = "Item Matched !";
                        String messages = "Is it yours ? ";

                        ArrayList<Founditem> adlist =  new ArrayList<>();
                        for(Founditem item:list){
                            if(item.getTitle().toLowerCase().contains("macbook".toLowerCase())){
                                adlist.add(item);
                            }
                        }

                       // mAdapter.filteredads(adlist);

                        if(adlist.size()==0){
                            return;
                        }
                        else{
                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(tokenId, title, messages, getApplicationContext());
                            notificationsSender.SendNotifications();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }}