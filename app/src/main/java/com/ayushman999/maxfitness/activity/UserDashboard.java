package com.ayushman999.maxfitness.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adminaccess.AdminAttendanceView;
import com.google.firebase.auth.FirebaseAuth;

public class UserDashboard extends AppCompatActivity {
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    static String email;
    Button userCheckIn,userCheckOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        email=mAuth.getCurrentUser().getEmail();
        userCheckIn=(Button) findViewById(R.id.user_check_in);
        userCheckOut=(Button) findViewById(R.id.user_check_out);
        userCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer=new Intent(getApplicationContext(),UserCodeScanner.class);
                transfer.putExtra("checkDetail","CheckIn");
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(transfer);
                } else {
                    ActivityCompat.requestPermissions(UserDashboard.this, new String[]{Manifest.permission.CAMERA}, 0);
                }

            }
        });
        userCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer=new Intent(getApplicationContext(),UserCodeScanner.class);
                transfer.putExtra("checkDetail","CheckOut");
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(transfer);
                } else {
                    ActivityCompat.requestPermissions(UserDashboard.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign_out, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sign_out_menu:
            {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(UserDashboard.this,"Sign out!",Toast.LENGTH_SHORT).show();
                Intent transfer=new Intent(UserDashboard.this,UserLogIn.class);
                startActivity(transfer);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public static String getEmail()
    {
        return email;
    }
}