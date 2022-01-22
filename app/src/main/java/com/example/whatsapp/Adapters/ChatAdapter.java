package com.example.whatsapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Models.MessagesModel;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessagesModel> arrayList;
    Context context;
    int SENDER_VIEW_TYPE=1,RECIEVER_VIEW_TYPE=2;
    String recieverId;


    public ChatAdapter(ArrayList arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessagesModel> arrayList, Context context, String recieverId) {
        this.arrayList = arrayList;
        this.context = context;
        this.recieverId = recieverId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }else{
            View view= LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessagesModel messagesModel=arrayList.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context).setTitle("Delete Message")
                        .setMessage("Are you sure want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                String senderRoom=FirebaseAuth.getInstance().getUid() + recieverId;
                                database.getReference().child("chats").child(senderRoom).child(messagesModel.getMessageId())
                                        .setValue(null);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });

        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderertextView.setText(messagesModel.getMessage());
        }else {
            ((RecieverViewHolder)holder).recievertextView.setText(messagesModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(arrayList.get(position).getMessagerId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else {
            return RECIEVER_VIEW_TYPE;
        }
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView recievertextView,recievertimetextview;
        public RecieverViewHolder(View itemView) {
            super(itemView);

            recievertextView=itemView.findViewById(R.id.recievertextView);
            recievertimetextview=itemView.findViewById(R.id.recievertimetextView);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderertextView,senderertimetextview;
        public SenderViewHolder(View itemView) {
            super(itemView);

            senderertextView=itemView.findViewById(R.id.sendertextView);
            senderertimetextview=itemView.findViewById(R.id.sendertimetextView);
        }
    }
}
