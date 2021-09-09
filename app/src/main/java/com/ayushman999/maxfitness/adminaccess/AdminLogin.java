package com.ayushman999.maxfitness.adminaccess;

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
import com.ayushman999.maxfitness.activity.ForgotPassword;
import com.ayushman999.maxfitness.activity.UserDashboard;
import com.ayushman999.maxfitness.activity.UserLogIn;
import com.ayushman999.maxfitness.fragments.AdminDashboardContainer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminLogin extends AppCompatActivity {
    EditText lEmail;
    EditText lPassword;
    Button lLogin;
    TextView lSignUp;
    TextView reset_link;
    FirebaseAuth mFirebaseAuth;
    ConstraintLayout layout;
    CheckBox visiblePass;
    FirebaseFirestore firestore;
    CollectionReference admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mFirebaseAuth= FirebaseAuth.getInstance();
        lEmail=(EditText) findViewById(R.id.admin_email);
        lPassword=(EditText) findViewById(R.id.admin_password);
        lLogin=(Button) findViewById(R.id.admin_login_btn);
        reset_link=(TextView) findViewById(R.id.admin_reset_link);
        visiblePass=(CheckBox) findViewById(R.id.admin_checkbox_loginpass);
        firestore=FirebaseFirestore.getInstance();
        admin=firestore.collection("Admin");
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
        lLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForAdmin(mFirebaseAuth.getCurrentUser());
            }
        });
        reset_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer=new Intent(AdminLogin.this, ForgotPassword.class);
                startActivity(transfer);
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
            final ProgressDialog progressDialog=new ProgressDialog(AdminLogin.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
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
                            Toast.makeText(AdminLogin.this,"Unable to login.",Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void checkForAdmin(FirebaseUser user) {
        if(user!=null)
        {
            String email=user.getEmail();
            admin.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                    {
                        if(snapshot.getId().equals(email))
                        {
                            startMainActivity(user);
                        }
                        else
                        {
                            Toast.makeText(AdminLogin.this, "Unable to identify.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminLogin.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(lEmail.getText().toString().length()>0)
        {
            String email=lEmail.getText().toString();
            admin.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                    {
                        if(snapshot.getId().equals(email))
                        {
                            callLogin();
                        }
                        else
                        {
                            Toast.makeText(AdminLogin.this, "Unable to identify.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminLogin.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void startMainActivity(FirebaseUser user) {
        if(user!=null)
        {
            Intent transfer=new Intent(this, AdminDashboardContainer.class);
            transfer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(transfer);
            finish();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        checkForAdmin(user);
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