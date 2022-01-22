package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.MessagesModel;
import com.example.whatsapp.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.groupchatbackarrorimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupChatActivity.this,DashBoardActivity.class));
                finish();
            }
        });

        final ArrayList<MessagesModel> arrayList=new ArrayList<>();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final ChatAdapter adapter=new ChatAdapter(arrayList,this);

        final String senderId= FirebaseAuth.getInstance().getUid();
        binding.groupchatnametextView.setText("Friends Group");

        binding.groupchatrecviewid.setAdapter(adapter);
        binding.groupchatrecviewid.setLayoutManager(new LinearLayoutManager(this));

        database.getReference().child("Group Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                arrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    MessagesModel model=snapshot1.getValue(MessagesModel.class);
                    arrayList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        binding.groupchatsendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.groupchatmessageeditText.getText().toString();
                final MessagesModel messagesModel=new MessagesModel(senderId,message);
                messagesModel.setTimeStamp(new Date().getTime());
                binding.groupchatmessageeditText.setText("");

                database.getReference().child("Group Chats").push().setValue(messagesModel)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });

    }
}