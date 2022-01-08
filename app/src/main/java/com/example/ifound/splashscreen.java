package com.example.ifound;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ifound.activities.MainActivity;
import com.example.ifound.activities.dashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class splashscreen extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                if (mAuth.getCurrentUser() != null) {

                    Intent intent = new Intent(splashscreen.this, dashboard.class);
                    startActivity(intent);


                }else {
                    Intent i = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 2000);
        setContentView(R.layout.activity_splashscreen);
    }


}



