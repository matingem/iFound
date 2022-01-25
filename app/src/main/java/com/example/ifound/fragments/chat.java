package com.example.ifound.fragments;

import static android.view.View.GONE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifound.Messages;
import com.example.ifound.R;
import com.example.ifound.Users;
import com.example.ifound.fragments.adapters.FounditemrecviewAdopter;
import com.example.ifound.message_adapter;
import com.example.ifound.model.Chatlist;
import com.example.ifound.model.Founditem;
import com.example.ifound.model.User;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class chat extends Fragment {
    List<Chatlist> userslist;
    List<Users> mUsers;
    RecyclerView recyclerView;
   message_adapter message_adapter;  //old 1
      User_adapter user_adapter;
   FirebaseUser firebaseUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat, container, false);

            userslist=new ArrayList<>();
            recyclerView=v.findViewById(R.id.recycler_view);  //code here
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  //new
          firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
          DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());  //new
          reference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  userslist.clear();
                  for(DataSnapshot ds:snapshot.getChildren()){
                       Chatlist chatslist=ds.getValue(Chatlist.class);
                       userslist.add(chatslist);
                  }
                  ChatListings();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });

        return v;
    }

    private void ChatListings() {
        mUsers=new ArrayList<>();

       DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               mUsers.clear();
               for (DataSnapshot ds:snapshot.getChildren()){
                   Users user=ds.getValue(Users.class);
                   for (Chatlist chatlist:userslist){
                       if (ds.getKey().equals(chatlist.getId())){
                           Users users=new Users();
                           users.setEmail(ds.child("Email").getValue(String.class));
                           users.setFullName(ds.child("Name").getValue(String.class));
                           users.setId(ds.getKey());
                           mUsers.add(users);
                           Log.e("chattttt","id "+ds.getKey());
                           Log.e("chattttt","email"+users.getEmail());
                           Log.e("chattttt","name"+users.getFullName());
                       }
                   }
               }
               Log.e("chattttt","in chat size"+mUsers.size());    //log here
               user_adapter=new User_adapter(getContext(), mUsers );
               recyclerView.setAdapter(user_adapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }


}

