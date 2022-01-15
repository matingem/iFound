package com.example.ifound.fragments;

import static com.example.ifound.activities.dashboard.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifound.R;
import com.example.ifound.fragments.adapters.FounditemrecviewAdopter;
import com.example.ifound.fragments.adapters.LostReturneditemrecviewAdopter;
import com.example.ifound.fragments.adapters.LostitemrecviewAdopter;
import com.example.ifound.fragments.adapters.ReturneditemrecviewAdopter;
import com.example.ifound.model.Founditem;
import com.example.ifound.model.Lostiitem;
import com.example.ifound.model.Returnediitem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class home extends Fragment {

    private RecyclerView recyclerView,recyclerView1;
    private  List<Returnediitem> list;
    private  List<Lostiitem> list1;
    private Button b;
    private ReturneditemrecviewAdopter mAdapter;
    private LostReturneditemrecviewAdopter mAdapter1;
    Button found,lost,post;
    TextView foundcount, lostcount;
    private ProgressBar mProgressCircle,mProgressCircle1;
    RelativeLayout foundrel,lostrel;
    EditText search;
    TextView not;
    private DatabaseReference mDatabase;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home, container, false);

        found = v.findViewById(R.id.found);
        lost = v.findViewById(R.id.lost);
        post = v.findViewById(R.id.post);
        not = v.findViewById(R.id.textview6);
        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentActivity activity = (FragmentActivity)(getContext());
                FragmentManager fm = activity.getSupportFragmentManager();
                Bundle args = new Bundle();

                MyDialogFragment alertDialog = new MyDialogFragment();
                alertDialog.show(fm,"simple dialog");
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();


        String userId = FirebaseAuth.getInstance().getUid();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            mDatabase.child("Users").child(userId).child("TokenId").setValue(token);
                        }

                    }
                });

        search = v.findViewById(R.id.simpleSearchView);
    /*    foundcount = v.findViewById(R.id.foundcount);
        lostcount = v.findViewById(R.id.lostcount);*/
        foundrel = v.findViewById(R.id.foundrel);
        lostrel = v.findViewById(R.id.lostrel);

        mProgressCircle = v.findViewById(R.id.progressBar) ;
        mProgressCircle1 = v.findViewById(R.id.progressBar1) ;

        BottomNavigationView navBar = getActivity().findViewById(R.id.chipNavigation);
        navBar.setVisibility(View.VISIBLE);

        LinearLayoutManager horizontalLayoutManagaer1
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //recyclerView = new RecyclerView(Objects.requireNonNull(getContext()));
        recyclerView = v.findViewById(R.id.returneditemrecview);

        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        horizontalLayoutManagaer.setStackFromEnd(true);

        recyclerView1 = v.findViewById(R.id.lostitemrecview);

        recyclerView1.setLayoutManager(horizontalLayoutManagaer1);
        horizontalLayoutManagaer1.setStackFromEnd(true);




        list = new ArrayList<>();
        list1 = new ArrayList<>();


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

                        String imageurl = Objects.requireNonNull(data.child("imageurl").getValue()).toString();


                        Returnediitem founditem = new Returnediitem(key,imageurl);
                        list.add(founditem);

                    }
                    Log.d(TAG, "onDataChange: length:"+list.size());

                    mAdapter = new ReturneditemrecviewAdopter(getActivity(),list);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    mProgressCircle.setVisibility(View.INVISIBLE);




                }else {
                    Log.d(TAG, "onDataChange: no exist");
                }
                Log.d(TAG, "onDataChange:snapshot:"+snapshot.toString());

            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                Log.d(TAG, "onDataChange: error:"+error.getMessage());
                mProgressCircle.setVisibility(View.INVISIBLE);


            }
        });
        Log.d(TAG, "onDataChange: length1:"+list.size());

        FirebaseDatabase receiverFirebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference receiverDatabaseReference1 = receiverFirebaseDatabase1.getReference("Lost Items");
        receiverDatabaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                list1.clear();
                if (snapshot.exists()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        //String key = Objects.requireNonNull(data.child("key").getValue()).toString();
                        String key = Objects.requireNonNull(data.child("key").getValue()).toString();

                        String imageurl = Objects.requireNonNull(data.child("imageurl").getValue()).toString();


                        Lostiitem founditem = new Lostiitem(key,imageurl);
                        list1.add(founditem);

                    }
                    Log.d(TAG, "onDataChange: length:"+list1.size());

                    mAdapter1 = new LostReturneditemrecviewAdopter(getActivity(),list1);
                    recyclerView1.setAdapter(mAdapter1);
                    mAdapter1.notifyDataSetChanged();

                    mProgressCircle1.setVisibility(View.INVISIBLE);




                }else {
                    Log.d(TAG, "onDataChange: no exist");
                }
                Log.d(TAG, "onDataChange:snapshot:"+snapshot.toString());

            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                Log.d(TAG, "onDataChange: error:"+error.getMessage());
                mProgressCircle.setVisibility(View.INVISIBLE);


            }
        });
        Log.d(TAG, "onDataChange: length1:"+list1.size());


       /* DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Found Items");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               long count= dataSnapshot.getChildrenCount();
                foundcount.setText(String.valueOf(count)) ;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef2.child("Lost Items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count1= dataSnapshot.getChildrenCount();
                lostcount.setText(String.valueOf(count1)) ;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/





     foundrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                found fragment = new found();;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                found fragment = new found();;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

      lostrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lost fragment = new lost();;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

  /*      post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lostitem_post fragment = new lostitem_post();;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/



        return v;
    }




}





