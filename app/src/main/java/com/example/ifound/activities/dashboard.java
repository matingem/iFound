package com.example.ifound.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ifound.R;
import com.example.ifound.fragments.account;
import com.example.ifound.fragments.chat;
import com.example.ifound.fragments.found;
import com.example.ifound.fragments.founditem_post;
import com.example.ifound.fragments.home;
import com.example.ifound.fragments.lost;
import com.example.ifound.fragments.lostitem_post;
import com.example.ifound.fragments.post;
import com.example.ifound.fragments.profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
public class dashboard extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private BottomNavigationView NavigationBar;
    private Fragment fragment;
    String EmailHolder;
    TextView Email;
    Button LogOUT ;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    //@SuppressLint("SetTextI18n")
    public static final String TAG="LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        BottomNavigationView bottomNav = findViewById(R.id.chipNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new home()).commit();
        }




        Intent intent = getIntent();

        // Receiving User Email Send By MainActivity.
        EmailHolder = intent.getStringExtra(MainActivity.userEmail);

        // Adding click listener to Log Out button.





    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;

                    switch (item.getItemId()) {
                        case R.id.homeitem:
                            fragment = new home();
                            break;
                        case R.id.chatitem:
                            fragment = new chat();
                            break;
                        case R.id.postitem:
                            fragment = new home();
                            Context wrapper =  new ContextThemeWrapper(getApplicationContext(),R.style.MyPopupMenu);
                            PopupMenu popup = new PopupMenu(wrapper, findViewById(R.id.postitem));
                            MenuInflater inflater = popup.getMenuInflater();
                            inflater.inflate(R.menu.postmenu, popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    if(menuItem.getItemId() == R.id.one){
                                        founditem_post fragment = new founditem_post();;
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                    else if(menuItem.getItemId() == R.id.two){
                                        lostitem_post fragment = new lostitem_post();;
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                    return false;
                                }
                            });
                            break;
                        case R.id.profile:
                            fragment = new profile();
                            break;
                        case R.id.setting:
                            fragment = new account();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            fragment).commit();

                    return true;
                }
            };



    @Override
    public void onBackPressed() {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                home fragment = new home();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.commit();
            } else {
                super.onBackPressed();
            }
        }


}