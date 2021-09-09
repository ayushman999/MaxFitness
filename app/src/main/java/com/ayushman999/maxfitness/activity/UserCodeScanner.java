package com.ayushman999.maxfitness.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adminaccess.AdminShowQR;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGSaver;

public class UserCodeScanner extends AppCompatActivity {
CodeScanner codeScanner;
CodeScannerView codeScannerView;
FirebaseFirestore firestore;
DocumentReference qr;
private String QR_DATA;
String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_code_scanner);
        firestore=FirebaseFirestore.getInstance();
        qr=firestore.collection("QR").document("QrCode");
        qr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                QR_DATA=documentSnapshot.getString("qrCode");
                checkQR();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserCodeScanner.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        codeScannerView=(CodeScannerView) findViewById(R.id.scan_view);
        codeScanner=new CodeScanner(this,codeScannerView);
        Intent statusIntent=getIntent();
        status=statusIntent.getStringExtra("checkDetail");

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }
    private void checkQR()
    {
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                String data=result.getText();
                                verifyQRData(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ActivityCompat.requestPermissions(UserCodeScanner.this, new String[]{Manifest.permission.CAMERA}, 0);
                        }

                    }
                });
            }
        });
    }

    private void verifyQRData(String data) {
        if(data.equals(QR_DATA))
        {
            Toast.makeText(this, "Please verify your data.", Toast.LENGTH_SHORT).show();
            if(status.equals("CheckIn")) {
                startActivity(new Intent(getApplicationContext(), UserAttendanceDetail.class));
                finish();
            }
            else if(status.equals("CheckOut"))
            {
                startActivity(new Intent(getApplicationContext(),UserCheckOut.class));
                finish();
            }
        }
        else
        {
            Toast.makeText(this, "Wrong QR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}