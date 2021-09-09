package com.ayushman999.maxfitness.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adminaccess.AdminDashboard;
import com.ayushman999.maxfitness.adminaccess.AdminLogin;

public class AppDashboard extends AppCompatActivity {
    CardView member,admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_dashboard);
        member=(CardView) findViewById(R.id.member);
        admin=(CardView) findViewById(R.id.admin);
        member.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),UserLogIn.class)));
        admin.setOnClickListener(v->startActivity(new Intent(getApplicationContext(), AdminLogin.class)));
    }
}