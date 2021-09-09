package com.ayushman999.maxfitness.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button sendLink;
    EditText mEmail;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        sendLink=(Button) findViewById(R.id.send_password_reset_btn);
        mEmail=(EditText) findViewById(R.id.editTextTextEmailAddress);
        mFirebaseAuth= FirebaseAuth.getInstance();
        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {

                    mFirebaseAuth.sendPasswordResetEmail(email);
                    Toast.makeText(getApplicationContext(), "Password reset link send! Please check your mail.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AppDashboard.class));
                    finish();
                }
            }
        });
    }
}