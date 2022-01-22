package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.MessagesModel;
import com.example.whatsapp.databinding.ActivityChatsDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatsDetailActivity extends AppCompatActivity {

    ActivityChatsDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.videocallimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatsDetailActivity.this, "Not Implemented AnyThing", Toast.LENGTH_SHORT).show();
            }
        });

        binding.voicecallimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatsDetailActivity.this, "Not Implemented AnyThing", Toast.LENGTH_SHORT).show();
            }
        });

        binding.optionmenuimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatsDetailActivity.this, "Not Implemented AnyThing", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        String senderId=auth.getUid();

        String recieverId=getIntent().getStringExtra("userid");
        String name=getIntent().getStringExtra("username");
        String profilePic=getIntent().getStringExtra("profilepic");

        binding.nametextView.setText(name);
        Picasso.get().load(profilePic).placeholder(R.drawable.profile_avatar).into(binding.chatdetailprofileimage);

        binding.backarrorimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatsDetailActivity.this,DashBoardActivity.class));
                finish();
            }
        });

        final ArrayList<MessagesModel> arrayList=new ArrayList<>();

        final ChatAdapter adapter=new ChatAdapter(arrayList,this,recieverId);

        final String senderRoom=senderId+recieverId;
        final String recieverRoom=recieverId+senderId;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                arrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    MessagesModel messagesModel=dataSnapshot.getValue(MessagesModel.class);

                    messagesModel.setMessageId(dataSnapshot.getKey());

                    arrayList.add(messagesModel);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        binding.chatdetailsrecviewid.setAdapter(adapter);
        binding.chatdetailsrecviewid.setLayoutManager(new LinearLayoutManager(this));

        binding.sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.messageeditText.getText().toString();
                final MessagesModel messagesModel=new MessagesModel(senderId,message);
                messagesModel.setTimeStamp(new Date().getTime());
                binding.messageeditText.setText("");

                database.getReference()
                        .child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messagesModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(recieverRoom).push().setValue(messagesModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                    }
                });
            }
        });


    }
}