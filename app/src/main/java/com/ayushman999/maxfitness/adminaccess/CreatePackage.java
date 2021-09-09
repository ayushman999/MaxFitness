package com.ayushman999.maxfitness.adminaccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.activity.User_SignIn;
import com.ayushman999.maxfitness.model.GymUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreatePackage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener {
    Spinner packageSpinner;
    FirebaseFirestore firestore;
    CollectionReference reference;
    Button startDateBtn;
    TextView DateView;
    Button mCreate;
    EditText mUniqueId;
    EditText mName;
    EditText mEmail;
    EditText mPhoneNum;
    String[] months={"1 month","2 months","3 months","6 months","1 year"};
    String date,packageName,name,email,uniqueID,phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);
        firestore=FirebaseFirestore.getInstance();
        startDateBtn=(Button) findViewById(R.id.user_start_date_btn);
        packageSpinner=(Spinner) findViewById(R.id.user_package_spinner);
        mCreate=(Button) findViewById(R.id.create_package);
        DateView=(TextView) findViewById(R.id.user_start_date_view);
        mUniqueId=(EditText) findViewById(R.id.admin_create_unique_id);
        mName=(EditText) findViewById(R.id.admin_create_name);
        mEmail=(EditText) findViewById(R.id.admin_create_email);
        mPhoneNum=(EditText) findViewById(R.id.admin_create_phone_num);
        setUpPackageSpinner(packageSpinner);
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date1=sdf.parse(date);
                    Calendar c = Calendar.getInstance();
                    c.setTime(date1);
                    int time=getDays();
                    c.add(Calendar.DATE, time); // Adding 5 days
                    String output = sdf.format(c.getTime());
                    registerUser(date,output);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void registerUser(String startDate, String endDate) {
        name=mName.getText().toString().trim();
        email=mEmail.getText().toString().trim();
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
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Creating Package...");
            progressDialog.show();
            reference=firestore.collection("GymMembers");
            GymUser data=new GymUser(name,email,uniqueID,phoneNum,startDate,endDate);
            reference.document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(CreatePackage.this, "Welcome to Max Fitness!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(CreatePackage.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setUpPackageSpinner(Spinner packageSpinner) {
        ArrayAdapter<String> packageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        packageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packageSpinner.setAdapter(packageAdapter);
        packageSpinner.setOnItemSelectedListener(new PackageSpinner());
    }
    class PackageSpinner implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            packageName = months[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            packageName = months[0];
        }
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.show();
    }
    private int getDays() {
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


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //String strDate = format.format(Calendar.getInstance().getTime());
        date = i+"-"+ ((i1+1)<10 ? "0"+(i1+1):(i1+1)) + "-" +(i2<10 ? "0"+i2:i2);
        DateView.setText(date);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}