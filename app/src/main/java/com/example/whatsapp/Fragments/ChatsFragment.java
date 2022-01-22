package com.example.whatsapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.Adapters.UsersAdapter;
import com.example.whatsapp.Models.MyUser;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    FragmentChatsBinding binding;
    ArrayList<MyUser> arrayList;
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatsBinding.inflate(inflater, container, false);
        arrayList=new ArrayList<>();
        database=FirebaseDatabase.getInstance();

        UsersAdapter adapter=new UsersAdapter(arrayList,getContext());
        binding.chatrecview.setAdapter(adapter);
        binding.chatrecview.setLayoutManager(new LinearLayoutManager(getContext()));

        database.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                arrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MyUser user=dataSnapshot.getValue(MyUser.class);
                    user.setUserid(dataSnapshot.getKey());

                    if(!user.getUserid().equals(FirebaseAuth.getInstance().getUid()))

                    arrayList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}