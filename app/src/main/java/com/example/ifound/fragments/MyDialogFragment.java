package com.example.ifound.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ifound.MyService;
import com.example.ifound.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDialogFragment extends DialogFragment {

    TextInputLayout text;

    DatabaseReference mDatabase;

    String uniqueKey;

    FirebaseAuth fAuth;
    String userId;


    FirebaseUser user;

    Button post;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().setTitle("simple dialog");

        View v = inflater.inflate(R.layout.partner_review_dialog, container, false);

        text = v.findViewById(R.id.Description_tag);

        post = v.findViewById(R.id.button_next2);


        mDatabase = FirebaseDatabase.getInstance().getReference();




        fAuth = FirebaseAuth.getInstance();


        user = FirebaseAuth.getInstance().getCurrentUser();


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String que = text.getEditText().getText().toString().trim();

                if(que.isEmpty()){
                    text.setError("Required");
                    text.requestFocus();
                }else if(que.length()<3){
                    text.setError("Too Short");
                    text.requestFocus();
                }else {
                    Intent i = new Intent(getActivity(), MyService.class);
                    i.putExtra("query", que);
                    getActivity().startService(i);

                    dismiss();

                }
            }
        });







        return v;
    }


}
