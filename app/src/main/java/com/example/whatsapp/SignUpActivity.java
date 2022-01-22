package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Models.MyUser;
import com.example.whatsapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    private static String userName,email,pass;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setTitle("Signing Up");

        binding.signupgooglebtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,LogInActivity.class);
                intent.putExtra("one",1);
                startActivity(intent);
                finish();
            }
        });


        binding.signupbuttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName=binding.signupusernameedtid.getText().toString();
                email=binding.signupemailedtid.getText().toString();
                pass=binding.signuppassedtid.getText().toString();

                if(userName.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please Enter UserName", Toast.LENGTH_LONG).show();
                }else if(email.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please Enter Email Address", Toast.LENGTH_LONG).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(SignUpActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_LONG).show();
                }else if(pass.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6){
                    Toast.makeText(SignUpActivity.this, "Minimum pass length must be six ", Toast.LENGTH_SHORT).show();
                }else{

                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                MyUser myUser=new MyUser(userName,email,pass);
                                String userId=task.getResult().getUser().getUid();
                                firebaseDatabase.getReference().child("User").child(userId).setValue(myUser);

                                progressDialog.dismiss();

                                startActivity(new Intent(SignUpActivity.this,DashBoardActivity.class));
                                finish();

                                Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_LONG).show();

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        binding.alreadyacounttvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
                finish();
            }
        });

        binding.signupwithphonetvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignUpWithPhoneActivity.class));
                finish();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        if(!isConnected()){
            new AlertDialog.Builder(SignUpActivity.this)
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