package com.ayushman999.maxfitness.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adminaccess.AdminShowQR;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;


public class ShowQR extends Fragment {

    FirebaseFirestore firestore;
    CollectionReference qr;
    ImageView qrView;
    Bitmap bp;
    String qrText;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.activity_admin_show_qr, container, false);
        qrView=(ImageView) myView.findViewById(R.id.show_qr);
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
                Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        qrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        QRGSaver qrgSaver = new QRGSaver();
                        qrgSaver.save(String.valueOf(getActivity().getExternalFilesDir(null)),"QRCode",bp, QRGContents.ImageType.IMAGE_JPEG);
                        Toast.makeText(getContext(), "QR Code saved!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        // Inflate the layout for this fragment
        return myView;
    }
    private void showQR(String text) {
        QRGEncoder qrgEncoder=new QRGEncoder(text,null, QRGContents.Type.TEXT,500);
        bp=qrgEncoder.getBitmap();

        if (bp!=null) {
            qrView.setImageBitmap(bp);
        }
    }
}