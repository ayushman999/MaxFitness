package com.ayushman999.maxfitness.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.adminaccess.AdminAttendanceRecords;
import com.ayushman999.maxfitness.adminaccess.AdminAttendanceView;

import java.util.Calendar;


public class ShowMemberRecord extends Fragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemClickListener {
    TextView DateView;
    Button getRecord,getDate;
    Spinner timeSpinner;
    String date;
    String timing;
    String timeArray[]={"none","morning","afternoon","evening"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.activity_admin_attendance_records, container, false);
        DateView=(TextView) myView.findViewById(R.id.date_textview);
        getDate=(Button) myView.findViewById(R.id.date_btn);
        getRecord=(Button) myView.findViewById(R.id.view_atttendance);
        timeSpinner=(Spinner) myView.findViewById(R.id.admin_time_spinner);
        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        setupTimeSpinner(timeSpinner);
        getRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date==null)
                {
                    Toast.makeText(getContext(),"Choose the date first",Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent transfer = new Intent(getContext(), AdminAttendanceView.class);
                    transfer.putExtra("timing", timing);
                    transfer.putExtra("date", date);
                    startActivity(transfer);
                    Toast.makeText(getContext(), date + "\n" + timing, Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return myView;
    }
    private void setupTimeSpinner(Spinner timeSpinner) {
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, timeArray);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(new TimingSpinner());
    }
    class TimingSpinner implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            timing = timeArray[position];
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            timing = timeArray[0];
        }
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //String strDate = format.format(Calendar.getInstance().getTime());
        date = i+"-"+ ((i1+1)<10 ? "0"+(i1+1):(i1+1)) + "-" +(i2<10 ? "0"+i2:i2);
        DateView.setText(date);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    }