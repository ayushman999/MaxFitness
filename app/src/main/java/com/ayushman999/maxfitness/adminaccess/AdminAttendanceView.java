package com.ayushman999.maxfitness.adminaccess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adapters.MemberAdapter;
import com.ayushman999.maxfitness.model.UserCheck;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AdminAttendanceView extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<UserCheck> data=new ArrayList<>();
    FirebaseFirestore firestore;
    CollectionReference attendance;
    String timing,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance_view);
        recyclerView=(RecyclerView) findViewById(R.id.attendance_recycler);
        Intent data=getIntent();
        timing=data.getStringExtra("timing");
        date=data.getStringExtra("date");
        firestore=FirebaseFirestore.getInstance();
        attendance=firestore.collection("AttendanceData");
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getAttendanceData(timing,date);

    }

    private void getAttendanceData(String timing, String date) {
        if(timing.equals("none")) {
            attendance.document(date).collection("data").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.exists()) {
                            UserCheck userData = snapshot.toObject(UserCheck.class);
                            String uniqueId = userData.getUniqueID();
                            String name = userData.getName();
                            String checkIn = userData.getCheckIn();
                            String checkOut = userData.getCheckOut();
                            String timing = userData.getTiming();
                            data.add(new UserCheck(uniqueId, name, timing, checkIn, checkOut));
                        }
                    }
                    updateData();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAttendanceView.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            attendance.document(date).collection("data").whereEqualTo("timing",timing).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (snapshot.exists()) {
                            UserCheck userData = snapshot.toObject(UserCheck.class);
                            String uniqueId = userData.getUniqueID();
                            String name = userData.getName();
                            String checkIn = userData.getCheckIn();
                            String checkOut = userData.getCheckOut();
                            String timing = userData.getTiming();
                            data.add(new UserCheck(uniqueId, name, timing, checkIn, checkOut));
                        }
                    }
                    updateData();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAttendanceView.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateData() {
        MemberAdapter adapter=new MemberAdapter(data,this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.excel_get, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sign_out_admin:
            {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(AdminAttendanceView.this,"Sign out!",Toast.LENGTH_SHORT).show();
                Intent transfer=new Intent(AdminAttendanceView.this, AdminLogin.class);
                startActivity(transfer);
                finish();
            }
            case R.id.get_excel_menu:
            {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    generateExcel();
                } else {
                    ActivityCompat.requestPermissions(AdminAttendanceView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateExcel() {
        if(data.size()>0)
        {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet firstSheet = workbook.createSheet("Sheet No 1");
            HSSFRow rowA = firstSheet.createRow(0);
            HSSFCell cellA = rowA.createCell(0);
            cellA.setCellValue(new HSSFRichTextString("Name:"));
            HSSFCell roll=rowA.createCell(1);
            roll.setCellValue(new HSSFRichTextString("Unique ID:"));
            HSSFCell checkIn=rowA.createCell(2);
            checkIn.setCellValue(new HSSFRichTextString("Check In:"));
            HSSFCell checkOut=rowA.createCell(3);
            checkOut.setCellValue(new HSSFRichTextString("Check Out:"));
            HSSFCell timing=rowA.createCell(4);
            timing.setCellValue(new HSSFRichTextString("Timing:"));
            for(int i=0;i<data.size();i++)
            {
                HSSFRow newRow=firstSheet.createRow(i+1);
                HSSFCell nameCell= newRow.createCell(0);
                nameCell.setCellValue(new HSSFRichTextString(data.get(i).getName()));
                HSSFCell rollCell= newRow.createCell(1);
                rollCell.setCellValue(new HSSFRichTextString(data.get(i).getUniqueID()+""));
                HSSFCell statusCell= newRow.createCell(2);
                statusCell.setCellValue(new HSSFRichTextString(data.get(i).getCheckIn()));
                HSSFCell checkOutCell= newRow.createCell(3);
                checkOutCell.setCellValue(new HSSFRichTextString(data.get(i).getCheckOut()));
                HSSFCell timingCell= newRow.createCell(4);
                timingCell.setCellValue(new HSSFRichTextString(data.get(i).getTiming()));
            }
            FileOutputStream fos = null;
            try {
                File file ;
                file = new File(getExternalFilesDir(null),  timing+date+ ".xls");
                fos = new FileOutputStream(file);
                workbook.write(fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(AdminAttendanceView.this, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "List is empty!", Toast.LENGTH_SHORT).show();
        }
    }
}