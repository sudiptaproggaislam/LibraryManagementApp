package com.progga.librarymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class issueBookActivityNew extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button checkRoll,issueBook;
    private EditText takeRoll;
    private Spinner catsp,bnamesp;
    private TextView issueDate,retDate;
    public String iDate,rDate,r,cRoll,category,book;

    ValueEventListener listener,listener2;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    ArrayAdapter<String> adapter2;
    ArrayList<String> spinnerDataList2;

    final HashMap<String, String> ibMap = new HashMap<>();

    private DatabaseReference refFindRoll,refCheckaccess,ref,refBook,refissue,groupNameRef,groupMessageKeyRef;

    DatePickerDialog.OnDateSetListener dateSetListerner1,dateSetListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book_new);

        mToolbar=(Toolbar)findViewById(R.id.issue_book_activity_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Issue Book");


        initialization();
        refFindRoll= FirebaseDatabase.getInstance().getReference().child("All Student List");
        refCheckaccess= FirebaseDatabase.getInstance().getReference().child("All Student List");

        retriveData();



        catsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                refBook=FirebaseDatabase.getInstance().getReference().child("Books Category").child(category);
                refissue=FirebaseDatabase.getInstance().getReference().child("Books Category").child(category);
                ibMap.put("category",category);

                retriveDataforBook();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bnamesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                book = parent.getItemAtPosition(position).toString();
                ibMap.put("book",book);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        iDate=simpleDateFormat.format(Calendar.getInstance().getTime());
        issueDate.setText(iDate);

      /*  issueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                final int year=calendar.get(Calendar.YEAR);
                final int month=calendar.get(Calendar.MONTH);
                final int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =new DatePickerDialog(issueBookActivityNew.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListerner1,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListerner1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                iDate= dayOfMonth+"/"+month+"/"+year;
                issueDate.setText(iDate);
            }
        };*/
        retDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                final int year=calendar.get(Calendar.YEAR);
                final int month=calendar.get(Calendar.MONTH);
                final int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =new DatePickerDialog(issueBookActivityNew.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener2,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateSetListener2=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                rDate= dayOfMonth+"/"+month+"/"+year;
                retDate.setText(rDate);
            }
        };


        issueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stdR=takeRoll.getText().toString();
                String isD=issueDate.getText().toString();
                String reD=takeRoll.getText().toString();



                if (TextUtils.isEmpty(stdR)||TextUtils.isEmpty(reD)||reD.equals("")){
                    Toast.makeText(issueBookActivityNew.this,"Please Set Every Option First...",Toast.LENGTH_SHORT).show();
                }else {
                    ibMap.put("issuedate", isD);
                    ibMap.put("returndate", retDate.getText().toString());
                    ibMap.put("roll", reD);
                    ibMap.put("status","issued");

                    groupNameRef= FirebaseDatabase.getInstance().getReference().child("All Student List").child(stdR).child("Taken Book");
                    String issueKey = groupNameRef.push().getKey();
                    ibMap.put("key",issueKey);

                    groupMessageKeyRef=groupNameRef.child(issueKey);
                    refissue.child(ibMap.get("book")).child("Taken Book").child(reD).setValue("1");

                    groupMessageKeyRef.setValue(ibMap);
                    Toast.makeText(issueBookActivityNew.this,"Issued",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void retriveDataforBook() {
        listener2=refBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerDataList2.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    spinnerDataList2.add(ds.getKey().toString());
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void retriveData() {
        listener=ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spinnerDataList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    spinnerDataList.add(ds.getKey().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initialization() {
        checkRoll=(Button)findViewById(R.id.btnCheckRollofStd);
        issueBook=(Button)findViewById(R.id.btnIBook);
        takeRoll=(EditText)findViewById(R.id.etibRoll);
        issueDate=(TextView) findViewById(R.id.ibIssueDate);
        retDate=(TextView) findViewById(R.id.ibReturnDate);
        catsp=(Spinner)findViewById(R.id.spibCategory);
        bnamesp=(Spinner)findViewById(R.id.spibBook);

        ref=FirebaseDatabase.getInstance().getReference().child("Books Category");
        refissue=FirebaseDatabase.getInstance().getReference().child("");

        spinnerDataList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(issueBookActivityNew.this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
        catsp.setAdapter(adapter);

        spinnerDataList2=new ArrayList<>();
        adapter2=new ArrayAdapter<String>(issueBookActivityNew.this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList2);
        bnamesp.setAdapter(adapter2);


    }

    public void checkUserAvail(View view) {
        refFindRoll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator iterator=dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()){
                    cRoll=((DataSnapshot)iterator.next()).getKey().toString();
                    if(cRoll.equals(takeRoll.getText().toString())){
                        doubleCheck();
                        break;

                    }else {
                        Toast.makeText(issueBookActivityNew.this,"Not found",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doubleCheck() {
        refCheckaccess.child(takeRoll.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnap) {
                Toast.makeText(issueBookActivityNew.this,dataSnap.child("request").getValue().toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}