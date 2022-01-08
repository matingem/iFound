package com.example.ifound.fragments;

import static android.view.View.GONE;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ifound.R;
import com.example.ifound.activities.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class profile extends Fragment {

    ImageView next;

    TextView viewname;

    StorageReference storageReference;

    CircularImageView imageView1;

    TextView email,phone;

    CardView  cardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        BottomNavigationView navBar = getActivity().findViewById(R.id.chipNavigation);
        navBar.setVisibility(View.VISIBLE);

        cardView = v.findViewById(R.id.ccard);
        next = v.findViewById(R.id.nextfrag);
        viewname = v.findViewById(R.id.names);
        imageView1 = v.findViewById(R.id.dp1);
        email = v.findViewById(R.id.femail);
        phone = v.findViewById(R.id.fphn);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference();


        String userId = user.getUid();

        StorageReference profileRef = storageReference.child("Users/" + userId + "/profilepicture.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView1);
            }
        });


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");

        rootRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String emails = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String name = dataSnapshot.child("Name").getValue(String.class);
                String phones = dataSnapshot.child("Phone Number").getValue(String.class);

                if ((name == "") && ( emails == "") && (phones =="")){

                } else{
                    viewname.setText(name);
                    email.setText(emails);
                    phone.setText(phones);

                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile1 fragment = new profile1();;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return v;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignIn.getClient(getContext(),new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getActivity().finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Logout Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }




}





