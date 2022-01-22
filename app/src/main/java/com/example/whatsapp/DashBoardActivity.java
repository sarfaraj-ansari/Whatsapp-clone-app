package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsapp.Adapters.FragmentAdapter;
import com.example.whatsapp.databinding.ActivityDashBoardBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class DashBoardActivity extends AppCompatActivity {

    ActivityDashBoardBinding binding;
    FirebaseAuth firebaseAuth;
    FragmentAdapter fragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("ChatsApp");


        firebaseAuth=FirebaseAuth.getInstance();

       // binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),binding.tabLayout.getTabCount()));
                                     //or
        fragmentAdapter =new FragmentAdapter(getSupportFragmentManager(),binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(fragmentAdapter);

        binding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());

                if(tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2){
                    fragmentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_file,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.settingmenuitem:
                startActivity(new Intent(DashBoardActivity.this,SettingsActivity.class));
                finish();
                break;

            case R.id.logoutmenuitem:
                firebaseAuth.signOut();
                startActivity(new Intent(DashBoardActivity.this,LogInActivity.class));
                finish();
                break;

            case R.id.groupchatmenu:
                startActivity(new Intent(DashBoardActivity.this,GroupChatActivity.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStart() {
        super.onStart();
        if(!isConnected()){
            new AlertDialog.Builder(DashBoardActivity.this)
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
}