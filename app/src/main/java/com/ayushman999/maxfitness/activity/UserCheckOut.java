package com.ayushman999.maxfitness.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.model.GymUser;
import com.ayushman999.maxfitness.model.UserCheck;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class UserCheckOut extends AppCompatActivity {
    String userEmail=UserDashboard.getEmail();
    String name,uniqueID,date,time,timing;
    boolean exist=false;
    CollectionReference userRef;
    FirebaseFirestore firestore;
    CollectionReference uploadRef;
    Button upload;
    TextView IDView,nameView,timeView,dateView;
    String hourS;
    SlideToActView slide;
    String timeS[];
    String checkIn;
    int hour;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_out);
        IDView=(TextView) findViewById(R.id.checkout_confirm_id);
        nameView=(TextView) findViewById(R.id.checkout_confirm_name);
        timeView=(TextView) findViewById(R.id.checkout_confirm_time);
        dateView=(TextView) findViewById(R.id.checkout_confirm_date);
        slide=(SlideToActView) findViewById(R.id.checkout_slide);
        slide.setBumpVibration(50);
        slide.setReversed(true);
        firestore=FirebaseFirestore.getInstance();
        userRef=firestore.collection("GymMembers");
        uploadRef=firestore.collection("AttendanceData");
        date= LocalDate.now().toString();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date date1=new Date();
        String date2[]= sdf.format(date1).split(" ");
        date=date2[0];
        time=date2[1]+" "+date2[2];
        hourS=time.substring(0,2);
        timeS=time.split(" ");
        hour=Integer.parseInt(hourS);
        timeView.setText(time);
        dateView.setText(date);
        getMemberData();
        slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                confirmCheckIn();
            }
        });

    }
    private void confirmCheckIn() {
        uploadRef.document(date).collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                {
                    if(snapshot.getId().equals(uniqueID))
                    {
                        checkIn=snapshot.getString("checkIn");
                        uploadCheckOut();
                    }
                    else
                    {
                        Toast.makeText(UserCheckOut.this, "CheckIn was not done.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserCheckOut.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadCheckOut() {
        if(hour<12 && timeS[1].equals("am"))
        {
            timing="morning";
            UserCheck data=new UserCheck(uniqueID,name,timing,checkIn,time);
            uploadRef.document(date).collection("data").document(uniqueID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UserCheckOut.this, "CheckOut successful!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserCheckOut.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(hour<5 && timeS[1].equals("pm"))
        {
            timing="afternoon";
            UserCheck data=new UserCheck(uniqueID,name,timing,checkIn,time);
            uploadRef.document(date).collection("data").document(uniqueID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UserCheckOut.this, "CheckOut successful!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserCheckOut.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            timing="evening";
            UserCheck data=new UserCheck(uniqueID,name,timing,checkIn,time);
            uploadRef.document(date).collection("data").document(uniqueID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UserCheckOut.this, "CheckOut successful!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserCheckOut.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getMemberData() {
        userRef.document(userEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GymUser data=documentSnapshot.toObject(GymUser.class);
                name=data.getName();
                nameView.setText(name);
                uniqueID=data.getUniqueID();
                IDView.setText(uniqueID);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(UserCheckOut.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
