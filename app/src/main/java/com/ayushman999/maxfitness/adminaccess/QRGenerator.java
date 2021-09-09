package com.ayushman999.maxfitness.adminaccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRGenerator extends AppCompatActivity {
    EditText uniqueID;
    Button generate,upload;
    ImageView qrCode;
    FirebaseFirestore firestore;
    CollectionReference qrCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        uniqueID=(EditText) findViewById(R.id.admin_createqr_edit);
        generate=(Button) findViewById(R.id.generate_qr);
        upload=(Button) findViewById(R.id.upload_qr);
        qrCode=(ImageView) findViewById(R.id.qr_view);
        firestore=FirebaseFirestore.getInstance();
        qrCollection=firestore.collection("QR");
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=uniqueID.getText().toString();
                QRGEncoder qrgEncoder=new QRGEncoder(text,null, QRGContents.Type.TEXT,500);
                Bitmap bp=qrgEncoder.getBitmap();
                qrCode.setImageBitmap(bp);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=uniqueID.getText().toString();
                HashMap<String,Object> data=new HashMap<>();
                data.put("qrCode",text);
                qrCollection.document("QrCode").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(QRGenerator.this, "QR uploaded!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QRGenerator.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}