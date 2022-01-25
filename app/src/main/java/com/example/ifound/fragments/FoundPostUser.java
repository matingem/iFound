package com.example.ifound.fragments;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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

import com.example.ifound.ChatActivity;
import com.example.ifound.R;
import com.example.ifound.fragments.adapters.EditPostAdopter;
import com.example.ifound.fragments.founditem_post;
import com.example.ifound.model.Founditem;
import com.example.ifound.model.Founditem1;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class FoundPostUser extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView1;
    private EditPostAdopter mAdapter;

    private ProgressBar mProgressCircle;
    TextView textView;
    TextView Count,Count1,coun;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private LinearLayoutManager mLayoutManager;

    private LinearLayoutManager mLayoutManager1;
    TextView text;
    Button btn;

    private List<Founditem1> mUploads;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.found_post_user, container, false);


        mLayoutManager = new LinearLayoutManager(getActivity());


        textView = v.findViewById(R.id.tet);
        Count = v.findViewById(R.id.count);
        coun = v.findViewById(R.id.co);
        btn = v.findViewById(R.id.post);




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                founditem_post fragment = new founditem_post();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slideup, R.anim.slidedown,R.anim.slide_down_reverse, R.anim.slide_up_reverse);
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);



        mLayoutManager.setReverseLayout(true);



        mProgressCircle = v.findViewById(R.id.progressBar) ;
        Sprite Circle = new Circle();
        mProgressCircle.setIndeterminateDrawable(Circle);




        mUploads = new ArrayList<>();


        mAdapter = new EditPostAdopter(getContext(), mUploads);




        mRecyclerView.setAdapter(mAdapter);




        //mAdapter.setOnItemClickListener(ImagesActivity.this);

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Found Items");

        String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mDatabaseRef.orderByChild("email").equalTo(Email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Founditem1 upload = postSnapshot.getValue(Founditem1.class);
                    upload.setKey(postSnapshot.child("key").getValue(String.class));
                    upload.setTitle(postSnapshot.child("title").getValue(String.class));
                    upload.setImageurl(postSnapshot.child("imageurl").getValue(String.class));
                    upload.setDiscriotion(postSnapshot.child("discriotion").getValue(String.class));
                    upload.setTime(postSnapshot.child("time").getValue(String.class));
                    upload.setcity(postSnapshot.child("city").getValue(String.class));
                    upload.setLocation(postSnapshot.child("location").getValue(String.class));
                    upload.setStatus(postSnapshot.child("status").getValue(String.class));




                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();

                if ((mAdapter.getItemCount() == 0))
                {
                    mRecyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.VISIBLE);
                    Count.setVisibility(View.GONE);
                    coun.setVisibility(View.GONE);

                }

                int no = mRecyclerView.getAdapter().getItemCount();
                //   Toast.makeText(getContext(), "Views = "+ no, Toast.LENGTH_SHORT).show();

                Count.setText(String.format("%d",no));

                mProgressCircle.setVisibility(GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(GONE);
            }
        });






        return v;
    }

}
