package com.example.whatsapp;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Models.MyUser;
import com.example.whatsapp.databinding.ActivityLogInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    private ActivityLogInBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String userEmail, userPass;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Please wait......");

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LogInActivity.this, DashBoardActivity.class));
            finish();
        }

        binding.signinbuttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userEmail = binding.loginemailedtid.getText().toString();
                userPass = binding.loginpassedtid.getText().toString();
                if (userEmail.isEmpty()) {
                    Toast.makeText(LogInActivity.this, "Blank space not allowed", Toast.LENGTH_SHORT).show();
                } else if (userPass.isEmpty()) {
                    Toast.makeText(LogInActivity.this, "Blank space not allowed", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(LogInActivity.this, DashBoardActivity.class));
                                        finish();
                                        Toast.makeText(LogInActivity.this, "LogedIn Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        binding.newusertvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        int one=getIntent().getIntExtra("one",0);

        if(one==1){
            signIn();
            one=0;
        }

       /* binding.logingooglebtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });*/

    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 69);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 69) {
            progressDialog.setTitle("Logging In");
            progressDialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressDialog.dismiss();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            MyUser myUser = new MyUser();
                            myUser.setUsername(user.getDisplayName());
                            myUser.setProfilePic(user.getPhotoUrl().toString());
                            myUser.setUserid(user.getUid());

                            firebaseDatabase.getReference().child("User").child(user.getUid()).setValue(myUser);

                            progressDialog.dismiss();
                            startActivity(new Intent(LogInActivity.this, DashBoardActivity.class));
                            finish();
                            Toast.makeText(LogInActivity.this, "Log In Successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    protected void onStart() {
        super.onStart();
        if(!isConnected()){
            new AlertDialog.Builder(LogInActivity.this)
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