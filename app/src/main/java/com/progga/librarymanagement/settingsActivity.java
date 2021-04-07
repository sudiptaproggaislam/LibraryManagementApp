package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class settingsActivity extends AppCompatActivity {
    private Spinner spinnerUser,spinnerSeries,spinnerDepart;
    public String text,dept,series,r,adPhn,adID;
    private ConstraintLayout studentsDetails,adminDetails;
    private EditText roll,userName,adminID,adminPhn;
    private Button reqButton;

    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference rafAdmin,rafStudent,rafSt,reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        rafAdmin= FirebaseDatabase.getInstance().getReference();
        rafStudent=FirebaseDatabase.getInstance().getReference();
        rafSt=FirebaseDatabase.getInstance().getReference();
        reference=FirebaseDatabase.getInstance().getReference().child("Admin Request");

        InnitializeFields();
        adminDetails.setVisibility(View.INVISIBLE);
        studentsDetails.setVisibility(View.INVISIBLE);

        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text=parent.getItemAtPosition(position).toString();
                if(text.equals("Student")){
                    studentsDetails.setVisibility(View.VISIBLE);
                    adminDetails.setVisibility(View.INVISIBLE);
                }else if(text.equals("Admin")){
                    studentsDetails.setVisibility(View.INVISIBLE);
                    adminDetails.setVisibility(View.VISIBLE);
                }

                else{
                    studentsDetails.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDepart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dept=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSeries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                series=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserData();
            }
        });
    }

    private void uploadUserData() {
        final String setUserName=userName.getText().toString();

        if (text.equals("User Status")|| TextUtils.isEmpty(setUserName)){
            Toast.makeText(settingsActivity.this,"Please complete every section...",Toast.LENGTH_SHORT).show();
        }
        else if(text.equals("Admin")) {
            adID=adminID.getText().toString();
            adPhn=adminPhn.getText().toString();
            if (TextUtils.isEmpty(adID)||TextUtils.isEmpty(adPhn)){
                Toast.makeText(settingsActivity.this,"Please complete every section...",Toast.LENGTH_SHORT).show();
            }else{
                final HashMap<String, String> AdminProfileMap = new HashMap<>();
                AdminProfileMap.put("uid", currentUserId);
                AdminProfileMap.put("name", setUserName);
                AdminProfileMap.put("status", text);
                AdminProfileMap.put("adminId",adID);
                AdminProfileMap.put("adminPhn",adPhn);
                AdminProfileMap.put("access","Not Checked");
                rafAdmin.child("User").child(currentUserId).setValue(AdminProfileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            reference.child(AdminProfileMap.get("adminId")).setValue(AdminProfileMap);
                            GoToMainActivity();
                            Toast.makeText(settingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(settingsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        else{

            r = roll.getText().toString();

            if (TextUtils.isEmpty(r)||dept.equals("Department")||series.equals("Series")){
                Toast.makeText(settingsActivity.this,"Please complete every section...",Toast.LENGTH_SHORT).show();
            }

            else {

                final HashMap<String, String> profileMap = new HashMap<>();
                profileMap.put("uid", currentUserId);
                profileMap.put("name", setUserName);
                profileMap.put("status", text);
                profileMap.put("department", dept);
                profileMap.put("series", series);
                profileMap.put("roll", r);
                profileMap.put("access","Not Checked");

                rafStudent.child("User").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            rafSt.child("Request Student List").child(r).setValue(profileMap);
                            GoToMainActivity();
                            Toast.makeText(settingsActivity.this, "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(settingsActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }

    private void InnitializeFields() {
        spinnerUser=(Spinner)findViewById(R.id.spinnerUserType);
        spinnerSeries=(Spinner)findViewById(R.id.spinnerSeries);
        spinnerDepart=(Spinner)findViewById(R.id.spinnerDept);
        studentsDetails=(ConstraintLayout)findViewById(R.id.studentsDetails);
        roll=(EditText)findViewById(R.id.etRoll);
        userName=(EditText)findViewById(R.id.etUserName);
        reqButton=(Button)findViewById(R.id.btnRequest);
        adminDetails=(ConstraintLayout)findViewById(R.id.adminsDetails);
        adminID=(EditText)findViewById(R.id.etAdminID);
        adminPhn=(EditText)findViewById(R.id.etAdminPhn);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.userType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.dept,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepart.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3=ArrayAdapter.createFromResource(this,R.array.series,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeries.setAdapter(adapter3);
    }

    private void GoToMainActivity() {
        Intent mainIntent= new Intent(settingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}