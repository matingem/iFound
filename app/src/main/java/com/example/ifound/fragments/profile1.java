package com.example.ifound.fragments;

import static android.view.View.GONE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ifound.R;
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
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class profile1 extends Fragment {

    EditText name;
    EditText phone;
    Button save;
    CircularImageView imageView;

    FirebaseUser user;
    StorageReference storageReference;
    FirebaseAuth fAuth;

    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_1, container, false);


        BottomNavigationView navBar = getActivity().findViewById(R.id.chipNavigation);
        navBar.setVisibility(GONE);

        name = v.findViewById(R.id.nam);
        phone = v.findViewById(R.id.phn);
        save = v.findViewById(R.id.Save);
        imageView = v.findViewById(R.id.dp);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();



        fAuth = FirebaseAuth.getInstance();


        user = FirebaseAuth.getInstance().getCurrentUser();

        String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String userId = user.getUid();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");

        StorageReference profileRef = storageReference.child("Users/" + userId + "/profilepicture.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        rootRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String names = dataSnapshot.child("Name").getValue(String.class);
                String phones = dataSnapshot.child("Phone Number").getValue(String.class);

                if (names == "" && phones == ""){

                } else{
                    name.setText(names);
                    phone.setText(phones);
                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateName() | !validatePhoneNo()) {
                    return;
                } else {

                    String phones = phone.getText().toString().trim();
                    String names = name.getText().toString().trim();


                    mDatabase.child("Users").child(userId).child("Name").setValue(names);
                    mDatabase.child("Users").child(userId).child("Phone Number").setValue(phones);
                    mDatabase.child("Users").child(userId).child("Email").setValue(Email);

                    profile fragment = new profile();;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }


            }
        });

        return  v;
    }



    private Boolean validatePhoneNo() {


        String phones = phone.getText().toString().trim();

        if (phones.isEmpty()) {
            phone.setError("Phone number cannot be empty");
            phone.requestFocus();
            return false;
        }

        if (phones.length() < 13) {
            phone.setError("Invalid phone number");
            phone.requestFocus();
            return false;
        }
        return true;
    }

    private Boolean validateName() {


        String phones = name.getText().toString().trim();

        if (phones.isEmpty()) {
            name.setError("Name cannot be empty");
            name.requestFocus();
            return false;
        }

        if (phones.length() < 4) {
            name.setError("Name too short");
            name.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);


            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage
        final StorageReference fileRef = storageReference.child("Users/" + fAuth.getCurrentUser().getUid() + "/profilepicture.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }




}





