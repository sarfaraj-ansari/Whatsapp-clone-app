package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.databinding.ActivitySignUpWithPhoneBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpWithPhoneActivity extends AppCompatActivity {

    ActivitySignUpWithPhoneBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseAuthProvider firebaseAuthProvider;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken myforceResendingToken;
    String otpId;
    String phoneNo;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpWithPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(SignUpWithPhoneActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.ccpid.registerCarrierNumberEditText(binding.phoneedtid);
        binding.otpLlid.setVisibility(View.INVISIBLE);



        binding.getOtpButtonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               phoneNo  = binding.ccpid.getFullNumberWithPlus().toString().trim();

                if (phoneNo.length()<4) {
                    Toast.makeText(SignUpWithPhoneActivity.this, "Please Enter Phone No", Toast.LENGTH_SHORT).show();
                } else if (phoneNo.length() > 13) {
                    Toast.makeText(SignUpWithPhoneActivity.this, "Please Enter Valid Phone No", Toast.LENGTH_SHORT).show();
                }else if(phoneNo.length()<13){
                    Toast.makeText(SignUpWithPhoneActivity.this, "Please Enter Valid Phone No", Toast.LENGTH_SHORT).show();
                }

                else {

                    progressDialog.setTitle("Sending Otp");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phoneNo)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(SignUpWithPhoneActivity.this)
                            .setCallbacks(callbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

                }

            }
        });

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onVerificationCompleted( PhoneAuthCredential phoneAuthCredential) {

                progressDialog.dismiss();

                binding.phoneLlid.setVisibility(View.INVISIBLE);
                binding.otpLlid.setVisibility(View.VISIBLE);

                signInuserWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                progressDialog.dismiss();

                Toast.makeText(SignUpWithPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String s,PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                otpId=s;
                myforceResendingToken=forceResendingToken;

                progressDialog.dismiss();

                binding.phoneLlid.setVisibility(View.INVISIBLE);
                binding.otpLlid.setVisibility(View.VISIBLE);

                Toast.makeText(SignUpWithPhoneActivity.this, "Otp Sent Successfully", Toast.LENGTH_SHORT).show();

            }
        };

        binding.verifybtnid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String otp=binding.otpedtid.getText().toString().trim();
                 PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(otpId,otp);

                 signInuserWithCredential(phoneAuthCredential);

            }
        });

        binding.resendotptvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

progressDialog.setTitle("Resending Otp");
progressDialog.show();
                PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phoneNo)
                        .setActivity(SignUpWithPhoneActivity.this)
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setForceResendingToken(myforceResendingToken)
                        .setCallbacks(callbacks)
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

            }
        });

    }

    private void signInuserWithCredential(PhoneAuthCredential credential){

        progressDialog.setTitle("Verifying");
        progressDialog.show();

        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                progressDialog.dismiss();
                Toast.makeText(SignUpWithPhoneActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpWithPhoneActivity.this,DashBoardActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

                progressDialog.dismiss();

                Toast.makeText(SignUpWithPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}