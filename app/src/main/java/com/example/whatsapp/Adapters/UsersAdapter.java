package com.example.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.ChatsDetailActivity;
import com.example.whatsapp.Models.MyUser;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    ArrayList arrayList;
    Context context;

    public UsersAdapter(ArrayList arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_sample_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.MyViewHolder holder, int position) {

        MyUser user = (MyUser) arrayList.get(position);
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.profile_avatar).into(holder.imageView);
        holder.name.setText(user.getUsername());

        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getUid() + user.getUserid())
                .orderByChild("timeStamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                holder.lastMessage.setText(dataSnapshot.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatsDetailActivity.class);
                intent.putExtra("profilepic",user.getProfilePic());
                intent.putExtra("userid",user.getUserid());
                intent.putExtra("username",user.getUsername());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, lastMessage;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileimage);
            name = itemView.findViewById(R.id.nametvid);
            lastMessage = itemView.findViewById(R.id.lastmsgtvid);
        }
    }
}
