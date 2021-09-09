package com.ayushman999.maxfitness.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adminaccess.AdminDashboard;
import com.ayushman999.maxfitness.adminaccess.AdminLogin;
import com.ayushman999.maxfitness.adminaccess.CreatePackage;
import com.ayushman999.maxfitness.adminaccess.QRGenerator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class AdminDashboardContainer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bar;
    FragmentManager manager;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_container);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        bar=(BottomNavigationView) findViewById(R.id.bottom_nav);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_bar_open,R.string.navigation_bar_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null)
        {
            FragmentManager manage=getSupportFragmentManager();
            ShowMemberRecord member=new ShowMemberRecord();
            manage.beginTransaction().replace(R.id.fragment_container,member).commit();
        }
        navigationView.setNavigationItemSelectedListener(this);
        bar.setOnNavigationItemSelectedListener(navListner);
    }
    BottomNavigationView.OnNavigationItemSelectedListener navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                    Fragment fragment=null;
                    switch (item.getItemId())
                    {
                        case R.id.record_show:
                        {
                            fragment=new ShowMemberRecord();
                            break;
                        }
                        case R.id.expiring_package:
                        {
                            fragment=new ExpiringPackage();
                            break;
                        }

                        case R.id.qr_generator:
                        {
                            fragment=new ShowQR();
                            break;
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                    return true;
                }
            };
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
                Toast.makeText(AdminDashboardContainer.this,"Sign out!",Toast.LENGTH_SHORT).show();
                Intent transfer=new Intent(AdminDashboardContainer.this, AdminLogin.class);
                startActivity(transfer);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_member:
            {
                Intent trasfer=new Intent(getApplicationContext(), CreatePackage.class);
                startActivity(trasfer);
                break;
            }
            case R.id.create_qr:
            {
                Intent trasfer=new Intent(getApplicationContext(), QRGenerator.class);
                startActivity(trasfer);
                break;
            }
            case R.id.show_profile:
            {
                Toast.makeText(this, "Still working...", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_about:
            {
                Toast.makeText(this, "About!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_contact:
            {
                Toast.makeText(this, "Contact Developer!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
}