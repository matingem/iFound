package com.example.ifound.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifound.R;
import com.example.ifound.activities.dashboard;
import com.example.ifound.fragments.adapters.FounditemrecviewAdopter;
import com.example.ifound.fragments.adapters.LostitemrecviewAdopter;
import com.example.ifound.model.Founditem;
import com.example.ifound.model.Lostitem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.ifound.activities.dashboard.TAG;


public class lost extends Fragment {


    private RecyclerView recyclerView;
    private  List<Lostitem> list;
    private Button b;
    private LostitemrecviewAdopter mAdapter;
    ImageView loc;

    private ProgressBar mProgressCircle;

    LinearLayoutManager manager;
    EditText search;
    ImageView searchresults;
    String city = "";
    int Count;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        BottomNavigationView navBar = getActivity().findViewById(R.id.chipNavigation);
        navBar.setVisibility(View.GONE);
        // Inflate the layout for this fragment

        try{
            Bundle bundle = getArguments();
            city = bundle.getString("city");


        }catch(Exception error1) {

        }

        View v = inflater.inflate(R.layout.fragment_lost, container, false);
        manager = new LinearLayoutManager(getContext());
        //recyclerView = new RecyclerView(Objects.requireNonNull(getContext()));
        recyclerView = v.findViewById(R.id.founditemrecview);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        list = new ArrayList<>();



        mProgressCircle = v.findViewById(R.id.progressBar) ;

        getFoundDatafromFirebase();


        search = v.findViewById(R.id.simpleSearchView);
        searchresults = v.findViewById(R.id.searchresult);
        loc = v.findViewById(R.id.loc);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());



            }
        });

        search.clearFocus();

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location fragment = new location();;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slideup, R.anim.slidedown);
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });




        return v;
    }

    private void filter(String toString) {

        ArrayList<Lostitem> adlist =  new ArrayList<>();
        for(Lostitem item:list){
            if(item.getTitle().toLowerCase().contains(toString.toLowerCase())){
                adlist.add(item);
            }
        }
        mAdapter.filteredads(adlist);

        if(adlist.size()==0){
            searchresults.setVisibility(View.VISIBLE);
        }
        else{
            searchresults.setVisibility(View.GONE);
        }

    }

    private void Locationfilter(String city) {

        ArrayList<Lostitem> loclist =  new ArrayList<>();
        for(Lostitem items:list){
            if(items.getStatus().toLowerCase().contains(city.toLowerCase())){
                loclist.add(items);
            }
        }
        mAdapter.locfilter(loclist);

        if(loclist.size()==0){
            searchresults.setVisibility(View.VISIBLE);
        }
        else{
            searchresults.setVisibility(View.GONE);
        }

    }

    private void getFoundDatafromFirebase() {
        FirebaseDatabase receiverFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference receiverDatabaseReference = receiverFirebaseDatabase.getReference("Lost Items");
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
                        String location = Objects.requireNonNull(data.child("reward").getValue()).toString();
                        String time = Objects.requireNonNull(data.child("time").getValue()).toString();
                        String imageurl = Objects.requireNonNull(data.child("imageurl").getValue()).toString();
                        String update = Objects.requireNonNull(data.child("status").getValue()).toString();
                        String email = Objects.requireNonNull(data.child("email").getValue()).toString();
                        String id = Objects.requireNonNull(data.child("uid").getValue()).toString();

                        Lostitem lostitem = new Lostitem(title, discription , email, location, time, status,imageurl,update,key,id);
                        list.add(lostitem);
                    }
                    Log.d(TAG, "onDataChange: length:"+list.size());



                    mAdapter = new LostitemrecviewAdopter(getActivity(),list);;
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();



                    mProgressCircle.setVisibility(View.INVISIBLE);

                    Locationfilter(city);



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

    }

}