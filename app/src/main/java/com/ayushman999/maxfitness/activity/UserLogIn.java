package com.ayushman999.maxfitness.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserLogIn extends AppCompatActivity {
    EditText lEmail;
    EditText lPassword;
    Button lLogin;
    TextView lSignUp;
    TextView reset_link;
    FirebaseAuth mFirebaseAuth;
    ConstraintLayout layout;
    CheckBox visiblePass;
    FirebaseFirestore firestore;
    CollectionReference user;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in);
        progressDialog=new ProgressDialog(UserLogIn.this);
        mFirebaseAuth= FirebaseAuth.getInstance();
        lEmail=(EditText) findViewById(R.id.login_email);
        lPassword=(EditText) findViewById(R.id.login_password);
        lLogin=(Button) findViewById(R.id.login);
        reset_link=(TextView) findViewById(R.id.reset_link);
        lSignUp=(TextView) findViewById(R.id.intentLoginText);
        visiblePass=(CheckBox) findViewById(R.id.checkbox_loginpass);
        firestore=FirebaseFirestore.getInstance();
        user=firestore.collection("GymMembers");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        visiblePass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    lPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    lPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        lSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer=new Intent(UserLogIn.this, User_SignIn.class);
                startActivity(transfer);
            }
        });
        lLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForExistance();
            }
        });
        reset_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer=new Intent(UserLogIn.this,ForgotPassword.class);
                startActivity(transfer);
            }
        });
    }

    private void checkForExistance() {
        String email=lEmail.getText().toString().trim();
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        user.document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    callLogin();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(UserLogIn.this, "Package not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callLogin() {
        String email=lEmail.getText().toString().trim();
        String password=lPassword.getText().toString().trim();
        if(email.isEmpty())
        {
            Toast.makeText(this,"Enter your email!",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Enter a valid email address!",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty())
        {
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful())
                    {
                        FirebaseUser user=task.getResult().getUser();
                        startMainActivity(user);
                    }
                    else
                    {

                        if(task.getException() instanceof FirebaseAuthInvalidUserException)
                        {
                            createAlert("Error","This email is not registered","OK");
                        }
                        else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            createAlert("Error","Wrong Password!","OK");
                        }
                        else
                        {
                            Toast.makeText(UserLogIn.this,"Unable to login.",Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }
                    }
                }
            });
        }
    }
    private void startMainActivity(FirebaseUser user) {
        if(user!=null)
        {
            Intent transfer=new Intent(this,UserDashboard.class);
            transfer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(transfer);
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        startMainActivity(user);
    }
    private void createAlert(String heading, String message, String possitive)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(heading)
                .setMessage(message)
                .setPositiveButton(possitive,null)
                .create().show();
    }
}