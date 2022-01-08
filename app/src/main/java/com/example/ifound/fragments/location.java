package com.example.ifound.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ifound.R;

import java.util.ArrayList;

public class location extends Fragment {

    AutoCompleteTextView Destination;
    Button btn;
    TextView txt3;
     String location;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location, container, false);




        Destination = v.findViewById(R.id.selectloc);
        btn = v.findViewById(R.id.nex);
        txt3 = v.findViewById(R.id.Txt3);

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Destination.setText("");
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

        Destination.setAdapter(adapter);

        try{
            Bundle bundle = getArguments();
            String city = bundle.getString("city");
            Destination.setText(city);


        }catch(Exception error1) {

        }



        Destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                location = (String)parent.getItemAtPosition(position);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                found fragment = new found();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(Destination.getEditableText().toString() == ""){
                    Bundle args = new Bundle();
                    args.putString("city", "NA" );
                    fragment.setArguments(args);
                }else{
                    Bundle args = new Bundle();
                    args.putString("city", location );
                    fragment.setArguments(args);
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slideup, R.anim.slidedown);
                fragmentTransaction.replace(((ViewGroup) getView().getParent()).getId(), fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }




}





