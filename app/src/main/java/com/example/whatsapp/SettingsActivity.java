package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.whatsapp.databinding.ActivitySettingsBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        if(!isConnected()){
            new AlertDialog.Builder(SettingsActivity.this)
                    .setMessage("Please connect to the Internet")
                    .setCancelable(false)
                    .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
                        }
                    }).show();
        }
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileConnection=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiConnection=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(mobileConnection!=null && mobileConnection.isConnected() || wifiConnection!=null&& wifiConnection.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.profilebackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,DashBoardActivity.class));
                finish();
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(SettingsActivity.this)
                        .crop()
                        .start(12);

            }
        });

    }
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 12 && resultCode == RESULT_OK) {
                Uri uri = data.getData();
                binding.profileActivitiesimageview.setImageURI(uri);

            }
        }



}