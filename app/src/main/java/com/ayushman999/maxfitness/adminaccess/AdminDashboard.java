package com.ayushman999.maxfitness.adminaccess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.activity.UserDashboard;
import com.ayushman999.maxfitness.activity.UserLogIn;
import com.ayushman999.maxfitness.activity.User_SignIn;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboard extends AppCompatActivity {
    Button checkRecord,getQR,generateQR,expiringPackages,addGymMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        checkRecord=(Button) findViewById(R.id.admin_check_record);
        getQR=(Button) findViewById(R.id.get_qr);
        generateQR=(Button) findViewById(R.id.generate_qr);
        expiringPackages=(Button) findViewById(R.id.package_check);
        addGymMember=(Button) findViewById(R.id.add_gym_member);
        checkRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminAttendanceRecords.class));
                Toast.makeText(AdminDashboard.this, "Check Record", Toast.LENGTH_SHORT).show();
            }
        });
        getQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminShowQR.class));
            }
        });
        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QRGenerator.class));
            }
        });
        expiringPackages.setOnClickListener(v -> { startActivity(new Intent(getApplicationContext(),PackageCheck.class));

        });
        addGymMember.setOnClickListener(v->{startActivity(new Intent(getApplicationContext(), CreatePackage.class));});
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
                Toast.makeText(AdminDashboard.this,"Sign out!",Toast.LENGTH_SHORT).show();
                Intent transfer=new Intent(AdminDashboard.this, AdminLogin.class);
                startActivity(transfer);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}