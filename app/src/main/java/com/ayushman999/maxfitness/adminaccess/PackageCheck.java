package com.ayushman999.maxfitness.adminaccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adapters.packageAdapter;
import com.ayushman999.maxfitness.model.GymUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PackageCheck extends AppCompatActivity {
    RecyclerView packageList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firestore;
    CollectionReference data;
    ArrayList<GymUser> userList=new ArrayList<>();
    ArrayList<GymUser> expiringUsers=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_check);
        packageList=(RecyclerView) findViewById(R.id.admin_package_recyclerview);
        layoutManager=new LinearLayoutManager(this);
        packageList.setLayoutManager(layoutManager);
        firestore=FirebaseFirestore.getInstance();
        data=firestore.collection("GymMembers");
        getData();
    }

    private void getData() {
        data.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    GymUser user=snapshot.toObject(GymUser.class);
                    String name=user.getName();
                    String uniqueID=user.getUniqueID();
                    String lastDate=user.getEndDate();
                    userList.add(new GymUser(name,uniqueID,lastDate));
                }
                try {
                    sendDataForAnalysis();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(PackageCheck.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendDataForAnalysis() throws ParseException {
        SimpleDateFormat expireSDF=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat showSDF=new SimpleDateFormat("dd/MM/yyy");
        Date todaysDate=new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(todaysDate);
        c.add(Calendar.DATE,10);
        Date lastDate=c.getTime();
        for(GymUser user:userList)
        {
            Date date=expireSDF.parse(user.getEndDate());
            if(date.before(todaysDate))
            {
                String name=user.getName();
                String uniqueId=user.getUniqueID();
                String exDate=user.getEndDate();
                Date exDate1=expireSDF.parse(exDate);
                Calendar C=Calendar.getInstance();
                C.setTime(exDate1);
                exDate=showSDF.format(C.getTime());
                expiringUsers.add(new GymUser(name,uniqueId,exDate));
            }
            else if(date.after(todaysDate) && date.before(lastDate))
            {
                String name=user.getName();
                String uniqueId=user.getUniqueID();
                String exDate=user.getEndDate();
                Date exDate1=expireSDF.parse(exDate);
                Calendar C=Calendar.getInstance();
                C.setTime(exDate1);
                exDate=showSDF.format(C.getTime());
                expiringUsers.add(new GymUser(name,uniqueId,exDate));
            }
            else
            {
                //User package is not expiring within 10 days.
            }

        }
        setAdapter();
    }

    private void setAdapter() {
        packageAdapter adapter=new packageAdapter(expiringUsers,getApplicationContext());
        packageList.setAdapter(adapter);
    }
}