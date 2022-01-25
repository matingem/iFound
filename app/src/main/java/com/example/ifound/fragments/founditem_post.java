package com.example.ifound.fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ifound.R;
import com.example.ifound.model.Founditem;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class founditem_post extends Fragment {

    Button SubmitbtnFound;
    TextInputLayout title, discription, location, edittexttime;
    AutoCompleteTextView city;
    TextInputEditText time;
    FirebaseAuth mAuth;
    String cit;
    int hour, minute;
    TextView image;
    ImageView choose;
    ProgressBar progressBar;


    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;


    String key;

    StorageReference storageReference;
    FirebaseAuth fAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_found_item, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.chipNavigation);
        navBar.setVisibility(GONE);


        String id = FirebaseAuth.getInstance().getUid();

        DatabaseReference ref4= FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String namee = dataSnapshot.child("Name").getValue(String.class);
                if (namee!=null){

                }
                else{
                    Toast.makeText(getContext(),"Set Profile First",Toast.LENGTH_SHORT).show();
                    profile1 fragment = new profile1();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                    fragmentTransaction.commit();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


        mAuth = FirebaseAuth.getInstance();
        choose = v.findViewById(R.id.choose_image);
        SubmitbtnFound = v.findViewById(R.id.SubmitbtnFound);
        title = v.findViewById(R.id.addTitle);
        discription = v.findViewById(R.id.addDescription);
        location = v.findViewById(R.id.addLocation);
        edittexttime = v.findViewById(R.id.time);
        city = v.findViewById(R.id.customerTextView);

        progressBar = v.findViewById(R.id.progress_circle);
        progressBar.setVisibility(View.INVISIBLE);

        fAuth = FirebaseAuth.getInstance();

        image = v.findViewById(R.id.button_choose_image);

        mStorageRef = FirebaseStorage.getInstance().getReference("Found Items");

  /*      StorageReference profileRef = storageReference.child("FoundItems/" + fAuth.getCurrentUser().getUid() + "/ItemPicture.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                image.setVisibility(GONE);
                Picasso.get().load(uri).into(choose);
                choose.setVisibility(View.VISIBLE);
            }
        });*/



        time =  v.findViewById(R.id.times);

        time.setShowSoftInputOnFocus(false);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);

            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        ArrayList<String> dest = new ArrayList<>();



        dest.add("Islamabad");
        dest.add("Karachi");
        dest.add("Rawalpindi");
        dest.add("Lahore");
        dest.add("Peshawar");
        dest.add("Faislabad");
        dest.add("Islamabad");
        dest.add("Multan");
        dest.add("Sargodah");
        dest.add("Mianwali");
        dest.add("Mirpur");
        dest.add("Rawlakot");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dest);

        city.setAdapter(adapter);

        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                cit = (String) parent.getItemAtPosition(position);
            }
        });


        SubmitbtnFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    uploadFile();
                }

            }
        });


        return v;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //cropImage();


            Picasso.get().load(mImageUri).into(choose);
            image.setVisibility(GONE);
            choose.setVisibility(View.VISIBLE);
        }
    }




    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                edittexttime.getEditText().setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void uploadFile() {


        if (title.getEditText().getText().toString().trim().isEmpty()) {
            title.setError("Title is mandatory. Please complete the required field.");
            title.requestFocus();
            return;
        }

        else if (title.getEditText().getText().toString().trim().length() < 20) {
            title.setError("A minimum length of 20 characters is required. Please edit the field.");
            title.requestFocus();
            return;
        }
        else{
            title.setError(null);
        }

        if (discription.getEditText().getText().toString().trim().isEmpty()) {
            discription.setError("Description is mandatory. Please complete the required field.");
            discription.requestFocus();
            return;
        }

        else if (discription.getEditText().getText().toString().trim().length() < 50) {
            discription.setError("A minimum length of 50 characters is required. Please edit the field.");
            discription.requestFocus();
            return;
        }
        else{
            discription.setError(null);
        }

        if (cit.isEmpty()) {
            city.setError("City is mandatory. Please complete the required field.");
            city.requestFocus();
            return;
        }
        else{
            city.setError(null);
        }

        if (edittexttime.getEditText().getText().toString().trim().isEmpty()) {
            edittexttime.setError("Time is mandatory. Please complete the required field.");
            edittexttime.requestFocus();
            return;
        }
        else{
            edittexttime.setError(null);
        }

        if (location.getEditText().getText().toString().trim().isEmpty()) {
            location.setError("Found Nearby Location is mandatory. Please complete the required field.");
            location.requestFocus();
            return;
        }

        else if (location.getEditText().getText().toString().trim().length() < 10) {
            location.setError("A minimum length of 10 characters is required. Please edit the field.");
            location.requestFocus();
            return;
        }
        else{
            location.setError(null);
        }





        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Found Items");
        String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        if (mImageUri != null) {


            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // mProgressBar.setProgress(0);
                                }
                            }, 500);


                            mImageUri = null;
                            choose.setImageBitmap(null);


                            String titles  = title.getEditText().getText().toString();
                            String Des = discription.getEditText().getText().toString();

                            String loc = location.getEditText().getText().toString();
                            String time = edittexttime.getEditText().getText().toString().trim();
                            String Status = "NA";


                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {



                                    String key = ref.push().getKey();
                                    String id = FirebaseAuth.getInstance().getUid();
                                    Founditem founditem = new Founditem(titles, Des , Email, loc, time, cit,uri.toString(),Status,key,id);

                                    ref.child(key).setValue(founditem);

                                    progressBar.setVisibility(View.GONE);

                                    home fragment = new home();;
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                    fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();




                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });

        } else {
            Toast.makeText(getActivity(), "No Photo selected", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);


        }





    }







}





