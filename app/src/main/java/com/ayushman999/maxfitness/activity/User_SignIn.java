package com.ayushman999.maxfitness.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.model.GymUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User_SignIn extends AppCompatActivity  {
    EditText mUniqueId;
    EditText mName;
    EditText mPassword;
    EditText mEmail;
    EditText mPhoneNum;
    Button mCreate;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    CheckBox visibleSignIn;
    String uniqueID,name,email,password,phoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth = FirebaseAuth.getInstance();
        mUniqueId=(EditText) findViewById(R.id.user_unique_id);
        mName=(EditText) findViewById(R.id.create_name);
        mEmail=(EditText) findViewById(R.id.create_email);
        visibleSignIn=(CheckBox) findViewById(R.id.sign_up_checkbox);
        mPassword=(EditText) findViewById(R.id.create_password);
        mPhoneNum=(EditText) findViewById(R.id.create_phone_num);
        mCreate=(Button) findViewById(R.id.create);


        visibleSignIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForExistance();
            }
        });
    }

    private void checkForExistance() {
        String email=mEmail.getText().toString().trim();
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        FirebaseFirestore.getInstance().collection("GymMembers").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    registerUser();
                }
                else 
                {
                    progressDialog.dismiss();
                    Toast.makeText(User_SignIn.this, "Package not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

   /* private int getDays() {
        switch (packageName)
        {
            case "1 month":
            {
                return 30;
            }
            case "2 months":
            {
                return 60;
            }
            case "3 months":
            {
                return 90;
            }
            case "6 months":
            {
                return 180;
            }
            case "1 year":
            {
                return 365;
            }
        }
        return -1;

    }
*/


    private void registerUser() {
        name=mName.getText().toString().trim();
        email=mEmail.getText().toString().trim();
        password=mPassword.getText().toString().trim();
        uniqueID=mUniqueId.getText().toString().trim();
        phoneNum=mPhoneNum.getText().toString().trim();
        final String phoneNum=mPhoneNum.getText().toString().trim();
        if(name.equals(""))
        {
            Toast.makeText(this,"Enter your name!",Toast.LENGTH_SHORT).show();
        }
        else if(email.isEmpty())
        {
            Toast.makeText(this,"Please Enter your email",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty())
        {
            Toast.makeText(this,"Enter a password!",Toast.LENGTH_SHORT).show();
        }
        else if(password.length()<6)
        {
            Toast.makeText(this,"Password must be greater than equal to 6 characters",Toast.LENGTH_SHORT).show();
        }
        else if(phoneNum.isEmpty())
        {
            Toast.makeText(this,"Enter your phone number!",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.PHONE.matcher(phoneNum).matches())
        {
            Toast.makeText(this,"Please enter a valid phone number.",Toast.LENGTH_SHORT).show();
        }
        else
        {

            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.dismiss();
                    Toast.makeText(User_SignIn.this, "Welcome to Max Fitness", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(User_SignIn.this,"Unable to signup.",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        }
    }



}