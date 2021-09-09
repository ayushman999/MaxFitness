package com.ayushman999.maxfitness.adminaccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.qrcode.encoder.QRCode;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class AdminShowQR extends AppCompatActivity {
    FirebaseFirestore firestore;
    CollectionReference qr;
    ImageView qrView;
    Bitmap bp;
    String qrText;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_qr);
        qrView=(ImageView) findViewById(R.id.show_qr);
        firestore=FirebaseFirestore.getInstance();
        qr=firestore.collection("QR");
        qr.document("QrCode").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                qrText=documentSnapshot.getString("qrCode");
                showQR(qrText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminShowQR.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        qrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        QRGSaver qrgSaver = new QRGSaver();
                        qrgSaver.save(String.valueOf(getExternalFilesDir(null)),"QRCode",bp,QRGContents.ImageType.IMAGE_JPEG);
                        Toast.makeText(AdminShowQR.this, "QR Code saved!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(AdminShowQR.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });

    }

    private void showQR(String text) {
        QRGEncoder qrgEncoder=new QRGEncoder(text,null, QRGContents.Type.TEXT,500);
        bp=qrgEncoder.getBitmap();
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        if (bp!=null) {
            qrView.setImageBitmap(bp);
        }
    }
}